package ru.minkinsoft.packmebot.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ru.minkinsoft.packmebot.domain.Thing;
import ru.minkinsoft.packmebot.domain.Trip;
import ru.minkinsoft.packmebot.domain.UserTrip;

public class UserTripsData implements UserTripsDao {
	
	public static void main(String[] args) {
		UserTripsData utd = new UserTripsData();
		
		try {
			System.out.println(utd.getFrequentDirection(5));
			System.out.println(utd.getFrequentCorrection("Командировка", 5));
			System.out.println(utd.getThingsList("Командировка", "Другой город"));
			System.out.println(utd.getLastID());
		} catch (DAOException e) {
			System.out.println(e.getMessage());
			System.out.println("Подавленных исключений " + e.getSuppressed().length);
			for(Throwable t : e.getSuppressed()) {
				System.out.println(t.getMessage());
			}
		}
	}
	
	
	DBConnection dbConn = new DBConnection();

	//Записать новую поездку в базу
	@Override
	public void addNewUserTrip(UserTrip userTrip) throws DAOException {
		TripsData tripsData = new TripsData();
		ThingsData thingsData = new ThingsData();

    	int userTripID = getLastID();
    	int userID = userTrip.getUserID();
    	Integer tripID = tripsData.findTripID(userTrip.getDirection(), 
    											userTrip.getCorrection());
    	List<Integer> thingsID = thingsData.getThingsID(userTrip.getUserTripThings());
    	
    	if(tripID == null) {
    		tripID = tripsData.addNewTrip(userTrip.getDirection(), userTrip.getCorrection());
    	}
    	
//    	for(int i = 0; i < thingsID.size(); i++) {
//    		writeTripToDB(userTripID, userID, tripsID, thingsID.get(i));
//    	}
		
	}

	@Override
	public void deleteUserTrip(int userTripId) {
		// TODO Auto-generated method stub
		
	}

	//Получить id последней поездки
	@Override
	public int getLastID() throws DAOException {
		int lastID = 0;
		String sql = "SELECT MAX (user_trip_id) FROM TripsData_sh.user_trips";
		 
		try(Connection connection = dbConn.getConnectionDB();
			Statement statement = connection.createStatement(); 
			ResultSet resultSet = statement.executeQuery(sql)){
			
			resultSet.next();
			lastID = resultSet.getInt("max");
		} catch (SQLException e) {
			throw new DAOException("Ошибка в UserTripsData.getLastID()", e);
		}
		return lastID;
	}

	// Метод для добавления поездки в список
	private void addTrip(List<Trip> tripList, Trip addedTrip) {
		if (tripList.contains(addedTrip)) {
			int count = tripList.get(tripList.indexOf(addedTrip)).getUseCount() + 1;
			tripList.get(tripList.indexOf(addedTrip)).setUseCount(count);
		} else {
			addedTrip.setUseCount(1);
			tripList.add(addedTrip);
		}
	}
	
	//Получить список поездок с частыми направлениями 
	@Override
	public List<Trip> getFrequentDirection(int numberOfTrips) throws DAOException  {
		List<Trip> resultList = new ArrayList<>();
		String sql = "SELECT direction FROM TripsData_sh.user_trips " 
				+ "INNER JOIN TripsData_sh.trips USING (trip_id) ";
		 
		try(Connection connection = dbConn.getConnectionDB();
			Statement statement = connection.createStatement(); 
			ResultSet resultSet = statement.executeQuery(sql)){

			while (resultSet.next()) {
				String tripDirection = resultSet.getString("direction");
				Trip trip = new Trip(tripDirection, "-");
				addTrip(resultList, trip);
			}
			
			if(resultList.size() > 0) {
				resultList.sort((o1, o2) -> Integer.compare(o2.getUseCount(), o1.getUseCount()));
				if (numberOfTrips > 0 && numberOfTrips < resultList.size()) {
					return resultList.subList(0, numberOfTrips);
				} else return resultList;
			}else return resultList;
		} catch (SQLException e) {
			throw new DAOException("Ошибка в UserTripsData.getFrequentDirection()", e);
		}
	}
	
	//Получить список поездок с частыми уточнениями для конкретного направления
	@Override
	public List<Trip> getFrequentCorrection(String direction, int numberOfTrips) throws DAOException {
		List<Trip> resultList = new ArrayList<>();
		String sql = "SELECT correction FROM TripsData_sh.user_trips " 
				+ "INNER JOIN TripsData_sh.trips USING (trip_id) " +
				"WHERE direction = '" + direction + "'";
		
		try(Connection connection = dbConn.getConnectionDB();
				Statement statement = connection.createStatement(); 
				ResultSet resultSet = statement.executeQuery(sql)){
		
			while (resultSet.next()) {
				String tripCorrection = resultSet.getString("correction");
				Trip trip = new Trip(direction, tripCorrection);
				addTrip(resultList, trip);
			}
			resultList.sort((o1, o2) -> Integer.compare(o2.getUseCount(), o1.getUseCount()));
			if (numberOfTrips > 0 && numberOfTrips < resultList.size()) {
				return resultList.subList(0, numberOfTrips);
			} else
				return resultList;
		
		} catch (SQLException e) {
			throw new DAOException("Ошибка в UserTripsData.getFrequentCorrection()", e);
		}
	}

	@Override
	public List<Thing> getThingsList(String direction, String correction) throws DAOException {
        List<Thing> thingsList = new ArrayList<>();
        String sql = "SELECT name, category FROM TripsData_sh.user_trips " 
				+ "INNER JOIN TripsData_sh.trips USING (trip_id) "
				+ "INNER JOIN TripsData_sh.things USING (thing_id)"
        		+ "WHERE direction = '" + direction + "' AND correction = '" + correction + "'";
        
        try(Connection connection = dbConn.getConnectionDB();
				Statement statement = connection.createStatement(); 
				ResultSet resultSet = statement.executeQuery(sql)){

	        while (resultSet.next()) {
	        	String category;
	        	if(resultSet.getString("category") != null) {
	        		category = resultSet.getString("category");
	        	}else {
	        		category = "нет";
	        	}
	        	Thing thing = new Thing(resultSet.getString("name"),category);
	        	if (thingsList.contains(thing)) {
	        		thing.usesCount++;
	        	}else {
	        		thing.usesCount = 1;
	        		thingsList.add(thing);
	        	}
	        }
	        return thingsList;
        
		} catch (SQLException e) {
			throw new DAOException("Ошибка в UserTripsData.getThingsList()", e);
		}
	}

}
