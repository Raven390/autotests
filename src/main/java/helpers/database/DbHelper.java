package helpers.database;

import io.qameta.allure.Step;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.net.InetAddress;

import static utils.ConfigFactory.*;

public class DbHelper {

    private static Process sshTunnelProcess;

    @Step("Get objects from table {tableName} with condition {where}")
    public static <T> List<T> getObjectsFromDB(String tableName, String where, Class<T> className) throws Exception {
        return getObjectsFromDB(DbName.CLICKHOUSE, tableName, where, className);
    }

    @Step("Get objects from {dbName}, table {tableName} with condition {where}")
    public static <T> List<T> getObjectsFromDB(DbName dbName, String tableName, String where, Class<T> className) throws Exception {
        if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
            startSshTunnel();
        }

        try (Connection connection = createConnection(dbName)) {
            return fetchObjects(connection, tableName, where, className);
        } finally {
            if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
                stopSshTunnel();
            }
        }
    }

    private static <T> List<T> fetchObjects(Connection connection, String tableName, String where, Class<T> className) throws Exception {
        String query;
        if (where == null || where.isEmpty()){
            query = String.format("SELECT * FROM %s", tableName);
        } else {
            query = String.format("SELECT * FROM %s WHERE %s", tableName, where);
        }
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            System.out.println(query);
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

        if (targetType.equals(Integer.class) && value instanceof Number) {
            return ((Number) value).intValue();
        } else if (targetType.equals(Double.class) && value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (targetType.equals(Long.class) && value instanceof Number) {
            return ((Number) value).longValue();
        } else if (targetType.equals(LocalDate.class) && value instanceof Date) {
            return ((Date) value).toLocalDate();
        } else if (targetType.equals(LocalDateTime.class) && value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        } else if (targetType.equals(String.class) && value instanceof UUID) {
            return value.toString(); // Convert UUID to String
        } else if (targetType.equals(String.class) && value instanceof LocalDateTime) {
            // Convert LocalDateTime to String
            return ((LocalDateTime) value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else if (targetType.equals(String.class) && value instanceof LocalDate) {
            // Convert LocalDate to String
            return ((LocalDate) value).format(DateTimeFormatter.ISO_LOCAL_DATE);
        } else if (targetType.equals(String.class) && value instanceof Timestamp) {
            // Convert Timestamp to String
            return ((Timestamp) value).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else if (targetType.equals(String.class) && value instanceof String[]) {
            return String.join(",", (String[]) value); // Convert String array to single String
        } else if (targetType.equals(String.class) && value instanceof InetAddress) {
            // Convert InetAddress (including Inet4Address) to String
            return ((InetAddress) value).getHostAddress();
        } else if (targetType.equals(String.class) && value instanceof org.postgresql.util.PGobject) {
            // Convert PGobject to String
            return ((org.postgresql.util.PGobject) value).getValue();
        }

        throw new IllegalArgumentException(String.format(
                "Cannot convert value of type %s to type %s",
                value.getClass().getName(), targetType.getName()
        ));
    }

    private static <T> Map<String, Field> mapDbColumnsToFields(ResultSet resultSet, Class<T> className) throws SQLException {
        Map<String, Field> fieldMappings = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);

            for (Field field : className.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(columnName) || camelToSnake(field.getName()).equalsIgnoreCase(columnName)) {
                    fieldMappings.put(columnName, field);
                    break;
                }
            }
        }
        return fieldMappings;
    }

    @Step("Insert objects: {objects}")
    public static <T> void insertObjectsToDb(String tableName, List<T> objects) throws SQLException, ReflectiveOperationException {
        insertObjectsToDb(DbName.CLICKHOUSE, tableName, objects);
    }

    @Step("Insert objects: {objects} to {dbName}")
    public static <T> void insertObjectsToDb(DbName dbName, String tableName, List<T> objects) throws SQLException, ReflectiveOperationException {
        if (objects == null || objects.isEmpty()) return;

        if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
            startSshTunnel();
        }

        try (Connection connection = createConnection(dbName)) {
            insertObjects(connection, tableName, objects);
        } finally {
            if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
                stopSshTunnel();
            }
        }
    }

    @Step("Insert single object: {object}")
    public static <T> void insertObjectToDb(String tableName, T object) throws SQLException, ReflectiveOperationException {
        insertObjectToDb(DbName.CLICKHOUSE, tableName, object);
    }

    @Step("Insert single object: {object} to {dbName}")
    public static <T> void insertObjectToDb(DbName dbName, String tableName, T object) throws SQLException, ReflectiveOperationException {
        if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
            startSshTunnel();
        }

        try (Connection connection = createConnection(dbName)) {
            insertSingleObject(connection, tableName, object);
        } finally {
            if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
                stopSshTunnel();
            }
        }
    }

    @Step("Delete {where} from {tableName}")
    public static void deleteEntryFromDb(String tableName, String where) throws SQLException {
        deleteEntryFromDb(DbName.CLICKHOUSE, tableName, where);
    }

    @Step("Delete {where} from {tableName} in {dbName}")
    public static void deleteEntryFromDb(DbName dbName, String tableName, String where) throws SQLException {
        if (where == null || where.trim().isEmpty()) {
            throw new IllegalArgumentException("The 'where' clause cannot be empty to prevent deleting all rows.");
        }

        if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
            startSshTunnel();
        }

        String query = String.format("DELETE FROM %s WHERE %s", tableName, where);
        try (Connection connection = createConnection(dbName);
             PreparedStatement statement = connection.prepareStatement(query)) {
            System.out.println(query);
            statement.executeUpdate();
        } finally {
            if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
                stopSshTunnel();
            }
        }
    }

    @Step("Execute query: {query} to {dbName}")
    public static void executeQueryToDb(DbName dbName, String query) throws SQLException {
        if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
            startSshTunnel();
        }

        try (Connection connection = createConnection(dbName)) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        } finally {
            if ((dbName == DbName.MITIGATION_POSTGRES || dbName == DbName.AUDIT || dbName == DbName.BO)&&((!"GITLAB_CI".equals(System.getenv("RUNNER"))))) {
                stopSshTunnel();
            }
        }
    }

    private static Connection createConnection(DbName dbName) throws SQLException {
        if (dbName == DbName.MITIGATION_POSTGRES) {
            return createPostgresConnection();
        }
        else if (dbName == DbName.AUDIT) {
            return createPostgresConnectionAudit();
        }
        else if (dbName == DbName.BO) {
            return createPostgresConnectionBO();
        }
        else {
            return DriverManager.getConnection(CLICKHOUSE_HOST, CLICKHOUSE_USER, CLICKHOUSE_PASSWORD);
        }
    }

    private static Connection createPostgresConnection() throws SQLException {
        String jdbcUrl;
        if("GITLAB_CI".equals(System.getenv("RUNNER"))) {
             jdbcUrl = String.format("jdbc:postgresql://"+POSTGRE_DB_HOST+":%s/%s", MITIGATION_DB_PORT, MITIGATION_DB_NAME);
        } else {
             jdbcUrl = String.format("jdbc:postgresql://localhost:%s/%s", MITIGATION_DB_PORT, MITIGATION_DB_NAME);
        }
        System.out.println("++++++++++++++++"+jdbcUrl+"+++++++++++++++++++++");

        Properties connectionProps = new Properties();
        connectionProps.setProperty("user", MITIGATION_DB_USER);
        connectionProps.setProperty("password", MITIGATION_DB_PASSWORD);

        return DriverManager.getConnection(jdbcUrl, connectionProps);
    }

    private static Connection createPostgresConnectionAudit() throws SQLException {
        String jdbcUrl;
        if("GITLAB_CI".equals(System.getenv("RUNNER"))) {
            jdbcUrl = String.format("jdbc:postgresql://"+POSTGRE_DB_HOST+":%s/%s", MITIGATION_DB_PORT, AUDIT_DB_NAME);
        } else {
            jdbcUrl = String.format("jdbc:postgresql://localhost:%s/%s", MITIGATION_DB_PORT, AUDIT_DB_NAME);
        }
        System.out.println("++++++++++++++++"+jdbcUrl+"+++++++++++++++++++++");
        Properties connectionProps = new Properties();
        connectionProps.setProperty("user", AUDIT_DB_USER);
        connectionProps.setProperty("password", AUDIT_DB_PASSWORD);

        return DriverManager.getConnection(jdbcUrl, connectionProps);
    }

    private static Connection createPostgresConnectionBO() throws SQLException {
        String jdbcUrl;
        if("GITLAB_CI".equals(System.getenv("RUNNER"))) {
            jdbcUrl = String.format("jdbc:postgresql://"+POSTGRE_DB_HOST+":%s/%s", MITIGATION_DB_PORT, BO_DB_NAME);
        } else {
            jdbcUrl = String.format("jdbc:postgresql://localhost:%s/%s", MITIGATION_DB_PORT, BO_DB_NAME);
        }
        System.out.println("++++++++++++++++"+jdbcUrl+"+++++++++++++++++++++");

        Properties connectionProps = new Properties();
        connectionProps.setProperty("user", BO_DB_USER);
        connectionProps.setProperty("password", BO_DB_PASSWORD);

        return DriverManager.getConnection(jdbcUrl, connectionProps);
    }

    private static void startSshTunnel() {
        if (sshTunnelProcess != null && sshTunnelProcess.isAlive()) {
            return; // Tunnel is already running
        }

        String sshCommand = String.join("","ssh -i ",
                MITIGATION_DB_SSH_PRIVATE_KEY,
                " -L ",
                MITIGATION_DB_PORT,
                ":",
                MITIGATION_DB_HOST,
                ":",
                MITIGATION_DB_PORT,
                " ",
                MITIGATION_DB_SSH_USER,
                "@",
                MITIGATION_DB_SSH_HOST
        );
        System.out.println(sshCommand);

        try {
            new ProcessBuilder("chmod", "600", System.getProperty("user.dir") + "/" + MITIGATION_DB_SSH_PRIVATE_KEY).start();
            Thread.sleep(500);
            sshTunnelProcess = new ProcessBuilder("bash", "-c", sshCommand)
                    .start();
            Thread.sleep(2000); // Wait for the tunnel to establish
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to start SSH tunnel", e);
        }
    }

    private static void stopSshTunnel() {
        if (sshTunnelProcess != null) {
            sshTunnelProcess.destroy();
            sshTunnelProcess = null;
        }
    }

    private static <T> void insertObjects(Connection connection, String tableName, List<T> objects) throws SQLException, ReflectiveOperationException {
        Map<String, String> fieldMappings = retrieveColumnMappings(connection, tableName, objects.get(0).getClass());
        for (T obj : objects) {
            insertSingleObject(connection, tableName, obj, fieldMappings);
        }
    }

    private static <T> void insertSingleObject(Connection connection, String tableName, T obj) throws SQLException, ReflectiveOperationException {
        Map<String, String> fieldMappings = retrieveColumnMappings(connection, tableName, obj.getClass());
        insertSingleObject(connection, tableName, obj, fieldMappings);
    }

    private static <T> void insertSingleObject(Connection connection, String tableName, T obj, Map<String, String> fieldMappings)
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

                    // Set value in the PreparedStatement
                    if (value instanceof LocalDate) {
                        statement.setDate(parameterIndex++, Date.valueOf((LocalDate) value));
                    } else if (value instanceof LocalDateTime) {
                        statement.setTimestamp(parameterIndex++, Timestamp.valueOf((LocalDateTime) value));
                    } else {
                        statement.setObject(parameterIndex++, value);
                    }
                }
            }

            // Print the query with filled values
            System.out.println("Executing Query: " + filledQuery);

            // Execute the query
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

    private static Map<String, String> retrieveColumnMappings(Connection connection, String tableName, Class<?> objClass) throws SQLException {
        Map<String, String> columnMappings = new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                for (Field field : objClass.getDeclaredFields()) {
                    if (field.getName().equalsIgnoreCase(columnName) || camelToSnake(field.getName()).equalsIgnoreCase(columnName)) {
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
}