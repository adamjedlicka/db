package jeda00.db.models;

import jeda00.db.relationships.HasMany;
import jeda00.db.Query;

public class User extends Model<Integer> {

    public User() {
        //
    }

    public User(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getFirstName() {
        return getString("first_name");
    }

    public void setFirstName(String firstName) {
        setString("first_name", firstName);
    }

    public String getLastName() {
        return getString("last_name");
    }

    public void setLastName(String lastName) {
        setString("last_name", lastName);
    }

    public HasMany<User, Role> roles() {
        return new HasMany<>(this, Role.class);
    }

    public static Query<User> query() {
        return new Query<>(User.class);
    }

}
