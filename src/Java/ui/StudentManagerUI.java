package Java.ui;

import Java.bean.Teacher;

import javax.swing.*;

public class StudentManagerUI extends JFrame {
    private JFrame  frame;

    public StudentManagerUI() {
        setTitle("学生管理系统");

        show();
        frame.setVisible(true);

    }
    public void show() {
        frame.setBounds(500, 200, 500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JTextField textField = new JTextField(20);
        JButton searchBtn = new JButton("查询");
        JButton addBtn = new JButton("添加");
        panel.add(textField);
        panel.add(searchBtn);
        panel.add(addBtn);

        frame.add(panel);
    }

}
