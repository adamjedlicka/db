package jeda00.db;

import jeda00.db.models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueryTest {

    @Before
    public void setUp() {
        Migrations migrations = new Migrations(Connection.get());
        migrations.runMigrations();
    }

    @Test
    public void itGetsRecordsFromTheDatabase() {
        User u1 = new User("Franta", "Sádlo");
        assertTrue(u1.save());
        User u2 = new User("Jirka", "Máslo");
        assertTrue(u2.save());
        User u3 = new User("Pepa", "Pažitka");
        assertTrue(u3.save());

        Query<User, Integer> query = new Query<>(User.class);

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
    public void itInterferesFromClass() {
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

    public void testComplexQuery() {
        Query<User, Integer> query = User.query()
                .select("id", "first_name", "last_name", "age")
                .where("first_name", "Franta")
                .where("age", 20)
                .limit(10);

        assertEquals(
                "SELECT id, first_name, last_name FROM users WHERE first_name = 'Franta' AND age = 20 LIMIT 10",
                query.toSql()
        );

        List<User> users = query.execute();

        assertEquals(0, users.size());
    }

}
