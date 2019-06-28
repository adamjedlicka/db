package jeda00.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Query<M extends Model<?>> {

    protected M model;

    protected Class<M> modelClass;

    protected List<String> fields;

    protected Map<String, Object> wheres;

    protected int limit;

    public Query(Class<M> modelClass) {
        this.modelClass = modelClass;

        try {
            this.model = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

        this.fields = new ArrayList<>();
        this.wheres = new HashMap<>();
        this.limit = 0;
    }

    public Query<M> select(String... fields) {
        this.fields.addAll(Arrays.asList(fields));

        return this;
    }

    public Query<M> where(String field, Object value) {
        this.wheres.put(field, value);

        return this;
    }

    public Query<M> limit(int limit) {
        this.limit = limit;

        return this;
    }

    protected List<M> execute() {
        List<M> list = new ArrayList<>();

        try {
            PreparedStatement stmt = model.getConnection().prepareStatement(toSql());
            bindValues(stmt);
            stmt.execute();

            ResultSet rs = stmt.getResultSet();
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                M model = modelClass.newInstance();

                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    String columnName = meta.getColumnName(i);
                    Object value = rs.getObject(i);

                    model.set(columnName, value);
                }

                list.add(model);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }

        return list;
    }

    public List<M> all() {
        return execute();
    }

    public M first() {
        List<M> models = limit(1).execute();

        return models.size() == 1
                ? models.get(0)
                : null;
    }

    public String toSql() {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append(stringifyFields());
        sb.append(" FROM ");
        sb.append(model.getTableName());

        if (wheres.size() > 0) {
            sb.append(" WHERE ");
            sb.append(stringifyWheres());
        }

        if (limit > 0) {
            sb.append(" LIMIT ");
            sb.append(limit);
        }

        return sb.toString();
    }

    protected void bindValues(PreparedStatement stmt) throws SQLException {
        int i = 1;

        for (Map.Entry<String, Object> e : wheres.entrySet()) {
            stmt.setObject(i++, e.getValue());
        }
    }

    public String stringifyFields() {
        return fields.size() == 0
                ? "*"
                : String.join(", ", fields);
    }

    public String stringifyWheres() {
        return wheres.entrySet().stream()
                .map(e -> e.getKey() + " = ?")
                .collect(Collectors.joining(" AND "));
    }

}
