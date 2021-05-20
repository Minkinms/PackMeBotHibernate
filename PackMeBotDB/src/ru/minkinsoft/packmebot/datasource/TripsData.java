package ru.minkinsoft.packmebot.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ru.minkinsoft.packmebot.domain.Trip;

public class TripsData implements TripsDao {
	DBConnection dbConn = new DBConnection();
	
	//Записать новую поездку в базу
	//Возвращается id сохраненной поездки
	@Override
	public Integer addNewTrip(String direction, String correction) throws DAOException {
		String sql = "INSERT INTO TripsData_sh.trips (trips_id, direction, correction) " +
						"VALUES (?, ?, ?)";
		int newID = getLastID() + 1;
		try(Connection connection = dbConn.getConnectionDB();
				PreparedStatement pStatement = connection.prepareStatement(sql)){
				  
			pStatement.setInt(1, newID); 
			pStatement.setString(2, direction); 
			pStatement.setString(3, correction); 
			pStatement.executeUpdate(); 
			return newID;
						
		}catch (SQLException e) {
			throw new DAOException("Ошибка в TripsData.addNewTrip()", e);
		}
	}

	@Override
	public Integer addNewTrip(Trip trip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTrip(int tripId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Trip findById(int tripId) {
		// TODO Auto-generated method stub
		return null;
	}

	//Получить id поездки по свойствам.
	//Если такая поездка отсутствует, вернуть null.
	@Override
	public Integer findTripID(String direction, String correction) throws DAOException {
		Integer tripID = null;
		String sql = "SELECT trip_id FROM TripsData_sh.trips " + 
					 "WHERE direction = '" + direction + "' " +
					 "AND correction = '" + correction + "'";
		
		try(Connection connection = dbConn.getConnectionDB();
				Statement statement = connection.createStatement(); 
				ResultSet resultSet = statement.executeQuery(sql)){

			while(resultSet.next()) {
				tripID = resultSet.getInt("trip_id");
			}
			return tripID;			
		} catch (SQLException e) {
			throw new DAOException("Ошибка в TripData.findTripID()", e);
		}
	}

	//Получить id поездки по свойствам. 
	//Если такая поездка отсутствует, записать в базу и вернуть id
	@Override
	public Integer findOrWrite(String direction, String correction) throws DAOException {
		Integer tripID = findTripID(direction, correction);
		if(tripID != null) {
			return tripID;
		}else {
			return addNewTrip(direction, correction);
		}
	}
	
	//Получить id последней поездки
	@Override
	public int getLastID() throws DAOException {
		int lastID = 0;
		String sql = "SELECT MAX (trip_id) FROM TripsData_sh.trips";
		 
		try(Connection connection = dbConn.getConnectionDB();
			Statement statement = connection.createStatement(); 
			ResultSet resultSet = statement.executeQuery(sql)){
			
			while(resultSet.next()) {
				lastID = resultSet.getInt("max");
			}
			
		} catch (SQLException e) {
			throw new DAOException("Ошибка в TripsData.getLastID()", e);
		}
		return lastID;
	}

}
