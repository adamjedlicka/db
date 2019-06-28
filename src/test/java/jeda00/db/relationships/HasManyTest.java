package jeda00.db.relationships;

import jeda00.db.Connection;
import jeda00.db.Migrations;
import jeda00.db.models.Role;
import jeda00.db.models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HasManyTest {

    @Before
    public void setUp() {
        Connection.reset();
        Migrations migrations = new Migrations(Connection.get());
        migrations.runMigrations();
    }

    @Test
    public void itReturnsRelatedModels() {
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

        List<Role> roles1 = u1.roles().all();
        assertEquals(2, roles1.size());

        List<Role> roles2 = u2.roles().all();
        assertEquals(1, roles2.size());
    }

}
