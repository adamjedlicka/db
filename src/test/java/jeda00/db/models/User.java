package jeda00.db.models;

public class User extends Model<Integer> {

    public User() {

    }

    public User(String firstName, String lastName) {
        set("first_name", firstName);
        set("last_name", lastName);
    }

}
