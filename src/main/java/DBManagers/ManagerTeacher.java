package DBManagers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import DBEntities.Teacher;
import services.EmailService;
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
	
	
	public Boolean updateTeacher(String email, String name, String lastName, String address, String phone1,
	        String phone2, String dni, String userType, String passwordHashed, String passwordNotHashed) {
		
		
		System.out.println("Receivin managerTeacher-" + email + "--- " + lastName + "--- " + name + "--- " + address + "--- " + phone1 + "--- " + phone2 + "--- " + dni + "--- " + userType + "--- " + passwordHashed + "--- " + passwordNotHashed);

		
		
	    String query = "from Teacher as e where email=:email";
	    Query<Teacher> queryResult = session.createQuery(query);
	    queryResult.setParameter("email", email);
	    queryResult.setMaxResults(1);
	    Teacher teacher = queryResult.uniqueResult();
	    
	    System.out.println(teacher.getName()+ "FROM QUERI TO DB");

	    teacher.setEmail(email);
	    teacher.setName(name);
	    teacher.setLastName(lastName);
	    teacher.setAddress(address);
	    teacher.setPhone1(phone1);
	    teacher.setPhone2(phone2);
	    teacher.setDni(dni);
	    teacher.setRegistered(true);
	 
	    teacher.setPasswordHashed(passwordHashed);
	    teacher.setPasswordNotHashed(Integer.parseInt(passwordNotHashed));
	    
	    try {
			Transaction tx = session.beginTransaction();

			session.save(teacher);

			tx.commit();
		    System.out.println("User Updated " + teacher.toString());

			return true;
		} catch (Exception e) {
		
			e.printStackTrace();
			return false;
		}  
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


	public Boolean sendPasswordEmail(String email) {
		EmailService emailService = new EmailService();
	    String query = "from Teacher as e where email=:email";
	    Query<Teacher> queryResult = session.createQuery(query);
	    queryResult.setParameter("email", email);
	    queryResult.setMaxResults(1);
	    Teacher teacher = queryResult.uniqueResult();
	    
	    System.out.println(teacher.getName()+ "IN RESETING PASSWRPD - " + email);
	    teacher.setEmail(email);

	    teacher.setPasswordHashed("88888888");
	    teacher.setPasswordNotHashed(88888888);
	    try {
			Transaction tx = session.beginTransaction();

			session.save(teacher);

			tx.commit();
		    System.out.println("Password Updated " + teacher.toString());
		    emailService.sendMail("javier.bravogu@elorrieta-errekamari.com",
					"Se ha realizado la insercion en la base de datos con sus nuevas contrasenas 88888888");
		    
		

			return true;
		} catch (Exception e) {
		
			System.out.println("No se actualizo la contrasena");;
			return false;
		}  		
	}
}
