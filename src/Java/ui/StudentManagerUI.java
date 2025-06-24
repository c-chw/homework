package Java.ui;

import Java.DBUtil;
import Java.bean.Score;
import Java.bean.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentManagerUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    public JLabel javaAvgLabel,mathAvgLabel,englishAvgLabel,maxScoreLabel,minScoreLabel;
    public static ArrayList<Student> students = new ArrayList<>();

    public StudentManagerUI() {
        setTitle("学生管理系统");

        showStudent();
        refreshStudent();
        this.setVisible(true);

    }

    public static void main(String[] args) {
        StudentManagerUI window = new StudentManagerUI();
    }
    public void showStudent() {
        this.setBounds(100, 100, 800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JTextField textField = new JTextField(20);
        JButton searchBtn = new JButton("查询");
        JButton addBtn = new JButton("添加");
        JButton sortBtn = new JButton("排序");
        panel.add(textField);
        panel.add(searchBtn);
        panel.add(addBtn);
        panel.add(sortBtn);

        JPopupMenu sortMenu = new JPopupMenu(); //降序 升序
        // 添加菜单项
        String[] sortOptions = {"学号", "姓名", "Java", "数学", "英语", "总分", "平均成绩"};
        for (String option : sortOptions) {
            JMenuItem item = new JMenuItem(option);
            sortMenu.add(item);
        }

        model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"学号", "姓名", "Java","数学","英语","总分"}
        ){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(30);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("编辑");
        JMenuItem deleteItem = new JMenuItem("删除");
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3){
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0){
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(table, e.getX(), e.getY());
                    }
                }
            }
        });

        // 右侧面板 - 显示平均分
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200, 300));
        rightPanel.setBorder(BorderFactory.createTitledBorder("评估"));

        javaAvgLabel = new JLabel("Java平均分：0");
        mathAvgLabel = new JLabel("数学平均分：0");
        englishAvgLabel = new JLabel("英语平均分：0");

        maxScoreLabel = new JLabel("最高分：0");
        minScoreLabel = new JLabel("最低分：0");

        rightPanel.add(javaAvgLabel);
        rightPanel.add(mathAvgLabel);
        rightPanel.add(englishAvgLabel);
        rightPanel.add(maxScoreLabel);
        rightPanel.add(minScoreLabel);


        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (Integer) model.getValueAt(selectedRow, 0);
                    System.out.println("编辑 ：ID" + id);
                    editStudent(id);
                }

            }
        });

        deleteItem.addActionListener(e->{
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id =(Integer) model.getValueAt(selectedRow, 0);
                    deleteStudent(id);
                    model.removeRow(selectedRow);
                }
        });

        searchBtn.addActionListener(e-> {
            String searchValue = textField.getText();
            model.setRowCount(0); // 清空表格

            String sql = "SELECT * FROM students WHERE name LIKE ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, "%" + searchValue + "%"); // 模糊查询
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double javaScore = rs.getDouble("Java");
                    double mathScore = rs.getDouble("Math");
                    double englishScore = rs.getDouble("English");

                    model.addRow(new Object[]{id, name, javaScore, mathScore, englishScore});
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "搜索失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        addBtn.addActionListener(e-> {
            new AddStudentUI(this);
        });

        // 绑定弹出菜单到按钮
        sortBtn.addActionListener(e -> sortMenu.show(sortBtn, 0, sortBtn.getHeight()));
        for (int i = 0; i < sortMenu.getComponentCount(); i++) {
            Component comp = sortMenu.getComponent(i);
            if (comp instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) comp;
                int index = i;

                item.addActionListener(e -> {
                    String sql = switch (index) {
                        case 0 -> // 学号降序
                                "SELECT * FROM students ORDER BY id DESC";
                        case 1 -> // 姓名降序
                                "SELECT * FROM students ORDER BY name DESC";
                        case 2 -> // Java 成绩降序
                                "SELECT * FROM students ORDER BY Java DESC";
                        case 3 -> // 数学成绩降序
                                "SELECT * FROM students ORDER BY Math DESC";
                        case 4 -> // 英语成绩降序
                                "SELECT * FROM students ORDER BY English DESC";
                        case 5 -> // 总分降序
                                "SELECT * FROM students ORDER BY (Java + Math + English) DESC";
                        case 6 -> // 按学生平均成绩降序
                                "SELECT * FROM students ORDER BY (Java + Math + English) / 3 DESC";
                        default -> "SELECT * FROM students";
                    };
                    model.setRowCount(0); // 清空表格

                    try (Connection conn = DBUtil.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(sql);
                         ResultSet rs = stmt.executeQuery()) {

                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String name = rs.getString("name");
                            double javaScore = rs.getDouble("Java");
                            double mathScore = rs.getDouble("Math");
                            double englishScore = rs.getDouble("English");
                            double totalScore = javaScore + mathScore + englishScore;

                            model.addRow(new Object[]{id, name, javaScore, mathScore, englishScore, totalScore});
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(StudentManagerUI.this, "排序失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }

        updateScore();//页面加载时自动计算一次平均分

        this.getContentPane().add(panel, BorderLayout.NORTH);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(rightPanel, BorderLayout.EAST);

    }
    private Student queryStudent(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                double javaScore = rs.getDouble("Java");
                double mathScore = rs.getDouble("Math");
                double englishScore = rs.getDouble("English");

                ArrayList<Score> scores = new ArrayList<>();
                scores.add(new Score("Java", javaScore));
                scores.add(new Score("Math", mathScore));
                scores.add(new Score("English", englishScore));

                return new Student(id, name, null, scores); // 假设密码不用于编辑界面
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "查询学生失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        return null; // 如果未找到学生，返回 null
    }
    public void editStudent(int id) {
        Student student = queryStudent(id);
        if (student != null) {
            new EditStudentUI(this,student);
        }else {
            JOptionPane.showMessageDialog(this, "该学生不存在！");
        }
    }

    private boolean deleteStudent (int id){
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
             stmt.setInt(1, id);
             return stmt.executeUpdate() > 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        updateScore();
        return false;
    }

    public void addStudent(Student student) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO students (id, name, Java, Math, English, TotalScore) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql); // 创建预编译SQL语句对象，防止SQL注入攻击
            stmt.setInt(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setDouble(3, student.getScores().get(0).getScore());
            stmt.setDouble(4, student.getScores().get(1).getScore());
            stmt.setDouble(5, student.getScores().get(2).getScore());
            stmt.setDouble(6, getTotalScore(student));

            int rowsAffected = stmt.executeUpdate(); // 执行SQL语句
            if (rowsAffected > 0) { // 判断是否添加成功
                model.addRow(new Object[]{student.getId(), student.getName(), student.getScores().get(0).getScore(), student.getScores().get(1).getScore(), student.getScores().get(2).getScore(), getTotalScore(student)});
                updateScore();
                JOptionPane.showMessageDialog(this, "添加学生信息成功");
            } else {
                JOptionPane.showMessageDialog(this, "添加学生信息失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "添加学生信息失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshStudent() {
        model.setRowCount(0); // 清空表格

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM students";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double javaScore = rs.getDouble("Java");
                double mathScore = rs.getDouble("Math");
                double englishScore = rs.getDouble("English");
                String totalScore = rs.getString("TotalScore");

                model.addRow(new Object[]{id, name, javaScore, mathScore, englishScore, totalScore});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "刷新学生信息失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateScore() {
        String sql = "SELECT AVG(Java) AS avgJava, AVG(Math) AS avgMath, AVG(English) AS avgEnglish, MAX(TotalScore) AS maxScore,MIN(TotalScore) AS minScore FROM students";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                double javaAvg = rs.getDouble("avgJava");
                double mathAvg = rs.getDouble("avgMath");
                double englishAvg = rs.getDouble("avgEnglish");
                double maxScore = rs.getDouble("maxScore");
                double minScore = rs.getDouble("minScore");

                // 判断是否真的有数据（防止 NULL 被转换为 0.0）
                boolean hasData = !rs.wasNull();

                if (hasData) {
                    javaAvgLabel.setText(String.format("Java平均分：%.2f", javaAvg));
                    mathAvgLabel.setText(String.format("数学平均分：%.2f", mathAvg));
                    englishAvgLabel.setText(String.format("英语平均分：%.2f", englishAvg));
                    maxScoreLabel.setText(String.format("最高分：%.2f", maxScore));
                    minScoreLabel.setText(String.format("最低分：%.2f", minScore));

                } else {
                    javaAvgLabel.setText("Java平均分：暂无数据");
                    mathAvgLabel.setText("数学平均分：暂无数据");
                    englishAvgLabel.setText("英语平均分：暂无数据");
                    maxScoreLabel.setText("最高分：暂无数据");
                    minScoreLabel.setText("最低分：暂无数据");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "查询平均分失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double getScoreSafely(Student student, int index) {
        ArrayList<Score> scores = (ArrayList<Score>) student.getScores();
        if (scores != null && scores.size() > index) {
            return scores.get(index).getScore();
        }
        return -1; // 缺失成绩默认值
    }

    private double getTotalScore(Student student) {
        ArrayList<Score> scores = (ArrayList<Score>) student.getScores();
        if (scores == null) return -1;
        return scores.stream().mapToDouble(Score::getScore).sum();
    }


}
