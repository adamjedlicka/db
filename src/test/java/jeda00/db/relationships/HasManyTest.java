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

    @Test
    public void itSyncsModels() {
        User u1 = new User("Franta", "Sádlo");
        assertTrue(u1.save());

        Role r1 = new Role();
        r1.setName("Admin");

        Role r2 = new Role();
        r2.setName("Teacher");

        Role r3 = new Role();
        r3.setName("Assistant");

        assertEquals(0, u1.roles().all().size());

        u1.roles().sync(r1, r2);

        assertEquals(2, u1.roles().all().size());
        assertEquals("Admin", u1.roles().all().get(0).getName());
        assertEquals("Teacher", u1.roles().all().get(1).getName());

        u1.roles().sync(r3);

        assertEquals(1, u1.roles().all().size());
        assertEquals("Assistant", u1.roles().all().get(0).getName());

        u1.roles().sync();

        assertEquals(0, u1.roles().all().size());
    }

}
