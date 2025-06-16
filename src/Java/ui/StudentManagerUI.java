package Java.ui;

import Java.bean.Score;
import Java.bean.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class StudentManagerUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    public static ArrayList<Student> students = new ArrayList<>();

    static {
        students.add(new Student(1, "张三", "123456", new ArrayList<>()));
    }

    public StudentManagerUI() {
        setTitle("学生管理系统");

        showStudent();
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
        panel.add(textField);
        panel.add(searchBtn);
        panel.add(addBtn);

        model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"学号", "姓名", "Java","数学","英语"}
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
        });

        addBtn.addActionListener(e-> {
            new AddStudentUI(this);
        });

        this.getContentPane().add(panel, BorderLayout.NORTH);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
    private int queryStudent(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return id;
            }
        }
        return -1;
    }
    private void editStudent(int id) {
        if (queryStudent(id) != -1){
            Student student = students.get(id);
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

    }

    public void addStudent(Student student) {
        students.add(student);
        model.addRow(new Object[]{student.getId(), student.getName(), student.getScores().get(0).getScore(), student.getScores().get(1).getScore(), student.getScores().get(2).getScore()});
    }
}
