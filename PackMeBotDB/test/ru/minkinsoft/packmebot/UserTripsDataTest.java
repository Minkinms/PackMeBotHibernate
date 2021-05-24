package ru.minkinsoft.packmebot;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ru.minkinsoft.packmebot.datasource.DAOException;
import ru.minkinsoft.packmebot.datasource.UserTripsData;
import ru.minkinsoft.packmebot.domain.Thing;
import ru.minkinsoft.packmebot.domain.Trip;
import ru.minkinsoft.packmebot.domain.UserTrip;

public class UserTripsDataTest {
	public static UserTripsData utd;
	static UserTrip userTrip1;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		utd = new UserTripsData();
		
		List<Thing> thingsUT1 = new ArrayList<Thing>();
		thingsUT1.add(new Thing("Вещь1", "Категория1"));
		thingsUT1.add(new Thing("Вещь2", "Категория2"));

		userTrip1 = new UserTrip("Направление1", "Уточнение1", thingsUT1);
		userTrip1.setUserID(12345);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tripsdata", 
				 "postgres", "1234");
		Statement statement = connection.createStatement();
		statement.executeUpdate("DELETE FROM tripsdata_sh.user_trips WHERE user_id = 12345");
		statement.executeUpdate("DELETE FROM tripsdata_sh.trips WHERE direction = 'Направление1'");
		statement.executeUpdate("DELETE FROM tripsdata_sh.trips WHERE correction = 'Уточнение1'");
		statement.executeUpdate("DELETE FROM tripsdata_sh.things WHERE name = 'Вещь1'");
		statement.executeUpdate("DELETE FROM tripsdata_sh.things WHERE name = 'Вещь2'");
		statement.executeUpdate("DELETE FROM tripsdata_sh.things WHERE category = 'Категория1'");
		statement.executeUpdate("DELETE FROM tripsdata_sh.things WHERE category = 'Категория2'");
		statement.close();
		connection.close();
	}

	@Test
	public void testAddNewUserTrip() throws DAOException{
			long lastIDBefore = utd.getLastID();
			utd.addNewUserTrip(userTrip1);
			long lastIDAfter = utd.getLastID();
			assertEquals(lastIDBefore + 1, lastIDAfter);
	}

	@Ignore
	@Test
	public void testDeleteUserTrip() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFrequentDirection() throws DAOException {
		List<Trip> actualList = utd.getFrequentDirection(3);
		boolean actual = false;
		if (actualList != null && actualList.size() > 0) {
			actual = true;
		}
		assertTrue(actual);
	}

	@Test
	public void testGetFrequentCorrection1() throws DAOException {
		List<Trip> actualList = utd.getFrequentCorrection("Командировка", 3);
		boolean actual = false;
		if (actualList != null && actualList.size() > 0) {
			actual = true;
		}
		assertTrue(actual);
	}

	//Вариант с неверным "Направлением"
	@Test
	public void testGetFrequentCorrection2() throws DAOException {
		List<Trip> actualList = utd.getFrequentCorrection("Командир", 3);
		boolean actual = false;
		if (actualList != null && actualList.size() == 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	
	@Test
	public void testGetThingsList1() throws DAOException {
		List<Thing> actualList = utd.getThingsList("Командировка", "Другой город");
		boolean actual = false;
		if (actualList != null && actualList.size() > 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	//Вариант с неверным "Направлением"
	@Test
	public void testGetThingsList2() throws DAOException {
		List<Thing> actualList = utd.getThingsList("Коман...", "Другой город");
		boolean actual = false;
		if (actualList != null && actualList.size() == 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	//Вариант с неверным "Уточнением"
	@Test
	public void testGetThingsList3() throws DAOException {
		List<Thing> actualList = utd.getThingsList("Командировка", "Другой г...");
		boolean actual = false;
		if (actualList != null && actualList.size() == 0) {
			actual = true;
		}
		assertTrue(actual);
	}

}
