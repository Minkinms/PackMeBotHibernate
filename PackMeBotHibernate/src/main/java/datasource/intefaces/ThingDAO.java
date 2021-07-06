package datasource.intefaces;

import datasource.DAOException;
import datasource.entity.Thing;

public interface ThingDAO {
	
	//Метод для поиска вещи в базе.
	//Если вещь не найдена, она добавляется в базу как новая  
	public Thing findOrAddThing(Thing thingToFind) throws DAOException;
	

}
