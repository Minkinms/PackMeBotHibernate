package datasource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import datasource.entity.Thing;
import datasource.entity.TripEntity;
import datasource.entity.UserTripEntity;
import datasource.intefaces.TripDAO;
import domain.Trip;
import presentation.PackMeBot;


public class TripData  {
	//DBConnection dbConnection = new DBConnection();
	//SessionFactory factory;
	
	public List<TripEntity> getFrequentDirection(int numberOfTrips) throws DAOException{
		System.out.println("Старт метода getFrequentDirection()");		//log message
		Session session = null;
		List<TripEntity> resultList = new ArrayList<TripEntity>();
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			resultList = session.createQuery("from TripEntity").getResultList();
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод getFrequentDirection()");		//log message
			
			if(resultList.size() > 0) {
				resultList.sort((o1, o2) -> Integer.compare(o2.getUseCount(), o1.getUseCount()));
				if (numberOfTrips > 0 && numberOfTrips < resultList.size()) {
					return resultList.subList(0, numberOfTrips);
				} else return resultList;
			}else return resultList;
			
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in getFrequentDirection()", e);
		}finally{
			session.close();
		}
	}
	
	
	public List<TripEntity> getFrequentCorrection(String direction, int numberOfTrips) throws DAOException{
		System.out.println("Старт метода getFrequentCorrection()");		//log message
		Session session = null;
		List<TripEntity> resultList = new ArrayList<TripEntity>();
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			resultList = session.createQuery("from TripEntity where direction = " + 
								"'" + direction + "'").getResultList();
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод getFrequentCorrection()");		//log message
			
			if(resultList.size() > 0) {
				resultList.sort((o1, o2) -> Integer.compare(o2.getUseCount(), o1.getUseCount()));
				if (numberOfTrips > 0 && numberOfTrips < resultList.size()) {
					return resultList.subList(0, numberOfTrips);
				} else return resultList;
			}else return resultList;
			
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in getFrequentCorrection()", e);
		}finally{
			session.close();
		}
	}
	
	public List<Thing> getThingsList(String direction, String correction) throws DAOException{
			System.out.println("Старт метода getThingsList()");		//log message
		Session session = null;
		List<Thing> resultList = new ArrayList<Thing>();
		List<UsedThing> usedThingList = new ArrayList<UsedThing>();
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			List<TripEntity> tripList = session.createQuery("from TripEntity where " +
							"direction = '" + direction + "' " +
							"AND correction = '" + correction + "'").getResultList();
			resultList = getSortedList(usedThingList, tripList);
			session.getTransaction().commit();
				System.out.println("Выполнено. Метод getThingsList()");		//log message
			return resultList;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in getThingsList()", e);
		}finally{
			session.close();
		}
	}
	
	//Метод для получения списка вещей, сортированного по количеству использований
	private List<Thing> getSortedList(List<UsedThing> usedThingList, List<TripEntity> tripList) {
		List<Thing> resultList = new ArrayList<Thing>();	
		for(TripEntity trip : tripList) {				
			for(Thing thing : trip.getKit().getThingList()) {
				UsedThing usedThing = new UsedThing(thing);
				if(usedThingList.contains(usedThing)) {
					int i = usedThingList.indexOf(usedThing);
					usedThingList.get(i).addUnit();
				}else {
					usedThingList.add(new UsedThing(thing));
				}
			}
		}
		
		usedThingList.sort(new Comparator<UsedThing>() {
			@Override
			public int compare(UsedThing arg0, UsedThing arg1) {
				if (!arg0.getThing().getCategory()
						.equals(arg1.getThing().getCategory())) {
					return arg0.getThing().getCategory()
							.compareTo(arg1.getThing().getCategory());
	        	} else {
	        		return Integer.compare(arg1.getUseCount(), arg0.getUseCount());
	        		}
	        	}
		});
		
		for(UsedThing UThing : usedThingList) {
			resultList.add(UThing.getThing());
		}
		return resultList;
	}	
	
	
	
//	
//	@Override
//	public Trip findById(int tripID) throws DAOException {
//		System.out.println("Начало получения объекта \"Trip\" по ID");		//log message
////		System.out.println("Started of obtaining object \"Trip\"");		//log message
//		Session session = null;
//		try{
//			session = PackMeBot.factory.getCurrentSession();
//			session.beginTransaction();
//			Trip trip = session.get(Trip.class, tripID);
//			session.getTransaction().commit();
//			System.out.println("Выполнено");		//log message
////			System.out.println("get Trip Done!");		//log message
//			return trip;
//		}catch (HibernateException e){
//			if(session.getTransaction() != null ) {
//				session.getTransaction().rollback();
//			}
//			throw new DAOException("Error in TripData.findById()", e);
//		}finally{
//			session.close();
//		}
//		
//	}
//	
//	public Trip findByParameters(String direction, String correction) throws DAOException {
//		System.out.println("Начало получения объекта \"Trip\" по параметру \"Направление\"");		//log message
////		System.out.println("Started of obtaining object \"Trip\"");		//log message
//		Session session = null;
//		try{
//			session = PackMeBot.factory.getCurrentSession();
//			session.beginTransaction();
//			List<Trip> trips = session.createQuery("from Trip " +
//								"where direction = '" + direction + "' "+
//								"AND correction = '" + correction + "' ")
//					.getResultList();
//			session.getTransaction().commit();
//			System.out.println("Выполнено");		//log message
////			System.out.println("get Trip Done!");		//log message
//			if(trips != null && trips.size() > 0) {
//				return trips.get(0);
//			}else {
//				return null;
//			}
//			
//		}catch (HibernateException e){
//			if(session.getTransaction() != null ) {
//				session.getTransaction().rollback();
//			}
//			throw new DAOException("Error in TripData.findById()", e);
//		}finally{
//			session.close();
//		}
//		
//	}
//
//	@Override
//	public void addNewTrip(String direction, String correction) throws DAOException {
//		System.out.println("Начало сохранения объекта \"Trip\"");	//log message
////		System.out.println("Started of save object \"Trip\"");		//log message
//		Session session = null;
//		try{
//			Trip trip = new Trip(direction, correction);
//			session = PackMeBot.factory.getCurrentSession();
//			session.beginTransaction();
//			session.save(trip);
//			session.getTransaction().commit();
//			System.out.println("Выполнено");			//log message
////			System.out.println("Save Trip Done!");		//log message
////			return trip;
//		}catch (Exception e){
//			if(session.getTransaction() != null ) {
//				session.getTransaction().rollback();
//			}
//			throw new DAOException("Error in TripData.addNewTrip()", e);
//		}finally{
//			session.close();
//		}
//	}
//	
//	@Override
//	public void addNewTrip(Trip trip) throws DAOException {
//		System.out.println("Начало сохранения объекта \"Trip\"");	//log message
////		System.out.println("Started of save object \"Trip\"");		//log message
//		Session session = null;
//		try{
//			session = PackMeBot.factory.getCurrentSession();
//			session.beginTransaction();
//			//session.save(trip);
//			session.persist(trip);
//			//session.saveOrUpdate(trip);
//			session.getTransaction().commit();
//			System.out.println("Выполнено");			//log message
////			System.out.println("Save Trip Done!");		//log message
////			return trip;
//		}catch (Exception e){
//			if(session.getTransaction() != null ) {
//				session.getTransaction().rollback();
//			}
//			throw new DAOException("Error in TripData.addNewTrip()", e);
//		}finally{
//			session.close();
//		}
//	}
//	
//	public void findOrSave(Trip trip) throws DAOException {
//		System.out.println("Начало поиска объекта \"Trip\"");	//log message
////		System.out.println("Started of save object \"Trip\"");		//log message
//		if(findByParameters(trip.getDirection(), trip.getCorrection()) == null) {
//			System.out.println("Объект не найден. Начинаю записывать");
//			addNewTrip(trip);
//		}else {
//			System.out.println("Объект найден");
//		}
//	}
//	
//	
//	
//	//public void addNewUserTrip(UserTrip userTrip) throws DAOException {
//		public void addNewUserTrip(Trip trip, Thing thing) throws DAOException {
//		System.out.println("Начало сохранения объекта \"UserTrip\"");	//log message
////		System.out.println("Started of save object \"Trip\"");		//log message
//		Session session = null;
//		try{
//			session = PackMeBot.factory.getCurrentSession();
//			session.beginTransaction();
//			//session.save(trip);
//			//session.persist(userTrip);
//			
//			
//			//session.save(trip);
//			
//			trip.addThing(thing);
//			//session.save(thing);
////			thing.addTrip(trip);
//			
//			session.getTransaction().commit();
//			System.out.println("Выполнено");			//log message
////			System.out.println("Save Trip Done!");		//log message
////			return trip;
//		}catch (Exception e){
//			if(session.getTransaction() != null ) {
//				session.getTransaction().rollback();
//			}
//			throw new DAOException("Error in TripData.addNewUserTrip()", e);
//		}finally{
//			if(session.isOpen()) {
//				session.close();
//			}
//		}
//	}

}


class UsedThing{
	private int useCount;
	private Thing thing;
	public UsedThing(Thing thing) {
		this.thing = thing;
		this.useCount = 1;
	}
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	
	public void addUnit() {
		this.useCount++;
	}
	
	public Thing getThing() {
		return thing;
	}
	public void setThing(Thing thing) {
		this.thing = thing;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((thing == null) ? 0 : thing.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsedThing other = (UsedThing) obj;
		if (thing == null) {
			if (other.thing != null)
				return false;
		} else if (!thing.equals(other.thing))
			return false;
		return true;
	}
	
}
