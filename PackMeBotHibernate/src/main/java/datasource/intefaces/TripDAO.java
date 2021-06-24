package datasource.intefaces;

import datasource.DAOException;
import datasource.entity.TripTest;


public interface TripDAO {
	
	public TripTest findById(int tripID) throws DAOException;
	
	public void addNewTrip(String direction, String correction) throws DAOException;
	
	public void addNewTrip(TripTest trip) throws DAOException;

}
