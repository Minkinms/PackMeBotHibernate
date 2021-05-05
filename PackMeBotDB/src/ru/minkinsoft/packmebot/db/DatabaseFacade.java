package ru.minkinsoft.packmebot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFacade {
	
	public static void main(String[] args) {
//		DatabaseFacade databaseFacade = new DatabaseFacade();
//		System.out.println(getID());
	}
	
	//Переменные класса
	 Connection connection;
	 Statement statement;
	
	//Конструктор класса
	public DatabaseFacade() throws SQLException{
		getConnectionDB();
	}
	
	private void getConnectionDB() throws SQLException{
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tripsdata", "postgres", "1234");
		statement = connection.createStatement();
//			System.out.println("Java JDBC PostgreSQL Example");
//			System.out.println("Connected to PostgreSQL dataBase!");

	}
	
	public static List<String> getID(){
		List<String> result = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tripsdata", "postgres", "1234") ){
			System.out.println("Java JDBC PostgreSQL Example");
			System.out.println("Connected to PostgreSQL dataBase!");
			
			Statement statement = connection.createStatement();
//			ResultSet resultSet = statement.executeQuery("SELECT * FROM TripsData_sh.result");
			ResultSet resultSet = statement.executeQuery("SELECT * FROM TripsData_sh.result "
														+ "INNER JOIN TripsData_sh.trips USING (trips_id) "
														+ "INNER JOIN TripsData_sh.things USING (things_id)");
			while(resultSet.next()) {
				result.add(resultSet.getString("thing_category"));
			}
			
			
			
		}catch(SQLException e) {
			System.out.println("Connection failure");
			e.printStackTrace();
		}
		
		return result;
		
	}

}
