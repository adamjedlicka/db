package jeda00.db.models;

public class Role extends Model<Integer> {

    public Role() {

    }

    public Role(User user, String name) {
        setUser(user);
        set("name", name);
    }

    public void setUser(User user) {
        set("user_id", user.getKey());
    }

}
