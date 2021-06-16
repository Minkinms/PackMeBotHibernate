package datasource.intefaces;

import datasource.DAOException;
import datasource.entity.Trip;


public interface TripDAO {
	
	public Trip findById(int tripID) throws DAOException;
	
	public void addNewTrip(String direction, String correction) throws DAOException;
	
	public void addNewTrip(Trip trip) throws DAOException;

}
