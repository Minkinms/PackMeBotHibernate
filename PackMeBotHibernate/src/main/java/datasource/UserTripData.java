package datasource;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import datasource.entity.Thing;
import datasource.entity.UserTrip;
import datasource.intefaces.UserTripDAO;
import presentation.PackMeBot;

public class UserTripData implements UserTripDAO{
	
	//Метод для сохранения завершенной подготовки к поездке
	public void writeUserTrip(int userID, 
								String direction, 
								String correction, 
								List<Thing> tookThingList) throws DAOException {
		System.out.println("Старт метода UserTripData.writeUserTrip()");		//log message
		UserTrip userTripToAdd = new UserTrip(userID);
		userTripToAdd.setTrip(new TripData().findOrAdd(direction, correction, tookThingList));
		
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			session.save(userTripToAdd);
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод UserTripData.writeUserTrip()");		//log message
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in UserTripData.writeUserTrip()", e);
		}finally{
			session.close();
		}
		
		
		
	}

}
