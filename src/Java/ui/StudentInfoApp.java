package Java.ui;

import Java.bean.Student;
import Java.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentInfoApp extends JFrame {
    private JLabel nameField, idField;
    private JTextField scoreField;
    private JButton queryScoreButton;
    private JButton changePasswordButton;
    private JButton exitButton;
    private Student student;

    public StudentInfoApp(Student student) {
        setTitle("学生信息管理系统");
        this.student = student;
        
        // 设置全局字体
        UIManager.put("Button.font", new Font("微软雅黑", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("微软雅黑", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("微软雅黑", Font.PLAIN, 14));
        
        init();
        this.setVisible(true);
    }

    public void init() {
        // 设置窗口属性
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(10, 10)); // 使用BorderLayout布局
        this.setLocationRelativeTo(null); // 居中显示
        this.setResizable(false); // 禁止用户改变窗口大小
        this.setBackground(new Color(240, 240, 240)); // 设置背景色
        
        // 创建顶部信息面板
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("个人信息"));
        topPanel.setBackground(Color.WHITE);
        
        // 创建组件
        JLabel nameLabel = new JLabel("姓名:");
        nameField = new JLabel(student.getName(), SwingConstants.LEFT); // 合法的Swing对齐常量

        JLabel idLabel = new JLabel("学号:");
        idField = new JLabel(student.getId() + "", SwingConstants.LEFT);
        
        // 添加组件到顶部面板
        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(idLabel);
        topPanel.add(idField);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        // 创建按钮
        queryScoreButton = new JButton("查询成绩");
        changePasswordButton = new JButton("修改密码");
        exitButton = new JButton("退出系统");
        
        // 设置按钮样式
        Dimension buttonSize = new Dimension(150, 30);
        queryScoreButton.setPreferredSize(buttonSize);
        changePasswordButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);
        
        // 添加组件到按钮面板
        buttonPanel.add(queryScoreButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(exitButton);
        
        // 创建底部结果面板
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("查询结果"));
        bottomPanel.setBackground(Color.WHITE);
        
        // 创建成绩文本框
        scoreField = new JTextField("点击按钮开始查询...", 20);
        scoreField.setEditable(false);
        scoreField.setHorizontalAlignment(JTextField.CENTER);
        scoreField.setFont(new Font("微软雅黑", Font.ITALIC, 14));
        
        // 添加组件到底部面板
        bottomPanel.add(scoreField);
        
        // 将所有面板添加到主窗口
        this.add(topPanel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        // 添加事件监听器
        queryScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里添加查询成绩的逻辑
                queryScore();
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里添加修改密码的逻辑
                showChangePasswordDialog();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    private void queryScore() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT Java, Math, English " +
                         "FROM students " +
                         "WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, student.getId());
            
            rs = pstmt.executeQuery();
            StringBuilder scoresBuilder = new StringBuilder();
            
            while (rs.next()) {
                double javaScore = rs.getDouble("Java");
                double mathScore = rs.getDouble("Math");
                double englishScore = rs.getDouble("English");
                
                scoresBuilder.append("Java: ").append(javaScore).append("\n");
                scoresBuilder.append("数学: ").append(mathScore).append("\n");
                scoresBuilder.append("英语: ").append(englishScore).append("\n");
            }
            
            if (scoresBuilder.length() > 0) {
                scoreField.setText(scoresBuilder.toString());
            } else {
                scoreField.setText("未找到成绩记录");
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "查询成绩失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            // 关闭数据库资源
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showChangePasswordDialog() {
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        
        Object[] message = {
            "原密码:", oldPasswordField,
            "新密码:", newPasswordField,
            "确认新密码:", confirmPasswordField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "修改密码", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // 验证原密码是否正确
            if (!verifyOldPassword(oldPassword)) {
                JOptionPane.showMessageDialog(this, "原密码不正确", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 验证新密码是否符合要求
            if (newPassword.isEmpty() || newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this, "新密码不能为空且至少需要6个字符", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 验证两次输入的新密码是否一致
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "两次输入的新密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 修改密码
            if (changePasswordInDatabase(newPassword)) {
                JOptionPane.showMessageDialog(this, "密码已成功修改", "信息", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "修改密码失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean verifyOldPassword(String oldPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT password FROM students WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, student.getId());
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(oldPassword);
            }
            
            return false;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "验证原密码失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            // 关闭数据库资源
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean changePasswordInDatabase(String newPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE students SET password = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, student.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "修改密码失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            // 关闭数据库资源
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // 使用SwingUtilities.invokeLater确保Swing组件在事件调度线程中运行
        SwingUtilities.invokeLater(() -> {
            StudentInfoApp window = new StudentInfoApp(null);
            window.setVisible(true);
        });
    }
}