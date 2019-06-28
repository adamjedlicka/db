package jeda00.db.models;

import jeda00.db.Query;
import jeda00.db.relationships.BelongsTo;

public class Role extends Model<Integer> {

    public Role() {

    }

    public Role(User user, String name) {
        user().set(user);
        setName(name);
    }

    public String getName() {
        return (String) get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getDescription() {
        return (String) get("description");
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public BelongsTo<Role, User> user() {
        return new BelongsTo<>(this, User.class);
    }

    public static Query<Role> query() {
        return new Query<>(Role.class);
    }

}
