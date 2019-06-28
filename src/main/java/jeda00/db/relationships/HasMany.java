package jeda00.db.relationships;

import jeda00.db.Model;

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

    public HasMany<M, R> limit(int limit) {
        query.limit(limit);

        return this;
    }

    public List<R> all() {
        return query.all();
    }

    public R first() {
        return query.first();
    }

}
