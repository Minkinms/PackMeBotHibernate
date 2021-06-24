package datasource;

import org.hibernate.HibernateException;
import org.hibernate.Session;


import datasource.entity.Thing;
import datasource.entity.TripTest;
import presentation.PackMeBot;

public class ThingData {

	public Thing findById(int thingID) throws DAOException {
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
			throw new DAOException("Error in TripData.findById()", e);
		}finally{
			session.close();
		}
	}
}
