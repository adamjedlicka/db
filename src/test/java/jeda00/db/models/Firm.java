package jeda00.db.models;

import jeda00.db.Query;

import java.util.Date;

public class Firm extends Model<Integer> {

    public Firm() {
        //
    }

    public Firm(String name) {
        setName(name);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        setString("name", name);
    }

    public Date getCreatedAt() {
        return getDate("created_at");
    }

    public Date getUpdatedAt() {
        return getDate("updated_at");
    }

    public Date getDeletedAt() {
        return getDate("deleted_at");
    }

    @Override
    public String createdTimestamp() {
        return "created_at";
    }

    @Override
    public String updatedTimestamp() {
        return "updated_at";
    }

    @Override
    public String deletedTimestamp() {
        return "deleted_at";
    }

    public static Query<Firm> query() {
        return new Query<>(Firm.class);
    }

}
