package DBManagers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import utils.HibernateUtils;

public class ManagerSubject {
	SessionFactory sessionFactory = null;
	Session session = null;
	
	
	public ManagerSubject() {
		sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}

}
