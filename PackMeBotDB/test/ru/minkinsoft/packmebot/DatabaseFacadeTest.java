package ru.minkinsoft.packmebot;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ru.minkinsoft.packmebot.datasource.DatabaseFacade;
import ru.minkinsoft.packmebot.domain.Thing;
import ru.minkinsoft.packmebot.domain.UserTrip;

public class DatabaseFacadeTest {
	public static DatabaseFacade dbF;
	
	static UserTrip userTrip1;
	static UserTrip userTrip2;
	static UserTrip userTrip3;
	static UserTrip userTrip4;
	static UserTrip userTrip5;
	static UserTrip userTrip6;
	static UserTrip userTrip7;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbF = new DatabaseFacade();
		
		List<Thing> thingsUT1 = new ArrayList<Thing>();
		thingsUT1.add(new Thing("Паспорт", "Документы"));
		//thingsUT1.add(new Thing("Блокнот", "Канцелярия"));
		List<Thing> thingsUT2 = new ArrayList<Thing>();
		thingsUT2.add(new Thing("Паспорт", "Документы"));
		thingsUT2.add(new Thing("Блокнот", "Канцелярия"));
		thingsUT2.add(new Thing("Загранпаспорт", "Документы"));
		List<Thing> thingsUT3 = new ArrayList<Thing>();
		thingsUT3.add(new Thing("Паспорт", "Документы"));
		thingsUT3.add(new Thing("Блокнот", "Канцелярия"));
		thingsUT3.add(new Thing("Визитка", "Свое"));
		List<Thing> thingsUT5 = new ArrayList<Thing>();
		thingsUT5.add(new Thing("Паспорт", "Документы"));
		thingsUT5.add(new Thing("Соль", "Продукты"));
		thingsUT5.add(new Thing("Стулья", "Инвентарь"));

		 userTrip1 = new UserTrip("Командировка", "Москва", thingsUT1);
		 userTrip1.setUserID(12345);
		 userTrip2 = new UserTrip("Направление", "Москва", thingsUT1);
		 userTrip2.setUserID(12345);
		 userTrip3 = new UserTrip("Командировка", "Уточнение", thingsUT1);
		 userTrip3.setUserID(12345);
		 userTrip4 = new UserTrip("Направление", "Уточнение", thingsUT1);
		 userTrip4.setUserID(12345);
		 userTrip5 = new UserTrip("Природа", "Пикник", thingsUT5);
		 userTrip6 = new UserTrip("Другой город", "Сочи", thingsUT3);
		 userTrip7 = new UserTrip("Командировка", "Москва", thingsUT1);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dbF.deleteRow("DELETE FROM tripsdata_sh.result WHERE user_id = 12345");
		dbF.deleteRow("DELETE FROM tripsdata_sh.trips WHERE direction = 'Направление'");
		dbF.deleteRow("DELETE FROM tripsdata_sh.trips WHERE correction = 'Уточнение'");
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//Вариант, когда направление, уточнение и вещи уже есть в базе
	@Test
	public void test1WriteUserTrip() {
		try {
			long nextIDBefore = dbF.getNextID("user_trips_id", "TripsData_sh.result");
			dbF.writeUserTrip(userTrip1);
			long nextIDAfter = dbF.getNextID("user_trips_id", "TripsData_sh.result");
			assertEquals(nextIDBefore + 1, nextIDAfter);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
		
	//Вариант, когда уточнение и вещи уже есть в базе.
	//Направление новое
	@Test
	public void test2WriteUserTrip() {
		try {
			long nextIDBeforeResult = dbF.getNextID("user_trips_id", "TripsData_sh.result");
			long nextIDBeforeTrips = dbF.getNextID("trips_id", "TripsData_sh.trips");
			dbF.writeUserTrip(userTrip2);
			long nextIDAfterResult = dbF.getNextID("user_trips_id", "TripsData_sh.result");
			long nextIDAfterTrips = dbF.getNextID("trips_id", "TripsData_sh.trips");
			assertEquals(nextIDBeforeResult + 1, nextIDAfterResult);
			assertEquals(nextIDBeforeTrips + 1, nextIDAfterTrips);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Вариант, когда направление и вещи уже есть в базе.
	//Уточнение новое
	@Test
	public void test3WriteUserTrip() {
		try {
			long nextIDBeforeResult = dbF.getNextID("user_trips_id", "TripsData_sh.result");
			long nextIDBeforeTrips = dbF.getNextID("trips_id", "TripsData_sh.trips");
			dbF.writeUserTrip(userTrip3);
			long nextIDAfterResult = dbF.getNextID("user_trips_id", "TripsData_sh.result");
			long nextIDAfterTrips = dbF.getNextID("trips_id", "TripsData_sh.trips");
			assertEquals(nextIDBeforeResult + 1, nextIDAfterResult);
			assertEquals(nextIDBeforeTrips + 1, nextIDAfterTrips);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Вариант, когда вещи уже есть в базе.
	//Направление и Уточнение новое
	@Test
	public void test4WriteUserTrip() {
		try {
			long nextIDBeforeResult = dbF.getNextID("user_trips_id", "TripsData_sh.result");
			long nextIDBeforeTrips = dbF.getNextID("trips_id", "TripsData_sh.trips");
			dbF.writeUserTrip(userTrip4);
			long nextIDAfterResult = dbF.getNextID("user_trips_id", "TripsData_sh.result");
			long nextIDAfterTrips = dbF.getNextID("trips_id", "TripsData_sh.trips");
			assertEquals(nextIDBeforeResult + 1, nextIDAfterResult);
			assertEquals(nextIDBeforeTrips + 1, nextIDAfterTrips);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
		

	

	@Ignore
	@Test
	public void testGetFrequentCorrection() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetFrequentDirection() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetThingsList() {
		fail("Not yet implemented");
	}

}
