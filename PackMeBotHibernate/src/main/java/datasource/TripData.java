package datasource;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import datasource.entity.Thing;
import datasource.entity.Trip;
import datasource.entity.UserTripEntity;
import datasource.intefaces.TripDAO;
import presentation.PackMeBot;


public class TripData implements TripDAO {
	//DBConnection dbConnection = new DBConnection();
	//SessionFactory factory;

	public TripData() {
	}
	
	@Override
	public Trip findById(int tripID) throws DAOException {
		System.out.println("Начало получения объекта \"Trip\" по ID");		//log message
//		System.out.println("Started of obtaining object \"Trip\"");		//log message
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			Trip trip = session.get(Trip.class, tripID);
			session.getTransaction().commit();
			System.out.println("Выполнено");		//log message
//			System.out.println("get Trip Done!");		//log message
			return trip;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in TripData.findById()", e);
		}finally{
			session.close();
		}
		
	}
	
	public Trip findByParameters(String direction, String correction) throws DAOException {
		System.out.println("Начало получения объекта \"Trip\" по параметру \"Направление\"");		//log message
//		System.out.println("Started of obtaining object \"Trip\"");		//log message
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			List<Trip> trips = session.createQuery("from Trip " +
								"where direction = '" + direction + "' "+
								"AND correction = '" + correction + "' ")
					.getResultList();
			session.getTransaction().commit();
			System.out.println("Выполнено");		//log message
//			System.out.println("get Trip Done!");		//log message
			if(trips != null && trips.size() > 0) {
				return trips.get(0);
			}else {
				return null;
			}
			
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in TripData.findById()", e);
		}finally{
			session.close();
		}
		
	}

	@Override
	public void addNewTrip(String direction, String correction) throws DAOException {
		System.out.println("Начало сохранения объекта \"Trip\"");	//log message
//		System.out.println("Started of save object \"Trip\"");		//log message
		Session session = null;
		try{
			Trip trip = new Trip(direction, correction);
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			session.save(trip);
			session.getTransaction().commit();
			System.out.println("Выполнено");			//log message
//			System.out.println("Save Trip Done!");		//log message
//			return trip;
		}catch (Exception e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in TripData.addNewTrip()", e);
		}finally{
			session.close();
		}
	}
	
	@Override
	public void addNewTrip(Trip trip) throws DAOException {
		System.out.println("Начало сохранения объекта \"Trip\"");	//log message
//		System.out.println("Started of save object \"Trip\"");		//log message
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			//session.save(trip);
			session.persist(trip);
			//session.saveOrUpdate(trip);
			session.getTransaction().commit();
			System.out.println("Выполнено");			//log message
//			System.out.println("Save Trip Done!");		//log message
//			return trip;
		}catch (Exception e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in TripData.addNewTrip()", e);
		}finally{
			session.close();
		}
	}
	
	public void findOrSave(Trip trip) throws DAOException {
		System.out.println("Начало поиска объекта \"Trip\"");	//log message
//		System.out.println("Started of save object \"Trip\"");		//log message
		if(findByParameters(trip.getDirection(), trip.getCorrection()) == null) {
			System.out.println("Объект не найден. Начинаю записывать");
			addNewTrip(trip);
		}else {
			System.out.println("Объект найден");
		}
	}
	
	
	
	//public void addNewUserTrip(UserTrip userTrip) throws DAOException {
		public void addNewUserTrip(Trip trip, Thing thing) throws DAOException {
		System.out.println("Начало сохранения объекта \"UserTrip\"");	//log message
//		System.out.println("Started of save object \"Trip\"");		//log message
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			//session.save(trip);
			//session.persist(userTrip);
			
			
			//session.save(trip);
			
			trip.addThing(thing);
			//session.save(thing);
//			thing.addTrip(trip);
			
			session.getTransaction().commit();
			System.out.println("Выполнено");			//log message
//			System.out.println("Save Trip Done!");		//log message
//			return trip;
		}catch (Exception e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in TripData.addNewUserTrip()", e);
		}finally{
			if(session.isOpen()) {
				session.close();
			}
		}
	}

}
