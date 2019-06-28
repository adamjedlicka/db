package jeda00.db.relationships;

import jeda00.db.Model;

public class BelongsTo<M extends Model<?>, R extends Model<?>> extends Relationship<M, R> {

    public BelongsTo(M model, Class<R> relatedClass, String foreignKey) {
        super(model, relatedClass, foreignKey);

        query.where(related.getKeyName(), model.get(getForeignKeyName()));
    }

    public BelongsTo(M model, Class<R> relatedClass) {
        this(model, relatedClass, relatedClass.getSimpleName().toLowerCase() + "_id");
    }

    public R get() {
        return query.first();
    }

    public void set(R related) {
        model.set(getForeignKeyName(), related.getKey());
    }

}
