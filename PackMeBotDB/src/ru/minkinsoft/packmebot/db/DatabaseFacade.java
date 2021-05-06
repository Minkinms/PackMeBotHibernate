package ru.minkinsoft.packmebot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
		ResultSet resultSet = statement
				.executeQuery("SELECT * FROM TripsData_sh.result " 
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

	// Метод для добавления поездки в список
	private void addTrip(List<Trip> tripList, Trip addTrip) {
		if (tripList.contains(addTrip)) {
			int count = tripList.get(tripList.indexOf(addTrip)).getUseCount() + 1;
			tripList.get(tripList.indexOf(addTrip)).setUseCount(count);
		} else {
			addTrip.setUseCount(1);
			tripList.add(addTrip);
		}
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
