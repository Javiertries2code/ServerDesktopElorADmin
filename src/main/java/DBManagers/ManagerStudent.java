package DBManagers;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import DBEntities.Student;
import utils.HibernateUtils;

public class ManagerStudent {

	SessionFactory sessionFactory = null;
	Session session = null;
/*
 * 
 * 
 * USER_NOT_FOUND, TRANSACTION_UPDATE_FAILED, TRANSACTION_UPDATE_SUCCESSFULL,
	EMAIL_DELIVERED, EMAIL_FAILURE
 * 
 * 
 */
	public ManagerStudent() {
		sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	private String updateStudent(Student email, Student newStudent) {

		String query = "from Student as e where email=:email";
		Query<Student> queryResult = session.createQuery(query);
		queryResult.setParameter("email", email);
		queryResult.setMaxResults(1);
		Student student = queryResult.uniqueResult();


		Transaction tx = session.beginTransaction();

		session.save(student);

		tx.commit();
		System.out.println("Insertada nueva contrasena!");

		return "OK, pending to controll errors";
	}
	
	

	public List<Student> getAllStudents() {
		String query = "from Student";
		Query<Student> queryResult = session.createQuery(query);
		System.out.println("getUserStudent geti maxresults 10 to test, gotta delete");
	//queryResult.setMaxResults(10);
		List<Student> results = queryResult.list();

//
		return results;
	}

	public String updateStudent(String email, String name, String lastName, String address, String phone1,
			String phone2, String dni, String userType, String passwordHashed, String passwordNotHashed) {
		
		String query = "from Student as e where email=:email";
		Query<Student> queryResult = session.createQuery(query);
		queryResult.setParameter("email", email);
		queryResult.setMaxResults(1);
		Student student = queryResult.uniqueResult();

		student.setEmail(email);
		student.setName(name);
		student.setLastName(lastName);
		student.setAddress(address);
		student.setPhone1(phone1);
		student.setPhone2(phone2);
		student.setDni(dni);
		student.setPasswordHashed(passwordHashed);
		student.setPasswordNotHashed(Integer.parseInt(passwordNotHashed));

		
		Transaction tx = session.beginTransaction();

		session.save(student);

		tx.commit();
	    System.out.println("User Updated " + student.toString());

		return "OK, pending to controll errors";		
	}
}
