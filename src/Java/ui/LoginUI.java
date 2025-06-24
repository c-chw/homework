package Java.ui;

import Java.DBUtil;
import Java.bean.Score;
import Java.bean.Student;
import Java.bean.Teacher;
import Java.bean.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Java.ui.StudentManagerUI.students;
import static Java.ui.TeacherManagerUI.teachers;

public class LoginUI extends JFrame implements ActionListener {
    private JTextField  usernameField;
    private JPasswordField  passwordField;
    private JButton  LoginButton = new JButton();
    private JButton  Registerbutton = new JButton();
    private User  user ;

    public LoginUI(User user) {
        this.user = user;
        this.setTitle("Login");
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); //居中显示

        showLoginUI();
    }
    public void showLoginUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 255, 255));

        Font  font = new Font("Arial", Font.PLAIN, 16);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setBounds(150, 50, 100, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(70, 100, 100, 30);
        usernameLabel.setFont(font);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(170, 100, 150, 30);
        usernameField.setFont(font);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(70, 150, 100, 30);
        passwordLabel.setFont(font);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(170, 150, 150, 30);
        passwordField.setFont(font);
        passwordField.setEchoChar('*');
        panel.add(passwordField);

        LoginButton  = new JButton("Login");
        LoginButton.setBounds(100, 200, 100, 30);
        LoginButton.setFont(font);
        LoginButton.setBackground(Color.DARK_GRAY);
        LoginButton.setForeground(Color.WHITE);
        panel.add(LoginButton);
        LoginButton.addActionListener(this);

        Registerbutton = new JButton("Register");
        Registerbutton.setBounds(230, 200, 100, 30);
        Registerbutton.setFont(font);
        Registerbutton.setBackground(Color.WHITE);
        Registerbutton.setForeground(Color.BLACK);
        panel.add(Registerbutton);
        Registerbutton.addActionListener(this);


        this.add(panel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton  button = (JButton) e.getSource();
        if (button == LoginButton) {
            System.out.println("Login Click");
            Login();
        }else if (button == Registerbutton) {
            System.out.println("Register Click");
            this.dispose();
            new RegisterUI(user);
        }else {
            System.out.println("Error");
        }
    }
    private void Login() {
        String  username = usernameField.getText();
        String  password = new String(passwordField.getPassword());
        Find(username,  password);
    }
    public void Find(String username, String password){
        if (user == User.Teacher){
            for (Teacher teacher : teachers) {
                if (teacher.getName().equals(username)&&teacher.getPassword().equals(password)) {
                    System.out.println("Login Success");
                    new StudentManagerUI();
                    this.dispose();
                    return;
                }
            }
        }else {
            // 替换硬编码逻辑，改为从数据库查询学生信息
            try (Connection conn = DBUtil.getConnection()) {
                String sql = "SELECT * FROM students WHERE name = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");

                    double javaScore = rs.getDouble("Java");
                    double mathScore = rs.getDouble("Math");
                    double englishScore = rs.getDouble("English");

                    // 构建 Score 对象列表
                    ArrayList<Score> scores = new ArrayList<>();
                    scores.add(new Score("Java", javaScore));
                    scores.add(new Score("Math", mathScore));
                    scores.add(new Score("English", englishScore));

                    Student student = new Student(id, name, password, scores);
                    System.out.println("Login Success");
                    new StudentInfoApp(student);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "用户名或密码错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "数据库查询失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
