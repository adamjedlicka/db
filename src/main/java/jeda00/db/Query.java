package jeda00.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Query<M extends Model<K>, K> {

    protected M model;

    protected Class<M> modelClass;

    public Query(Class<M> modelClass) {
        this.modelClass = modelClass;

        try {
            this.model = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<M> execute() {
        List<M> list = new ArrayList<>();

        try {
            PreparedStatement stmt = model.getConnection().prepareStatement(toSql());
            stmt.execute();

            ResultSet rs = stmt.getResultSet();
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                M model = modelClass.newInstance();

                for(int i = 1; i <= meta.getColumnCount(); i++) {
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

    public String toSql() {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append("*");
        sb.append(" FROM ");
        sb.append(model.getTableName());

        return sb.toString();
    }

}
