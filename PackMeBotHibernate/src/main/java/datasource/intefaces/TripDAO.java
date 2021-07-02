package datasource.intefaces;

import datasource.DAOException;



public interface TripDAO {
	

	
	public void addNewTrip(String direction, String correction) throws DAOException;
	


}
