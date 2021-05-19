package ru.minkinsoft.packmebot.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DBConnection {

	// Метод для соединения с базой данных
	public Connection getConnectionDB() throws SQLException {
		Connection connection = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/tripsdata", 
				"postgres", "1234");
		return connection;
	}

}
