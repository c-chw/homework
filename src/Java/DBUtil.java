package Java;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    public static final String URL = "jdbc:mysql://localhost:3306/homework?serverTimezone=GMT";
    public static final String USER = "root";
    public static final String PASSWORD = "051223";

    static {
        try {
            // 加载 MySQL JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("初始化数据库驱动失败：" + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // 添加更详细的错误信息提示
            StringBuilder errorMessage = new StringBuilder("数据库连接失败:\n");
            errorMessage.append("错误代码: ").append(e.getErrorCode()).append("\n");
            errorMessage.append("SQL状态: ").append(e.getSQLState()).append("\n");
            errorMessage.append("详细信息: ").append(e.getMessage());

            // 打印详细的错误信息到控制台
            System.err.println(errorMessage.toString());

            // 弹出错误对话框
            JOptionPane.showMessageDialog(null, errorMessage.toString(), "错误", JOptionPane.ERROR_MESSAGE);

            // 检查常见的连接问题
            if (e.getErrorCode() == 2002) {
                errorMessage.append("\n常见问题检查:\n");
                errorMessage.append("- 确认 MySQL 服务已启动。\n");
                errorMessage.append("- 检查端口 3306 是否被占用。\n");
                errorMessage.append("- 确认防火墙或安全软件未阻止连接。\n");
                errorMessage.append("- 尝试使用 127.0.0.1 替换 localhost。\n");

                // 更新弹出的错误对话框信息
                JOptionPane.showMessageDialog(null, errorMessage.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            }

            // 重新抛出异常以便上层捕获
            throw e;
        }
    }
}



