package ru.minkinsoft.packmebot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	private Comparator<Integer> categoryComparator = new Comparator<>() {   //TODO: Здесь или отдельный файл?
        @Override
        public int compare(Integer o1, Integer o2) {
            return Integer.compare(o1, o2);
        }
    };
	
	

	// Конструктор класса
	public DatabaseFacade() throws SQLException {
		getConnectionDB();
		
	}

	// Метод для соединения с базой данных
	private void getConnectionDB() throws SQLException {
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tripsdata", "postgres", "1234");
		statement = connection.createStatement();
	}

	// Метод для получения частых направлений поездок
	public List<Trip> getFrequentTripsList(String direction, int numberOfFrequentTrips) throws SQLException {
		List<Trip> frequentTrips = new ArrayList<>();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM TripsData_sh.result " 
				+ "INNER JOIN TripsData_sh.trips USING (trips_id) "
				+ "INNER JOIN TripsData_sh.things USING (things_id)");
		while (resultSet.next()) {
			String tripDirection = resultSet.getString("direction");
			String tripCorrection = resultSet.getString("correction");
			Trip trip = new Trip(tripDirection, tripCorrection);
			if (direction == null) {
				addTrip(frequentTrips, trip);
			} else {
				if (tripDirection.equals(direction)) {
					addTrip(frequentTrips, trip);
				}
			}
		}

		frequentTrips.sort((o1, o2) -> Integer.compare(o2.getUseCount(), o1.getUseCount()));

		if (numberOfFrequentTrips > 0 && numberOfFrequentTrips < frequentTrips.size()) {
			return frequentTrips.subList(0, numberOfFrequentTrips);
		} else
			return frequentTrips;
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
        
        
        
//        for (UserTrip userTrip : allTrips) {
//            for (Thing thing : userTrip.getUserTripThings()) {
//                String key = (userTrip.getDirection() + "/" + userTrip.getCorrection()).toLowerCase();
//                if (thingsList.contains(thing)) {
//                    Thing extractedThing = thingsList.get(thingsList.indexOf(thing));
//                    if (extractedThing.tagsMap.containsKey(key)) {
//                        extractedThing.tagsMap.put(key, extractedThing.tagsMap.get(key) + 1);
//                    } else {
//                        extractedThing.tagsMap.put(key, 1);
//                    }
//                } else {
//                    thing.tagsMap.put(key, 1);
//                    thingsList.add(thing);
//                }
//            }
//        }
        
        
        
        return thingsList;
    }
	
	
	
	public static List<String> getID() {
		List<String> result = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tripsdata",
				"postgres", "1234")) {
			System.out.println("Java JDBC PostgreSQL Example");
			System.out.println("Connected to PostgreSQL dataBase!");

			Statement statement = connection.createStatement();
//			ResultSet resultSet = statement.executeQuery("SELECT * FROM TripsData_sh.result");
			ResultSet resultSet = statement.executeQuery(
					"SELECT * FROM TripsData_sh.result " + "INNER JOIN TripsData_sh.trips USING (trips_id) "
							+ "INNER JOIN TripsData_sh.things USING (things_id)");
			while (resultSet.next()) {
				result.add(resultSet.getString("thing_category"));
			}

		} catch (SQLException e) {
			System.out.println("Connection failure");
			e.printStackTrace();
		}

		return result;

	}

}
