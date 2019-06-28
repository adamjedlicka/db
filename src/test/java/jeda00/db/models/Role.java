package jeda00.db.models;

import jeda00.db.relationships.BelongsTo;

public class Role extends Model<Integer> {

    public Role() {

    }

    public Role(User user, String name) {
        set("user_id", user.getKey());
        set("name", name);
    }

    public BelongsTo<Role, User> user() {
        return new BelongsTo<>(this, User.class);
    }

}
