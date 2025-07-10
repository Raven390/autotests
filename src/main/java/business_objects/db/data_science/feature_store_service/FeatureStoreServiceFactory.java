package business_objects.db.data_science.feature_store_service;

import helpers.data.ClientHelper;
import java.math.BigDecimal;

import static utils.Utils.getCurrentTimestampDbFormat;

public class FeatureStoreServiceFactory {

    /**
     * Creates a FeatureStoreService object with action=2 (deposit)
     */
    public static FeatureStoreService getFeatureStoreServiceDepositObject(ClientHelper client) {
        FeatureStoreService data = createBaseFeatureStoreService(client);

        // Set specific values for deposit action
        data.setAction(2);
        data.setEntry(-1);
        data.setName("Credit Card");
        data.setPaymentChannelId(8);
        data.setPaymentTypeId(1);
        data.setStatusId(5);
        data.setAmountUsdDeposit(new BigDecimal(1000.00));
        data.setAmountUsdDepositCard(new BigDecimal(1000.00));
        data.setAmountUsdDepositSuccess(new BigDecimal(1000.00));

        return data;
    }

    /**
     * Creates a FeatureStoreService object with action=0 (trade open)
     */
    public static FeatureStoreService getFeatureStoreServiceTradeOpenObject(ClientHelper client) {
        FeatureStoreService data = createBaseFeatureStoreService(client);

        // Set specific values for trade open action
        data.setAction(0);
        data.setEntry(0);
        data.setSymbolUnderlying("XAUUSD");
        data.setVolumeLots(new BigDecimal("0.3000"));
        data.setNotionalValueUsd(new BigDecimal("94875.00"));
        data.setNotionalValueUsdAction0(new BigDecimal("94875.00"));
        data.setNotionalValueUsdEntry0(new BigDecimal("94875.00"));
        data.setAmountUsdAction(new BigDecimal("-94875.00"));

        return data;
    }

    /**
     * Creates a FeatureStoreService object with action=1 (trade close)
     */
    public static FeatureStoreService getFeatureStoreServiceTradeCloseObject(ClientHelper client) {
        FeatureStoreService data = createBaseFeatureStoreService(client);

        // Set specific values for trade close action
        data.setAction(1);
        data.setEntry(1);
        data.setSymbolUnderlying("XAUUSD");
        data.setVolumeLots(new BigDecimal("0.3000"));
        data.setNotionalValueUsd(new BigDecimal("95100.00"));
        data.setNotionalValueUsdAction1(new BigDecimal("95100.00"));
        data.setNotionalValueUsdEntry1(new BigDecimal("95100.00"));
        data.setAmountUsdAction(new BigDecimal("95100.00"));
        data.setProfitUsd(new BigDecimal("225.00"));

        return data;
    }

    /**
     * Creates a base FeatureStoreService object with common default values
     */
    private static FeatureStoreService createBaseFeatureStoreService(ClientHelper client) {
        FeatureStoreService data = new FeatureStoreService();

        data.setUcid(client.getUcid().toLowerCase());
        data.setBrand(client.getBrand().toLowerCase());
        data.setTimeUtc(getCurrentTimestampDbFormat());
        data.setInsertTimeUtc(getCurrentTimestampDbFormat());
        data.setId(client.getTradingAccount().toString());

        // Set Integer fields to default values
        data.setEntry(-1);
        data.setPaymentChannelId(-1);
        data.setPaymentTypeId(-1);
        data.setStatusId(-1);
        data.setCommentLabel(0);
        data.setAccountEmailResult(0);
        data.setAccountTelephoneResult(0);

        // Set Long fields to default values
        data.setCountConnection(8L);
        data.setCountFraud(2L);
        data.setTimeDiff(0L);
        data.setTimeDiffReg(0L);
        data.setCumUniqueDeviceId(1L);
        data.setCumUniqueDigitalId(1L);
        data.setCumUniqueOs(1L);
        data.setCumUniqueSessionId(1L);
        data.setCumUniqueSymbolUnderlying(1L);
        data.setCumUniqueWebSessionId(1L);
        data.setCumcountAction(1L);
        data.setCumcountAction0(0L);
        data.setCumcountAction1(0L);
        data.setCumcountAction2(0L);
        data.setCumcountAction3(0L);
        data.setCumcountAction5(0L);
        data.setCumcountAction6(1L);
        data.setCumcountActionTrade(0L);
        data.setCumcountEntry0(0L);
        data.setCumcountEntry1(0L);
        data.setCumcountDepositCard(0L);
        data.setCumcountDepositCrypto(0L);
        data.setCumcountDepositP2p(0L);
        data.setCumcountDepositTransfer(0L);
        data.setCumcountDepositFail(0L);
        data.setCumcountDepositSuccess(0L);
        data.setCumcountWithdrawalCard(0L);
        data.setCumcountWithdrawalCrypto(0L);
        data.setCumcountWithdrawalP2p(0L);
        data.setCumcountWithdrawalTransfer(0L);
        data.setCumcountWithdrawalFail(0L);
        data.setCumcountWithdrawalSuccess(0L);

        // Set Double fields to default values
        data.setDigitalIdTrustScore(0.0);
        data.setPolicyScore(0.0);
        data.setConnectionScore(0.3);
        data.setCumavgDigitalIdTrustScore(64.755);
        data.setCumavgPolicyScore(3.0);
        data.setCreditToDeposit(0.0);
        data.setProfitSymbolToProfit(0.0);
        data.setProfitToCredit(0.0);
        data.setProfitToDeposit(0.0);
        data.setProfitToDepositCredit(0.0);
        data.setShareSymbol(0.0);
        data.setRatioAction0(0.0);
        data.setRatioAction1(0.0);
        data.setRatioAction2(0.0);
        data.setRatioAction3(0.0);
        data.setRatioAction5(0.0);
        data.setRatioAction6(1.0);
        data.setRatioActionTrade(0.0);
        data.setRatioEntry0(0.0);
        data.setRatioEntry1(0.0);
        data.setRatioDepositCard(0.0);
        data.setRatioDepositCrypto(0.0);
        data.setRatioDepositP2p(0.0);
        data.setRatioDepositTransfer(0.0);
        data.setRatioDepositFail(0.0);
        data.setRatioDepositSuccess(0.0);
        data.setRatioWithdrawalCard(0.0);
        data.setRatioWithdrawalCrypto(0.0);
        data.setRatioWithdrawalP2p(0.0);
        data.setRatioWithdrawalTransfer(0.0);
        data.setRatioWithdrawalFail(0.0);
        data.setRatioWithdrawalSuccess(0.0);

        // Set BigDecimal fields to default values
        BigDecimal zero = new BigDecimal("0.00");
        data.setNotionalValueUsd(zero);
        data.setNotionalValueUsdAction0(zero);
        data.setNotionalValueUsdAction1(zero);
        data.setNotionalValueUsdEntry0(zero);
        data.setNotionalValueUsdEntry1(zero);
        data.setAmountUsdAction(zero);
        data.setVolumeLots(new BigDecimal("0.0000"));
        data.setProfitUsd(zero);
        data.setAmountUsdDeposit(zero);
        data.setAmountUsdDepositCard(zero);
        data.setAmountUsdDepositCrypto(zero);
        data.setAmountUsdDepositFail(zero);
        data.setAmountUsdDepositSuccess(zero);
        data.setAmountUsdWithdrawal(zero);
        data.setAmountUsdWithdrawalCard(zero);
        data.setAmountUsdWithdrawalCrypto(zero);
        data.setAmountUsdWithdrawalFail(zero);
        data.setAmountUsdWithdrawalSuccess(zero);
        data.setCreditUsd(zero);
        data.setCumavgAmountUsdDeposit(zero);
        data.setCumavgAmountUsdDepositCard(zero);
        data.setCumavgAmountUsdDepositCrypto(zero);
        data.setCumavgAmountUsdDepositFail(zero);
        data.setCumavgAmountUsdDepositSuccess(zero);
        data.setCumavgAmountUsdWithdrawal(zero);
        data.setCumavgAmountUsdWithdrawalCard(zero);
        data.setCumavgAmountUsdWithdrawalCrypto(zero);
        data.setCumavgAmountUsdWithdrawalFail(zero);
        data.setCumavgAmountUsdWithdrawalSuccess(zero);
        data.setCumavgCreditUsd(zero);
        data.setCumavgNotionalValueUsd(zero);
        data.setCumavgNotionalValueUsdAction0(zero);
        data.setCumavgNotionalValueUsdAction1(zero);
        data.setCumavgNotionalValueUsdEntry0(zero);
        data.setCumavgNotionalValueUsdEntry1(zero);
        data.setCumavgProfitUsd(zero);
        data.setCumavgTimeDiff(zero);
        data.setCumavgVolumeLots(new BigDecimal("0.0000"));
        data.setCumsumAmountUsdAction(zero);
        data.setCumsumAmountUsdDeposit(zero);
        data.setCumsumAmountUsdDepositCard(zero);
        data.setCumsumAmountUsdDepositCrypto(zero);
        data.setCumsumAmountUsdDepositFail(zero);
        data.setCumsumAmountUsdDepositSuccess(zero);
        data.setCumsumAmountUsdWithdrawal(zero);
        data.setCumsumAmountUsdWithdrawalCard(zero);
        data.setCumsumAmountUsdWithdrawalCrypto(zero);
        data.setCumsumAmountUsdWithdrawalFail(zero);
        data.setCumsumAmountUsdWithdrawalSuccess(zero);
        data.setCumsumCreditUsd(zero);
        data.setCumsumVolumeLots(new BigDecimal("0.0000"));
        data.setCumsumLotsSymbol(new BigDecimal("0.0000"));
        data.setCumsumNotionalValueUsdAction0(zero);
        data.setCumsumNotionalValueUsdAction1(zero);
        data.setCumsumNotionalValueUsdEntry0(zero);
        data.setCumsumNotionalValueUsdEntry1(zero);
        data.setCumsumProfitSymbol(zero);
        data.setCumsumProfitUsd(zero);

        // Set String fields to default values
        data.setSymbolUnderlying("-");
        data.setName("-");
        data.setDeviceId("-");
        data.setDigitalId("-");
        data.setOs("-");
        data.setSessionId("-");
        data.setWebSessionId("-");

        return data;
    }
}
