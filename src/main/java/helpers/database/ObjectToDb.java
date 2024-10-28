package helpers.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.StringJoiner;

import static utils.ConfigFactory.*;

public class ObjectToDb {

    public static <T> void insertObjectsToDb(String tableName, List<T> objects) throws SQLException, ReflectiveOperationException {
        if (objects == null || objects.isEmpty()) return;

        try (Connection connection = createConnection()) {
            for (T obj : objects) {
                insertSingleObject(connection, tableName, obj);
            }
        }
    }

    public static <T> void insertObjectToDb(String tableName, T object) throws SQLException, ReflectiveOperationException {
        try (Connection connection = createConnection()) {
            insertSingleObject(connection, tableName, object);
        }
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(CLICKHOUSE_HOST, CLICKHOUSE_USER, CLICKHOUSE_PASSWORD);
    }

    private static <T> void insertSingleObject(Connection connection, String tableName, T obj) throws SQLException, ReflectiveOperationException {
        String insertQuery = buildInsertQuery(tableName, obj);

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            // Set only non-null values in the statement
            int parameterIndex = 1;
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    statement.setObject(parameterIndex++, value);
                }
            }
            statement.executeUpdate();
        }
    }

    private static <T> String buildInsertQuery(String tableName, T obj) throws IllegalAccessException {
        StringJoiner columnNames = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(obj) != null) { // Include only non-null fields
                columnNames.add(camelToSnake(field.getName()));
                placeholders.add("?");
            }
        }

        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnNames, placeholders);
    }

    private static String camelToSnake(String camel) {
        StringBuilder result = new StringBuilder();
        for (char ch : camel.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                result.append('_').append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
