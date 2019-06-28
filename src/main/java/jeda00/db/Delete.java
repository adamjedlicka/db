package jeda00.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete<M extends Model<?>> extends Statement<M> {

    public Delete(M model) {
        super(model);
    }

    @Override
    public boolean execute() {
        try {
            PreparedStatement stmt = model.getConnection().prepareStatement(toSql());
            bindValues(stmt);
            stmt.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public String toSql() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM ");
        sb.append(model.getTableName());
        sb.append(" WHERE ");
        sb.append(model.getKeyName());
        sb.append(" = ?");

        return sb.toString();
    }

    protected void bindValues(PreparedStatement stmt) throws SQLException {
        stmt.setObject(1, model.getKey());
    }
}
