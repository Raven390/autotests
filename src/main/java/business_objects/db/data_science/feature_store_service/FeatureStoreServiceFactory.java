package business_objects.db.data_science.feature_store_service;

import helpers.data.ClientHelper;
import java.math.BigDecimal;

import static utils.Utils.getCurrentTimestampDbFormat;

public class FeatureStoreServiceFactory {

    /**
     * Creates a FeatureStoreService object with values matching the second INSERT statement
     * 
     * @return FeatureStoreService object with predefined values
     */
    public static FeatureStoreService createFeatureStoreServiceForInsert1(ClientHelper clientHelper) {
        FeatureStoreService data = new FeatureStoreService();

        // Set String fields
        data.setUcid(clientHelper.getUcid());
        data.setBrand(clientHelper.getBrand().toLowerCase());
        data.setTimeUtc("2024-11-08 16:13:13.000");
        data.setInsertTimeUtc(getCurrentTimestampDbFormat());
        data.setId("62849");
        data.setSymbolUnderlying("-");
        data.setName("-");
        data.setDeviceId("098f03fb22d94377970425d0a9ac0f96");
        data.setDigitalId("477ab8bb5d46424c82de4624a964f766");
        data.setOs("Windows");
        data.setSessionId("894d3d7e74b261d197c87ff12a0567e399c05f675ee765c633474790be6a8026");
        data.setWebSessionId("ff63bc3f-1280-4f64-9d68-6e6df8001fef");

        // Set Integer fields
        data.setAction(6);
        data.setEntry(-1);
        data.setPaymentChannelId(-1);
        data.setPaymentTypeId(-1);
        data.setStatusId(-1);
        data.setCommentLabel(0);
        data.setAccountEmailResult(1);
        data.setAccountTelephoneResult(1);

        // Set BigDecimal fields
        BigDecimal zero = new BigDecimal("0.00");
        data.setNotionalValueUsd(zero);
        data.setNotionalValueUsdAction0(zero);
        data.setNotionalValueUsdAction1(zero);
        data.setNotionalValueUsdEntry0(zero);
        data.setNotionalValueUsdEntry1(zero);
        data.setAmountUsdAction(zero);
        data.setVolumeLots(new BigDecimal("0.0000"));
        data.setProfitUsd(zero);
        data.setStorageUsd(BigDecimal.valueOf(2d));
        data.setCommissionUsd(BigDecimal.valueOf(2d));
        data.setSl(BigDecimal.valueOf(2d));
        data.setTp(BigDecimal.valueOf(2d));
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

        // Set Double fields
        data.setDigitalIdTrustScore(75.4);
        data.setPolicyScore(0.0);
        data.setConnectionScore(1.0);
        data.setRatioFraud(2.0);
        data.setRatioPotential(2.0);

        // Set Long fields
        data.setCountConnection(4L);
        data.setCountFraud(1L);
        data.setCountPotential(2L);
        data.setTimeDiff(16164418L);
        data.setTimeDiffReg(16164418L);
        data.setCumavgTimeDiff(new BigDecimal("16164418.15"));

        // Set additional fields
        data.setSumCpa(BigDecimal.valueOf(2d));
        data.setSumRebate(BigDecimal.valueOf(2d));
        data.setSumSwapFreePositive(BigDecimal.valueOf(2d));
        data.setSumSwapFreeNegative(BigDecimal.valueOf(2d));
        data.setSumSpread(BigDecimal.valueOf(2d));

        // Set cumulative fields
        data.setCumUniqueDeviceId(1L);
        data.setCumUniqueDigitalId(1L);
        data.setCumUniqueOs(1L);
        data.setCumUniqueSessionId(1L);
        data.setCumUniqueSymbolUnderlying(0L);
        data.setCumUniqueWebSessionId(1L);

        data.setCumavgAmountUsdDeposit(new BigDecimal("200.00"));
        data.setCumavgAmountUsdDepositCard(zero);
        data.setCumavgAmountUsdDepositCrypto(zero);
        data.setCumavgAmountUsdDepositFail(new BigDecimal("200.00"));
        data.setCumavgAmountUsdDepositSuccess(zero);
        data.setCumavgAmountUsdWithdrawal(zero);
        data.setCumavgAmountUsdWithdrawalCard(zero);
        data.setCumavgAmountUsdWithdrawalCrypto(zero);
        data.setCumavgAmountUsdWithdrawalFail(zero);
        data.setCumavgAmountUsdWithdrawalSuccess(zero);
        data.setCumavgCreditUsd(zero);
        data.setCumavgDigitalIdTrustScore(75.4);
        data.setCumavgNotionalValueUsd(zero);
        data.setCumavgNotionalValueUsdAction0(zero);
        data.setCumavgNotionalValueUsdAction1(zero);
        data.setCumavgNotionalValueUsdEntry0(zero);
        data.setCumavgNotionalValueUsdEntry1(zero);
        data.setCumavgPolicyScore(0.0);
        data.setCumavgProfitUsd(zero);
        data.setCumavgVolumeLots(new BigDecimal("0.0000"));

        data.setCumcountAction(2L);
        data.setCumcountAction0(0L);
        data.setCumcountAction1(0L);
        data.setCumcountAction2(1L);
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
        data.setCumcountDepositFail(1L);
        data.setCumcountDepositSuccess(1L);
        data.setCumcountWithdrawalCard(0L);
        data.setCumcountWithdrawalCrypto(0L);
        data.setCumcountWithdrawalP2p(0L);
        data.setCumcountWithdrawalTransfer(0L);
        data.setCumcountWithdrawalFail(0L);
        data.setCumcountWithdrawalSuccess(0L);

        data.setCumsumAmountUsdAction(zero);
        data.setCumsumAmountUsdDeposit(new BigDecimal("200.00"));
        data.setCumsumAmountUsdDepositCard(zero);
        data.setCumsumAmountUsdDepositCrypto(zero);
        data.setCumsumAmountUsdDepositFail(new BigDecimal("200.00"));
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
        data.setCumsumStorageUsd(BigDecimal.valueOf(2d));
        data.setCumsumCommissionUsd(BigDecimal.valueOf(2d));

        // Set ratio fields
        data.setCreditToDeposit(0.0);
        data.setProfitSymbolToProfit(0.0);
        data.setProfitToCredit(0.0);
        data.setProfitToDeposit(0.0);
        data.setProfitToDepositCredit(0.0);
        data.setShareSymbol(0.0);
        data.setRatioAction0(0.0);
        data.setRatioAction1(0.0);
        data.setRatioAction2(0.5);
        data.setRatioAction3(0.0);
        data.setRatioAction5(0.0);
        data.setRatioAction6(0.5);
        data.setRatioActionTrade(0.0);
        data.setRatioEntry0(0.0);
        data.setRatioEntry1(0.0);
        data.setRatioDepositCard(0.0);
        data.setRatioDepositCrypto(0.0);
        data.setRatioDepositP2p(0.0);
        data.setRatioDepositTransfer(0.0);
        data.setRatioDepositFail(1.0);
        data.setRatioDepositSuccess(1.0);
        data.setRatioWithdrawalCard(0.0);
        data.setRatioWithdrawalCrypto(0.0);
        data.setRatioWithdrawalP2p(0.0);
        data.setRatioWithdrawalTransfer(0.0);
        data.setRatioWithdrawalFail(0.0);
        data.setRatioWithdrawalSuccess(0.0);

        return data;
    }
}