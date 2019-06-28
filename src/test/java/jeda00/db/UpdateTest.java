package jeda00.db;

import jeda00.db.models.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateTest {

    @Before
    public void setUp() {
        Connection.reset();
        Migrations migrations = new Migrations(Connection.get());
        migrations.runMigrations();
    }

    @Test
    public void itUpdatesRecordsInTheDatabase() {
        User u = new User("Adam", "Jedlička");

        Insert<User, Integer> insert = new Insert<>(u);

        assertTrue(insert.execute());

        u.set("first_name", "Jirka");

        Update<User, Integer> update = new Update<>(u);

        assertEquals(
                "UPDATE users SET last_name = ?, first_name = ? WHERE id = ?",
                update.toSql()
        );

        assertTrue(update.execute());
    }

}
