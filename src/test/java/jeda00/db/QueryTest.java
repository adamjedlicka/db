package jeda00.db;

import jeda00.db.models.Firm;
import jeda00.db.models.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueryTest {

    @Before
    public void setUp() {
        Connection.reset();
        Migrations migrations = new Migrations(Connection.get());
        migrations.runMigrations();
    }

    @Test
    public void itGetsRecordsFromTheDatabase() throws SQLException {
        User u1 = new User("Franta", "Sádlo");
        assertTrue(u1.save());
        User u2 = new User("Jirka", "Máslo");
        assertTrue(u2.save());
        User u3 = new User("Pepa", "Pažitka");
        assertTrue(u3.save());

        Query<User> query = new Query<>(User.class);

        assertEquals(
                "SELECT * FROM users",
                query.toSql()
        );

        List<User> users = query.execute();

        assertEquals("Franta", users.get(0).get("first_name"));
        assertEquals("Jirka", users.get(1).get("first_name"));
        assertEquals("Pepa", users.get(2).get("first_name"));
    }

    @Test
    public void itInterferesFromClass() throws SQLException {
        User u1 = new User("Franta", "Sádlo");
        assertTrue(u1.save());
        User u2 = new User("Jirka", "Máslo");
        assertTrue(u2.save());
        User u3 = new User("Pepa", "Pažitka");
        assertTrue(u3.save());

        List<User> users = User.query().execute();

        assertEquals("Franta", users.get(0).get("first_name"));
        assertEquals("Jirka", users.get(1).get("first_name"));
        assertEquals("Pepa", users.get(2).get("first_name"));
    }

    @Test
    public void testComplexQuery() throws SQLException {
        User u1 = new User("Franta", "Sádlo");
        assertTrue(u1.save());
        User u2 = new User("Jirka", "Máslo");
        assertTrue(u2.save());
        User u3 = new User("Pepa", "Pažitka");
        assertTrue(u3.save());

        Query<User> query = User.query()
                .select("id", "first_name", "last_name")
                .where("first_name", "Franta")
                .where("last_name", "!=", "Máslo")
                .whereNotNull("id")
                .limit(10);

        assertEquals(
                "SELECT id, first_name, last_name FROM users WHERE last_name != ? AND first_name = ? AND id IS NOT NULL LIMIT 10",
                query.toSql()
        );

        List<User> users = query.execute();

        assertEquals(1, users.size());
    }

    @Test
    public void itHasCount() throws SQLException {
        assertEquals(0, User.query().count());

        User u1 = new User("Franta", "Sádlo");
        assertTrue(u1.save());

        assertEquals(1, User.query().count());

        User u2 = new User("Jirka", "Máslo");
        assertTrue(u2.save());

        assertEquals(2, User.query().count());

        User u3 = new User("Pepa", "Pažitka");
        assertTrue(u3.save());

        assertEquals(3, User.query().count());

        u2.delete();

        assertEquals(2, User.query().count());
    }

    @Test
    public void itHandlesSoftDeletes() throws SQLException {
        Firm f1 = new Firm("A");
        f1.save();

        Firm f2 = new Firm("B");
        f2.save();

        Firm f3 = new Firm("C");
        f3.save();

        assertEquals(3, Firm.query().count());

        assertTrue(f2.delete());

        assertEquals(2, Firm.query().count());
        assertEquals(1, Firm.query().trashed().count());
        assertEquals(3, Firm.query().withTrashed().count());
    }

}
