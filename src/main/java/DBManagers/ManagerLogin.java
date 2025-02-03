package DBManagers;

import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import DBEntities.Student;
import DBEntities.Teacher;
import utils.HibernateUtils;

public class ManagerLogin {
	
	SessionFactory sessionFactory = null;
	Session session = null;

	public ManagerLogin() {
		sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public Map<String, Object> validUser(String email, String password) {
		System.out.println("In managerLogin.validUSer ---CRUDLOGIN--" + email +"--- "+password);
		
		email =email.trim().toLowerCase();
		password = password.trim();
		
		System.out.println("SEARCHING FOR TEACHERS");
		String query = "from Teacher where email = :email and passwordNotHashed = :passwordNotHashed";
		Query<Teacher> queryResult = session.createQuery(query, Teacher.class);
		queryResult.setParameter("email", email);
		queryResult.setParameter("passwordNotHashed", password);
		Teacher result = queryResult.uniqueResult();

		if (result != null) {
			System.out.println("Found Teacher with email: " + result.getEmail()+ "--nname  = " + result.getName());
			if ((result.getRegistered())) {
				System.out.println("returning  registered");

				return Map.of(
					"status", "registered",
					"type", "Teacher",
					"user", result
				);
			} else {
				//sendPasswordEmail(result.getEmail());
				System.out.println("returning not registered");
				return Map.of(
					"status", "not registered",
					"type", "Teacher",
					"user", result
				);
			}
		}
		System.out.println("SEARCHING FOR STUDENTS");

		query = "from Student where email = :email and passwordNotHashed = :passwordNotHashed";
		Query<Student> studentQueryResult = session.createQuery(query, Student.class);
		studentQueryResult.setParameter("email", email);
		studentQueryResult.setParameter("passwordNotHashed", password);

		Student studentResult = studentQueryResult.uniqueResult();
		if (studentResult != null) {
			System.out.println("Found Student with email: " + studentResult.getEmail()+ "--nname  = " + studentResult.getName());
			if ((studentResult.getRegistered())) {
				return Map.of(
					"status", "registered",
					"type", "Student",
					"user", studentResult
				);
			} else {
				System.out.println("SEARCHING FOR STUDENTS");

				sendPasswordEmail(studentResult.getEmail());
				return Map.of(
					"status", "not registered",
					"type", "Student",
					"user", studentResult
				);
			}
		}

		System.out.println("User not found in DB for email: " + email);
		return Map.of(
			"status", "not existent",
			"type", null,
			"user", null
		);
	}
	
	
	/*
	public DataListener<MessageInput> resetPassword() {
		return ((client, data, ackSender) -> {
			System.out.println("Client from " + client.getRemoteAddress() + " requested password reset");

			// Extracting the JSON message
			String message = data.getMessage();
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

			// Validate input JSON
			if (!jsonObject.has("email")) {
				System.out.println("Invalid request: email is missing");
				client.sendEvent(Events.ON_RESET_PASSWORD_ANSWER.value, new MessageOutput("Invalid request: email is missing"));
				return;
			}

			// Extract email from the JSON
			String email = jsonObject.get("email").getAsString();

			// Validate user with extracted email
			ManagerLogin mL = new ManagerLogin();
			boolean userFound;
			String responseMessage;

			try {
				userFound = mL.findByEmail(email);
				if (userFound) {
					// TODO: Call method to send the reset password email
					responseMessage = "An email with a new password was delivered";
				} else {
					responseMessage = "No such user found in the database";
				}
			} catch (Exception e) {
				System.err.println("Error during password reset: " + e.getMessage());
				responseMessage = "An error occurred while processing your request";
			}

			// Send response back to the client
			MessageOutput messageOutput = new MessageOutput(gson.toJson(Map.of("message", responseMessage)));
			client.sendEvent(Events.ON_RESET_PASSWORD_ANSWER.value, messageOutput);
		});
	}
	
	*/

	public boolean findByEmail(String email) {
	    String query = "from Teacher where email = :email";
	    Query<Teacher> queryResult = session.createQuery(query, Teacher.class);
	    queryResult.setParameter("email", email);

	    Teacher result = queryResult.uniqueResult();

	    if (result != null) {
	        System.out.println("Found Teacher with email: " + result.getEmail());
	        sendPasswordEmail( email);
	        return true;
	    } else {
	        query = "from Student where email = :email";
	        Query<Student> studentQueryResult = session.createQuery(query, Student.class);
	        studentQueryResult.setParameter("email", email);

	        Student studentResult = studentQueryResult.uniqueResult();
	        if (studentResult != null) {
	            System.out.println("Found Student with email: " + studentResult.getEmail());
	            sendPasswordEmail( email);
	            return true;
	        }
	    }

	    return false;
	}

	private void sendPasswordEmail(String email) {
		// Logic to send a password setup email
		
		
		System.out.println("Sending password setup email to: " + email);
		return;
	}

	
}
