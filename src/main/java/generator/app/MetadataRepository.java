package generator.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

final class MetadataRepository {

    public record Result(Object value, Timestamp timestamp) {}

    private final Connection connection;
    private final ObjectMapper objectMapper;

    public MetadataRepository(Connection connection, ObjectMapper objectMapper) {
        this.connection = connection;
        this.objectMapper = objectMapper;

        try (var st = connection.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS result (key TEXT, value TEXT, created INTEGER)");
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<Result> find(String name, TypeReference<?> asType) throws IllegalStateException {
        try (var st =
                connection.prepareStatement("SELECT value, created FROM result WHERE key = ? ORDER BY created DESC")) {
            st.setString(1, name);
            var rs = st.executeQuery();
            if (!rs.isBeforeFirst()) {
                return Optional.empty();
            }
            var value = asType == null ? rs.getString("value") : objectMapper.readValue(rs.getString("value"), asType);
            return Optional.of(new Result(value, rs.getTimestamp("created")));
        } catch (SQLException | JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<Result> find(String name) {
        return find(name, null);
    }

    public void save(String name, Object value) throws IllegalStateException {
        try (var st = connection.prepareStatement("INSERT INTO result (key, value, created) VALUES (?, ?, ?)")) {
            st.setString(1, name);
            st.setString(2, objectMapper.writeValueAsString(value));
            st.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            st.executeUpdate();
        } catch (SQLException | JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public void remove(String name, Timestamp timestamp) throws IllegalStateException {
        try (var st = connection.prepareStatement("DELETE FROM result WHERE key = ? AND created = ?")) {
            st.setString(1, name);
            st.setTimestamp(2, timestamp);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
