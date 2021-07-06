package datasource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import datasource.entity.Kit;
import datasource.entity.Thing;
import datasource.entity.Trip;
import datasource.intefaces.TripDAO;
import presentation.PackMeBot;


public class TripData implements TripDAO {
	//Метод для поиска поездки по параметрам.
	//Если подходящая поездка не найдена, то в базу записывается новая
	public Trip findOrAdd(String direction, String correction, List<Thing> thingList) throws DAOException {
		System.out.println("Старт метода TripData.findOrAdd()");		//log message
		Trip resultTrip = new Trip();
		Kit kitThings = new KitData().findOrAddKit(thingList);
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			List<Trip> tripList = session.createQuery("from Trip where "
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
			//Если поездка с подходящим набором не найдена, 
			//добавляется новая поездка с новым набором
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

	//Метод для получения списка направлений поездок.
	//Возвращаемый список сортируется по частоте использования направлений
	public List<Trip> getFrequentDirection(int numberOfTrips) throws DAOException{
		System.out.println("Старт метода TripData.getFrequentDirection()");		//log message
		Session session = null;
		List<Trip> resultList = new ArrayList<Trip>();
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			resultList = session.createQuery("from Trip").getResultList();
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод TripData.getFrequentDirection()");		//log message
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
			throw new DAOException("Error in TripData.getFrequentDirection()", e);
		}finally{
			session.close();
		}
	}
	
	//Метод для получения списка уточнений для направления поездки.
	//Возвращаемый список сортируется по частоте использования уточнений
	public List<Trip> getFrequentCorrection(String direction, int numberOfTrips) throws DAOException{
		System.out.println("Старт метода TripData.getFrequentCorrection()");		//log message
		Session session = null;
		List<Trip> resultList = new ArrayList<Trip>();
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			resultList = session.createQuery("from Trip where direction = " + 
								"'" + direction + "'").getResultList();
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод TripData.getFrequentCorrection()");		//log message
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
			throw new DAOException("Error in TripData.getFrequentCorrection()", e);
		}finally{
			session.close();
		}
	}
	
	//Метод для получения списка вещей для выбранной поездки.
	//Возвращаемый список сортируется по частоте использования вещей в подобных поездках
	public List<Thing> getThingsList(String direction, String correction) throws DAOException{
			System.out.println("Старт метода TripData.getThingsList()");		//log message
		Session session = null;
		List<Thing> resultList = new ArrayList<Thing>();
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			List<Trip> tripList = session.createQuery("from Trip where " +
							"direction = '" + direction + "' " +
							"AND correction = '" + correction + "'").getResultList();
			resultList = getSortedList(tripList);	//TODO: Возможно передать resultList как параметр и перенести метод в return
			session.getTransaction().commit();
				System.out.println("Выполнено. Метод TripData.getThingsList()");		//log message
			return resultList;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in TripData.getThingsList()", e);
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
