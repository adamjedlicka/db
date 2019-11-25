package jeda00.db.relationships;

import jeda00.db.Model;
import jeda00.db.Query;

public abstract class Relationship<M extends Model<?>, R extends Model<?>> {

    protected M model;

    protected Class<M> modelClass;

    protected R related;

    protected Class<R> relatedClass;

    protected Query<R> query;

    protected String foreignKey;

    public Relationship(M model, Class<R> relatedClass, String foreignKey) {
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
    }

    public String getForeignKeyName() {
        return foreignKey;
    }

    public String toSql() {
        return query.toSql();
    }

    public Query<R> getQuery() {
        return query;
    }

}
