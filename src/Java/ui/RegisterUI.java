package Java.ui;

import Java.bean.Teacher;
import Java.bean.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUI extends JFrame implements ActionListener {
    private JButton RegisterButton,LoginButton;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private User user;
    private TeacherManagerUI teacherManagerUI;

    public RegisterUI(User user) {
        this.user = user;
        this.setTitle("Register");
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        showRegisterUI();
    }

    private void showRegisterUI() {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 255, 255));

        Font  font = new Font("楷体", Font.PLAIN, 16);

        JLabel titleLabel = new JLabel("Register");
        titleLabel.setBounds(150, 50, 100, 30);
        titleLabel.setFont(new Font("楷体", Font.BOLD, 20));
        panel.add(titleLabel);

        JLabel usernameLabel = new JLabel("username");
        usernameLabel.setBounds(70, 100, 100, 30);
        usernameLabel.setFont(font);
        panel.add(usernameLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(170, 100, 150, 30);
        usernameTextField.setFont(font);
        panel.add(usernameTextField);

        JLabel passwordLabel = new JLabel("password");
        passwordLabel.setBounds(70, 150, 100, 30);
        passwordLabel.setFont(font);
        panel.add(passwordLabel);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(170, 150, 150, 30);
        passwordTextField.setFont(font);
        passwordTextField.setEchoChar('*');
        panel.add(passwordTextField);

        LoginButton = new JButton("Login");
        LoginButton.setBounds(100, 200, 100, 30);
        LoginButton.setFont(font);
        LoginButton.setBackground(Color.DARK_GRAY);
        LoginButton.setForeground(Color.WHITE);
        panel.add(LoginButton);
        LoginButton.addActionListener(this);

        RegisterButton = new JButton("Register");
        RegisterButton.setBounds(230, 200, 100, 30);
        RegisterButton.setFont(font);
        RegisterButton.setBackground(Color.WHITE);
        RegisterButton.setForeground(Color.BLACK);
        panel.add(RegisterButton);
        RegisterButton.addActionListener(this);

        this.add(panel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton  button = (JButton) e.getSource();
        if (button == RegisterButton) {
            System.out.println("Register Click");
            Register();
            new LoginUI(user);
        }else if (button == LoginButton) {
            System.out.println("Login Click");
            this.dispose();
            new LoginUI(user);
        }else {
            System.out.println("Error");
        }

    }

    private void Register() {
        if (user == User.Student) return;
        Teacher teacher = new Teacher(usernameTextField.getText(), passwordTextField.getText());
        teacherManagerUI.addTeacher(teacher);
        JOptionPane.showMessageDialog(this, "Register Success");
        this.dispose();
    }
}
