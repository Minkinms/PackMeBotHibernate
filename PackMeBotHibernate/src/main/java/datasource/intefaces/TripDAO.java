package datasource.intefaces;

import java.util.List;

import datasource.DAOException;
import datasource.entity.Thing;
import datasource.entity.Trip;

public interface TripDAO {
	
	//Метод для поиска поездки по параметрам.
	//Если подходящая поездка не найдена, то в базу записывается новая
	public Trip findOrAdd(String direction, 
							String correction, 
								List<Thing> thingList) throws DAOException;
	
	//Метод для получения списка направлений поездок.
	//Возвращаемый список сортируется по частоте использования направлений
	public List<Trip> getFrequentDirection(int numberOfTrips) throws DAOException;
	
	//Метод для получения списка уточнений для направления поездки.
	//Возвращаемый список сортируется по частоте использования уточнений
	public List<Trip> getFrequentCorrection(String direction, int numberOfTrips) throws DAOException;
	
	//Метод для получения списка вещей для выбранной поездки.
	//Возвращаемый список сортируется по частоте использования вещей в подобных поездках
	public List<Thing> getThingsList(String direction, String correction) throws DAOException;

}
