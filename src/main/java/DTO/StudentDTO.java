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
    private final String dni;
	private String address;
	private String phone1;
	private String phone2;




    // Constructor
    public StudentDTO(Student student) {
        this.id = student.getIdStudent();
        this.name = student.getName();
        this.lastName = student.getLastName();
        this.dni = student.getDni();
        this.email = student.getEmail();
        this.passwordHashed = student.getPasswordHashed();
        this.passwordNotHashed = student.getPasswordNotHashed();
    	this.address= student.getAddress();
    	this.phone1= student.getPhone1();
    	this.phone2= student.getPhone2();
        this.user_type = "student";


    }
}
