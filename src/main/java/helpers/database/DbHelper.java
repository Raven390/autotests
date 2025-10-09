package helpers.database;

import io.qameta.allure.Step;
import org.postgresql.jdbc.PgArray;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.net.InetAddress;
import java.util.stream.Collectors;


import static utils.ConfigFactory.*;
import static utils.ConfigFactory.POSTGRES_DB_USER;
import static utils.Utils.writeLog;

public class DbHelper {

    private static final int MAX_RETRIES = 5;
    private static Process sshTunnelProcess;

    @Step("Get objects from table {tableName} with condition {where}")
    public static <T> List<T> getObjectsFromDB(String tableName, String where, Class<T> className) throws Exception {
        return executeWithRetry(() -> getObjectsFromDB(DbName.CLICKHOUSE, tableName, where, className));
    }

    @Step("Get objects from {dbName}, table {tableName} with condition {where}")
    public static <T> List<T> getObjectsFromDB(DbName dbName, String tableName, String where, Class<T> className)
            throws Exception {
        return executeWithRetry(() -> {
            try (Connection connection = createConnection(dbName)) {
                return fetchObjects(connection, tableName, where, className);
            }
        });
    }

    @Step("Get objects from {dbName}, table {tableName} with condition {where}")
    public static <T> List<T> getObjectsFromDB(DbName dbName, String tableName, String where, Class<T> className,
            int retries)
            throws Exception {
        return executeWithRetry(() -> {
            try (Connection connection = createConnection(dbName)) {
                return fetchObjects(connection, tableName, where, className);
            }
        }, retries);
    }

    @Step("Get objects from {dbName}, table {tableName} with condition {where}")
    public static <T> List<T> getObjectsFromDBFinal(DbName dbName, String tableName, String where, Class<T> className) {
        return executeWithRetry(() -> {
            try (Connection connection = createConnection(dbName)) {
                return fetchFinal(connection, tableName, where, className);
            }
        });
    }

    private static <T> List<T> fetchObjects(Connection connection, String tableName, String where, Class<T> className)
            throws Exception {
        String query;
        if (where == null || where.isEmpty()) {
            query = String.format("SELECT * FROM %s", tableName);
        } else if (where.contains("SELECT")) {
            query = String.format(where);
        } else {
            query = String.format("SELECT * FROM %s WHERE %s", tableName, where);
        }
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            writeLog(query);
            return mapResultSetToObjects(resultSet, className);
        }
    }

    private static <T> List<T> fetchFinal(Connection connection, String tableName, String where, Class<T> className)
            throws Exception {
        String query;
        if (where == null || where.isEmpty()) {
            query = String.format("SELECT * FROM %s FINAL", tableName);
        } else {
            query = String.format("SELECT * FROM %s FINAL WHERE %s", tableName, where);
        }
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            writeLog(query);
            return mapResultSetToObjects(resultSet, className);
        }
    }

    private static <T> List<T> mapResultSetToObjects(ResultSet resultSet, Class<T> className) throws Exception {
        List<T> objects = new ArrayList<>();
        Map<String, Field> fieldMappings = mapDbColumnsToFields(resultSet, className);

        while (resultSet.next()) {
            T obj = className.getDeclaredConstructor().newInstance();

            for (Map.Entry<String, Field> entry : fieldMappings.entrySet()) {
                String columnName = entry.getKey();
                Field field = entry.getValue();
                Object dbValue = resultSet.getObject(columnName);

                if (dbValue != null) {
                    field.setAccessible(true);
                    Object convertedValue = convertValue(dbValue, field.getType());
                    field.set(obj, convertedValue);
                }
            }
            objects.add(obj);
        }
        return objects;
    }

    private static Object convertValue(Object value, Class<?> targetType) {
        if (targetType.isAssignableFrom(value.getClass())) {
            return value; // No conversion needed
        }

        // Handle blank String inputs gracefully
        if (value instanceof String) {
            String trimmed = ((String) value).trim();
            if (trimmed.isEmpty()) {
                // For String targets keep empty string, for others return null to avoid NumberFormatException
                return targetType.equals(String.class) ? "" : null;
            }
        }

        // Numeric targets from Number
        if (targetType.equals(Integer.class) && value instanceof Number) {
            return ((Number) value).intValue();
        } else if (targetType.equals(Double.class) && value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (targetType.equals(Long.class) && value instanceof Number) {
            return ((Number) value).longValue();
        }

        // Numeric targets from numeric Strings (non-blank handled above)
        if (targetType.equals(Integer.class) && value instanceof String) {
            return Integer.parseInt(((String) value).trim());
        } else if (targetType.equals(Double.class) && value instanceof String) {
            return Double.parseDouble(((String) value).trim());
        } else if (targetType.equals(Long.class) && value instanceof String) {
            return Long.parseLong(((String) value).trim());
        }

        // Date/time targets
        if (targetType.equals(LocalDate.class) && value instanceof Date) {
            return ((Date) value).toLocalDate();
        } else if (targetType.equals(LocalDateTime.class) && value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        }

        // UUID target from String
        if (targetType.equals(UUID.class) && value instanceof String) {
            String s = ((String) value).trim();
            if (s.isEmpty()) {
                return null;
            } else {
                return UUID.fromString(s);
            }
        }

        // String targets
        if (targetType.equals(String.class) && value instanceof UUID) {
            return value.toString(); // Convert UUID to String
        } else if (targetType.equals(String.class) && value instanceof LocalDateTime) {
            // Convert LocalDateTime to String
            return ((LocalDateTime) value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else if (targetType.equals(String.class) && value instanceof LocalDate) {
            // Convert LocalDate to String
            return ((LocalDate) value).format(DateTimeFormatter.ISO_LOCAL_DATE);
        } else if (targetType.equals(String.class) && value instanceof Timestamp) {
            // Convert Timestamp to ISO-8601 UTC String with 'Z' to avoid timezone shifts
            return ((Timestamp) value).toInstant().toString();
        } else if (targetType.equals(String.class) && value instanceof String[]) {
            return String.join(",", (String[]) value); // Convert String array to single String
        } else if (targetType.equals(String.class) && value instanceof InetAddress) {
            // Convert InetAddress (including Inet4Address) to String
            return ((InetAddress) value).getHostAddress();
        } else if (targetType.equals(String.class) && value instanceof org.postgresql.util.PGobject) {
            // Convert PGobject to String
            return ((org.postgresql.util.PGobject) value).getValue();
        } else if (targetType.equals(String.class) && value instanceof PgArray) {
            // Convert PGArray to String
            try {
                return Arrays.toString((String[]) ((PgArray) value).getArray());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (targetType.equals(String.class)) {
            // Generic fallback: convert any value to String
            return String.valueOf(value);
        }

        throw new IllegalArgumentException(String.format(
                "Cannot convert value of type %s to type %s", value.getClass().getName(), targetType.getName()
        ));
    }

    private static <T> Map<String, Field> mapDbColumnsToFields(ResultSet resultSet, Class<T> className)
            throws SQLException {
        Map<String, Field> fieldMappings = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);

            for (Field field : className.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(columnName.replace("_", ""))) {
                    fieldMappings.put(columnName, field);
                    break;
                }
            }
        }
        return fieldMappings;
    }

    @Step("Insert objects: {objects}")
    public static <T> void insertObjectsToDb(String tableName, List<T> objects) {
        executeWithRetry(() -> {
            insertObjectsToDb(DbName.CLICKHOUSE, tableName, objects);
            return null;
        });
    }

    @Step("Insert objects: {objects}")
    public static <T> void insertObjectsToDbSlow(String tableName, List<T> objects) {
        executeWithRetry(() -> {
            insertObjectsToDbSlow(DbName.CLICKHOUSE, tableName, objects);
            return null;
        });
    }

    @Step("Insert objects: {objects} to {dbName}")
    public static <T> void insertObjectsToDb(DbName dbName, String tableName, List<T> objects) {
        if (objects == null || objects.isEmpty()) return;
        executeWithRetry(() -> {
            try (Connection connection = createConnection(dbName)) {
                insertObjects(connection, tableName, objects);
            }
            return null;
        });
    }

    @Step("Insert objects: {objects} to {dbName}")
    public static <T> void insertObjectsToDbSlow(DbName dbName, String tableName, List<T> objects) {
        if (objects == null || objects.isEmpty()) return;
        executeWithRetry(() -> {
            try (Connection connection = createConnection(dbName)) {
                insertObjectsSlow(connection, tableName, objects);
            }
            return null;
        });
    }

    @Step("Insert single object: {object}")
    public static <T> void insertObjectToDb(String tableName, T object) {
        executeWithRetry(() -> {
            insertObjectToDb(DbName.CLICKHOUSE, tableName, object);
            return null;
        });
    }

    @Step("Insert single object: {object} to {dbName}")
    public static <T> void insertObjectToDb(DbName dbName, String tableName, T object) {
        executeWithRetry(() -> {
            try (Connection connection = createConnection(dbName)) {
                insertSingleObject(connection, tableName, object);
            }
            return null;
        });
    }

    @Step("Delete {where} from {tableName}")
    public static void deleteEntryFromDb(String tableName, String where) {
        executeWithRetry(() -> {
            deleteEntryFromDb(DbName.CLICKHOUSE, tableName, where);
            return null;
        });
    }

    public static void deleteObjectFromDb(String tableName, String where) {
        deleteEntryFromDb(tableName, where);
    }

    @Step("Delete entries from {tableName} in {dbName} where {columnName} matches the provided values")
    public static void deleteObjectsFromDb(DbName dbName, String tableName, String columnName, List<?> values)
            throws SQLException {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("The 'values' list cannot be null or empty to prevent unintended deletions.");
        }

        // Ensure all values are of supported types (String or Number)
        for (Object value : values) {
            if (!(value instanceof String || value instanceof Number)) {
                throw new IllegalArgumentException(
                        "Unsupported value type: " + value.getClass().getSimpleName() + ". Only String or Number is allowed.");
            }
        }

        // Build the IN clause dynamically
        String placeholders = values.stream().map(value -> {
            if (value instanceof Number) {
                return String.valueOf(value); // Numbers are added directly without quotes
            } else if (value instanceof String) {
                return String.format("'%s'", ((String) value).replace("'", "''").replace("[", "").replace("]", "").replace(", ", "','")); // Escape and quote strings
            } else {
                throw new IllegalArgumentException(
                        "Unsupported value type: " + value.getClass().getSimpleName());
            }
        }).collect(Collectors.joining(", "));

        // Generate the SQL query
        String query = String.format("DELETE FROM %s WHERE %s IN (%s)", tableName, columnName, placeholders);

        // Execute the query
        try (Connection connection = createConnection(dbName); PreparedStatement statement = connection.prepareStatement(query)) {
            writeLog("Executing query: " + query);
            statement.executeUpdate();
        }
    }

    @Step("Delete {where} from {tableName} in {dbName}")
    public static void deleteEntryFromDb(DbName dbName, String tableName, String where) throws Exception {
        if (where == null || where.trim().isEmpty()) {
            throw new IllegalArgumentException("The 'where' clause cannot be empty to prevent deleting all rows.");
        }

        String query = String.format("DELETE FROM %s WHERE %s", tableName, where);
        executeWithRetry(() -> {
            try (Connection connection = createConnection(dbName); PreparedStatement statement = connection.prepareStatement(query)) {
                writeLog(query);
                statement.executeUpdate();
            }
            Thread.sleep(100);
            return null;
        });
    }

    @Step("Execute query: {query} to {dbName}")
    public static void executeQueryToDb(DbName dbName, String query) {
        executeWithRetry(() -> {
            try (Connection connection = createConnection(dbName); PreparedStatement statement = connection.prepareStatement(query)) {
                writeLog("Executing query: " + query);
                statement.executeUpdate();
            }
            return null;
        });
    }

    private static Connection createConnection(DbName dbName) throws SQLException {
        if (dbName == DbName.POSTGRES) {
            return createPostgresConnection();
        } else {
            return DriverManager.getConnection(CLICKHOUSE_HOST, CLICKHOUSE_USER, CLICKHOUSE_PASSWORD);
        }
    }

    public static Connection createPostgresConnection() throws SQLException {
        String host;
        if (POSTGRES_DB_HOST != null && !POSTGRES_DB_HOST.isBlank()) {
            host = POSTGRES_DB_HOST;
        } else {
            host = "localhost";
        }
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, POSTGRES_DB_PORT, POSTGRES_DB_NAME);
        writeLog("++++++++++++++++" + jdbcUrl + "+++++++++++++++++++++");

        Properties connectionProps = new Properties();
        connectionProps.setProperty("user", POSTGRES_DB_USER);
        connectionProps.setProperty("password", POSTGRES_DB_PASSWORD);

        return DriverManager.getConnection(jdbcUrl, connectionProps);
    }

    public static void startSshTunnel() {
        if (sshTunnelProcess != null && sshTunnelProcess.isAlive()) {
            return; // Tunnel is already running
        }

        if (!"GITLAB_CI".equals(System.getenv("RUNNER"))) {
            String sshCommand = String.join("", "ssh -i ", POSTGRES_DB_SSH_PRIVATE_KEY, " -L ", POSTGRES_DB_PORT, ":", POSTGRES_DB_HOST, ":", POSTGRES_DB_PORT, " ", POSTGRES_DB_SSH_USER, "@", POSTGRES_DB_SSH_HOST
            );
            writeLog(sshCommand);

            try {
                new ProcessBuilder("chmod", "600", System.getProperty("user.dir") + "/" + POSTGRES_DB_SSH_PRIVATE_KEY).start();
                Thread.sleep(500);
                sshTunnelProcess = new ProcessBuilder("bash", "-c", sshCommand).start();
                Thread.sleep(2000); // Wait for the tunnel to establish
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Failed to start SSH tunnel", e);
            }
        }
    }

    public static void stopSshTunnel() {
        if (sshTunnelProcess != null) {
            sshTunnelProcess.destroy();
            sshTunnelProcess = null;
        }
    }

    private static <T> void insertObjects(Connection connection, String tableName, List<T> objects) throws SQLException,
            ReflectiveOperationException {
        Map<String, String> fieldMappings = retrieveColumnMappings(connection, tableName, objects.get(0).getClass());
        for (T obj : objects) {
            insertSingleObject(connection, tableName, obj, fieldMappings);
        }
    }

    private static <T> void insertObjectsSlow(Connection connection, String tableName, List<T> objects)
            throws SQLException,
            ReflectiveOperationException, InterruptedException {
        Map<String, String> fieldMappings = retrieveColumnMappings(connection, tableName, objects.get(0).getClass());
        for (T obj : objects) {
            insertSingleObject(connection, tableName, obj, fieldMappings);
            Thread.sleep(500);
        }
    }

    private static <T> void insertSingleObject(Connection connection, String tableName, T obj) throws SQLException,
            ReflectiveOperationException {
        Map<String, String> fieldMappings = retrieveColumnMappings(connection, tableName, obj.getClass());
        insertSingleObject(connection, tableName, obj, fieldMappings);
    }

    private static <T> void insertSingleObject(Connection connection, String tableName, T obj,
            Map<String, String> fieldMappings)
            throws SQLException, ReflectiveOperationException {
        String insertQuery = buildInsertQuery(tableName, obj, fieldMappings);
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            int parameterIndex = 1;
            StringBuilder filledQuery = new StringBuilder(insertQuery);
            int placeholderIndex = 0; // Tracks where placeholders ("?") are in the query.

            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    // Replace placeholders with actual values for debugging.
                    int questionMarkPos = filledQuery.indexOf("?", placeholderIndex);
                    String replacement;
                    if (value instanceof String || value instanceof LocalDate || value instanceof LocalDateTime) {
                        replacement = "'" + value + "'";
                    } else {
                        replacement = value.toString();
                    }
                    filledQuery.replace(questionMarkPos, questionMarkPos + 1, replacement);
                    placeholderIndex = questionMarkPos + replacement.length();

                    // Determine the mapped column name for this field
                    String columnName = fieldMappings.getOrDefault(field.getName(), camelToSnake(field.getName()));

                    // Set value in the PreparedStatement (with special handling for jsonb)
                    if (value instanceof LocalDate) {
                        statement.setDate(parameterIndex++, Date.valueOf((LocalDate) value));
                    } else if (value instanceof LocalDateTime) {
                        statement.setTimestamp(parameterIndex++, Timestamp.valueOf((LocalDateTime) value));
                    } else
                        if ("payload".equalsIgnoreCase(columnName) && tableName.toLowerCase().endsWith("payment_details")) {
                            // Bind as jsonb for Postgres to avoid VARCHAR -> JSONB type mismatch
                            org.postgresql.util.PGobject jsonbObject = new org.postgresql.util.PGobject();
                            jsonbObject.setType("jsonb");
                            jsonbObject.setValue(value.toString());
                            statement.setObject(parameterIndex++, jsonbObject);
                        } else {
                            statement.setObject(parameterIndex++, value);
                        }
                }
            }

            // Print the query with filled values
            writeLog("Executing Query: " + filledQuery);

            // Execute the query
            statement.executeUpdate();
        }
    }

    private static <T> String buildInsertQuery(String tableName, T obj, Map<String, String> fieldMappings)
            throws IllegalAccessException {
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

    private static Map<String, String> retrieveColumnMappings(Connection connection, String tableName,
            Class<?> objClass) throws SQLException {
        Map<String, String> columnMappings = new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();
        String catalog = null;
        String schema = null;
        String table;
        String[] arr = tableName.split("\\.");
        if (arr.length == 3) {
            catalog = arr[0];
            schema = arr[1];
            table = arr[2];
        } else if (arr.length == 2) {
            schema = arr[0];
            table = arr[1];
        } else {
            table = tableName;
        }
        try (ResultSet columns = metaData.getColumns(catalog, schema, table, null)) {
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                for (Field field : objClass.getDeclaredFields()) {
                    if (field.getName().equalsIgnoreCase(columnName.replace("_", ""))) {
                        columnMappings.put(field.getName(), columnName);
                    }
                }
            }
        }
        return columnMappings;
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

    // Generic method to handle retries
    private static <T> T executeWithRetry(DatabaseOperation<T> operation) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                return operation.execute();
            } catch (SQLException | ReflectiveOperationException e) {
                attempt++;
                writeLog("Database operation failed (attempt " + attempt + "): " + e.getMessage());
                if (attempt >= MAX_RETRIES) {
                    throw new RuntimeException("Operation failed after " + MAX_RETRIES + " attempts"); // Give up after 5 attempts
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Operation failed after " + MAX_RETRIES + " attempts");
    }

    private static <T> T executeWithRetry(DatabaseOperation<T> operation, int retries) {
        int attempt = 0;
        while (attempt < retries) {
            try {
                return operation.execute();
            } catch (SQLException | ReflectiveOperationException e) {
                attempt++;
                writeLog("Database operation failed (attempt " + attempt + "): " + e.getMessage());
                if (attempt >= retries) {
                    throw new RuntimeException("Operation failed after " + retries + " attempts"); // Give up after 5 attempts
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Operation failed after " + retries + " attempts");
    }

    // Functional interface for retry logic
    @FunctionalInterface
    private interface DatabaseOperation<T> {
        T execute() throws Exception;
    }

}