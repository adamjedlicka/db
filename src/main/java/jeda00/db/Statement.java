package jeda00.db;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Statement<M extends Model<?>> {

    protected M model;

    protected Class<M> modelClass;

    protected Connection connection;

    public Statement(M model) {
        this.model = model;
        this.modelClass = (Class<M>) model.getClass();
        this.connection = model.getConnection();
    }

    public abstract boolean execute();

    public abstract String toSql();

    protected List<String> getFieldsWithoutKey() {
        return model.getAttributes().entrySet().stream()
                .filter(e -> !e.getKey().equals(model.getKeyName()))
                .map(e -> e.getKey())
                .collect(Collectors.toList());
    }

    protected List<Object> getValuesWithoutKey() {
        return model.getAttributes().entrySet().stream()
                .filter(e -> !e.getKey().equals(model.getKeyName()))
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    protected boolean transaction(Transaction.Callback callback) {
        return Transaction.run(connection, callback);
    }

    @Override
    public String toString() {
        return toSql();
    }

}
