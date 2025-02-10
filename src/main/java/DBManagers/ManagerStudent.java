package DBManagers;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import DBEntities.Student;
import DBEntities.Teacher;
import errorCodes.ErrorCode;
import services.EmailService;
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
		
		
		
		Student student = null;
		try {
			student = queryResult.uniqueResult();
			Transaction tx = session.beginTransaction();

			session.save(student);

			tx.commit();
		} catch (Exception e) {
			System.err.println("Error " + ErrorCode.DATABASE_ERROR.getCode() + ": " + ErrorCode.EMAIL_SEND_FAILED.getMessage());
			e.printStackTrace();
		}


	

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

	public Boolean updateStudent(String email, String name, String lastName, String address, String phone1,
			String phone2, String dni, String userType, String passwordHashed, String passwordNotHashed) {
		
		System.out.println("REceived in  managerStudent-" + email + "--- " + lastName + "--- " + name + "--- " + address + "--- " + phone1 + "--- " + phone2 + "--- " + dni + "--- " + userType + "--- " + passwordHashed + "--- " + passwordNotHashed);

		
		String query = "from Student as e where email=:email";
		Query<Student> queryResult = session.createQuery(query);
		queryResult.setParameter("email", email);
		queryResult.setMaxResults(1);
		Student student = queryResult.uniqueResult();
		
	    System.out.println(student.getName()+ "FROM QUERI TO DB");


		student.setEmail(email);
		student.setName(name);
		student.setLastName(lastName);
		student.setAddress(address);
		student.setPhone1(phone1);
		student.setPhone2(phone2);
		student.setDni(dni);
		student.setRegistered(true);

		student.setPasswordHashed(passwordHashed);
		student.setPasswordNotHashed(Integer.parseInt(passwordNotHashed));

		
		try {
			Transaction tx = session.beginTransaction();

			session.save(student);

			tx.commit();
		    System.out.println("User Updated " + student.toString());

			return true;
		} catch (Exception e) {
		
			System.err.println("Error " + ErrorCode.DATABASE_ERROR.getCode() + ": " + ErrorCode.EMAIL_SEND_FAILED.getMessage());
			return false;
		}
		
	}

	public Boolean sendPasswordEmail(String email) {
		EmailService emailService = new EmailService();


	    String query = "from Student as e where email=:email";
	    Query<Student> queryResult = session.createQuery(query);
	    queryResult.setParameter("email", email);
	    queryResult.setMaxResults(1);
	    Student student = queryResult.uniqueResult();
	    
	    System.out.println(student.getName()+ "IN RESETING PASSWRPD - " + email);
	    student.setEmail(email);

	    student.setPasswordHashed("88888888");
	    student.setPasswordNotHashed(88888888);
	    try {
			Transaction tx = session.beginTransaction();

			session.save(student);

			tx.commit();
		    System.out.println("Password updated " + student.toString());
		    emailService.sendMail("javier.bravogu@elorrieta-errekamari.com",
					"Se ha realizado la insercion en la base de datos con sus nuevas contrasenas 88888888");

			return true;
		} catch (Exception e) {
			System.err.println("Error " + ErrorCode.DATABASE_ERROR.getCode() + ": " + ErrorCode.EMAIL_SEND_FAILED.getMessage());
			System.out.println("No se actualizo la contrasena");;
			return false;
		}  		
			
	}
}
