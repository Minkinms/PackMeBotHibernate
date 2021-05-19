package ru.minkinsoft.packmebot.datasource;

import ru.minkinsoft.packmebot.domain.Trip;


public interface TripsDao {
	
	//Записать новую поездку в базу
	//Возвращается id сохраненной поездки
	public Integer addNewTrip(Trip trip);	
	
	public Integer addNewTrip(String direction, String correction);
		
	//Удалить поездку из базы
	public void deleteTrip(int tripId);	
	
	//Получить поездку из базы по id
	public Trip findById(int tripId);
	
	//Получить поездку из базы по id
	public Integer findTripID(String direction, String correction) throws DAOException;

	

}
