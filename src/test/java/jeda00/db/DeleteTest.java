package jeda00.db;

import jeda00.db.models.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeleteTest {

    @Before
    public void setUp() {
        Connection.reset();
        Migrations migrations = new Migrations(Connection.get());
        migrations.runMigrations();
    }

    @Test
    public void itDeletesRecordsFromTheDatabase() {
        User u1 = new User("Franta", "Sádlo");
        assertTrue(u1.save());
        User u2 = new User("Jirka", "Máslo");
        assertTrue(u2.save());
        User u3 = new User("Pepa", "Pažitka");
        assertTrue(u3.save());

        assertEquals(3, User.query().all().size());

        u1.delete();

        assertEquals(2, User.query().all().size());

        u2.delete();

        assertEquals(1, User.query().all().size());

        u3.delete();

        assertEquals(0, User.query().all().size());
    }

}
