package jeda00.db.statements;

import jeda00.db.Model;
import jeda00.db.Transaction;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    protected Set<String> getFields() {
        Set<String> fields = new HashSet<>(model.getAttributes().keySet());

        if (model.createdTimestamp() != null) fields.add(model.createdTimestamp());
        if (model.updatedTimestamp() != null) fields.add(model.updatedTimestamp());
        if (model.deletedTimestamp() != null) fields.add(model.deletedTimestamp());

        return fields;
    }

    protected List<String> getFieldsWithoutKey() {
        return getFields().stream()
                .filter(e -> !e.equals(model.getKeyName()))
                .collect(Collectors.toList());
    }

    protected List<Object> getValuesWithoutKey() {
        return getFields().stream()
                .filter(e -> !e.equals(model.getKeyName()))
                .map(e -> model.get(e))
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
