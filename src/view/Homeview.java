package view;

import Login.StudentLogin;
import Login.TeacherLogin;
import User.Student;
import User.Teacher;

import java.util.ArrayList;
import java.util.Scanner;

public class Homeview {
    public static final Scanner sc = new Scanner(System.in);
    public static final ArrayList<Student> students = new ArrayList<>();
    public static final ArrayList<Teacher> teachers = new ArrayList<>();
    public static void welcome() {
        System.out.println("欢迎使用学生成绩管理系统");
        Teacher teacher1 = new Teacher(1,"张三", "1".toCharArray());
        teachers.add(teacher1);
        Student student1 = new Student(2,"李四", "2".toCharArray());
        students.add(student1);
    }
    public static void main(String[] args) {
        welcome();
        System.out.println("请选择您的身份（1.学生/2.老师）：");
        while(true) {
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    StudentLogin.Student();
                    return;
                case 2:
                    TeacherLogin.Teachar();
                    return;
                default:
                    System.out.println("请输入正确信息...");
                    break;
            }

        }
    }
}
