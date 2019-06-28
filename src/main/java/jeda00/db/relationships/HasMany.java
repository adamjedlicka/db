package jeda00.db.relationships;

import jeda00.db.Model;
import jeda00.db.Query;

import java.util.List;

public class HasMany<M extends Model<?>, R extends Model<?>> {

    protected M model;

    protected Class<M> modelClass;

    protected R related;

    protected Class<R> relatedClass;

    protected Query<R> query;

    protected String foreignKey;

    public HasMany(M model, Class<R> relatedClass, String foreignKey) {
        try {
            this.related = relatedClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
            return;
        }

        this.model = model;
        this.modelClass = (Class<M>) model.getClass();
        this.relatedClass = relatedClass;
        this.query = new Query<>(relatedClass);
        this.foreignKey = foreignKey;

        this.query.where(getForeignKey(), model.getKey());
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

    public String getForeignKey() {
        return foreignKey;
    }

}
