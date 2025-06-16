package Java.ui;

import Java.bean.Student;

import javax.swing.*;
import java.awt.*;

public class EditStudentUI extends JFrame {
    private JLabel nameField,idField;
    private JTextField JavaTextField,MathTextField,EnglishTextField;
    private StudentManagerUI studentManagerUI;


    public EditStudentUI(StudentManagerUI studentManagerUI, Student student) {
        setTitle("Edit Student");
        this.studentManagerUI = studentManagerUI;
        setLayout(new GridBagLayout());// 网格布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; //  设置组件水平居中
        gbc.insets = new Insets(5, 5, 5, 5); //  设置组件与边界的间距

        // 创建组件
        JLabel nameLabel = new JLabel("姓名:");
        nameField = new JLabel(student.getName(), SwingConstants.LEFT); //合法的Swing对齐常量

        JLabel idLabel = new JLabel("学号:");
        idField = new JLabel(student.getId()+"" ,SwingConstants.LEFT);

        JLabel JavaLabel = new JLabel("Java:");
        JavaTextField = new JTextField(student.getScores().get(0).getScore()+"", 15);

        JLabel MathLabel = new JLabel("Math:");
        MathTextField = new JTextField(student.getScores().get(1).getScore()+"", 15);

        JLabel EnglishLabel = new JLabel("English:");
        EnglishTextField = new JTextField(student.getScores().get(2).getScore()+"", 15);

        JButton saveButton = new JButton("保存");
        JButton resetButton = new JButton("重置");


        // 添加组件到窗口
        this.add(nameLabel);
        this.add(nameField);
        this.add(idLabel);
        this.add(idField);
        this.add(JavaLabel);
        this.add(JavaTextField);
        this.add(MathLabel);
        this.add(MathTextField);
        this.add(EnglishLabel);
        this.add(EnglishTextField);
        this.add(saveButton);

        // 设置窗口属性
        pack();//  根据组件大小确定窗口大小
        setLocationRelativeTo(null);// 设置窗口居中
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        EditStudentUI window = new EditStudentUI(null, null);
    }
}
