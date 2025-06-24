package Java.ui;

import Java.DBUtil;
import Java.bean.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("姓名:");
        this.add(nameLabel,gbc);

        gbc.gridx = 1;
        nameField = new JLabel(student.getName(), SwingConstants.LEFT); //合法的Swing对齐常量
        this.add(nameField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("学号:");
        this.add(idLabel,gbc);

        gbc.gridx = 1;
        idField = new JLabel(student.getId()+"" ,SwingConstants.LEFT);
        this.add(idField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel JavaLabel = new JLabel("Java:");
        this.add(JavaLabel,gbc);

        gbc.gridx = 1;
        JavaTextField = new JTextField(student.getScores().get(0).getScore()+"", 15);
        this.add(JavaTextField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel MathLabel = new JLabel("Math:");
        this.add(MathLabel,gbc);

        gbc.gridx = 1;
        MathTextField = new JTextField(student.getScores().get(1).getScore()+"", 15);
        this.add(MathTextField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel EnglishLabel = new JLabel("English:");
        this.add(EnglishLabel,gbc);

        gbc.gridx = 1;
        EnglishTextField = new JTextField(student.getScores().get(2).getScore()+"", 15);
        this.add(EnglishTextField,gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("保存");
        JButton resetButton = new JButton("重置");
        saveButton.setPreferredSize(new Dimension(100, 30));
        resetButton.setPreferredSize(new Dimension(100, 30));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        this.add(buttonPanel,gbc);


        saveButton.addActionListener(e -> {
            try {
                String javaText = JavaTextField.getText();
                String mathText = MathTextField.getText();
                String englishText = EnglishTextField.getText();

                double javaScore = parseScore(javaText);
                double mathScore = parseScore(mathText);
                double englishScore = parseScore(englishText);

                student.getScores().get(0).setScore(javaScore);
                student.getScores().get(1).setScore(mathScore);
                student.getScores().get(2).setScore(englishScore);

                try (Connection conn = DBUtil.getConnection()) {
                    String sql = "UPDATE students SET Java = ?, Math = ?, English = ?, TotalScore = ? WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setDouble(1, javaScore);
                    pstmt.setDouble(2, mathScore);
                    pstmt.setDouble(3, englishScore);
                    pstmt.setString(4, String.valueOf(javaScore + mathScore + englishScore));
                    pstmt.setInt(5, student.getId());

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        studentManagerUI.refreshStudent();
                        JOptionPane.showMessageDialog(EditStudentUI.this, "修改学生信息成功");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(EditStudentUI.this, "修改学生信息失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(EditStudentUI.this, "修改学生信息失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(EditStudentUI.this, "请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        resetButton.addActionListener(e -> {
            JavaTextField.setText("");
            MathTextField.setText("");
            EnglishTextField.setText("");
        });


        // 设置窗口属性
        pack();//  根据组件大小确定窗口大小
        setLocationRelativeTo(null);// 设置窗口居中
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private double parseScore(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("无效的成绩输入: " + text);
        }
    }

    public static void main(String[] args) {
        EditStudentUI window = new EditStudentUI(null, null);
    }
}
