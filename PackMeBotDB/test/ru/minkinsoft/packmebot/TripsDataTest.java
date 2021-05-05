package ru.minkinsoft.packmebot;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TripsDataTest {
	private static TripsData tripsData;
	static List<UserTrip> allTrips;    //Полный список поездок

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String tripHistoryPath = "C:\\Java\\Progwards\\PackMe\\src\\TripHistory.txt";
		tripsData = new TripsData(tripHistoryPath);
		allTrips = new ArrayList<>();    //Полный список поездок

		List<Thing> thingsUT1 = new ArrayList<Thing>();
		thingsUT1.add(new Thing("Паспорт", "Документы"));
		thingsUT1.add(new Thing("Блокнот", "Свое"));
		List<Thing> thingsUT2 = new ArrayList<Thing>();
		thingsUT2.add(new Thing("Паспорт", "Документы"));
		thingsUT2.add(new Thing("Блокнот", "Свое"));
		thingsUT2.add(new Thing("Загранпаспорт", "Документы"));
		List<Thing> thingsUT3 = new ArrayList<Thing>();
		thingsUT3.add(new Thing("Паспорт", "Документы"));
		thingsUT3.add(new Thing("Блокнот", "Свое"));
		thingsUT3.add(new Thing("Визитка", "Свое"));
		List<Thing> thingsUT5 = new ArrayList<Thing>();
		thingsUT5.add(new Thing("Паспорт", "Документы"));
		thingsUT5.add(new Thing("Соль", "Продукты"));
		thingsUT5.add(new Thing("Стулья", "Инвентарь"));

		UserTrip userTrip1 = new UserTrip("Командировка", "Москва", thingsUT1);
		UserTrip userTrip2 = new UserTrip("Командировка", "Москва", thingsUT2);
		UserTrip userTrip3 = new UserTrip("Командировка", "Элерон", thingsUT3);
		UserTrip userTrip4 = new UserTrip("Другой город", "Сочи", thingsUT1);
		UserTrip userTrip5 = new UserTrip("Природа", "Пикник", thingsUT5);
		UserTrip userTrip6 = new UserTrip("Другой город", "Сочи", thingsUT3);
		UserTrip userTrip7 = new UserTrip("Командировка", "Москва", thingsUT1);
		
		allTrips.add(userTrip1);
		allTrips.add(userTrip2);
		allTrips.add(userTrip3);
		allTrips.add(userTrip4);
		allTrips.add(userTrip5);
		allTrips.add(userTrip6);
		allTrips.add(userTrip7);
		
		tripsData.allTrips = allTrips;
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		allTrips.clear();
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testTripsData() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testWriteTrip() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFrequentTripsList() {
		List<Trip> expected = new ArrayList<Trip>();
		expected.add(new Trip("Командировка", "Москва"));
		expected.add(new Trip("Другой город", "Сочи"));
		
		List<Trip> actual = new ArrayList<Trip>(tripsData.getFrequentTripsList(null, 2));
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetFrequentTripsListWithDirection() {
		List<Trip> expected = new ArrayList<Trip>();
		expected.add(new Trip("Командировка", "Москва"));
		expected.add(new Trip("Командировка", "Элерон"));
		List<Trip> actual = new ArrayList<Trip>(tripsData.getFrequentTripsList("Командировка", 2));
		
		assertEquals(expected, actual);
	}


	@Test
	public void testGetTingsList() {
		List<Thing> expected = new ArrayList<Thing>();
		expected.add(new Thing("Паспорт", "Документы"));
		expected.add(new Thing("Блокнот", "Свое"));
		expected.add(new Thing("Загранпаспорт", "Документы"));
		expected.add(new Thing("Визитка", "Свое"));
		expected.add(new Thing("Соль", "Продукты"));
		expected.add(new Thing("Стулья", "Инвентарь"));
		List<Thing> actual = new ArrayList<Thing>(tripsData.getTingsList());
		
		assertEquals(expected, actual);
	}

}
