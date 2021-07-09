package datasource.intefaces;

import java.util.List;

import datasource.DAOException;
import datasource.entity.Kit;
import datasource.entity.Thing;

public interface KitDAO {
	
	//Метод для поиска набора по списку вещей
	//Если подходящий набор не найден, то в базу записывается новый
	public Kit findOrAddKit(List<Thing> listThing) throws DAOException;
	
}
