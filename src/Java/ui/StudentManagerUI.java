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
    public JLabel javaAvgLabel,mathAvgLabel,englishAvgLabel;
    public static ArrayList<Student> students = new ArrayList<>();

    static {
        students.add(new Student(1, "张三", "123456", new ArrayList<>()));
        students.add(new Student(2, "李四", "123456", new ArrayList<>()));
    }

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
        String[] sortOptions = {"学号", "姓名", "Java", "数学", "英语", "总分"};
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

        JButton maxScoreBtn = new JButton("最高分");
        JButton minScoreBtn = new JButton("最低分");

        rightPanel.add(javaAvgLabel);
        rightPanel.add(mathAvgLabel);
        rightPanel.add(englishAvgLabel);
        rightPanel.add(maxScoreBtn);
        rightPanel.add(minScoreBtn);


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
            model.setRowCount(0);
            for (Student student : students) {
                if (student.getName().contains(searchValue)) {
                    model.addRow(new Object[]{student.getId(), student.getName(), student.getScores().get(0).getScore(), student.getScores().get(1).getScore(), student.getScores().get(2).getScore()});
                }
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
                    switch (index) {
                        case 0: // 学号
                            students.sort((s1, s2) -> Integer.compare(s2.getId(), s1.getId()));
                            break;
                        case 1: // 姓名
                            students.sort((s1, s2) -> s2.getName().compareToIgnoreCase(s1.getName()));
                            break;
                        case 2: // Java 成绩
                            students.sort((s1, s2) -> {
                                double score1 = getScoreSafely(s1, 0);
                                double score2 = getScoreSafely(s2, 0);
                                return Double.compare(score2, score1);
                            });
                            break;
                        case 3: // 数学成绩
                            students.sort((s1, s2) -> {
                                double score1 = getScoreSafely(s1, 1);
                                double score2 = getScoreSafely(s2, 1);
                                return Double.compare(score2, score1);
                            });
                            break;
                        case 4: // 英语成绩
                            students.sort((s1, s2) -> {
                                double score1 = getScoreSafely(s1, 2);
                                double score2 = getScoreSafely(s2, 2);
                                return Double.compare(score2, score1);
                            });
                            break;
                        case 5: // 总分
                            students.sort((s1, s2) -> {
                                double total1 = getTotalScore(s1);
                                double total2 = getTotalScore(s2);
                                return Double.compare(total2, total1);
                            });
                            break;
                    }
                    refreshStudent();
                    updateAvgScore();
                });
            }
        }


        maxScoreBtn.addActionListener(e -> {

        });

        minScoreBtn.addActionListener(e -> {

        });

        updateAvgScore();//页面加载时自动计算一次平均分

        this.getContentPane().add(panel, BorderLayout.NORTH);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(rightPanel, BorderLayout.EAST);

    }
    private int queryStudent(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return id;
            }
        }
        return -1;
    }
    public void editStudent(int id) {
        int index = queryStudent(id);
        if (index != -1){
            Student student = students.get(index);
            new EditStudentUI(this,student);
        }else {
            JOptionPane.showMessageDialog(this, "该学生不存在！");
        }
    }

    private void deleteStudent (int id){
        if (queryStudent(id) != -1){
            students.remove(id);
        }else {
            JOptionPane.showMessageDialog(this, "该学生不存在！");
        }
        updateAvgScore();
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
                updateAvgScore();
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
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

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

    private void updateAvgScore() {
        if (students.isEmpty()) {
            javaAvgLabel.setText("Java平均分：0");
            mathAvgLabel.setText("数学平均分：0");
            englishAvgLabel.setText("英语平均分：0");
            return;
        }

        double javaSum = 0, mathSum = 0, englishSum = 0;
        int validCount = 0;

        for (Student student : students) {
            ArrayList<Score> scores = (ArrayList<Score>) student.getScores();
            if (scores.size() >= 3) {
                javaSum += scores.get(0).getScore();
                mathSum += scores.get(1).getScore();
                englishSum += scores.get(2).getScore();
                validCount++;
            }
        }

        if (validCount == 0) {
            javaAvgLabel.setText("Java平均分：暂无数据");
            mathAvgLabel.setText("数学平均分：暂无数据");
            englishAvgLabel.setText("英语平均分：暂无数据");
            return;
        }

        double javaAvg = javaSum / validCount;
        double mathAvg = mathSum / validCount;
        double englishAvg = englishSum / validCount;

        // 更新标签内容
        javaAvgLabel.setText(String.format("Java平均分：%.2f", javaAvg));
        mathAvgLabel.setText(String.format("数学平均分：%.2f", mathAvg));
        englishAvgLabel.setText(String.format("英语平均分：%.2f", englishAvg));
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
