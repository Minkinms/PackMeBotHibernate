package datasource;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import datasource.entity.Kit;
import datasource.entity.Thing;
import presentation.PackMeBot;

public class ThingData {
	
	public Thing findOrAddThing(Thing thingToFind) throws DAOException {
		int thingId = findThing(thingToFind);
		if(thingId < 0) {
			thingId = addThing(thingToFind);
		}
		return getThing(thingId);
	}
	
	
	//Метод для поиска ID вещи 
	public int findThing(Thing thingToFind) throws DAOException {
		System.out.println("Старт метода findThing()");		//log message
		Session session = null;
		int resultId;
		List<Thing> allThings = new ArrayList<Thing>();
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			allThings = session.createQuery("from Thing").getResultList();
			
			for(Thing thing : allThings) {
				if(thing.equals(thingToFind)) {
					resultId = thing.getId();
					session.getTransaction().commit();
					System.out.println("Выполнено. Метод findThing(). Вещь найдена");		//log message
					return resultId;
				}
			}
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод findThing(). Вещь не найдена");		//log message
			return -1;
			
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in findThing()", e);
		}finally{
			session.close();
		}
	}
	
	
	private int addThing(Thing thingToAdd) throws DAOException {
		System.out.println("Старт метода addThing()");		//log message
		Session session = null;
		int thingId;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			session.save(thingToAdd);
			thingId = thingToAdd.getId();
			session.getTransaction().commit();
			return thingId;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in addThing()", e);
		}finally{
			session.close();
		}
	}
	

	public Thing getThing(int thingID) throws DAOException {
		System.out.println("Начало получения объекта \"Thing\" по ID");		//log message
//		System.out.println("Started of obtaining object \"Trip\"");		//log message
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			Thing thing = session.get(Thing.class, thingID);
			session.getTransaction().commit();
			System.out.println("Выполнено");		//log message
//			System.out.println("get Thing Done!");		//log message
			return thing;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in TripData.findById()", e);	//TODO: Ввести полный путь в других сообщениях 
		}finally{
			session.close();
		}
	}
}
