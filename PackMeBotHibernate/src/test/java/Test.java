import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import datasource.DAOException;
import datasource.DBConnection;
import datasource.KitData;
import datasource.TripData;
import datasource.entity.Kit;
import datasource.entity.Thing;
import datasource.entity.Trip;
import datasource.entity.UserTrip;
import presentation.PackMeBot;



public class Test {
	
	public static void main(String[] args) throws DAOException {
		//HibernateException 


		
		
		PackMeBot packMebot = new PackMeBot();
		
		Thing thing1 = new Thing("Вещь1", "Категория1");
		Thing thing2 = new Thing("Вещь2", "Категория2");
		Thing thing3 = new Thing("Вещь3", "Категория1");
		Thing thing4 = new Thing("Вещь4", "Категория2");
		Thing thing5 = new Thing("Вещь5", "Категория3");
		Thing thing6 = new Thing("Вещь6", "Категория3");
		
		
//		List<Thing> list1 = new ArrayList<Thing>();
//		list1.add(thing1);
//		list1.add(thing2);
		
		
//		KitThingsData kitThingsData = new KitThingsData();
//		KitThingsEntity kit1 = new KitThingsEntity();
//		kit1 = kitThingsData.findKitThings(list1);
//		System.out.println(kit1.toString());
		
//		Trip trip11 = new Trip("Направление1", "Уточение1");
//		Trip trip12 = new Trip("Направление1", "Уточение2");
		//trip3.addUserThing(thing1);
		//trip3.addUserThing(thing2);
//		Trip trip21 = new Trip("Направление2", "Уточение1");
//		Trip trip22 = new Trip("Направление2", "Уточение2");
		//trip4.addUserThing(thing3);
		//trip4.addUserThing(thing4);
		
				
//		UserTripEntity ute;
		
		
//		KitThingsEntity kit1 = new KitThingsEntity();
//		KitThingsEntity kit2 = new KitThingsEntity();
//		KitThingsEntity kit3 = new KitThingsEntity();
		
		long startTime = new Date().getTime();
		//TripData td = new TripData();
		
//-------------------------------------------------------------------------		
		System.out.println("Начало ...");		//log message
		Session session = null;
		//String line = "";
		List<Thing> list = new ArrayList<Thing>();
		try{
			session = PackMeBot.factory.getCurrentSession();
			session.beginTransaction();
			
//			session.save(thing4);
//			session.save(thing5);
//			session.save(thing6);
			
//			session.save(trip12);
//			session.save(trip21);
			
				
//			session.save(kit2);
//			session.save(kit3);
			
//			Trip t1 = session.get(Trip.class, 1);
//			Trip t2 = session.get(Trip.class, 2);
//			Trip t3 = session.get(Trip.class, 3);
//			UserTripEntity ute1 = session.get(UserTripEntity.class, 1);
////			
			Thing thingDB = session.get(Thing.class, 1);
			
			System.out.println("Equals: " + thing1.equals(thingDB));
			System.out.println("HashCade thing1: " + thing1.hashCode());
			System.out.println("HashCade thingDB: " + thingDB.hashCode());
//			Thing thing_2 = session.get(Thing.class, 2);
//			Thing thing_3 = session.get(Thing.class, 3);
//			Thing thing_4 = session.get(Thing.class, 5);
//			Thing thing_5 = session.get(Thing.class, 6);
//			Thing thing_6 = session.get(Thing.class, 7);
//			
//			t1.addThing(thing_6);
//			t2.addThing(thing_2);
//			t2.addThing(thing_3);
//			t3.addThing(thing_1);
//			t3.addThing(thing_6);
//			t3.addThing(thing_4);
			
//			list.add(thing_1);
//			list.add(thing_2);
//			list.add(thing_3);
//			
			session.getTransaction().commit();
			System.out.println("Выполнено");		//log message
		}catch (HibernateException e){
			if(session.getTransaction() != null ) {
				session.getTransaction().rollback();
			}
			throw new DAOException("Error in ...", e);
		}finally{
			session.close();
		}
		
		System.out.println("Время выполнения " + (new Date().getTime() - startTime));
		
//		Session session1 = null;
		//String line = "";
//		List<Thing> list = new ArrayList<Thing>();
//		try{
//			session1 = PackMeBot.factory.getCurrentSession();
//			session1.beginTransaction();
//			KitThingsEntity kit  = new KitThingsEntity(list);
////			kit.setKitID(4);
//			session1.save(kit);
//			System.out.println(kit.getKitID());
////			TripEntity tren11 = new TripEntity("Направление4", "Уточение1", kit);
////			session.save(tren11);
//			
//			
////			TripEntity tripEn31 = new TripEntity("Направление1", "Уточение1", kit_1);
////			tripEn31.setUseCount(1);
////			session.save(tripEn31);
////			TripEntity tripEn31 = session.get(TripEntity.class, 4);
////			tripEn31.setUseCount(3);
////			UserTripEntity ute = new UserTripEntity(12345, tripEn31);
////			session.persist(ute);
//
////			UserTripEntity ute = session.get(UserTripEntity.class, 1);
////			
////			System.out.println(ute.toString());
////			System.out.println("Используемые вещи: ");
////			for(Thing thing : t1.getThingList()) {
////			System.out.println(thing);
////			}
////			System.out.println("*******************************");			
////			System.out.println(t1.toString());
////			System.out.println("Используемые вещи: ");
////			for(Thing thing : t1.getThingList()) {
////				System.out.println(thing);
////			}
////			System.out.println("*******************************");
////			System.out.println(t2.toString());
////			System.out.println("Используемые вещи: ");
////			for(Thing thing : t2.getThingList()) {
////				System.out.println(thing);
////			}
////			System.out.println("*******************************");
////			System.out.println(t3.toString());
////			System.out.println("Используемые вещи: ");
////			for(Thing thing : t3.getThingList()) {
////				System.out.println(thing);
////			}
////			System.out.println("*******************************");
////			System.out.println(thing_1.toString());
////			System.out.println("Используется в поездках: ");
////			for(Trip trip : thing_1.getTripList()) {
////				System.out.println(trip);
////			}
////			System.out.println("*******************************");
////			System.out.println(thing_2.toString());
////			System.out.println("Используется в поездках: ");
////			for(Trip trip : thing_2.getTripList()) {
////				System.out.println(trip);
////			}
////			System.out.println("*******************************");
////			System.out.println(thing_3.toString());
////			System.out.println("Используется в поездках: ");
////			for(Trip trip : thing_3.getTripList()) {
////				System.out.println(trip);
////			}
////			
//			
//			
//			
//			
//			session1.getTransaction().commit();
//			System.out.println("Выполнено");		//log message
//		}catch (HibernateException e){
//			if(session1.getTransaction() != null ) {
//				session1.getTransaction().rollback();
//			}
//			throw new DAOException("Error in ...", e);
//		}finally{
//			session1.close();
//		}
//		
//		System.out.println("Время выполнения " + (new Date().getTime() - startTime));
		
	}

}
