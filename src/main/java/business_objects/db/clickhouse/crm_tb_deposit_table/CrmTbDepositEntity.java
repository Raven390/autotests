package business_objects.db.clickhouse.crm_tb_deposit_table;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrmTbDepositEntity {
    private Integer sourceIdSt;
    private Integer brandUid;
    private String brand;
    private String regulator;
    private Long userId;
    private String ucid;
    private BigInteger account;
    private BigInteger transferId;
    private OffsetDateTime createTime;
    private OffsetDateTime createTimeUtc;
    private OffsetDateTime updateTime;
    private OffsetDateTime updateTimeUtc;
    private BigDecimal amountSubmitted;
    private BigDecimal amountSubmittedUsd;
    private BigDecimal amount;
    private BigDecimal amountUsd;
    private String currency;
    private Integer statusId;
    private String status;
    private String statusGroup;
    private Integer paymentTypeId;
    private String paymentType;
    private Integer paymentChannelId;
    private String paymentChannel;
    private String paymentFamily;
    private String paymentSystemAccount;
    private String paymentSystemCurrency;
    private String paymentDetails;
    private String paymentExpirationDate;
    private String ticket;
    private String vWalletAccount;
    private BigDecimal fee;
    private String processedNotes;
    private Integer isDel;
    private Integer isNonApp;
    private OffsetDateTime lastUpdated;
    private Long creditCardId;
    private String paymentProfile;
    private String paymentProfileMasked;
    private String paymentProfileKey;
    private Integer isDeleted;
    private String cardHolderName;
    private String firstSixDigits;
    private String threeDomainSecure;
    private String orderNumber;
}
