package datasource.intefaces;

import datasource.DAOException;
import datasource.entity.Thing;

public interface ThingDAO {
	
	public Thing findById(int thingID) throws DAOException;
	
	
	
}
