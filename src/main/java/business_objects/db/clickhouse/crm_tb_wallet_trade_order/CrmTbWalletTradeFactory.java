package business_objects.db.clickhouse.crm_tb_wallet_trade_order;

import static utils.Utils.*;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;

public class CrmTbWalletTradeFactory {

    private CrmTbWalletTradeFactory() {}

    @Step("Generate crm___tb_wallet_trade_order object by client")
    public static CrmTbWalletTradeObject generateCrmTbWalletTradeObjectByClient(ClientHelper client) {
        var now = OffsetDateTime.now();
        return CrmTbWalletTradeObject.builder()
                .sourceIdSt(1)
                .brandUid(1)
                .brand(client.getBrand())
                .regulator(client.getRegulator())
                .userId((long) client.getUserId())
                .ucid(client.getUcid())
                .walletAccountNo("AUVF111188234")
                .channel("T00400_008")
                .transferId(BigInteger.valueOf(getRandomLongPositive()))
                .createTime(now)
                .createTimeUtc(now)
                .updateTime(now)
                .updateTimeUtc(now)
                .tradeOrderNo("AUVF111188234USDT176526932500FC")
                .businessOrderNo("VU216160752025120911313261980500")
                .externalOrderNo("1447989056805289984")
                .orderType(0)
                .tradeStyle(0)
                .tradeType(0)
                .tradeDirection(1)
                .fromAccountName("Wallet on chain(USDT)")
                .fromAccountType(2)
                .fromCurrency("USDT")
                .fromAmount(BigDecimal.valueOf(1500))
                .fromAmountUsd(BigDecimal.valueOf(1500))
                .fromAccountNo("AUVF111188234USDT")
                .toAccountName("Crypto Wallet(USDT)")
                .toAccountType(0)
                .toCurrency("USDT")
                .toAmount(BigDecimal.valueOf(1500))
                .toAmountUsd(BigDecimal.valueOf(1500))
                .status(4)
                .tradeTime(now)
                .tradeTimeUtc(now)
                .auditTime(now)
                .auditTimeUtc(now)
                .finishTime(now)
                .finishTimeUtc(now)
                .isDeleted(0)
                .lastUpdated(now)
                .build();
    }
}
