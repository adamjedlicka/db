package jeda00.db;

import jeda00.db.models.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InsertTest {

    @Before
    public void setUp() {
        Connection.reset();
        Migrations migrations = new Migrations(Connection.get());
        migrations.runMigrations();
    }

    @Test
    public void itInsertsRecordIntoTheDatabase() {
        User u = new User("Adam", "Jedlička");

        Insert<User, Integer> insert = new Insert<>(u);

        assertEquals(
                "INSERT INTO users (last_name, first_name) VALUES (?, ?)",
                insert.toSql()
        );

        assertTrue(insert.execute());
    }

}
