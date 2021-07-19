package testdao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import datasource.DAOException;
import datasource.KitData;
import datasource.entity.Kit;
import datasource.entity.Thing;
import presentation.PackMeBot;

public class KitDataTest {
	static PackMeBot packMebot;
	static KitData kitData;
	
	static Kit existingKit;
	static Kit newKitExistingThings;
	static Kit newKitNewThings;
	static Kit newKitMixedThings;
	
	static List<Thing> newThingList = new ArrayList<Thing>();
	static List<Thing> existingThingList1 = new ArrayList<Thing>();
	static List<Thing> existingThingList2 = new ArrayList<Thing>();
	static List<Thing> mixedThingList =  new ArrayList<Thing>();
	
	static Thing newThing1;
	static Thing newThing2;
	static Thing newThing3;
	static Thing newThing4;
	static Thing newThing5;
	static Thing existingThing1;
	static Thing existingThing2;
	static Thing existingThing3;
	static Thing existingThing4;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		packMebot = new PackMeBot();
		kitData = new KitData();
		
		newThing1 = new Thing("Вещь1", "Категория1");
		newThing2 = new Thing("Вещь2", "Категория1");
		newThingList.add(newThing1);
		newThingList.add(newThing2);
		
		existingThing1 = new Thing("Паспорт", "Документы");
		existingThing2 = new Thing("Билеты", "Документы");
		existingThing3 = new Thing("Телефон", "Девайсы");
		existingThing4 = new Thing("Подзарядка", "Девайсы");
		existingThingList1.add(existingThing1);
		existingThingList1.add(existingThing3);
		existingThingList1.add(existingThing4);
		
		newThing3 = new Thing("Вещь3", "Категория1");
		mixedThingList.add(newThing3);
		mixedThingList.add(existingThing1);
		
		existingKit = new Kit(existingThingList1);
		existingKit.setKitID(12);
		

		//Для теста testNewKitExistingThings()
		newThing4 = new Thing("Вещь4", "Категория1");
		newThing5 = new Thing("Вещь5", "Категория1");
		Session session = null;
		try {
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			session.save(newThing4);
			session.save(newThing5);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally{
			session.close();

		}
		existingThingList2.add(newThing4);
		existingThingList2.add(newThing5);
		//***
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Session session = null;
		try {
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			session.delete(newKitExistingThings);
			session.delete(newKitNewThings);
			session.delete(newKitMixedThings);
			session.delete(newThing1);
			session.delete(newThing2);
			session.delete(newThing3);
			session.delete(newThing4);
			session.delete(newThing5);
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

	
	//Проверка функции для поиска или добавления (в случае отсутствия) в базу нового набора.
	//Вариант с набором, существующим в базе (Id=4)
	//@Ignore
	@Test
	public final void testExistingKit() throws DAOException {
		Kit actualKit = kitData.findOrAddKit(existingThingList1);
		assertEquals(existingKit, actualKit);
	}
	
	//Проверка функции для поиска или добавления (в случае отсутствия) в базу нового набора.
	//Вариант с новым набором с вещами, существующими в базе
	@Test
	public final void testNewKitExistingThings() throws DAOException {
		Kit actualKit = kitData.findOrAddKit(existingThingList2);
		newKitExistingThings = actualKit;
		boolean actual = false;
		if(actualKit != null) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	//Проверка функции для поиска или добавления (в случае отсутствия) в базу нового набора.
	//Вариант с новым набором с вещами, отсутствующими в базе
	@Test
	public final void testNewKitNewThings() throws DAOException {
		Kit actualKit = kitData.findOrAddKit(newThingList);
		newKitNewThings = actualKit;
		boolean actual = false;
		if(actualKit != null) {
			actual = true;
		}
		assertTrue(actual);
	}
	
	//Проверка функции для поиска или добавления (в случае отсутствия) в базу нового набора.
	//Вариант с новым набором с вещами, отсутствующими и существующими в базе
	@Test
	public final void testNewKitMixedThings() throws DAOException {
		Kit actualKit = kitData.findOrAddKit(mixedThingList);
		newKitMixedThings = actualKit;
		boolean actual = false;
		if(actualKit != null) {
			actual = true;
		}
		assertTrue(actual);
	}

}
