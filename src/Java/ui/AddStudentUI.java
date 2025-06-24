package Java.ui;

import Java.bean.Score;
import Java.bean.Student;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddStudentUI extends JFrame {
    private JTextField txtId,txtName,txtScore;
    private JComboBox<String> cmbSubject;
    private JButton btnSave,btnCancel;
    private StudentManagerUI studentManagerUI;

    public AddStudentUI(StudentManagerUI studentManagerUI) {
        super("Add Student");
        this.studentManagerUI = studentManagerUI;
        setLayout(new GridBagLayout());// 网格布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; //  设置组件水平居中
        gbc.insets = new Insets(5, 5, 5, 5); //  设置组件与边界的间距

        //  创建字体
        Font labelFont = new Font("楷体", Font.PLAIN, 14);

        // 标签和文本框
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel jLabel = new JLabel("ID:");
        jLabel.setFont(labelFont);
        add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtId = new JTextField(10);
        add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel jLabel1 = new JLabel("姓名:");
        jLabel1.setFont(labelFont);
        add(jLabel1, gbc);

        gbc.gridx = 1;
        txtName = new JTextField(10);
        add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel jLabel2 = new JLabel("科目:");
        jLabel2.setFont(labelFont);
        add(jLabel2, gbc);

        gbc.gridx = 1;
        String[] subjects = {"Java","Math","English"};
        cmbSubject = new JComboBox<>(subjects);
        add(cmbSubject, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel jLabel3 = new JLabel("成绩:");
        jLabel3.setFont(labelFont);
        add(jLabel3, gbc);

        gbc.gridx = 1;
        txtScore = new JTextField(10);
        add(txtScore, gbc);

        //添加和取消按钮
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; //  取消填充
        btnSave = new JButton("保存");
        btnCancel = new JButton("取消");
        btnSave.setPreferredSize(new Dimension(100, 30)); // 设置按钮大小
        btnCancel.setPreferredSize(new Dimension(100, 30));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, gbc);

        //给添加学生信息绑定一个点击事件监听器
        btnSave.addActionListener(e -> {
            // 获取输入框内容
            String idText = txtId.getText().trim();
            String nameText = txtName.getText().trim();
            String scoreText = txtScore.getText().trim();

            // 校验 ID 是否为空
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "学号不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 校验姓名是否为空
            if (nameText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "姓名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "学号必须为整数", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double scoreValue = 0.0;

            // 成绩非空时才解析
            if (!scoreText.isEmpty()) {
                try {
                    scoreValue = Double.parseDouble(scoreText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "成绩必须为数字", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // 创建 scores 列表并初始化三个科目
            ArrayList<Score> scores = new ArrayList<>();
            String selectedSubject = (String) cmbSubject.getSelectedItem();

            // 初始化三个科目的成绩为 0
            scores.add(new Score("Java", 0.0));
            scores.add(new Score("Math", 0.0));
            scores.add(new Score("English", 0.0));

            // 更新用户选择的科目对应的成绩
            for (Score s : scores) {
                if (s.getCourse().equals(selectedSubject)) {
                    s.setScore(scoreValue);
                }
            }

            // 创建学生对象并设置数据
            Student student = new Student(id, nameText, null, scores); // 假设密码可为空

            // 添加学生并刷新界面
            studentManagerUI.addStudent(student);
            JOptionPane.showMessageDialog(this, "添加学生成功");
            dispose(); // 关闭当前窗口
        });

        // 设置窗口属性
        pack();//  根据组件大小确定窗口大小
        setLocationRelativeTo(null);// 设置窗口居中
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        AddStudentUI window = new AddStudentUI(new StudentManagerUI());
    }
}
