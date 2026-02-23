package business_objects.kafka.ai_decisions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDecisions {

    @JsonProperty(value = "messageId", required = true)
    private UUID messageId;

    @JsonProperty(value = "timestamp", required = true)
    private OffsetDateTime timestamp;

    @JsonProperty(value = "producedAtUtc", required = true)
    private OffsetDateTime producedAtUtc;

    @JsonProperty(value = "alertId", required = true)
    private UUID alertId;

    @JsonProperty("alertDate")
    private OffsetDateTime alertDate;

    @JsonProperty("eventDate")
    private OffsetDateTime eventDate;

    @JsonProperty(value = "alertType", required = true)
    private String alertType;

    @JsonProperty(value = "ucid", required = true)
    private String ucid;

    @JsonProperty(value = "tradingAccount", required = true)
    private Long tradingAccount;

    @JsonProperty(value = "serverId", required = true)
    private Integer serverId;

    @JsonProperty(value = "rule", required = true)
    private String rule;

    @JsonProperty(value = "fraudType", required = true)
    private String fraudType;

    @JsonProperty(value = "classifications", required = true)
    private List<Classification> classifications;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Classification {

        @JsonProperty(value = "category", required = true)
        private String category;

        @JsonProperty("fraud")
        private String fraud;

        @JsonProperty(value = "confidenceScore", required = true)
        private Integer confidenceScore;

        @JsonProperty("reasoningCN")
        private String reasoningCN;

        @JsonProperty("manualReviewCN")
        private String manualReviewCN;

        @JsonProperty("reasoningEN")
        private String reasoningEN;

        @JsonProperty("manualReviewEN")
        private String manualReviewEN;
    }
}
