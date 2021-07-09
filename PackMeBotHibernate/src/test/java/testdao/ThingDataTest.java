package testdao;

import static org.junit.Assert.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import datasource.DAOException;
import datasource.ThingData;
import datasource.entity.Thing;
import presentation.PackMeBot;

public class ThingDataTest {
	static PackMeBot packMebot;
	static ThingData thingData;
	
	static Thing newThing;
	static Thing existingThing;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		packMebot = new PackMeBot();
		thingData = new ThingData();
		
		newThing = new Thing("Вещь1", "Категория1");
		existingThing = new Thing("Паспорт", "Документы");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Session session = null;
		try {
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			session.delete(newThing);
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

	//Проверка функции для поиска или добавления (в случае отсутствия) в базу новой вещи
	//Вариант с вещью, отсутствующей в базе
	@Test
	public final void testNewThing() throws DAOException {
		Thing thingActual = thingData.findOrAddThing(newThing);
		assertEquals(newThing, thingActual);
	}
	
	//Проверка функции для поиска или добавления (в случае отсутствия) в базу новой вещи
	//Вариант с вещью, присутствующей в базе
	@Test
	public final void testExistingThing() throws DAOException {
		Thing thingActual = thingData.findOrAddThing(existingThing);
		assertEquals(existingThing, thingActual);
	}

}
