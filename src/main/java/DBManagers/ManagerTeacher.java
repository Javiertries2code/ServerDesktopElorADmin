package DBManagers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import DBEntities.Teacher;
import utils.HibernateUtils;
public class ManagerTeacher {

	
	SessionFactory sessionFactory = null;
	Session session = null;
	/*
	 * USER_NOT_FOUND, TRANSACTION_UPDATE_FAILED, TRANSACTION_UPDATE_SUCCESSFULL,
	EMAIL_DELIVERED, EMAIL_FAILURE
	 * 
	 * 
	 * 
	 */
	
	public ManagerTeacher() {
		sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	
	public String updateTeacher(String email, String name, String lastName, String address, String phone1,
	        String phone2, String dni, String userType, String passwordHashed, String passwordNotHashed) {
	    
	    String query = "from Teacher as e where email=:email";
	    Query<Teacher> queryResult = session.createQuery(query);
	    queryResult.setParameter("email", email);
	    queryResult.setMaxResults(1);
	    Teacher teacher = queryResult.uniqueResult();

	    teacher.setEmail(email);
	    teacher.setName(name);
	    teacher.setLastName(lastName);
	    teacher.setAddress(address);
	    teacher.setPhone1(phone1);
	    teacher.setPhone2(phone2);
	    teacher.setDni(dni);
	 
	    teacher.setPasswordHashed(passwordHashed);
	    teacher.setPasswordNotHashed(Integer.parseInt(passwordNotHashed));
	    
	    Transaction tx = session.beginTransaction();
	    session.save(teacher);
	    tx.commit();
	    System.out.println("User Updated " + teacher.toString());

	    return "OK, pending to controll errors";    
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
