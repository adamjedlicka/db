package jeda00.db;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MigrationsTest {

    public Migrations migrations;

    @Before
    public void setUp() {
        Connection.reset();

        migrations = new Migrations(Connection.get());
    }

    @Test
    public void itListsMigrations() {
        List<String> allMigrations = migrations.getAllMigrations();

        assertEquals(1, allMigrations.size());
        assertEquals("00001_create_users_table.sql", allMigrations.get(0));
    }

    @Test
    public void itExecutesMigrations() {
        assertTrue(migrations.runMigrations());
    }

}
