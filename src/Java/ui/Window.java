package Java.ui;

import Java.bean.Student;
import Java.bean.Teacher;
import Java.bean.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements ActionListener {
    private JButton TeacherButton;
    private JButton StudentButton;

    public Window() {
        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);

        showChooseWindow();
    }
    public void showChooseWindow() {

        JPanel panel = new JPanel();

        JLabel label = new JLabel("Please Choose your role:");
        label.setBounds(50, 30, 300, 30);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label);

        TeacherButton = new JButton("Teacher");
        TeacherButton.setBounds(50, 100, 150, 30);
        TeacherButton.setFont(new Font("Arial", Font.BOLD, 14));
        TeacherButton.setBackground(Color.DARK_GRAY);
        TeacherButton.setForeground(Color.WHITE);
        panel.add(TeacherButton);
        TeacherButton.addActionListener(this);

        StudentButton = new JButton("Student");
        StudentButton.setBounds(150, 100, 190, 30);
        StudentButton.setFont(new Font("Arial", Font.BOLD, 14));
        StudentButton.setBackground(Color.WHITE);
        StudentButton.setForeground(Color.BLACK);
        panel.add(StudentButton);
        StudentButton.addActionListener(this);

        this.add(panel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton  button = (JButton) e.getSource();
        if (button == TeacherButton) {
            System.out.println("Teacher Click");
            WindowClose(User.Teacher);
        } else if(button == StudentButton){
            System.out.println("Student Click");
            WindowClose(User.Student);
        }else {
            System.out.println("Error");
        }
    }
    public void WindowClose(User user) {
        try {
            System.out.println("Success");
            new LoginUI(user);
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
