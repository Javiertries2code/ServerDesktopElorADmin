package DBManagers;

import utils.HibernateUtils;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import DBEntities.*;
import utils.HibernateUtils;

public class ManagerSubject {
	SessionFactory sessionFactory = null;
	Session session = null;
	
	
	public ManagerSubject() {
		sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}

}
