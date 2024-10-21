package helpers.kafka;

import java.util.Map;

public record MessageWithHeaders(String message, Map<String, String> headers) {
}
