package Login;

import User.Password;
import User.Student;
import User.Teacher;


import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import static User.Student.*;
import static User.Teacher.FindTeacherByID;
import static view.Homeview.*;

public class TeacherLogin {
    private static Teacher teacher = new Teacher();
    static boolean teacherLogin() {
        System.out.println("Hello Teacher!\n\n");
            System.out.println("请输入工号:");
            int ID = sc.nextInt();
            System.out.println("请输入密码：");
            char[] password = new char[1024];
            Password.GetPass(password);
            if (FindTeacherByID(ID)){
                teacher = teachers.get(positionByID(ID));
                return true;
            }
        System.out.println("密码错误！");
        return false;
    }

    public static void Teachar(){
        if (teacherLogin()) {
            System.out.println("\n登录成功！");
            while (true) {
                teacherMenu();
                teacherMenuChoose();
            }
        } else
            System.out.println("\n登录失败！");
    }

    static void teacherMenu() {
        welcome();
        System.out.println("1.增加学生信息");
        System.out.println("2.修改学生信息");
        System.out.println("3.显示所有学生信息");
        System.out.println("4.按学号查询学生信息");
        System.out.println("5.删除学生信息");
        System.out.println("6.按学号进行排序");
        System.out.println("7.保存到文件");
        System.out.println("8.修改密码");
        System.out.println("9.退出系统");
    }

    static void teacherMenuChoose() {
        while (true) {
            System.out.println("请输入您的选择（1-3）：");
            switch (sc.nextInt()) {
                case 1:
                    AddStudent();
                    return;
                case 2:
                    updateStudent();
                    return;
                case 3:
                    displayAll();
                    return;
                case 4:
                    querybySno();
                    return;
                case 5:
                    delStudentbySno();
                    return;
                case 6:
                    sortbySno();
                    return;
                case 7:
                    save();
                    return;
                case 8:
                    Password.ModifyPassword(teacher.getId());
                    return;
                case 9:
                    return;
                default:
                    System.out.println("请输入1-3之间的数字");
                    break;
            }
        }
    }

    private static void updateStudent() {
        System.out.println("请输入要修改的学号：");
        int ID = sc.nextInt();
        int position = positionByID(ID);
        if (position==-1) {
            System.out.println("没有学号为"+ID+"学生的信息");
        } else {
            showTitle();
            PrintOneStudents(position);
            changeStudent(position);
        }
        System.out.println("修改成功！");
        PrintOneStudents(position);
    }

    private static void displayAll() {
        showTitle();
        ShowAllStudents();
    }

    private static void querybySno() {
        System.out.println("请输入要查询的学号：");
        int ID = sc.nextInt();
        if(positionByID(ID)!=-1){
            PrintOneStudents(positionByID(ID));
        }else System.out.println("没有学号为"+ID+"学生的信息");
    }

    private static void delStudentbySno() {
        System.out.println("请输入要删除学生学号：");
        int ID = sc.nextInt();
        if (RemoveStudent(ID)){
            System.out.println("删除成功！");
        }else {
            System.out.println("删除失败!");
        }
    }

    private static void sortbySno() {
        if (students.isEmpty()){
            System.out.println("没有学生信息");
            return;
        }
        Arrays.sort(new Comparator[]{Comparator.comparing(Student::getId)});
    }

    public static void save() {
        File file = new File(FILE_NAME);
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(file, true))) {
            fos.writeObject(students);
            System.out.println("保存成功！");
        } catch (IOException e) {
            System.out.println("文件保存失败:"+e.getMessage());
        }
        System.out.println("请按任意键继续......");
        new Scanner(System.in).nextLine();
    }

}
