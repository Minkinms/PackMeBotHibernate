package datasource.intefaces;

import java.util.List;

import datasource.DAOException;
import datasource.entity.Thing;

public interface UserTripDAO {
	
	//Метод для сохранения завершенной подготовки к поездке
	public void writeUserTrip(int userID, 
								String direction, 
								String correction, 
								List<Thing> tookThingList) throws DAOException;
}
