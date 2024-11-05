package helpers.database;

import io.qameta.allure.Step;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static utils.ConfigFactory.*;

public class DbHelper {

    @Step("Insert objects: {objects}")
    public static <T> void insertObjectsToDb(String tableName, List<T> objects) throws SQLException, ReflectiveOperationException {
        if (objects == null || objects.isEmpty()) return;

        try (Connection connection = createConnection()) {
            Map<String, String> fieldMappings = retrieveColumnMappings(connection, tableName, objects.get(0).getClass());
            for (T obj : objects) {
                insertSingleObject(connection, tableName, obj, fieldMappings);
            }
        }
    }

    @Step("Insert single object: {object}")
    public static <T> void insertObjectToDb(String tableName, T object) throws SQLException, ReflectiveOperationException {
        try (Connection connection = createConnection()) {
            Map<String, String> fieldMappings = retrieveColumnMappings(connection, tableName, object.getClass());
            insertSingleObject(connection, tableName, object, fieldMappings);
        }
    }

    @Step("Delete {where} from {tableName}")
    public static void deleteEntryFromDb(String tableName, String where) throws SQLException {
        if (where == null || where.trim().isEmpty()) {
            throw new IllegalArgumentException("The 'where' clause cannot be empty to prevent deleting all rows.");
        }
        String deleteQuery = String.format("DELETE FROM %s WHERE %s", tableName, where);
        try (Connection connection = createConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            System.out.println("Executing delete query: " + deleteQuery);
            statement.executeUpdate();
        }
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(CLICKHOUSE_HOST, CLICKHOUSE_USER, CLICKHOUSE_PASSWORD);
    }

    private static <T> void insertSingleObject(Connection connection, String tableName, T obj, Map<String, String> fieldMappings) throws SQLException, ReflectiveOperationException {
        String insertQuery = buildInsertQuery(tableName, obj, fieldMappings);
        String insertQueryToPrint = insertQuery;
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            int parameterIndex = 1;
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value != null) {
                    if (value instanceof LocalDate) {
                        statement.setDate(parameterIndex++, Date.valueOf((LocalDate) value));
                    } else if (value instanceof LocalDateTime) {
                        statement.setTimestamp(parameterIndex++, Timestamp.valueOf((LocalDateTime) value));
                    } else {
                        statement.setObject(parameterIndex++, value);
                    }
                    insertQueryToPrint = insertQueryToPrint.replaceFirst("\\?", "'" + value + "'");
                }
            }
            System.out.println(insertQueryToPrint);
            statement.executeUpdate();
        }
    }

    private static <T> String buildInsertQuery(String tableName, T obj, Map<String, String> fieldMappings) throws IllegalAccessException {
        StringJoiner columnNames = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(obj) != null) {
                String columnName = fieldMappings.getOrDefault(field.getName(), camelToSnake(field.getName()));
                columnNames.add(columnName);
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

    private static Map<String, String> retrieveColumnMappings(Connection connection, String tableName, Class<?> objClass) throws SQLException {
        Map<String, String> columnMappings = new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();

        try (ResultSet columns = metaData.getColumns(tableName.split("\\.")[0], null, tableName.split("\\.")[1], null)) {
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                // Normalize column names by replacing multiple underscores with a single underscore
                String normalizedColumnName = columnName.replaceAll("__+", "_");

                for (Field field : objClass.getDeclaredFields()) {
                    String fieldName = field.getName();

                    // Compare the field name directly with the normalized column name and with underscores
                    if (fieldName.equalsIgnoreCase(normalizedColumnName) || camelToSnake(fieldName).equalsIgnoreCase(normalizedColumnName)) {
                        columnMappings.put(fieldName, columnName);
                        break;
                    }
                }
            }
        }

        // Print mappings for verification
        System.out.println("Column mappings:");
        for (Map.Entry<String, String> entry : columnMappings.entrySet()) {
            System.out.println("Field: " + entry.getKey() + " -> Column: " + entry.getValue());
        }

        return columnMappings;
    }
}
