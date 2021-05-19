package ru.minkinsoft.packmebot.datasource;

import java.util.List;

import ru.minkinsoft.packmebot.domain.Thing;

public interface ThingsDao {

	//Записать новую вещь в базу
	public int addNewThing(Thing thing) throws DAOException;	
		
	//Удалить вещь из базы
	public void deleteThing(int thingId);	
	
	//Получить вещь из базы по id
	public Thing findById(int thingId);
	
	//Получить вещь из базы по id
	public Integer findThingID(Thing thing) throws DAOException;
	
	//Получить вещь из базы по имени и категории
	public Integer findThingID(String name, String category) throws DAOException;
	
	//Получить список ID соответствующий списку вещей
	List<Integer> getThingsID(List<Thing> UserTripThings) throws DAOException;

	//Получить id последней вещи
	int getLastID() throws DAOException;
	
}
