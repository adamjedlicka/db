package jeda00.db.statements;

import jeda00.db.Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Delete<M extends Model<?>> extends Statement<M> {

    public Delete(M model) {
        super(model);
    }

    @Override
    public boolean execute() throws SQLException {
        if (model.deletedTimestamp() != null) {
            model.setDate(model.deletedTimestamp(), new Date());
            return model.save();
        }

        PreparedStatement stmt = model.getConnection().prepareStatement(toSql());
        bindValues(stmt);
        stmt.execute();

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
