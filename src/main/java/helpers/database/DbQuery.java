package helpers.database;

import java.util.ArrayList;
import java.util.List;

public class DbQuery {
    private final String tableName;
    private final List<String> conditions = new ArrayList<>();
    private String orderBy;
    private boolean isFinal = false;

    private DbQuery(String tableName) {
        this.tableName = tableName;
    }

    public static DbQuery from(String tableName) {
        return new DbQuery(tableName);
    }

    // For complex or custom conditions
    public DbQuery where(String condition) {
        if (condition != null && !condition.isBlank()) {
            this.conditions.add(condition);
        }
        return this;
    }

    // Prevents quote-related syntax errors and handles nulls gracefully
    public DbQuery whereEquals(String column, Object value) {
        if (value == null) {
            this.conditions.add(column + " IS NULL");
        } else {
            String formattedValue;
            if (value instanceof String) {
                formattedValue = "'" + value + "'";
            } else {
                formattedValue = value.toString();
            }
            this.conditions.add(column + " = " + formattedValue);
        }
        return this;
    }

    public DbQuery orderBy(String column, String direction) {
        this.orderBy = column + " " + direction;
        return this;
    }

    // Specific to ClickHouse
    public DbQuery isFinal() {
        this.isFinal = true;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(tableName);

        if (isFinal) {
            sb.append(" FINAL");
        }
        if (!conditions.isEmpty()) {
            sb.append(" WHERE ").append(String.join(" AND ", conditions));
        }
        if (orderBy != null) {
            sb.append(" ORDER BY ").append(orderBy);
        }
        return sb.toString();
    }
}
