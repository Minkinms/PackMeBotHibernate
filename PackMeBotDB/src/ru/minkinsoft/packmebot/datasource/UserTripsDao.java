package ru.minkinsoft.packmebot.datasource;

import java.util.List;

import ru.minkinsoft.packmebot.domain.Thing;
import ru.minkinsoft.packmebot.domain.Trip;
import ru.minkinsoft.packmebot.domain.UserTrip;

public interface UserTripsDao {
	
	//Записать новую поездку в базу
	public void addNewUserTrip(UserTrip userTrip) throws DAOException;	
	
	//Удалить поездку из базы
	public void deleteUserTrip(int userTripId);		
	
	//Получить id последней поездки
	public int getLastID() throws DAOException;							
	
	//Получить список поездок с частыми направлениями  
	public List<Trip> getFrequentDirection(int numberOfTrips) throws DAOException;
	
	//Получить список поездок с частыми уточнениями для конкретного направления
	public List<Trip> getFrequentCorrection(String direction, int numberOfTrips) throws DAOException;
	
	//Получить полный список вещей, использованных в поездке.
	//В свойствах вещей указать количество раз использования в поездках
    public List<Thing> getThingsList(String direction, String correction) throws DAOException;
}
