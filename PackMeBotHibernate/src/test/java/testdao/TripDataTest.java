package testdao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import datasource.DAOException;
import datasource.TripData;
import datasource.entity.Trip;
import presentation.PackMeBot;
import datasource.entity.Kit;
import datasource.entity.Thing;

/*Перечень тестов:
// 1. Поиск имеющейся в базе поездки
// Новые поездки:
// 2. Имеющиеся направления с набором, который уже использовался //TODO: Доделать
// 6. Новое Направление, новое Уточнение, новый набор
// Другие методы:
// 7. Частые Направления
// 8. Частые Уточнения для Направления
// 9. Частые Уточнения для Направления (с ошибкой)
// 10. Список вещей для поездки
// 11. Список вещей для поездки (с ошибкой в Направлении)
// 12. Список вещей для поездки (с ошибкой в Уточнении)
*/

public class TripDataTest {
	static PackMeBot packMebot;
	static TripData tripData;

	static List<Thing> existingThingList1 = new ArrayList<Thing>();
	static List<Thing> newThingList1 = new ArrayList<Thing>();
	
	static Thing existingThing1;
	static Thing existingThing2;
	static Thing existingThing3;
	static Thing newThing1;
	static Thing newThing2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		packMebot = new PackMeBot();
		tripData = new TripData();
		
		existingThing1 = new Thing("Паспорт", "Документы");
		existingThing2 = new Thing("Билеты", "Документы");
		existingThing3 = new Thing("Деньги", "нет");
		
		existingThingList1.add(existingThing1);
		existingThingList1.add(existingThing2);
		existingThingList1.add(existingThing3);
		
		//Для тестов с новыми поездками
		newThing1 = new Thing("Вещь1", "Категория1");
		newThing2 = new Thing("Вещь2", "Категория1");
		Session session = null;
		try {
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			session.save(newThing1);
			session.save(newThing2);
			
			session.getTransaction().commit();
		} catch (HibernateException e) {
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally{
			session.close();

		}
		newThingList1.add(newThing1);
		newThingList1.add(newThing2);
		//***
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Session session = null;
		try {
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
//			Trip trip = (Trip)session.createQuery("from Trip where "
//					+ "direction = 'Направление' AND "
//					+ "correction = 'Уточнение'").getSingleResult();
			Trip newTrip = (Trip)session.createQuery("from Trip where "
					+ "direction = 'Направление1' AND "
					+ "correction = 'Уточнение1'").getSingleResult();
//			Kit kit = trip.getKit();
			Kit newKit = newTrip.getKit();
//			session.delete(trip);
			session.delete(newTrip);
			session.delete(newThing1);
			session.delete(newThing2);
//			session.delete(kit);
			session.delete(newKit);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally{
			session.close();
			PackMeBot.factory.close();
		}
	}

	//Проверка функции для поиска или добавления (в случае отсутствия) в базу новой поездки.
	//Вариант с поездкой, существующей в базе (Id=4)	
	@Test
	public void testExistingTrip() throws DAOException {
		Trip actualTrip = tripData.findOrAdd("Отпуск", "Другой город", existingThingList1);
		int actualTripId = actualTrip.getTripID();
		assertEquals(14, actualTripId);
	}
	
	
	//Проверка функции для поиска или добавления (в случае отсутствия) в базу новой поездки.
	//Вариант с новой поездкой и набором, существующим в базе (Id=4)
	//TODO: Требуется доработка
	@Ignore
	@Test
	public void testNewTripExistingKit() throws DAOException {
		Trip actualTrip = tripData.findOrAdd("Направление", "Уточнение", existingThingList1);
		boolean actual = false;
		if(actualTrip.getTripID() > 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	//Проверка функции для поиска или добавления (в случае отсутствия) в базу новой поездки.
	//Вариант с новой поездкой и новым набором	
	@Test
	public void testNewTripNewKit() throws DAOException {
		Trip actualTrip = tripData.findOrAdd("Направление1", "Уточнение1", newThingList1);
		boolean actual = false;
		if(actualTrip.getTripID() > 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	

	@Test
	public void testGetFrequentDirection() throws DAOException {
		List<Trip> actualList = tripData.getFrequentDirection(3);
		boolean actual = false;
		if (actualList != null && actualList.size() > 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	@Test
	public void testGetFrequentCorrection1() throws DAOException {
		List<Trip> actualList = tripData.getFrequentCorrection("Отпуск", 3);
		boolean actual = false;
		if (actualList != null && actualList.size() > 0) {
			actual = true;
		}
		assertTrue(actual);
	}

	//Вариант с неверным "Направлением"
	@Test
	public void testGetFrequentCorrection2() throws DAOException {
		List<Trip> actualList = tripData.getFrequentCorrection("Отп", 3);
		boolean actual = false;
		if (actualList != null && actualList.size() == 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	
	@Test
	public void testGetThingsList1() throws DAOException {
		List<Thing> actualList = tripData.getThingsList("Отпуск", "Другой город");
		boolean actual = false;
		if (actualList != null && actualList.size() > 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	//Вариант с неверным "Направлением"
	@Test
	public void testGetThingsList2() throws DAOException {
		List<Thing> actualList = tripData.getThingsList("Отп...", "Другой город");
		boolean actual = false;
		if (actualList != null && actualList.size() == 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	//Вариант с неверным "Уточнением"
	@Test
	public void testGetThingsList3() throws DAOException {
		List<Thing> actualList = tripData.getThingsList("Отпуск", "Другой г...");
		boolean actual = false;
		if (actualList != null && actualList.size() == 0) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	

}
