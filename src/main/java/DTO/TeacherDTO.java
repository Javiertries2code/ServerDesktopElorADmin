package DTO;

import DBEntities.Teacher;

public class TeacherDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

	
    private final int id;
    private final String name;
    private final String lastName;
    private final String email;
    private final String dni;

    private final String passwordHashed;
    private final Integer passwordNotHashed;
    private final String user_type;



    // Constructor
    public TeacherDTO(Teacher teacher) {
        this.id = teacher.getIdTeacher();
        this.name = teacher.getName();
        this.lastName = teacher.getLastName();
        this.email = teacher.getEmail();
        this.dni = teacher.getDni();
        this.passwordHashed = teacher.getPasswordHashed();
        this.passwordNotHashed = teacher.getPasswordNotHashed();
        this.user_type = "teacher";

    }
}

