package jeda00.db.relationships;

import jeda00.db.Model;

import java.sql.SQLException;
import java.util.List;

public class HasMany<M extends Model<?>, R extends Model<?>> extends Relationship<M, R> {

    public HasMany(M model, Class<R> relatedClass, String foreignKey) {
        super(model, relatedClass, foreignKey);

        query.where(getForeignKeyName(), model.getKey());
    }

    public HasMany(M model, Class<R> relatedClass) {
        this(model, relatedClass, model.getClass().getSimpleName().toLowerCase() + "_id");
    }

    public HasMany<M, R> select(String... fields) {
        query.select(fields);

        return this;
    }

    public HasMany<M, R> where(String field, Object value) {
        query.where(field, value);

        return this;
    }

    public HasMany<M, R> where(String field, String operator, Object value) {
        query.where(field, operator, value);

        return this;
    }

    public HasMany<M, R> whereNotNull(String field) {
        query.whereNotNull(field);

        return this;
    }

    public HasMany<M, R> whereNull(String field) {
        query.whereNull(field);

        return this;
    }

    public HasMany<M, R> limit(int limit) {
        query.limit(limit);

        return this;
    }

    public HasMany<M, R> trashed() {
        query.trashed();

        return this;
    }

    public HasMany<M, R> withTrashed() {
        query.withTrashed();

        return this;
    }

    public List<R> get() {
        List<R> cache = (List<R>) model.get("_cache_" + getForeignKeyName());
        if (cache != null) {
            return cache;
        }

        return query.get();
    }

    public R first() {
        return query.first();
    }

    public boolean sync(R... models) throws SQLException {
        for (R m : select(related.getKeyName()).get()) {
            m.delete();
        }

        for (R m : models) {
            m.set(getForeignKeyName(), model.getKey());
            m.save();
        }

        return true;
    }

}
