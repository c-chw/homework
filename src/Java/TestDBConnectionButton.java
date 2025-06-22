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

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = DBUtil.getConnection();
                    if (conn != null && !conn.isClosed()) {
                        JOptionPane.showMessageDialog(null, "✅ 数据库连接成功！");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "❌ 数据库连接失败: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }
}
