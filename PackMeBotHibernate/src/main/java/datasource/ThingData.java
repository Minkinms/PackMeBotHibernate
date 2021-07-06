package datasource;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import datasource.entity.Thing;
import datasource.intefaces.ThingDAO;
import presentation.PackMeBot;

public class ThingData implements ThingDAO{
	
	//Метод для поиска вещи в базе.
	//Если вещь не найдена, она добавляется в базу как новая  
	public Thing findOrAddThing(Thing thingToFind) throws DAOException {
		int thingId = findThing(thingToFind);
		if(thingId < 0) {
			thingId = addThing(thingToFind);
		}
		return getThing(thingId);
	}
	
	
	//Метод для получения Id вещи 
	private int findThing(Thing thingToFind) throws DAOException {
		System.out.println("Старт метода ThingData.findThing()");		//log message
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
					System.out.println("Выполнено. Метод ThingData.findThing(). Вещь найдена");		//log message
					return resultId;
				}
			}
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод ThingData.findThing(). Вещь не найдена");		//log message
			return -1;
			
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in ThingData.findThing()", e);
		}finally{
			session.close();
		}
	}
	
	//Метод для добавления вещи в базу
	private int addThing(Thing thingToAdd) throws DAOException {
		System.out.println("Старт метода ThingData.addThing()");		//log message
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
			throw new DAOException("Error in ThingData.addThing()", e);
		}finally{
			session.close();
		}
	}
	
	//Метод для поиска вещи по Id 
	private Thing getThing(int thingID) throws DAOException {
		System.out.println("Старт метода ThingData.getThing()");		//log message
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			Thing thing = session.get(Thing.class, thingID);
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод ThingData.getThing(). Вещь не найдена");	//log message
			return thing;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in ThingData.getThing()", e);	 
		}finally{
			session.close();
		}
	}
}
