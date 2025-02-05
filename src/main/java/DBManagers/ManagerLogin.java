package DBManagers;

import java.sql.Date;
import java.util.Map;
import services.EmailService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import DBEntities.Student;
import DBEntities.Teacher;
import jakarta.mail.MessagingException;
import server.socketIO.config.Events;
import server.socketIO.model.MessageOutput;
import services.EmailService;
import utils.HibernateUtils;

public class ManagerLogin {

	SessionFactory sessionFactory = null;
	Session session = null;

	public ManagerLogin() {
		sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public Map<String, Object> validUser(String email, String password) {
		System.out.println("In managerLogin.validUSer ---CRUDLOGIN--" + email + "--- " + password);

		email = email.trim().toLowerCase();
		password = password.trim();

		System.out.println("SEARCHING FOR TEACHERS");
		String query = "from Teacher where email = :email and passwordNotHashed = :passwordNotHashed";
		Query<Teacher> queryResult = session.createQuery(query, Teacher.class);
		queryResult.setParameter("email", email);
		queryResult.setParameter("passwordNotHashed", password);
		Teacher result = queryResult.uniqueResult();

		if (result != null) {
			System.out.println("Found Teacher with email: " + result.getEmail() + "--nname  = " + result.getName());
			if ((result.getRegistered())) {
				System.out.println("returning  registered");

				return Map.of("status", "registered", "type", "Teacher", "user", result);
			} else {
				// sendPasswordEmail(result.getEmail());
				System.out.println("returning not registered");
				return Map.of("status", "not registered", "type", "Teacher", "user", result);
			}
		}
		System.out.println("SEARCHING FOR STUDENTS");

		query = "from Student where email = :email and passwordNotHashed = :passwordNotHashed";
		Query<Student> studentQueryResult = session.createQuery(query, Student.class);
		studentQueryResult.setParameter("email", email);
		studentQueryResult.setParameter("passwordNotHashed", password);

		Student studentResult = studentQueryResult.uniqueResult();
		if (studentResult != null) {
			System.out.println(
					"Found Student with email: " + studentResult.getEmail() + "--nname  = " + studentResult.getName());
			if ((studentResult.getRegistered())) {
				return Map.of("status", "registered", "type", "Student", "user", studentResult);
			} else {
				System.out.println("SEARCHING FOR STUDENTS");

				sendPasswordEmail(studentResult.getEmail());
				return Map.of("status", "not registered", "type", "Student", "user", studentResult);
			}
		}

		System.out.println("User not found in DB for email: " + email);
		return Map.of("status", "not existent", "type", null, "user", null);
	}

	/*
	 * public String findReturnByEmail(String email) throws MessagingException {
	 * EmailService eS = new EmailService(); String defaultPassword = "8888888";
	 * String defaultEncryptedPassword = "88888888";
	 * 
	 * String query = "from Teacher where email = :email"; Query<Teacher>
	 * queryResult = session.createQuery(query, Teacher.class);
	 * queryResult.setParameter("email", email); Teacher result =
	 * queryResult.uniqueResult();
	 * 
	 * if (result != null) { System.out.println("Found Teacher with email: " +
	 * result.getEmail()); //RESETPASSWORD HERE: String resetComunication =
	 * updatePasswordTeacher(email,defaultPassword,defaultEncryptedPassword );
	 * eS.sendMail(email, defaultPassword);
	 * 
	 * return resetComunication; } else { query =
	 * "from Student where email = :email"; Query<Student> studentQueryResult =
	 * session.createQuery(query, Student.class);
	 * studentQueryResult.setParameter("email", email);
	 * 
	 * Student studentResult = studentQueryResult.uniqueResult();
	 * 
	 * if (studentResult != null) { System.out.println("Found Student with email: "
	 * + studentResult.getEmail()); String resetComunication =
	 * updatePasswordStudent(email,defaultPassword,defaultEncryptedPassword );
	 * eS.sendMail(email, defaultPassword);
	 * 
	 * return resetComunication; } }
	 * 
	 * return null; }
	 * 
	 */
	public int resetPassword(String email) {

		EmailService emailService = new EmailService();
		String query = "from Teacher where email = :email";
		Query<Teacher> queryResult = session.createQuery(query, Teacher.class);
		queryResult.setParameter("email", email);

		Teacher result = queryResult.uniqueResult();

		if (result != null) {
			System.out.println("Found Teacher with email: " + result.getEmail());
			if (new ManagerTeacher().sendPasswordEmail(email)) {
				try {
					// emailService.sendMail(email.trim().toLowerCase(), "88888888");
					emailService.sendMail("javier.bravogu@elorrieta-errekamari.com", "88888888");
System.out.println("password changed, email sent");
					return 1;

				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("password changed, email not sent");
					return 2;

				}
			}
		} else {
			query = "from Student where email = :email";
			Query<Student> studentQueryResult = session.createQuery(query, Student.class);
			studentQueryResult.setParameter("email", email);

			Student studentResult = studentQueryResult.uniqueResult();
			if (studentResult != null) {
				System.out.println("Found Student with email: " + studentResult.getEmail());
				if (new ManagerStudent().sendPasswordEmail(email)) {

					try {
						// emailService.sendMail(email.trim().toLowerCase(), "88888888");
						emailService.sendMail("javier.bravogu@elorrieta-errekamari.com", "88888888");
						System.out.println("password changed, email sent");
						return 1;
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						System.out.println("password changed, email not sent");
						return 2;
					}
				}
				return 0;
			}
		}

		return 0;
	}

	private void sendPasswordEmail(String email) {
		// Logic to send a password setup email

		System.out.println("Sending password setup email to: " + email);
		return;
	}

	public void setNewPassword(String email, String newPassword) {

	}

}
