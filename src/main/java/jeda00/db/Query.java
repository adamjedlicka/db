package jeda00.db;

import com.sun.org.apache.xpath.internal.operations.Mod;
import jeda00.db.relationships.Relationship;

import java.lang.reflect.Method;
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

    protected Map<String, Boolean> whereNotNulls;

    protected List<String> withs;

    protected int limit;

    public Query(Class<M> modelClass) {
        try {
            this.model = modelClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        this.modelClass = modelClass;
        this.fields = new ArrayList<>();
        this.wheres = new HashMap<>();
        this.whereNotNulls = new HashMap<>();
        this.withs = new ArrayList<>();
        this.limit = 0;

        if (model.deletedTimestamp() != null) {
            whereNull(model.deletedTimestamp());
        }
    }

    public Query<M> select(String... fields) {
        this.fields.addAll(Arrays.asList(fields));

        return this;
    }

    public Query<M> where(String field, String operator, Object value) {
        this.wheres.put(field + " " + operator + " ?", value);

        return this;
    }

    public Query<M> where(String field, Object value) {
        return where(field, "=", value);
    }

    public Query<M> whereIn(String field, List values) {
        String questionMarks = (String) values.stream().map(e -> "?").collect(Collectors.joining(", "));
        this.wheres.put(field + " IN (" + questionMarks + ")", values);

        return this;
    }

    public Query<M> whereNotNull(String field) {
        whereNotNulls.put(field, true);

        return this;
    }

    public Query<M> whereNull(String field) {
        whereNotNulls.put(field, false);

        return this;
    }

    public Query<M> with(String... relationships) {
        withs.addAll(Arrays.asList(relationships));

        return this;
    }

    public Query<M> limit(int limit) {
        this.limit = limit;

        return this;
    }

    public Query<M> trashed() {
        whereNotNull(model.deletedTimestamp());

        return this;
    }

    public Query<M> withTrashed() {
        whereNotNulls.remove(model.deletedTimestamp());

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

        try {
            loadRelationships(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<M> get() {
        return execute();
    }

    public M first() {
        List<M> models = limit(1).execute();

        return models.size() == 1
                ? models.get(0)
                : null;
    }

    public int count() {
        return (int) select("COUNT(*)").get().get(0).get("COUNT(*)");
    }

    public String toSql() {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append(stringifyFields());
        sb.append(" FROM ");
        sb.append(model.getTableName());

        if (wheres.size() + whereNotNulls.size() > 0) {
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
            Object value = e.getValue();

            if (value instanceof List) {
                for (Object subvalue : (List) value) {
                    stmt.setObject(i++, subvalue);
                }
            } else {
                stmt.setObject(i++, e.getValue());
            }
        }
    }

    protected void loadRelationships(List<M> models) throws Exception {
        List keys = models.stream()
                .map(model -> model.getKey())
                .collect(Collectors.toList());

        for (String relationshipName : withs) {
            Method relationshipMethod = modelClass.getMethod(relationshipName);
            Relationship relationship = (Relationship) relationshipMethod.invoke(model);
            Query query = relationship.getQuery();

            query.wheres.clear();
            query.whereIn(relationship.getForeignKeyName(), keys);

            List<Model> allRelatedModels = (List<Model>) query.get();

            for (Model model : models) {
                List<Model> relatedModels = allRelatedModels.stream()
                        .filter(relatedModel -> relatedModel.get(relationship.getForeignKeyName()).equals(model.getKey()))
                        .collect(Collectors.toList());

                model.set("_cache_" + relationship.getForeignKeyName(), relatedModels);
            }
        }
    }

    public String stringifyFields() {
        return fields.size() == 0
                ? "*"
                : String.join(", ", fields);
    }

    public String stringifyWheres() {
        List<String> list = new ArrayList<>(wheres.keySet());

        for (Map.Entry<String, Boolean> e : whereNotNulls.entrySet()) {
            list.add(e.getKey() + (e.getValue()
                    ? " IS NOT NULL"
                    : " IS NULL"));
        }

        return String.join(" AND ", list);
    }

}
