package datasource;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import datasource.entity.Thing;
import datasource.entity.Trip;
import datasource.entity.UserTripEntity;


public class DBConnection {
	
	public SessionFactory getFactory() throws HibernateException {
		return new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Trip.class)
				.addAnnotatedClass(Thing.class)
				.addAnnotatedClass(UserTripEntity.class)
				.buildSessionFactory();
	}
}
