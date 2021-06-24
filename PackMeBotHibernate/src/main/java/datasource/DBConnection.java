package datasource;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import datasource.entity.KitThingsEntity;
import datasource.entity.Thing;
import datasource.entity.TripTest;
import datasource.entity.TripEntity;
import datasource.entity.UserTripEntity;


public class DBConnection {
	
	public SessionFactory getFactory() throws HibernateException {
		return new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(TripEntity.class)
				.addAnnotatedClass(Thing.class)
				.addAnnotatedClass(UserTripEntity.class)
				.addAnnotatedClass(KitThingsEntity.class)
				.buildSessionFactory();
	}
}
