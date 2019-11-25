package jeda00.db.relationships;

import jeda00.db.Connection;
import jeda00.db.Migrations;
import jeda00.db.models.Role;
import jeda00.db.models.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

public class BelongsToTest {

    @Before
    public void setUp() {
        Connection.reset();
        Migrations migrations = new Migrations(Connection.get());
        migrations.runMigrations();
    }

    @Test
    public void itReturnsRelatedModels() throws SQLException {
        User u1 = new User("Franta", "Sádlo");
        assertTrue(u1.save());
        User u2 = new User("Jirka", "Máslo");
        assertTrue(u2.save());

        Role r1 = new Role(u1, "Admin");
        assertTrue(r1.save());
        Role r2 = new Role(u2, "Teacher");
        assertTrue(r2.save());
        Role r3 = new Role(u1, "Assistant");
        assertTrue(r3.save());

        User ru1 = r1.user().get();
        assertEquals(u1.getKey(), ru1.getKey());

        User ru2 = r2.user().get();
        assertEquals(u2.getKey(), ru2.getKey());

        User ru3 = r3.user().get();
        assertEquals(u1.getKey(), ru3.getKey());
    }

}
