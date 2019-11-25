package jeda00.db.statements;

import jeda00.db.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.stream.Collectors;

public class Insert<M extends Model<?>> extends Statement<M> {

    public Insert(M model) {
        super(model);
    }

    @Override
    public boolean execute() throws SQLException {
        if (model.createdTimestamp() != null) model.setDate(model.createdTimestamp(), new Date());
        if (model.updatedTimestamp() != null) model.setDate(model.updatedTimestamp(), new Date());

        PreparedStatement stmt = model.getConnection().prepareStatement(toSql(), java.sql.Statement.RETURN_GENERATED_KEYS);
        bindValues(stmt);
        stmt.execute();

        ResultSet rs = stmt.getGeneratedKeys();

        model.set(model.getKeyName(), rs.getObject(1));

        return true;
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
