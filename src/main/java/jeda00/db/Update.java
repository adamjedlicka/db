package jeda00.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class Update<M extends Model<K>, K> extends Statement<M, K> {

    public Update(M model) {
        super(model);
    }

    @Override
    public boolean execute() {
        return transaction(() -> {
            PreparedStatement stmt = model.getConnection().prepareStatement(toSql());
            bindValues(stmt);
            stmt.execute();
        });
    }

    @Override
    public String toSql() {
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE ");
        sb.append(model.getTableName());
        sb.append(" SET ");
        sb.append(
                getFieldsWithoutKey().stream()
                        .map(f -> f + " = ?")
                        .collect(Collectors.joining(", "))
        );
        sb.append(" WHERE ");
        sb.append(model.getKeyName());
        sb.append(" = ?");

        return sb.toString();
    }

    protected void bindValues(PreparedStatement stmt) throws SQLException {
        int i = 1;

        for (Object value : getValuesWithoutKey()) {
            stmt.setObject(i++, value);
        }

        stmt.setObject(i++, model.getKey());
    }
}
