package jeda00.db;

import jeda00.db.statements.Delete;
import jeda00.db.statements.Insert;
import jeda00.db.statements.Update;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
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

    public String getString(String attribute) {
        return (String) get(attribute);
    }

    public void setString(String attribute, String value) {
        set(attribute, value);
    }

    public Integer getInteger(String attribute) {
        return (Integer) get(attribute);
    }

    public void setInteger(String attribute, Integer value) {
        set(attribute, value);
    }

    public Date getDate(String attribute) {
        return Date.from(Instant.ofEpochMilli((Long) get(attribute)));
    }

    public void setDate(String attribute, Date value) {
        set(attribute, value.toInstant().toEpochMilli());
    }

    public boolean save() throws SQLException {
        if (getKey() == null) {
            return new Insert<>(this).execute();
        } else {
            return new Update<>(this).execute();
        }
    }

    public boolean delete() throws SQLException {
        return new Delete<>(this).execute();
    }

    public String getTableName() {
        return getClass().getSimpleName().toLowerCase() + "s";
    }

    public String getKeyName() {
        return "id";
    }

    @SuppressWarnings("unchecked")
    public K getKey() {
        return (K) get(getKeyName());
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public String createdTimestamp() {
        return null;
    }

    public String updatedTimestamp() {
        return null;
    }

    public String deletedTimestamp() {
        return null;
    }

    @Override
    public String toString() {
        return getTableName() + "." + getKey().toString();
    }
}
