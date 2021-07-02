package testdao;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import datasource.DAOException;
import datasource.TripData;
import presentation.PackMeBot;

public class TripDataTest {
	static PackMeBot packMebot;
	static TripData tripData;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		packMebot = new PackMeBot();
		tripData = new TripData();
	}

	@SuppressWarnings("static-access")
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		packMebot.factory.close();
	}

	
	@Test
	public void findByIdTest() throws DAOException {
//		TripTest tripExpected = new TripTest("Отдых", "Кемпинг");
//		TripTest tripActual = tripData.findById(7);
//		assertEquals(tripExpected, tripActual);
	}

}
