package ru.minkinsoft.packmebot.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ru.minkinsoft.packmebot.domain.Thing;

public class ThingsData implements ThingsDao {

	DBConnection dbConn = new DBConnection();
		
	//Записать новую вещь в базу
	//Возвращается id сохраненной вещи
	@Override
	public int addNewThing(Thing thing) throws DAOException {
		String sql = "INSERT INTO TripsData_sh.things (thing_id, name, category) " +
					"VALUES (?, ?, ?)";
		int newID = getLastID() + 1;
		try(Connection connection = dbConn.getConnectionDB();
			PreparedStatement pStatement = connection.prepareStatement(sql)){
	  
			pStatement.setInt(1, newID); 
			pStatement.setString(2, thing.getNameThing()); 
			pStatement.setString(3, thing.getCategoryThing()); 
			pStatement.executeUpdate(); 
			return newID;
			
		}catch (SQLException e) {
			throw new DAOException("Ошибка в ThingsData.addNewThing()", e);
		}
	}

	//Пока не нужен в работе
	@Override
	public void deleteThing(int thingId) {
		// TODO Auto-generated method stub

	}

	//Пока не нужен в работе
	@Override
	public Thing findById(int thingId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Пока не нужен в работе
	//Получить ID вещи из базы по имени и категории.	
	//В случае отсутствия вернуть null.
	@Override
	public Integer findThingID(String name, String category) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	//Получить ID вещи из базы.
	//В случае отсутствия вернуть null.
	@Override
	public Integer findThingID(Thing thing) throws DAOException {
		Integer thingID = null;
		String sql = "SELECT thing_id FROM TripsData_sh.things " + 
					 "WHERE name = '" + thing.getNameThing() + "' " +
					 "AND category = '" + thing.getCategoryThing() + "'";
		
		try(Connection connection = dbConn.getConnectionDB();
			Statement statement = connection.createStatement(); 
			ResultSet resultSet = statement.executeQuery(sql)){
			
			while(resultSet.next()) {
				thingID = resultSet.getInt("thing_id");
			}
			return thingID;
			
		} catch (SQLException e) {
			throw new DAOException("Ошибка в ThingsData.findThingID()", e);
		}
	
	}

	//Получить список ID соответствующий списку вещей
	@Override
	public List<Integer> getThingsID(List<Thing> UserTripThings) throws DAOException {
		List<Integer> resultList = new ArrayList<Integer>();
		if(!UserTripThings.isEmpty()) {
    		for(Thing UserThing	: UserTripThings) {
    			if(findThingID(UserThing) != null) {
    				resultList.add(findThingID(UserThing));
    			}else {
    				resultList.add(addNewThing(UserThing));
    			}
    		}
    	}
		return resultList;
	}

	//Получить id последней вещи
	@Override
	public int getLastID() throws DAOException {
		int lastID = 0;
		String sql = "SELECT MAX (thing_id) FROM TripsData_sh.things";
		 
		try(Connection connection = dbConn.getConnectionDB();
			Statement statement = connection.createStatement(); 
			ResultSet resultSet = statement.executeQuery(sql)){
			
			while(resultSet.next()) {
				lastID = resultSet.getInt("max");
			}
			
		} catch (SQLException e) {
			throw new DAOException("Ошибка в ThingsData.getLastID()", e);
		}
		return lastID;
	}
	
}
