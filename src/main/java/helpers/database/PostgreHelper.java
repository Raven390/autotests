package helpers.database;

import static utils.ConfigFactory.*;

import java.sql.*;
import java.util.Properties;

public class PostgreHelper {

    private static Statement connectToPostgreSql() throws SQLException {
        String url = POSTGRE_HOST;
        Properties props = new Properties();
        props.setProperty("user", POSTGRE_LOGIN);
        props.setProperty("password", POSTGRE_PASSWORD);
        props.setProperty("ssl", "true");
        Connection conn = DriverManager.getConnection(url, props);
        Statement st = conn.createStatement();
        return st;
    }

    public static ResultSet executeSqlQuery(String query) throws SQLException {
        Statement statement = connectToPostgreSql();
        ResultSet rs = connectToPostgreSql().executeQuery(query);
        while (rs.next()) {

            System.out.println(rs.getString(1));
        }
        rs.close();
        statement.close();
        return rs;
    }

    public static void insertUser() {
    }

    public static void insertUserToAbuseRegistry() {
    }
}
