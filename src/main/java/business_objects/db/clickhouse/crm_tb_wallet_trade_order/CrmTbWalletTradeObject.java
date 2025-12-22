package business_objects.db.clickhouse.crm_tb_wallet_trade_order;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrmTbWalletTradeObject {
    private Integer sourceIdSt;
    private Integer brandUid;
    private String brand;
    private String regulator;
    private Long userId;
    private String ucid;
    private String walletAccountNo;
    private String channelAccountNo;
    private String channel;
    private BigInteger transferId;
    private OffsetDateTime createTime;
    private OffsetDateTime createTimeUtc;
    private OffsetDateTime updateTime;
    private OffsetDateTime updateTimeUtc;
    private String tradeOrderNo;
    private String businessOrderNo;
    private String externalOrderNo;
    private Integer orderType;
    private Integer tradeStyle;
    private Integer tradeType;
    private Integer tradeDirection;
    private String fromAccountNo;
    private String fromAccountName;
    private Integer fromAccountType;
    private String fromCurrency;
    private BigDecimal fromAmount;
    private BigDecimal fromAmountUsd;
    private String toAccountNo;
    private String toAccountName;
    private Integer toAccountType;
    private String toCurrency;
    private BigDecimal toAmount;
    private BigDecimal toAmountUsd;
    private Integer status;
    private OffsetDateTime tradeTime;
    private OffsetDateTime tradeTimeUtc;
    private OffsetDateTime auditTime;
    private OffsetDateTime auditTimeUtc;
    private OffsetDateTime finishTime;
    private OffsetDateTime finishTimeUtc;
    private Integer isDeleted;
    private OffsetDateTime lastUpdated;
}
