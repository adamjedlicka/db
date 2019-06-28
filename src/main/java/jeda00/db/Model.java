package jeda00.db;

import jeda00.db.statements.Delete;
import jeda00.db.statements.Insert;
import jeda00.db.statements.Update;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public abstract class Model<K> {

    protected Map<String, Object> attributes;

    public Model() {
        this.attributes = new HashMap<>();
    }

    public abstract Connection getConnection();

    public Object get(String attribute) {
        return attributes.get(attribute);
    }

    public void set(String attribute, Object value) {
        attributes.put(attribute, value);
    }

    public boolean save() {
        if (getKey() == null) {
            return new Insert<>(this).execute();
        } else {
            return new Update<>(this).execute();
        }
    }

    public boolean delete() {
        return new Delete<>(this).execute();
    }

    public String getTableName() {
        return getClass().getSimpleName().toLowerCase() + "s";
    }

    public String getKeyName() {
        return "id";
    }

    public K getKey() {
        return (K) get(getKeyName());
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return getTableName() + "." + getKey().toString();
    }
}
