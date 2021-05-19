package ru.minkinsoft.packmebot.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ru.minkinsoft.packmebot.domain.Trip;

public class TripsData implements TripsDao {
	DBConnection dbConn = new DBConnection();
	
	//Записать новую поездку в базу
	//Возвращается id сохраненной поездки
	@Override
	public Integer addNewTrip(String direction, String correction) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public Integer findTripID(String direction, String correction) throws DAOException {
		Integer tripsID = null;
		String sql = "SELECT trip_id FROM TripsData_sh.trips " + 
					 "WHERE direction = '" + direction + "' " +
					 "AND correction = '" + correction + "'";
		
		try(Connection connection = dbConn.getConnectionDB();
				Statement statement = connection.createStatement(); 
				ResultSet resultSet = statement.executeQuery(sql)){

			resultSet.next();
			tripsID = resultSet.getInt("trip_id");
			return tripsID;
		} catch (SQLException e) {
			throw new DAOException("Ошибка в TripData.findTripID()", e);
		}
	}
	
	

}
