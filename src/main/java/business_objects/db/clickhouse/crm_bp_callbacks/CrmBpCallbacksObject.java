package business_objects.db.clickhouse.crm_bp_callbacks;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrmBpCallbacksObject {

    private Long userId;                     // UInt32
    private String brand;                    // LowCardinality(String)
    private String regulator;                // LowCardinality(String)
    private String ucid;                     // String
    private String businessOrderId;          // String
    private String orderNumber;              // String
    private String type;                     // LowCardinality(String)
    private OffsetDateTime createTime;        // DateTime64(3)
    private UUID messageId;                  // UUID
    private String pspName;                  // LowCardinality(String)
    private String status;                   // LowCardinality(String)

    private Short is3d;                      // UInt8 → Short
    private String declineCode;              // LowCardinality(String)
    private String declineReason;            // LowCardinality(String)

    private Short isDeclined;                // UInt8 → Short
    private Short isFraudDeclined;           // UInt8 → Short

    private Double amount;                   // Float64
    private String currency;                 // LowCardinality(String)

    private String cardHolderName;           // String
    private String cardMaskedNumber;         // String
    private String cardBrand;                // LowCardinality(String)
    private String cardIssuerCountryIso2;    // LowCardinality(String)
    private String cardExpirationDate;       // String
    private String paymentProfileKey;        // String

    private OffsetDateTime kafkaTimestamp;    // DateTime64(3)
    private OffsetDateTime lastUpdated;       // DateTime64(3)
    private OffsetDateTime chInsertTs;        // DateTime64(3)
}
