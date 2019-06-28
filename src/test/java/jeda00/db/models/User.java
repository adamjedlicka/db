package jeda00.db.models;

import jeda00.db.Query;

public class User extends Model<Integer> {

    public User() {
        //
    }

    public User(String firstName, String lastName) {
        set("first_name", firstName);
        set("last_name", lastName);
    }

    public static Query<User, Integer> query() {
        return new Query<>(User.class);
    }

}
