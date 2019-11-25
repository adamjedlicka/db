package jeda00.db;

import jeda00.db.models.Firm;
import jeda00.db.models.User;
import jeda00.db.statements.Insert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;

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
    public void itInsertsRecordIntoTheDatabase() throws SQLException {
        User u = new User("Adam", "Jedlička");

        Insert<User> insert = new Insert<>(u);

        assertEquals(
                "INSERT INTO users (last_name, first_name) VALUES (?, ?)",
                insert.toSql()
        );

        assertTrue(insert.execute());
    }

    @Test
    public void itSetsPrimaryKey() throws SQLException {
        User u1 = new User("Adam", "Jedlička");
        u1.save();

        User u2 = new User("Franta", "Sádlo");
        u2.save();

        assertEquals(1, u1.getKey().intValue());
        assertEquals(2, u2.getKey().intValue());
    }

    @Test
    public void itHandlesTimestamps() throws InterruptedException, SQLException {
        Firm firm = new Firm("ABRA");
        assertTrue(firm.save());

        Thread.sleep(5);
        Date date = new Date();
        Thread.sleep(5);

        assertTrue(firm.getCreatedAt().before(date));
        assertTrue(firm.getUpdatedAt().before(date));

        firm.setName("3ok");
        firm.save();

        assertTrue(firm.getCreatedAt().before(date));
        assertTrue(firm.getUpdatedAt().after(date));
    }

}
