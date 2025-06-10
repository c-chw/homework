package User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static view.Homeview.sc;
import static view.Homeview.students;

public class Student extends User implements Serializable {
    private ArrayList<Double> grade = new ArrayList<>();

    @Override
    public String toString() {
        return " "+super.getId()+" "+super.getName()+" "+grade;
    }

    public Student() {
    }
    public Student(int id, String name, char[] password) {
        super(id, name,password);
    }

    public ArrayList<Double> getGrade() {
        return grade;
    }

    public void setGrade(ArrayList<Double> grade) {
        this.grade = grade;
    }

    public static void showTitle(){
        System.out.println("学号 姓名 语文成绩 数学成绩 英语成绩");
    }
    public static boolean FindStudentByID(int ID){
        for (Student student : students) {
            if (ID == student.getId()) {
                return true;
            }
        }
        return false;
    }
    public static void ShowAllStudents(){
        for (int i = 0; i < students.size(); i++) {
            PrintOneStudents(i);
        }
    }
    public static void AddStudent(){
        Student student = new Student();
        System.out.println("请输入学号：");
        student.setId(sc.nextInt());
        System.out.println("请输入姓名：");
        student.setName(sc.next());
        System.out.println("请输入成绩：");
        student.grade.add(sc.nextDouble());
        students.add(student);
    }
    public static boolean RemoveStudent(int ID){
        int position=positionByID(ID);
        if(position==-1) {
            System.out.println("没有该位学生");
            return false;
        }
        students.remove(students.get(position));
        return true;
    }
    public static void PrintOneStudents(int position){
        showTitle();
        System.out.println(students.get(position).toString());
    }
    public static void changeStudent(int position) {
        System.out.println("请输入要更改的内容：\n1.学号\n2.姓名\n3.语文成绩\n4.数学成绩\n5.英语成绩\n6.退出");
        while (true) {
            switch (sc.nextInt()) {
                case 1:
                    students.get(position).setId(sc.nextInt());
                    break;
                case 2:
                    students.get(position).setName(sc.next());
                    break;
                case 3:
                    students.get(position).grade.set(0,sc.nextDouble());
                    break;
                case 4:
                    students.get(position).grade.set(1,sc.nextDouble());
                    break;
                case 5:
                    students.get(position).grade.set(2,sc.nextDouble());
                    break;
                case 6:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
    public static int positionByID(int ID){
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId()==ID){
                return i;
            }
        }
        return -1;
    }
}
