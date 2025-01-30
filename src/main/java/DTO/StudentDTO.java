package DTO;

import DBEntities.Student;

public class StudentDTO implements java.io.Serializable {
	
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;
    private final String lastName;
    private final String email;
    private final String passwordHashed;
    private final Integer passwordNotHashed;
    private final String user_type;



    // Constructor
    public StudentDTO(Student student) {
        this.id = student.getIdStudent();
        this.name = student.getName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.passwordHashed = student.getPasswordHashed();
        this.passwordNotHashed = student.getPasswordNotHashed();
        this.user_type = "student";


    }
}
