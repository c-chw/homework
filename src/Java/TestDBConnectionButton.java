package Java;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class TestDBConnectionButton extends JButton {
    public TestDBConnectionButton() {
        setText("测试数据库连接");
        setBounds(100, 100, 200, 30);
        this.setVisible(true);

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 打印连接 URL、用户名和密码以供调试
                    System.out.println("Connecting to: " + DBUtil.URL);
                    System.out.println("User: " + DBUtil.USER);
                    System.out.println("Password: " + DBUtil.PASSWORD);

                    Connection conn = DBUtil.getConnection();
                    if (conn != null && !conn.isClosed()) {
                        JOptionPane.showMessageDialog(null, "数据库连接成功！");
                        // 添加连接成功后的操作
                        System.out.println("Connection successful: " + conn);
                    } else {
                        JOptionPane.showMessageDialog(null, "数据库连接失败，请检查配置。");
                    }
                } catch (SQLException ex) {
                    // 打印堆栈跟踪以供调试
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "数据库连接失败: " + ex.getMessage());
                }
            }
        });
    }
}
