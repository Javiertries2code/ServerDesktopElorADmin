package DBManagers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import DBEntities.Teacher;
import utils.HibernateUtils;
public class ManagerTeacher {

	
	SessionFactory sessionFactory = null;
	Session session = null;
	
	
	public ManagerTeacher() {
		sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	
	public List<Teacher> getAllTeachers() {
		String query = "from Teacher";
		Query<Teacher> queryResult = session.createQuery(query);
//		System.out.println("getUserTeacher geti maxresults 10 to test, gotta delete");
//		queryResult.setMaxResults(10);
		List<Teacher> results = queryResult.list();


//		}
		return results;
	}


	public Teacher getOneEmailPassword() {
		// TODO Auto-generated method stub
		return null;
	}
}
