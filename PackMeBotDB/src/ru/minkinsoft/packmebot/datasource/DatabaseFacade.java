package ru.minkinsoft.packmebot.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ru.minkinsoft.packmebot.domain.Thing;
import ru.minkinsoft.packmebot.domain.Trip;
import ru.minkinsoft.packmebot.domain.UserTrip;

public class DatabaseFacade {

	// Переменные класса
	Connection connection;
	Statement statement;

	// Конструктор класса
	public DatabaseFacade() {
	}

	// Метод для соединения с базой данных
	public void getConnectionDB() throws SQLException {
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tripsdata", 
												 "postgres", "1234");
		statement = connection.createStatement();
	}
	
	public void closeConnectionDB() throws SQLException {
		statement.close();
		connection.close();
	}
	
	//To use at test
	public void deleteRow(String SQLStatement) throws SQLException {
		statement.executeUpdate(SQLStatement);
	}

	public int getNextID(String column, String table) throws SQLException {
		ResultSet resultSet = statement.executeQuery(
    						"SELECT MAX(" + column + ") " +
    						"FROM " + table);
		int nextID = 0;
		while (resultSet.next()) {
			nextID = resultSet.getInt("max") + 1;
		}
    	resultSet.close();
    	return nextID;
	}
	
	private int findTripsID(String direction, String correction) throws SQLException {
		ResultSet resultSet = statement.executeQuery(
							"SELECT trips_id FROM TripsData_sh.trips " + 
		 					"WHERE direction = '" + direction + "' " +
		 					"AND correction = '" + correction + "'");
		Integer tripsID = null;
		while (resultSet.next()) {
			tripsID = resultSet.getInt("trips_id");
		}
		
		if(tripsID != null) {
			return tripsID;
		}else {
			int newTripID = getNextID("trips_id", "TripsData_sh.trips");
			writeNewTripToDB(direction, correction, newTripID);
			return newTripID;
		}
		
	}
	
	private void writeNewTripToDB(String direction, String correction, int newTripID) throws SQLException {
		PreparedStatement prepareStatement = connection.prepareStatement(
				  "INSERT INTO TripsData_sh.trips (trips_id, direction, correction) VALUES (?, ?, ?)");
		prepareStatement.setInt(1, newTripID); 
		prepareStatement.setString(2, direction); 
		prepareStatement.setString(3, correction); 
		prepareStatement.executeUpdate(); 
		prepareStatement.close();
	}
	
	private int findThingsID(Thing UserThing) throws SQLException {
		ResultSet resultSet = statement.executeQuery(
							"SELECT things_id FROM TripsData_sh.things " + 
		 					"WHERE thing_name = '" + UserThing.getNameThing() + "' " +
		 					"AND thing_category = '" + UserThing.getCategoryThing() + "'");
		Integer thingsID = null;
		while (resultSet.next()) {
			thingsID = resultSet.getInt("things_id");
		}
		
		if(thingsID != null) {
			return thingsID;
		}else {
			int newThingID = getNextID("trips_id", "TripsData_sh.trips");
			writeNewThingToDB(UserThing, newThingID);
			return newThingID;
		}
		
	}
	
	private void writeNewThingToDB(Thing thing, int newThingID) throws SQLException {
		PreparedStatement prepareStatement = connection.prepareStatement(
				  "INSERT INTO TripsData_sh.things (things_id, thing_name, thing_category) VALUES (?, ?, ?)");
		prepareStatement.setInt(1, newThingID); 
		prepareStatement.setString(2, thing.getNameThing()); 
		prepareStatement.setString(3, thing.getCategoryThing()); 
		prepareStatement.executeUpdate(); 
		prepareStatement.close();
	}
	

	private List<Integer> getThingsID(List<Thing> UserTripThings) throws SQLException {
		List<Integer> resultList = new ArrayList<Integer>();
		if(!UserTripThings.isEmpty()) {
    		for(Thing UserThing	: UserTripThings) {
    			resultList.add(findThingsID(UserThing));
    		}
    	}
		return resultList;
	}
	
	
	private void writeTripToDB(int userTripsID, int userID, int tripsID, int thingsID) throws SQLException {
		PreparedStatement prepareStatement = connection.prepareStatement(
					"INSERT INTO TripsData_sh.result (user_trips_id, user_id, trips_id, things_id) " +
					"VALUES (?, ?, ?, ?)");
		prepareStatement.setInt(1, userTripsID); 
		prepareStatement.setInt(2, userID); 
		prepareStatement.setInt(3, tripsID); 
		prepareStatement.setInt(4, thingsID);
		prepareStatement.executeUpdate(); 
		prepareStatement.close();
	}
	
    //Метод для записи строки поездки в файл
    public void writeUserTrip(UserTrip userTrip) throws SQLException {	
    	int userTripsID = getNextID("user_trips_id", "TripsData_sh.result");
    	int userID = userTrip.getUserID();
    	int tripsID = findTripsID(userTrip.getDirection(), userTrip.getCorrection());
    	List<Integer> thingsID = new ArrayList<Integer>(getThingsID(userTrip.getUserTripThings()));
    	for(int i = 0; i < thingsID.size(); i++) {
    		writeTripToDB(userTripsID, userID, tripsID, thingsID.get(i));
    	}
    }
	

	// Метод для получения частых направлений поездок
	public List<Trip> getFrequentCorrection(String direction, int numberOfFrequentTrips) throws SQLException {
		List<Trip> resultList = new ArrayList<>();
		ResultSet resultSet = statement.executeQuery("SELECT correction FROM TripsData_sh.result " 
				+ "INNER JOIN TripsData_sh.trips USING (trips_id) " +
				"WHERE direction = '" + direction + "'");
		while (resultSet.next()) {
			String tripCorrection = resultSet.getString("correction");
			Trip trip = new Trip(direction, tripCorrection);
			addTrip(resultList, trip);
		}
		resultList.sort((o1, o2) -> Integer.compare(o2.getUseCount(), o1.getUseCount()));
		if (numberOfFrequentTrips > 0 && numberOfFrequentTrips < resultList.size()) {
			return resultList.subList(0, numberOfFrequentTrips);
		} else
			return resultList;
	}
	
	public List<Trip> getFrequentDirection(int numberOfFrequentTrips) throws SQLException {
		List<Trip> resultList = new ArrayList<>();
		ResultSet resultSet = statement.executeQuery("SELECT direction FROM TripsData_sh.result " 
											+ "INNER JOIN TripsData_sh.trips USING (trips_id) ");
		while (resultSet.next()) {
			String tripDirection = resultSet.getString("direction");
			Trip trip = new Trip(tripDirection, "-");
			addTrip(resultList, trip);
		}
		resultList.sort((o1, o2) -> Integer.compare(o2.getUseCount(), o1.getUseCount()));
		if (numberOfFrequentTrips > 0 && numberOfFrequentTrips < resultList.size()) {
			return resultList.subList(0, numberOfFrequentTrips);
		} else
			return resultList;
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

	
    //Метод для получения полного списка вещей с определением количества раз использования в поездках
    public List<Thing> getThingsList(String direction, String correction) throws SQLException {
        List<Thing> thingsList = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery(
        		"SELECT thing_name, thing_category FROM TripsData_sh.result " 
				+ "INNER JOIN TripsData_sh.trips USING (trips_id) "
				+ "INNER JOIN TripsData_sh.things USING (things_id)"
        		+ "WHERE direction = '" + direction + "' AND correction = '" + correction + "'");
        while (resultSet.next()) {
        	String category;
        	if(resultSet.getString("thing_category") != null) {
        		category = resultSet.getString("thing_category");
        	}else {
        		category = "нет";
        	}
        	Thing thing = new Thing(resultSet.getString("thing_name"),category);
        	if (thingsList.contains(thing)) {
        		thing.usesCount++;
        	}else {
        		thing.usesCount = 1;
        		thingsList.add(thing);
        	}
        }
        return thingsList;
    }
	

}
