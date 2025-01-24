package DBManagers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import utils.HibernateUtils;
public class ManagerTeacher {

	
	SessionFactory sessionFactory = null;
	Session session = null;
	
	
	public ManagerTeacher() {
		sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}
}
