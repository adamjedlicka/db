package jeda00.db.statements;

import jeda00.db.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class Insert<M extends Model<?>> extends Statement<M> {

    public Insert(M model) {
        super(model);
    }

    @Override
    public boolean execute() {
        return transaction(() -> {
            PreparedStatement stmt = model.getConnection().prepareStatement(toSql());
            bindValues(stmt);
            stmt.execute();

            stmt = model.getConnection().prepareStatement("SELECT last_insert_rowid()");
            ResultSet rs = stmt.executeQuery();

            model.set(model.getKeyName(), rs.getObject(1));
        });
    }

    @Override
    public String toSql() {
        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO ");
        sb.append(model.getTableName());
        sb.append(" (");
        sb.append(String.join(", ", getFieldsWithoutKey()));
        sb.append(") VALUES (");
        sb.append(
                getFieldsWithoutKey().stream()
                        .map(f -> "?")
                        .collect(Collectors.joining(", "))
        );
        sb.append(")");

        return sb.toString();
    }

    protected void bindValues(PreparedStatement stmt) throws SQLException {
        int i = 1;

        for (Object value : getValuesWithoutKey()) {
            stmt.setObject(i++, value);
        }
    }
}
