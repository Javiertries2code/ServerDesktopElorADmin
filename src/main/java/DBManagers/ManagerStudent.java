package DBManagers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import DBEntities.*;
import utils.HibernateUtils;

public class ManagerStudent {

	SessionFactory sessionFactory = null;
	Session session = null;

	public ManagerStudent() {
		sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public List<Student> getUserStudent() {
		String query = "from Student";
		Query<Student> queryResult = session.createQuery(query);
		System.out.println("getUserStudent geti maxresults 10 to test, gotta delete");
		queryResult.setMaxResults(10);
		List<Student> results = queryResult.list();

		for (int i = 0; i < results.size(); i++) {
			Student student = results.get(i);
			System.out.println("--------- Estudiante " + (i + 1) + " ---------");

			System.out.println("Nombre");
			System.out.println(student.getName());

			System.out.println("Apellido");
			System.out.println(student.getLastName());
			System.out.println("");
		}
		return results;
	}
}
