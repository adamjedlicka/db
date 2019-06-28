package jeda00.db.models;

import jeda00.db.relationships.HasMany;
import jeda00.db.Query;

import java.time.Instant;
import java.util.Date;

public class User extends Model<Integer> {

    public User() {
        //
    }

    public User(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
        setCreatedAt(new Date());
    }

    public String getFirstName() {
        return (String) get("first_name");
    }

    public void setFirstName(String firstName) {
        set("first_name", firstName);
    }

    public String getLastName() {
        return (String) get("last_name");
    }

    public void setLastName(String lastName) {
        set("last_name", lastName);
    }

    public Date getCreatedAt() {
        return Date.from(Instant.ofEpochSecond((int) get("created_at")));
    }

    public void setCreatedAt(Date date) {
        set("created_at", date.toInstant().getEpochSecond());
    }

    public HasMany<User, Role> roles() {
        return new HasMany<>(this, Role.class);
    }

    public static Query<User> query() {
        return new Query<>(User.class);
    }

}
