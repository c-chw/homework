package User;

import java.io.Console;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import static User.Student.positionByID;
import static view.Homeview.sc;
import static view.Homeview.students;

public class Password {
    private static char[] password;

    public static char[] getPassword(Student student) {
        return student.password;
    }

    public static void setPassword(char[] password) {
        Password.password = password;
    }

    public static void GetPass(char[] password) {
        Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            int length = Math.min(input.length(), password.length);
            for (int i = 0; i < length; i++) {
                password[i] = input.charAt(i);
                System.out.print("*");
            }
            for (int i = length; i < password.length; i++) {
                password[i] = '\0';
            }

    }
    public static void ModifyPassword (int ID) {
        while (true) {
            System.out.print("请输入原密码：");
            char[] pass = readPassword();
            int index = positionByID(ID);
            if (index == -1) {
                System.out.println("未找到对应的学生信息！");
                return;
            }
            Student student = students.get(index);
            if (Arrays.equals(pass, Password.getPassword(student))) {
                break;
            } else {
                System.out.println("\n密码错误！\n");
            }
            Arrays.fill(pass, '\0');
        }
        char[] newPass = null;
        char[] newPassAgain = null;
        try {
            newPass = promptForNewPassword("请输入新密码：");
            newPassAgain = promptForNewPassword("\n请再次输入新密码：");
        } catch (Exception e) {
            System.out.println("\n密码输入过程中发生错误：" + e.getMessage());
            return;
        }

        if (Arrays.equals(newPass, newPassAgain)) {
            setPassword(newPass);
            try {
                if (savePass(positionByID(ID), students.get(positionByID(ID)))) {
                    System.out.println("\n密码修改成功！");
                } else {
                    System.out.println("\n密码修改失败！");
                }
            } catch (Exception e) {
                System.out.println("\n密码修改过程中发生错误：" + e.getMessage());
            }
        } else {
            System.out.println("\n两次密码不一致，修改失败");
        }

        Arrays.fill(newPass, '\0');
        Arrays.fill(newPassAgain, '\0');

        System.out.println("按任意键继续......");
        sc.nextLine();

        }

    private static char[] readPassword() {
        Console console = System.console();
        if (console != null) {
            return console.readPassword();
        } else {
            return sc.nextLine().toCharArray();
        }
    }
    private static char[] promptForNewPassword(String prompt) {
        System.out.print(prompt);
        return view.Homeview.sc.nextLine().toCharArray();
    }


    public static final String FILE_NAME = "stu_info.txt";
    private static final int RECORD_SIZE = 1024; // 假设每条记录的大小为1024字节

    public static boolean savePass(int studentPosition, Student student) {
        if (student == null) {
            System.err.println("学生对象不能为空");
            return false;
        }
        if (studentPosition < 0) {
            System.err.println("学生位置索引不能为负数");
            return false;
        }

        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
            long position = (long) studentPosition * RECORD_SIZE;
            file.seek(position);
            String record = student.toString();
            byte[] bytes = record.getBytes(StandardCharsets.UTF_8);
            file.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
