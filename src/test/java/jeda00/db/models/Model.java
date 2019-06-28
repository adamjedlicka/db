package jeda00.db.models;

import java.sql.Connection;

public abstract class Model<K> extends jeda00.db.Model<K> {

    @Override
    public Connection getConnection() {
        return jeda00.db.Connection.get();
    }

}
