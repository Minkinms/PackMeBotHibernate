package datasource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import datasource.entity.Kit;
import datasource.entity.Thing;
import datasource.entity.Trip;
import presentation.PackMeBot;


public class TripData  {
	
	public Trip findOrAdd(String direction, String correction, List<Thing> thingList) throws DAOException {
		System.out.println("Старт метода TripData.findOrAdd()");		//log message
		Trip resultTrip = new Trip();
		Kit kitThings = new KitThingsData().findOrAddKit(thingList);
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			List<Trip> tripList = session.createQuery("from TripEntity where "
					+ "direction = '" + direction + "' AND "
					+ "correction = '" + correction + "'").getResultList();

			for(Trip trip : tripList) {
				if(trip.getKit().equals(kitThings) ) {
					trip.upUseCount();
					resultTrip = trip;
					session.getTransaction().commit();
					System.out.println("Выполнено. Метод TripData.findOrAdd()");		//log message
					return resultTrip;
				}
			}
			Trip tripToAdd = new Trip(direction,correction,kitThings);
			tripToAdd.setUseCount(1);
			session.save(tripToAdd);
			resultTrip = tripToAdd;
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод TripData.findOrAdd()");		//log message
			return resultTrip;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in TripData.findOrAdd()", e);
		}finally{
			session.close();
		}
	}
	
//	public void addTrip(String direction, String correction, List<Thing> thingList) throws DAOException {
//		System.out.println("Старт метода addTrip()");		//log message
//		//Для записи поездки нужен Kit для вещей
//		//поэтому вначале определим его
////		KitThingsData kitThingsData = new KitThingsData();
//		KitThingsEntity kitThings = new KitThingsData().findOrAddKit(thingList);
//		
//		Session session = null;
//		try{
//			session = PackMeBot.factory.getCurrentSession();
//			session.beginTransaction();
////			resultList = session.createQuery("from TripEntity where direction = " + 
////								"'" + direction + "'").getResultList();
//			List<TripEntity> tripList = (List<TripEntity>) session.createQuery("from TripEntity where "
//					+ "direction = '" + direction + "'"
//					+ "correction = '" + correction + "'"
//					+ "kit = '" + kitThings + "'");
//			
//			if(tripList.size() > 0) {
//				tripList.get(0).upUseCount();
//			}else {
//				TripEntity tripToAdd = new TripEntity(direction,correction,kitThings);
//				tripToAdd.setUseCount(1);
//				session.save(tripToAdd);
//			}
//			session.getTransaction().commit();
//			System.out.println("Выполнено. Метод getFrequentCorrection()");		//log message
//	
//		}catch (HibernateException e){
//			if(session.getTransaction() != null ) {
//				session.getTransaction().rollback();
//			}
//			throw new DAOException("Error in getFrequentCorrection()", e);
//		}finally{
//			session.close();
//		}
//	}

	
	
	
	
	
	
	
	public List<Trip> getFrequentDirection(int numberOfTrips) throws DAOException{
		System.out.println("Старт метода getFrequentDirection()");		//log message
		Session session = null;
		List<Trip> resultList = new ArrayList<Trip>();
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
	
	
	public List<Trip> getFrequentCorrection(String direction, int numberOfTrips) throws DAOException{
		System.out.println("Старт метода getFrequentCorrection()");		//log message
		Session session = null;
		List<Trip> resultList = new ArrayList<Trip>();
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
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			List<Trip> tripList = session.createQuery("from TripEntity where " +
							"direction = '" + direction + "' " +
							"AND correction = '" + correction + "'").getResultList();
			resultList = getSortedList(tripList);	//TODO: Возможно передать resultList как параметр и перенести метод в return
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
	private List<Thing> getSortedList(List<Trip> tripList) {
		List<UsedThing> usedThingList = new ArrayList<UsedThing>();
		List<Thing> resultList = new ArrayList<Thing>();	
		for(Trip trip : tripList) {				
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
