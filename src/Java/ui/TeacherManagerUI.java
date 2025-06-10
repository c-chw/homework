package Java.ui;

import Java.bean.Student;
import Java.bean.Teacher;
import Java.bean.User;

import java.util.ArrayList;

public class TeacherManagerUI {
    public static ArrayList<Teacher> teachers =new ArrayList<>();


    static {
        teachers.add(new Teacher( "admin", "123456"));
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
}
