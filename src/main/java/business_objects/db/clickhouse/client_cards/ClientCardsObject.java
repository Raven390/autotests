package business_objects.db.clickhouse.client_cards;


import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientCardsObject {

    private Short sourceIdSt;                 // UInt8
    private Short brandUid;                   // UInt8
    private String brand;                     // LowCardinality(String)
    private String regulator;                 // LowCardinality(String)

    private Long id;                          // UInt32
    private Long userId;                      // UInt32

    private String ucid;                      // String

    private OffsetDateTime createTime;        // DateTime64(3)
    private OffsetDateTime updateTime;        // DateTime64(3)

    private Short isDel;                      // UInt8
    private Short usedForDeposit;             // UInt8
    private Short usedForWithdrawal;          // UInt8

    private String cardBeginSixDigits;        // String
    private String cardLastFourDigits;        // String
    private String cardHolderName;            // String

    private Short expiryMonth;                // UInt8
    private Integer expiryYear;               // UInt16

    private Short threeDomainSecure;          // UInt8
    private Short paymentType;                // UInt8
    private Short status;                     // UInt8

    private OffsetDateTime lastUpdated;       // DateTime64(3)
}
