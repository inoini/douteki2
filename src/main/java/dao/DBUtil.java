package dao;


import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private static final String URL =
        "jdbc:mysql://localhost:3306/testdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Tokyo";

    private static final String USER = "user10";
    private static final String PASSWORD = "user10";

    public static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver"); // ドライバ読み込み

        // ★ここが重要（接続して返す）
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}