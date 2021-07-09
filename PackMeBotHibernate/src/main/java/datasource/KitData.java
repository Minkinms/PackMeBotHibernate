package datasource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import datasource.entity.Kit;
import datasource.entity.Thing;
import datasource.intefaces.KitDAO;
import presentation.PackMeBot;

public class KitData implements KitDAO{
	
	//Метод для поиска набора по списку вещей
	//Если подходящий набор не найден, то в базу записывается новый
	public Kit findOrAddKit(List<Thing> listThing) throws DAOException {
		System.out.println("Старт метода KitData.findOrAddKit()");		//log message
		int kitId = findKit(listThing);
		if(kitId < 0) {
			kitId = addKit(listThing);
		}
		System.out.println("Выполнено. Метод KitData.findOrAddKit()");		//log message
		return getKit(kitId);
	}
	
	//Метод для поиска набора по списку вещей
	//Возвращает Id набора или -1
	private int findKit(List<Thing> listThing) throws DAOException {
		System.out.println("Старт метода KitData.findKit()");		//log message
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			List<Kit> kitList = session.createQuery("from Kit").getResultList();
			int kitID = compareListThing(kitList, listThing);
			session.getTransaction().commit();
			return kitID;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in KitData.findKit()", e);
		}finally{
			session.close();
		}
	}
	
	//Метод для сравнения списков вещей, в наборах и пользователя
	private int compareListThing(List<Kit> kitList, List<Thing> listThing) {
		System.out.println("Старт метода KitData.compareListThing()");		//log message
		boolean kitFinded = false;
		for(Kit kit : kitList) {
			List<Thing> kitThings = new ArrayList<Thing>(kit.getThingList());
			kitThings.sort(comparator);
			List<Thing> userThings = new ArrayList<Thing>(listThing);
			userThings.sort(comparator);
			if(kitThings.equals(userThings)) {
				System.out.println("Выполнено. Метод KitData.compareListThing(). Набор найден");		//log message
				return kit.getKitID();
			}
		}
		System.out.println("Выполнено. Метод KitData.compareListThing(). Набор не найден");		//log message
		return -1;
	}
	
    private Comparator<Thing> comparator = new Comparator<Thing>() {   
        @Override
        public int compare(Thing o1, Thing o2) {
        	if(o1.getName().equals(o2.getName())) {
        		return o1.getCategory().compareTo(o2.getCategory());
        	}else {
        		return o1.getName().compareTo(o2.getName());
        	}
        }
    };

	//Метод для добавления нового набора в таблицу
	//Возвращает Id нового Kit
	private int addKit(List<Thing> listThing) throws DAOException {
		System.out.println("Старт метода KitData.addKit()");		//log message
		Session session = null;
		Kit kitThingsToAdd = new Kit();
		ThingData thingData = new ThingData();
		int kitThingsId;
		for(Thing thing : listThing) {
			kitThingsToAdd.addThing(thingData.findOrAddThing(thing));	//По ID достаю вещь из базы и добавляю к списку набора
		}
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			session.save(kitThingsToAdd);
			kitThingsId = kitThingsToAdd.getKitID();
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод KitData.addKit().");		//log message
			return 	kitThingsId;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in KitData.addKitThings()", e);
		}finally{
			session.close();
		}
	}

	//Метод для получения набора по Id
	private Kit getKit(int id) throws DAOException {
		System.out.println("Старт метода KitData.getKitThings()");		//log message
		Session session = null;
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			Kit kitThings = session.get(Kit.class, id);
			session.getTransaction().commit();
			System.out.println("Выполнено. Метод KitData.getKitThings()");		//log message
			return kitThings;
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in KitData.getKitThings()", e);
		}finally{
			session.close();
		}
	}
	
}
