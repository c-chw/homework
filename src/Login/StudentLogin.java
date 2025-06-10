package Login;

import User.Password;
import User.Student;

import static Login.TeacherLogin.save;
import static User.Student.*;
import static view.Homeview.*;
import static view.Homeview.sc;

public class StudentLogin {
    private static Student student = new Student();
    static boolean studentLogin() {
        System.out.println("Hello Student!\n\n");
            System.out.println("请输入学号:");
            int ID = sc.nextInt();
            System.out.println("请输入密码：");
            char[] password = new char[1024];
            Password.GetPass(password);
            if (FindStudentByID(ID)){
                student = students.get(positionByID(ID));
                return true;
            }
            System.out.println("密码错误！");
            return false;
    }
    public static void Student() {
        if(studentLogin())
        {
            System.out.println("\n登录成功！");
            while(true)
            {
                studentMenu();
                studentMenuChoose();
            }
        }
        else
            System.out.println("\n登录失败！");
    }
    static void studentMenu() {
        welcome();
        System.out.println("1.查看个人信息");
        System.out.println("2.修改密码");
        System.out.println("3.退出系统");
    }
    static void studentMenuChoose() {
        while (true) {
            System.out.println("请输入您的选择（1-3）：");
            switch (sc.nextInt()) {
                case 1:
                    PrintOneStudents(positionByID(student.getId()));
                    return;
                case 2:
                    Password.ModifyPassword(student.getId());
                    return;
                case 3:
                    studentQuit(student);
                    return;
                default:
                    System.out.println("请输入1-3之间的数字");
            }
        }
    }
    private static void studentQuit(Student student) {
    try {
        save();
    } catch (Exception e) {
        System.err.println("保存数据时发生错误，程序将强制退出");
        e.printStackTrace();
        System.exit(1);
    }
    student = null;

    System.out.println("请按任意键继续......");
    sc.nextLine();
    sc.close();

    System.exit(0);
}

}
