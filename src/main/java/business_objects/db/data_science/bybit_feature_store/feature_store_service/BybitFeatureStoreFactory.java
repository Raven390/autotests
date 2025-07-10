package business_objects.db.data_science.bybit_feature_store.feature_store_service;

import helpers.data.ClientHelper;

import java.math.BigDecimal;

import static utils.Utils.getCurrentTimestampDbFormat;

public class BybitFeatureStoreFactory {

    /**
     * Creates a BybitFeatureStore object with action=4 (withdrawal)
     */
    public static BybitFeatureStore getBybitFeatureStoreWithdrawalObject(ClientHelper client) {
        BybitFeatureStore data = createBaseBybitFeatureStore(client);

        // Set specific values for withdrawal action
        data.setAction(4);
        data.setEntry(0);
        data.setWithdrawalUsd(new BigDecimal("-278.66"));
        data.setTimeDiff(71863L);
        data.setTimeDiffReg(76720L);
        data.setCumavgTimeDiff(new BigDecimal("25573.44"));
        data.setCumUniqueSymbolUnderlying(2L);
        data.setCumavgDepositUsd(new BigDecimal("180.01"));
        data.setCumavgWithdrawalUsd(new BigDecimal("-278.67"));
        data.setCumavgNotionalValueUsd(new BigDecimal("66203.49"));
        data.setCumavgNotionalValueUsdAction0(new BigDecimal("66153.59"));
        data.setCumavgNotionalValueUsdAction1(new BigDecimal("66253.39"));
        data.setCumavgNotionalValueUsdEntry0(new BigDecimal("22051.19"));
        data.setCumavgNotionalValueUsdEntry1(new BigDecimal("66253.39"));
        data.setCumavgProfitUsd(new BigDecimal("99.80"));
        data.setCumavgVolumeLots(new BigDecimal("0.2000"));
        data.setCumcountAction(4L);
        data.setCumcountAction0(1L);
        data.setCumcountAction1(1L);
        data.setCumcountAction2(1L);
        data.setCumcountAction4(1L);
        data.setCumcountAction6(0L);
        data.setCumcountActionTrade(2L);
        data.setCumcountEntry0(3L);
        data.setCumcountEntry1(1L);
        data.setCumsumAmountUsdAction(new BigDecimal("99.80"));
        data.setCumsumDepositUsd(new BigDecimal("180.01"));
        data.setCumsumWithdrawalUsd(new BigDecimal("-278.67"));
        data.setCumsumVolumeLots(new BigDecimal("0.4000"));
        data.setCumsumLotsSymbol(new BigDecimal("0.0000"));
        data.setCumsumNotionalValueUsdAction0(new BigDecimal("66153.59"));
        data.setCumsumNotionalValueUsdAction1(new BigDecimal("66253.39"));
        data.setCumsumNotionalValueUsdEntry0(new BigDecimal("66153.59"));
        data.setCumsumNotionalValueUsdEntry1(new BigDecimal("66253.39"));
        data.setCumsumProfitSymbol(new BigDecimal("0.00"));
        data.setCumsumProfitUsd(new BigDecimal("99.80"));
        data.setCumsumCommissionUsd(new BigDecimal("-1.20"));
        data.setCreditToDeposit(0.0);
        data.setBonusToDeposit(0.0);
        data.setProfitSymbolToProfit(0.0);
        data.setProfitToCredit(0.0);
        data.setProfitToBonus(0.0);
        data.setProfitToDeposit(0.5544);
        data.setProfitToDepositCredit(0.5544);
        data.setShareSymbol(0.0);
        data.setRatioAction0(0.5);
        data.setRatioAction1(0.5);
        data.setRatioAction2(0.25);
        data.setRatioAction4(0.25);
        data.setRatioActionTrade(0.5);
        data.setRatioEntry0(1.5);
        data.setRatioEntry1(0.5);

        return data;
    }

    /**
     * Creates a base BybitFeatureStore object with common default values
     */
    private static BybitFeatureStore createBaseBybitFeatureStore(ClientHelper client) {
        BybitFeatureStore data = new BybitFeatureStore();

        data.setUcid(client.getUcid());
        data.setTimeUtc(getCurrentTimestampDbFormat());
        data.setInsertTimeUtc(getCurrentTimestampDbFormat());
        data.setId(client.getTradingAccount() + "-0");

        // Set default values for all fields
        BigDecimal zero = new BigDecimal("0.00");
        data.setNotionalValueUsd(zero);
        data.setNotionalValueUsdAction0(zero);
        data.setNotionalValueUsdAction1(zero);
        data.setNotionalValueUsdEntry0(zero);
        data.setNotionalValueUsdEntry1(zero);
        data.setAmountUsdAction(zero);
        data.setSymbolUnderlying("");
        data.setVolumeLots(new BigDecimal("0.0000"));
        data.setProfitUsd(zero);
        data.setCommissionUsd(zero);
        data.setSl(zero);
        data.setTp(zero);
        data.setDepositUsd(zero);
        data.setWithdrawalUsd(zero);
        data.setCreditUsd(zero);
        data.setBonusUsd(zero);
        data.setTimeDiff(0L);
        data.setTimeDiffReg(0L);
        data.setCumavgTimeDiff(zero);
        data.setCumUniqueSymbolUnderlying(0L);
        data.setCumavgDepositUsd(zero);
        data.setCumavgWithdrawalUsd(zero);
        data.setCumavgCreditUsd(zero);
        data.setCumavgBonusUsd(zero);
        data.setCumavgNotionalValueUsd(zero);
        data.setCumavgNotionalValueUsdAction0(zero);
        data.setCumavgNotionalValueUsdAction1(zero);
        data.setCumavgNotionalValueUsdEntry0(zero);
        data.setCumavgNotionalValueUsdEntry1(zero);
        data.setCumavgProfitUsd(zero);
        data.setCumavgVolumeLots(new BigDecimal("0.0000"));
        data.setCumcountAction(1L);
        data.setCumcountAction0(0L);
        data.setCumcountAction1(0L);
        data.setCumcountAction2(0L);
        data.setCumcountAction3(0L);
        data.setCumcountAction4(0L);
        data.setCumcountAction6(0L);
        data.setCumcountActionTrade(0L);
        data.setCumcountEntry0(0L);
        data.setCumcountEntry1(0L);
        data.setCumsumAmountUsdAction(zero);
        data.setCumsumDepositUsd(zero);
        data.setCumsumWithdrawalUsd(zero);
        data.setCumsumCreditUsd(zero);
        data.setCumsumBonusUsd(zero);
        data.setCumsumVolumeLots(new BigDecimal("0.0000"));
        data.setCumsumLotsSymbol(new BigDecimal("0.0000"));
        data.setCumsumNotionalValueUsdAction0(zero);
        data.setCumsumNotionalValueUsdAction1(zero);
        data.setCumsumNotionalValueUsdEntry0(zero);
        data.setCumsumNotionalValueUsdEntry1(zero);
        data.setCumsumProfitSymbol(zero);
        data.setCumsumProfitUsd(zero);
        data.setCumsumCommissionUsd(zero);
        data.setCreditToDeposit(0.0);
        data.setBonusToDeposit(0.0);
        data.setProfitSymbolToProfit(0.0);
        data.setProfitToCredit(0.0);
        data.setProfitToBonus(0.0);
        data.setProfitToDeposit(0.0);
        data.setProfitToDepositCredit(0.0);
        data.setShareSymbol(0.0);
        data.setRatioAction0(0.0);
        data.setRatioAction1(0.0);
        data.setRatioAction2(0.0);
        data.setRatioAction3(0.0);
        data.setRatioAction4(0.0);
        data.setRatioAction6(0.0);
        data.setRatioActionTrade(0.0);
        data.setRatioEntry0(0.0);
        data.setRatioEntry1(0.0);

        return data;
    }
}
