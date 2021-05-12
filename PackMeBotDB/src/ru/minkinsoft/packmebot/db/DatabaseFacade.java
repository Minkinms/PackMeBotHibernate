package ru.minkinsoft.packmebot.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.minkinsoft.packmebot.Thing;
import ru.minkinsoft.packmebot.Trip;
import ru.minkinsoft.packmebot.UserTrip;

public class DatabaseFacade {

	public static void main(String[] args) {
//		DatabaseFacade databaseFacade = new DatabaseFacade();
//		System.out.println(getID());
	}

	// Переменные класса
	Connection connection;
	Statement statement;
//	ResultSet resultSet;
	
//	private Comparator<Thing> categoryComparator = new Comparator<>() {   //TODO: Здесь или отдельный файл?
//        @Override
//        public int compare(Thing o1, Thing o2) {
//            return Integer.compare(o1.usesCount, o2.usesCount);
//        }
//    };
	
	

	// Конструктор класса
	public DatabaseFacade() throws SQLException {
		getConnectionDB();
		
	}

	// Метод для соединения с базой данных
	private void getConnectionDB() throws SQLException {
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tripsdata", "postgres", "1234");
		statement = connection.createStatement();
	}
	
	private void closeConnectionDB() {
		
	}
	
    //Метод для записи строки поездки в файл
    public void writeTrip(UserTrip userTrip) throws SQLException {
    	int userTripsID;
    	int userNumber;
    	int tripsID;
    	int thingsID;
    	
    	ResultSet resultSet = statement.executeQuery(
    						"SELECT MAX(user_trips_id) FROM TripsData_sh.result");
    	userTripsID = resultSet.getInt("user_trips_id") + 1;
    	
		/*
		 * PreparedStatement stmt = connection.prepareStatement(
		 * "INSERT INTO JC_CONTACT (FIRST_NAME, LAST_NAME, PHONE, EMAIL) VALUES (?, ?, ?, ?)"
		 * ); "INSERT INTO TripsData_sh.result (trips_id, direction,correction)" +
		 * "VALUES (0, 'Командировка', 'Москва')");
		 */
		/*
		 * stmt.setString(1, firstName); 
		 * stmt.setString(2, lastName); 
		 * stmt.setString(3, phone); 
		 * stmt.setString(4, email); 
		 * stmt.executeUpdate(); 
		 * stmt.close();
		 */
    	
		
		/*
		 * File tripHistoryFile = new File(tripHistoryPath); 
		 * FileWriter tripWriter = new FileWriter(tripHistoryFile, true); 
		 * StringBuilder stringToWrite = new StringBuilder(); 
		 * stringToWrite.append("\ntr").append(",");
		 * stringToWrite.append(userTrip.getDirection()).append("/");
		 * stringToWrite.append(userTrip.getCorrection()).append(","); //Дата записи не
		 * используется в работе. Сохранение необходимо для анализа истории при
		 * необходимости
		 * stringToWrite.append(dateTimeFormatter.format(LocalDateTime.now())).append(","); 
		 * for (Thing thing : userTrip.getUserTripThings()) 
		 * {
		 * 	stringToWrite.append(thing.toString()).append(","); 
		 * }
		 * 	stringToWrite.deleteCharAt(stringToWrite.length() - 1);
		 * 	tripWriter.write(stringToWrite.toString()); 
		 * 	tripWriter.close();
		 */
		 
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
//        String key = (direction + "/" + correction).toLowerCase();
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
//        thingsList.sort((o1, o2) -> Integer.compare(o2.usesCount, o1.usesCount));
        return thingsList;
    }
	
	
	
	/*
	 * public static List<String> getID() { List<String> result = new ArrayList<>();
	 * 
	 * try (Connection connection =
	 * DriverManager.getConnection("jdbc:postgresql://localhost:5432/tripsdata",
	 * "postgres", "1234")) { System.out.println("Java JDBC PostgreSQL Example");
	 * System.out.println("Connected to PostgreSQL dataBase!");
	 * 
	 * Statement statement = connection.createStatement(); // ResultSet resultSet =
	 * statement.executeQuery("SELECT * FROM TripsData_sh.result"); ResultSet
	 * resultSet = statement.executeQuery( "SELECT * FROM TripsData_sh.result " +
	 * "INNER JOIN TripsData_sh.trips USING (trips_id) " +
	 * "INNER JOIN TripsData_sh.things USING (things_id)"); while (resultSet.next())
	 * { result.add(resultSet.getString("thing_category")); }
	 * 
	 * } catch (SQLException e) { System.out.println("Connection failure");
	 * e.printStackTrace(); }
	 * 
	 * return result;
	 * 
	 * }
	 */

}
