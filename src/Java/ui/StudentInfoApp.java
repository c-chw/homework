package Java.ui;

import Java.bean.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StudentInfoApp extends JFrame {
    private JLabel nameField,idField;
    private JTextField scoreField;
    private JButton queryScoreButton;
    private JButton changePasswordButton;
    private JButton exitButton;
    private Student student;

    public StudentInfoApp(Student  student) {
        setTitle("Student");
        this.student = student;

        init();
        this.setVisible(true);
    }

    public void init() {
        // 设置窗口属性
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(6, 2)); //panel默认布局是FlowLayout（流式布局），不会遵循GridLayout（网格布局）
        this.setLocationRelativeTo(null); //居中显示
        this.setResizable(false); //禁止用户改变窗口大小


        // 创建组件
        JLabel nameLabel = new JLabel("姓名:");
        nameField = new JLabel(student.getName(), SwingConstants.LEFT); //合法的Swing对齐常量

        JLabel idLabel = new JLabel("学号:");
        idField = new JLabel(student.getId()+"" ,SwingConstants.LEFT);


        JLabel scoreLabel = new JLabel("成绩查询:");
        scoreField = new JTextField("", 15);
        queryScoreButton = new JButton("成绩查询");

        changePasswordButton = new JButton("修改密码");
        exitButton = new JButton("退出系统");

        // 添加组件到窗口
        this.add(nameLabel);
        this.add(nameField);
        this.add(idLabel);
        this.add(idField);
        this.add(scoreLabel);
        this.add(scoreField);
        this.add(queryScoreButton);
        this.add(changePasswordButton);
        this.add(exitButton);

        // 添加事件监听器
        queryScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里添加查询成绩的逻辑
                scoreField.setText("查询结果");
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里添加修改密码的逻辑
                JOptionPane.showMessageDialog(StudentInfoApp.this, "密码已修改");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        StudentInfoApp window = new StudentInfoApp(null);
    }
}
