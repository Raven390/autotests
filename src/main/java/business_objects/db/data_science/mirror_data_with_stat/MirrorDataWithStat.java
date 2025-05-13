package business_objects.db.data_science.mirror_data_with_stat;

import java.util.Objects;

public class MirrorDataWithStat {
    private String accountEmailResult;
    private String accountTelephoneResult;
    private String action;
    private Double amountUsdAction;
    private Double amountUsdDeposit;
    private Double amountUsdDepositCard;
    private Double amountUsdDepositCrypto;
    private Double amountUsdDepositFail;
    private Double amountUsdDepositSuccess;
    private Double amountUsdWithdrawal;
    private Double amountUsdWithdrawalCard;
    private Double amountUsdWithdrawalCrypto;
    private Double amountUsdWithdrawalFail;
    private Double amountUsdWithdrawalSuccess;
    private String commentLabel;
    private Integer countConnection;
    private Integer countFraud;
    private Double connectionScore;
    private Double creditToDeposit;
    private Double creditUsd;
    private Integer cumUniqueDeviceId;
    private Integer cumUniqueDigitalId;
    private Integer cumUniqueOs;
    private Integer cumUniqueSessionId;
    private Integer cumUniqueSymbolUnderlying;
    private Integer cumUniqueWebSessionId;
    private Double cumavgAmountUsdDeposit;
    private Double cumavgAmountUsdDepositCard;
    private Double cumavgAmountUsdDepositCrypto;
    private Double cumavgAmountUsdDepositFail;
    private Double cumavgAmountUsdDepositSuccess;
    private Double cumavgAmountUsdWithdrawal;
    private Double cumavgAmountUsdWithdrawalCard;
    private Double cumavgAmountUsdWithdrawalCrypto;
    private Double cumavgAmountUsdWithdrawalFail;
    private Double cumavgAmountUsdWithdrawalSuccess;
    private Double cumavgCreditUsd;
    private Double cumavgDigitalIdTrustScore;
    private Double cumavgNotionalValueUsd;
    private Double cumavgNotionalValueUsdAction0;
    private Double cumavgNotionalValueUsdAction1;
    private Double cumavgNotionalValueUsdEntry0;
    private Double cumavgNotionalValueUsdEntry1;
    private Double cumavgPolicyScore;
    private Double cumavgProfitUsd;
    private Double cumavgTimeDiffMinute;
    private Double cumavgVolumeLots;
    private Integer cumcountAction;
    private Integer cumcountAction0;
    private Integer cumcountAction1;
    private Integer cumcountAction2;
    private Integer cumcountAction3;
    private Integer cumcountAction4;
    private Integer cumcountAction5;
    private Integer cumcountAction6;
    private Integer cumcountActionTrade;
    private Integer cumcountDepositCard;
    private Integer cumcountDepositCrypto;
    private Integer cumcountDepositP2p;
    private Integer cumcountDepositSuccess;
    private Integer cumcountDepositFail;
    private Integer cumcountDepositTransfer;
    private Integer cumcountEntry0;
    private Integer cumcountEntry1;
    private Integer cumcountWithdrawalCard;
    private Integer cumcountWithdrawalCrypto;
    private Integer cumcountWithdrawalP2p;
    private Integer cumcountWithdrawalSuccess;
    private Integer cumcountWithdrawalFail;
    private Integer cumcountWithdrawalTransfer;
    private Double cumsumAmountUsdAction;
    private Double cumsumAmountUsdDeposit;
    private Double cumsumAmountUsdDepositCard;
    private Double cumsumAmountUsdDepositCrypto;
    private Double cumsumAmountUsdDepositFail;
    private Double cumsumAmountUsdDepositSuccess;
    private Double cumsumAmountUsdWithdrawal;
    private Double cumsumAmountUsdWithdrawalCard;
    private Double cumsumAmountUsdWithdrawalCrypto;
    private Double cumsumAmountUsdWithdrawalFail;
    private Double cumsumAmountUsdWithdrawalSuccess;
    private Double cumsumCreditUsd;
    private Double cumsumLotsSymbol;
    private Double cumsumNotionalValueUsdAction0;
    private Double cumsumNotionalValueUsdAction1;
    private Double cumsumNotionalValueUsdEntry0;
    private Double cumsumNotionalValueUsdEntry1;
    private Double cumsumProfitSymbol;
    private Double cumsumProfitUsd;
    private Double cumsumToxicityUsd;
    private Double cumsumVolumeLots;
    private String deviceId;
    private String digitalId;
    private Double digitalIdTrustScore;
    private String entry;
    private String id;
    private String name;
    private Double notionalValueUsd;
    private Double notionalValueUsdAction0;
    private Double notionalValueUsdAction1;
    private Double notionalValueUsdEntry0;
    private Double notionalValueUsdEntry1;
    private String os;
    private String paymentChannelId;
    private String paymentTypeId;
    private Double policyScore;
    private Double profitSymbolToProfit;
    private Double profitToCredit;
    private Double profitToDeposit;
    private Double profitToDepositCredit;
    private Double profitUsd;
    private String sessionId;
    private Double shareSymbol;
    private String statusId;
    private String symbolUnderlying;
    private String time;
    private String lastUpdated;
    private Double timeDiff;
    private Double timeDiffMinute;
    private Double timeDiffReg;
    private Double timeDiffRegMinute;
    private Double toxicityUsd;
    private String ucid;
    private Double volumeLots;
    private String webSessionId;

    public MirrorDataWithStat() {
    }

    public MirrorDataWithStat(
            String accountEmailResult, String accountTelephoneResult, String action, Double amountUsdAction,
            Double amountUsdDeposit, Double amountUsdDepositCard, Double amountUsdDepositCrypto,
            Double amountUsdDepositFail, Double amountUsdDepositSuccess, Double amountUsdWithdrawal,
            Double amountUsdWithdrawalCard, Double amountUsdWithdrawalCrypto, Double amountUsdWithdrawalFail,
            Double amountUsdWithdrawalSuccess, String commentLabel, Integer countConnection, Integer countFraud,
            Double connectionScore, Double creditToDeposit, Double creditUsd, Integer cumUniqueDeviceId,
            Integer cumUniqueDigitalId, Integer cumUniqueOs, Integer cumUniqueSessionId,
            Integer cumUniqueSymbolUnderlying,
            Integer cumUniqueWebSessionId, Double cumavgAmountUsdDeposit, Double cumavgAmountUsdDepositCard,
            Double cumavgAmountUsdDepositCrypto, Double cumavgAmountUsdDepositFail,
            Double cumavgAmountUsdDepositSuccess,
            Double cumavgAmountUsdWithdrawal, Double cumavgAmountUsdWithdrawalCard,
            Double cumavgAmountUsdWithdrawalCrypto,
            Double cumavgAmountUsdWithdrawalFail, Double cumavgAmountUsdWithdrawalSuccess, Double cumavgCreditUsd,
            Double cumavgDigitalIdTrustScore, Double cumavgNotionalValueUsd, Double cumavgNotionalValueUsdAction0,
            Double cumavgNotionalValueUsdAction1, Double cumavgNotionalValueUsdEntry0,
            Double cumavgNotionalValueUsdEntry1,
            Double cumavgPolicyScore, Double cumavgProfitUsd, Double cumavgTimeDiffMinute, Double cumavgVolumeLots,
            Integer cumcountAction, Integer cumcountAction0, Integer cumcountAction1, Integer cumcountAction2,
            Integer cumcountAction3, Integer cumcountAction4, Integer cumcountAction5, Integer cumcountAction6,
            Integer cumcountActionTrade, Integer cumcountDepositCard, Integer cumcountDepositCrypto,
            Integer cumcountDepositP2p, Integer cumcountDepositSuccess, Integer cumcountDepositFail,
            Integer cumcountDepositTransfer, Integer cumcountEntry0, Integer cumcountEntry1,
            Integer cumcountWithdrawalCard,
            Integer cumcountWithdrawalCrypto, Integer cumcountWithdrawalP2p, Integer cumcountWithdrawalSuccess,
            Integer cumcountWithdrawalFail, Integer cumcountWithdrawalTransfer, Double cumsumAmountUsdAction,
            Double cumsumAmountUsdDeposit, Double cumsumAmountUsdDepositCard, Double cumsumAmountUsdDepositCrypto,
            Double cumsumAmountUsdDepositFail, Double cumsumAmountUsdDepositSuccess, Double cumsumAmountUsdWithdrawal,
            Double cumsumAmountUsdWithdrawalCard, Double cumsumAmountUsdWithdrawalCrypto,
            Double cumsumAmountUsdWithdrawalFail, Double cumsumAmountUsdWithdrawalSuccess, Double cumsumCreditUsd,
            Double cumsumLotsSymbol, Double cumsumNotionalValueUsdAction0, Double cumsumNotionalValueUsdAction1,
            Double cumsumNotionalValueUsdEntry0, Double cumsumNotionalValueUsdEntry1, Double cumsumProfitSymbol,
            Double cumsumProfitUsd, Double cumsumToxicityUsd, Double cumsumVolumeLots, String deviceId,
            String digitalId,
            Double digitalIdTrustScore, String entry, String id, String name, Double notionalValueUsd,
            Double notionalValueUsdAction0, Double notionalValueUsdAction1, Double notionalValueUsdEntry0,
            Double notionalValueUsdEntry1, String os, String paymentChannelId, String paymentTypeId, Double policyScore,
            Double profitSymbolToProfit, Double profitToCredit, Double profitToDeposit, Double profitToDepositCredit,
            Double profitUsd, String sessionId, Double shareSymbol, String statusId, String symbolUnderlying,
            String time, String lastUpdated, Double timeDiff, Double timeDiffMinute,
            Double timeDiffReg, Double timeDiffRegMinute, Double toxicityUsd, String ucid, Double volumeLots,
            String webSessionId) {
        this.accountEmailResult = accountEmailResult;
        this.accountTelephoneResult = accountTelephoneResult;
        this.action = action;
        this.amountUsdAction = amountUsdAction;
        this.amountUsdDeposit = amountUsdDeposit;
        this.amountUsdDepositCard = amountUsdDepositCard;
        this.amountUsdDepositCrypto = amountUsdDepositCrypto;
        this.amountUsdDepositFail = amountUsdDepositFail;
        this.amountUsdDepositSuccess = amountUsdDepositSuccess;
        this.amountUsdWithdrawal = amountUsdWithdrawal;
        this.amountUsdWithdrawalCard = amountUsdWithdrawalCard;
        this.amountUsdWithdrawalCrypto = amountUsdWithdrawalCrypto;
        this.amountUsdWithdrawalFail = amountUsdWithdrawalFail;
        this.amountUsdWithdrawalSuccess = amountUsdWithdrawalSuccess;
        this.commentLabel = commentLabel;
        this.countConnection = countConnection;
        this.countFraud = countFraud;
        this.connectionScore = connectionScore;
        this.creditToDeposit = creditToDeposit;
        this.creditUsd = creditUsd;
        this.cumUniqueDeviceId = cumUniqueDeviceId;
        this.cumUniqueDigitalId = cumUniqueDigitalId;
        this.cumUniqueOs = cumUniqueOs;
        this.cumUniqueSessionId = cumUniqueSessionId;
        this.cumUniqueSymbolUnderlying = cumUniqueSymbolUnderlying;
        this.cumUniqueWebSessionId = cumUniqueWebSessionId;
        this.cumavgAmountUsdDeposit = cumavgAmountUsdDeposit;
        this.cumavgAmountUsdDepositCard = cumavgAmountUsdDepositCard;
        this.cumavgAmountUsdDepositCrypto = cumavgAmountUsdDepositCrypto;
        this.cumavgAmountUsdDepositFail = cumavgAmountUsdDepositFail;
        this.cumavgAmountUsdDepositSuccess = cumavgAmountUsdDepositSuccess;
        this.cumavgAmountUsdWithdrawal = cumavgAmountUsdWithdrawal;
        this.cumavgAmountUsdWithdrawalCard = cumavgAmountUsdWithdrawalCard;
        this.cumavgAmountUsdWithdrawalCrypto = cumavgAmountUsdWithdrawalCrypto;
        this.cumavgAmountUsdWithdrawalFail = cumavgAmountUsdWithdrawalFail;
        this.cumavgAmountUsdWithdrawalSuccess = cumavgAmountUsdWithdrawalSuccess;
        this.cumavgCreditUsd = cumavgCreditUsd;
        this.cumavgDigitalIdTrustScore = cumavgDigitalIdTrustScore;
        this.cumavgNotionalValueUsd = cumavgNotionalValueUsd;
        this.cumavgNotionalValueUsdAction0 = cumavgNotionalValueUsdAction0;
        this.cumavgNotionalValueUsdAction1 = cumavgNotionalValueUsdAction1;
        this.cumavgNotionalValueUsdEntry0 = cumavgNotionalValueUsdEntry0;
        this.cumavgNotionalValueUsdEntry1 = cumavgNotionalValueUsdEntry1;
        this.cumavgPolicyScore = cumavgPolicyScore;
        this.cumavgProfitUsd = cumavgProfitUsd;
        this.cumavgTimeDiffMinute = cumavgTimeDiffMinute;
        this.cumavgVolumeLots = cumavgVolumeLots;
        this.cumcountAction = cumcountAction;
        this.cumcountAction0 = cumcountAction0;
        this.cumcountAction1 = cumcountAction1;
        this.cumcountAction2 = cumcountAction2;
        this.cumcountAction3 = cumcountAction3;
        this.cumcountAction4 = cumcountAction4;
        this.cumcountAction5 = cumcountAction5;
        this.cumcountAction6 = cumcountAction6;
        this.cumcountActionTrade = cumcountActionTrade;
        this.cumcountDepositCard = cumcountDepositCard;
        this.cumcountDepositCrypto = cumcountDepositCrypto;
        this.cumcountDepositP2p = cumcountDepositP2p;
        this.cumcountDepositSuccess = cumcountDepositSuccess;
        this.cumcountDepositFail = cumcountDepositFail;
        this.cumcountDepositTransfer = cumcountDepositTransfer;
        this.cumcountEntry0 = cumcountEntry0;
        this.cumcountEntry1 = cumcountEntry1;
        this.cumcountWithdrawalCard = cumcountWithdrawalCard;
        this.cumcountWithdrawalCrypto = cumcountWithdrawalCrypto;
        this.cumcountWithdrawalP2p = cumcountWithdrawalP2p;
        this.cumcountWithdrawalSuccess = cumcountWithdrawalSuccess;
        this.cumcountWithdrawalFail = cumcountWithdrawalFail;
        this.cumcountWithdrawalTransfer = cumcountWithdrawalTransfer;
        this.cumsumAmountUsdAction = cumsumAmountUsdAction;
        this.cumsumAmountUsdDeposit = cumsumAmountUsdDeposit;
        this.cumsumAmountUsdDepositCard = cumsumAmountUsdDepositCard;
        this.cumsumAmountUsdDepositCrypto = cumsumAmountUsdDepositCrypto;
        this.cumsumAmountUsdDepositFail = cumsumAmountUsdDepositFail;
        this.cumsumAmountUsdDepositSuccess = cumsumAmountUsdDepositSuccess;
        this.cumsumAmountUsdWithdrawal = cumsumAmountUsdWithdrawal;
        this.cumsumAmountUsdWithdrawalCard = cumsumAmountUsdWithdrawalCard;
        this.cumsumAmountUsdWithdrawalCrypto = cumsumAmountUsdWithdrawalCrypto;
        this.cumsumAmountUsdWithdrawalFail = cumsumAmountUsdWithdrawalFail;
        this.cumsumAmountUsdWithdrawalSuccess = cumsumAmountUsdWithdrawalSuccess;
        this.cumsumCreditUsd = cumsumCreditUsd;
        this.cumsumLotsSymbol = cumsumLotsSymbol;
        this.cumsumNotionalValueUsdAction0 = cumsumNotionalValueUsdAction0;
        this.cumsumNotionalValueUsdAction1 = cumsumNotionalValueUsdAction1;
        this.cumsumNotionalValueUsdEntry0 = cumsumNotionalValueUsdEntry0;
        this.cumsumNotionalValueUsdEntry1 = cumsumNotionalValueUsdEntry1;
        this.cumsumProfitSymbol = cumsumProfitSymbol;
        this.cumsumProfitUsd = cumsumProfitUsd;
        this.cumsumToxicityUsd = cumsumToxicityUsd;
        this.cumsumVolumeLots = cumsumVolumeLots;
        this.deviceId = deviceId;
        this.digitalId = digitalId;
        this.digitalIdTrustScore = digitalIdTrustScore;
        this.entry = entry;
        this.id = id;
        this.name = name;
        this.notionalValueUsd = notionalValueUsd;
        this.notionalValueUsdAction0 = notionalValueUsdAction0;
        this.notionalValueUsdAction1 = notionalValueUsdAction1;
        this.notionalValueUsdEntry0 = notionalValueUsdEntry0;
        this.notionalValueUsdEntry1 = notionalValueUsdEntry1;
        this.os = os;
        this.paymentChannelId = paymentChannelId;
        this.paymentTypeId = paymentTypeId;
        this.policyScore = policyScore;
        this.profitSymbolToProfit = profitSymbolToProfit;
        this.profitToCredit = profitToCredit;
        this.profitToDeposit = profitToDeposit;
        this.profitToDepositCredit = profitToDepositCredit;
        this.profitUsd = profitUsd;
        this.sessionId = sessionId;
        this.shareSymbol = shareSymbol;
        this.statusId = statusId;
        this.symbolUnderlying = symbolUnderlying;
        this.time = time;
        this.lastUpdated = lastUpdated;
        this.timeDiff = timeDiff;
        this.timeDiffMinute = timeDiffMinute;
        this.timeDiffReg = timeDiffReg;
        this.timeDiffRegMinute = timeDiffRegMinute;
        this.toxicityUsd = toxicityUsd;
        this.ucid = ucid;
        this.volumeLots = volumeLots;
        this.webSessionId = webSessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MirrorDataWithStat that)) return false;
        return Objects.equals(accountEmailResult, that.accountEmailResult) && Objects.equals(
                accountTelephoneResult, that.accountTelephoneResult) && Objects.equals(action, that.action) && Objects.equals(
                        amountUsdAction, that.amountUsdAction) && Objects.equals(amountUsdDeposit, that.amountUsdDeposit) && Objects.equals(
                                amountUsdDepositCard, that.amountUsdDepositCard) && Objects.equals(amountUsdDepositCrypto, that.amountUsdDepositCrypto) && Objects.equals(
                                        amountUsdDepositFail, that.amountUsdDepositFail) && Objects.equals(amountUsdDepositSuccess, that.amountUsdDepositSuccess) && Objects.equals(
                                                amountUsdWithdrawal, that.amountUsdWithdrawal) && Objects.equals(amountUsdWithdrawalCard, that.amountUsdWithdrawalCard) && Objects.equals(
                                                        amountUsdWithdrawalCrypto, that.amountUsdWithdrawalCrypto) && Objects.equals(
                                                                amountUsdWithdrawalFail, that.amountUsdWithdrawalFail) && Objects.equals(
                                                                        amountUsdWithdrawalSuccess, that.amountUsdWithdrawalSuccess) && Objects.equals(commentLabel, that.commentLabel) && Objects.equals(
                                                                                countConnection, that.countConnection) && Objects.equals(countFraud, that.countFraud) && Objects.equals(
                                                                                        connectionScore, that.connectionScore) && Objects.equals(creditToDeposit, that.creditToDeposit) && Objects.equals(
                                                                                                creditUsd, that.creditUsd) && Objects.equals(cumUniqueDeviceId, that.cumUniqueDeviceId) && Objects.equals(
                                                                                                        cumUniqueDigitalId, that.cumUniqueDigitalId) && Objects.equals(cumUniqueOs, that.cumUniqueOs) && Objects.equals(
                                                                                                                cumUniqueSessionId, that.cumUniqueSessionId) && Objects.equals(cumUniqueSymbolUnderlying, that.cumUniqueSymbolUnderlying) && Objects.equals(
                                                                                                                        cumUniqueWebSessionId, that.cumUniqueWebSessionId) && Objects.equals(cumavgAmountUsdDeposit, that.cumavgAmountUsdDeposit) && Objects.equals(
                                                                                                                                cumavgAmountUsdDepositCard, that.cumavgAmountUsdDepositCard) && Objects.equals(
                                                                                                                                        cumavgAmountUsdDepositCrypto, that.cumavgAmountUsdDepositCrypto) && Objects.equals(
                                                                                                                                                cumavgAmountUsdDepositFail, that.cumavgAmountUsdDepositFail) && Objects.equals(
                                                                                                                                                        cumavgAmountUsdDepositSuccess, that.cumavgAmountUsdDepositSuccess) && Objects.equals(
                                                                                                                                                                cumavgAmountUsdWithdrawal, that.cumavgAmountUsdWithdrawal) && Objects.equals(
                                                                                                                                                                        cumavgAmountUsdWithdrawalCard, that.cumavgAmountUsdWithdrawalCard) && Objects.equals(
                                                                                                                                                                                cumavgAmountUsdWithdrawalCrypto, that.cumavgAmountUsdWithdrawalCrypto) && Objects.equals(
                                                                                                                                                                                        cumavgAmountUsdWithdrawalFail, that.cumavgAmountUsdWithdrawalFail) && Objects.equals(
                                                                                                                                                                                                cumavgAmountUsdWithdrawalSuccess, that.cumavgAmountUsdWithdrawalSuccess) && Objects.equals(
                                                                                                                                                                                                        cumavgCreditUsd, that.cumavgCreditUsd) && Objects.equals(cumavgDigitalIdTrustScore, that.cumavgDigitalIdTrustScore) && Objects.equals(
                                                                                                                                                                                                                cumavgNotionalValueUsd, that.cumavgNotionalValueUsd) && Objects.equals(
                                                                                                                                                                                                                        cumavgNotionalValueUsdAction0, that.cumavgNotionalValueUsdAction0) && Objects.equals(
                                                                                                                                                                                                                                cumavgNotionalValueUsdAction1, that.cumavgNotionalValueUsdAction1) && Objects.equals(
                                                                                                                                                                                                                                        cumavgNotionalValueUsdEntry0, that.cumavgNotionalValueUsdEntry0) && Objects.equals(
                                                                                                                                                                                                                                                cumavgNotionalValueUsdEntry1, that.cumavgNotionalValueUsdEntry1) && Objects.equals(
                                                                                                                                                                                                                                                        cumavgPolicyScore, that.cumavgPolicyScore) && Objects.equals(cumavgProfitUsd, that.cumavgProfitUsd) && Objects.equals(
                                                                                                                                                                                                                                                                cumavgTimeDiffMinute, that.cumavgTimeDiffMinute) && Objects.equals(cumavgVolumeLots, that.cumavgVolumeLots) && Objects.equals(
                                                                                                                                                                                                                                                                        cumcountAction, that.cumcountAction) && Objects.equals(cumcountAction0, that.cumcountAction0) && Objects.equals(
                                                                                                                                                                                                                                                                                cumcountAction1, that.cumcountAction1) && Objects.equals(cumcountAction2, that.cumcountAction2) && Objects.equals(
                                                                                                                                                                                                                                                                                        cumcountAction3, that.cumcountAction3) && Objects.equals(cumcountAction4, that.cumcountAction4) && Objects.equals(
                                                                                                                                                                                                                                                                                                cumcountAction5, that.cumcountAction5) && Objects.equals(cumcountAction6, that.cumcountAction6) && Objects.equals(
                                                                                                                                                                                                                                                                                                        cumcountActionTrade, that.cumcountActionTrade) && Objects.equals(cumcountDepositCard, that.cumcountDepositCard) && Objects.equals(
                                                                                                                                                                                                                                                                                                                cumcountDepositCrypto, that.cumcountDepositCrypto) && Objects.equals(cumcountDepositP2p, that.cumcountDepositP2p) && Objects.equals(
                                                                                                                                                                                                                                                                                                                        cumcountDepositSuccess, that.cumcountDepositSuccess) && Objects.equals(cumcountDepositFail, that.cumcountDepositFail) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                cumcountDepositTransfer, that.cumcountDepositTransfer) && Objects.equals(cumcountEntry0, that.cumcountEntry0) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                        cumcountEntry1, that.cumcountEntry1) && Objects.equals(cumcountWithdrawalCard, that.cumcountWithdrawalCard) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                cumcountWithdrawalCrypto, that.cumcountWithdrawalCrypto) && Objects.equals(cumcountWithdrawalP2p, that.cumcountWithdrawalP2p) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                        cumcountWithdrawalSuccess, that.cumcountWithdrawalSuccess) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                cumcountWithdrawalFail, that.cumcountWithdrawalFail) && Objects.equals(cumcountWithdrawalTransfer, that.cumcountWithdrawalTransfer) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                        cumsumAmountUsdAction, that.cumsumAmountUsdAction) && Objects.equals(cumsumAmountUsdDeposit, that.cumsumAmountUsdDeposit) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                cumsumAmountUsdDepositCard, that.cumsumAmountUsdDepositCard) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                        cumsumAmountUsdDepositCrypto, that.cumsumAmountUsdDepositCrypto) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                cumsumAmountUsdDepositFail, that.cumsumAmountUsdDepositFail) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                        cumsumAmountUsdDepositSuccess, that.cumsumAmountUsdDepositSuccess) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                cumsumAmountUsdWithdrawal, that.cumsumAmountUsdWithdrawal) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                        cumsumAmountUsdWithdrawalCard, that.cumsumAmountUsdWithdrawalCard) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                cumsumAmountUsdWithdrawalCrypto, that.cumsumAmountUsdWithdrawalCrypto) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                        cumsumAmountUsdWithdrawalFail, that.cumsumAmountUsdWithdrawalFail) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                cumsumAmountUsdWithdrawalSuccess, that.cumsumAmountUsdWithdrawalSuccess) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                        cumsumCreditUsd, that.cumsumCreditUsd) && Objects.equals(cumsumLotsSymbol, that.cumsumLotsSymbol) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                cumsumNotionalValueUsdAction0, that.cumsumNotionalValueUsdAction0) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        cumsumNotionalValueUsdAction1, that.cumsumNotionalValueUsdAction1) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                cumsumNotionalValueUsdEntry0, that.cumsumNotionalValueUsdEntry0) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        cumsumNotionalValueUsdEntry1, that.cumsumNotionalValueUsdEntry1) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                cumsumProfitSymbol, that.cumsumProfitSymbol) && Objects.equals(cumsumProfitUsd, that.cumsumProfitUsd) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        cumsumToxicityUsd, that.cumsumToxicityUsd) && Objects.equals(cumsumVolumeLots, that.cumsumVolumeLots) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                deviceId, that.deviceId) && Objects.equals(digitalId, that.digitalId) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        digitalIdTrustScore, that.digitalIdTrustScore) && Objects.equals(entry, that.entry) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                id, that.id) && Objects.equals(name, that.name) && Objects.equals(notionalValueUsd, that.notionalValueUsd) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        notionalValueUsdAction0, that.notionalValueUsdAction0) && Objects.equals(notionalValueUsdAction1, that.notionalValueUsdAction1) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                notionalValueUsdEntry0, that.notionalValueUsdEntry0) && Objects.equals(notionalValueUsdEntry1, that.notionalValueUsdEntry1) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        os, that.os) && Objects.equals(paymentChannelId, that.paymentChannelId) && Objects.equals(paymentTypeId, that.paymentTypeId) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                policyScore, that.policyScore) && Objects.equals(profitSymbolToProfit, that.profitSymbolToProfit) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        profitToCredit, that.profitToCredit) && Objects.equals(profitToDeposit, that.profitToDeposit) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                profitToDepositCredit, that.profitToDepositCredit) && Objects.equals(profitUsd, that.profitUsd) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        sessionId, that.sessionId) && Objects.equals(shareSymbol, that.shareSymbol) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                statusId, that.statusId) && Objects.equals(symbolUnderlying, that.symbolUnderlying) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        time, that.time) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                timeDiff, that.timeDiff) && Objects.equals(timeDiffMinute, that.timeDiffMinute) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        timeDiffReg, that.timeDiffReg) && Objects.equals(timeDiffRegMinute, that.timeDiffRegMinute) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                toxicityUsd, that.toxicityUsd) && Objects.equals(ucid, that.ucid) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        volumeLots, that.volumeLots) && Objects.equals(webSessionId, that.webSessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountEmailResult, accountTelephoneResult, action, amountUsdAction, amountUsdDeposit, amountUsdDepositCard, amountUsdDepositCrypto, amountUsdDepositFail, amountUsdDepositSuccess, amountUsdWithdrawal, amountUsdWithdrawalCard, amountUsdWithdrawalCrypto, amountUsdWithdrawalFail, amountUsdWithdrawalSuccess, commentLabel, countConnection, countFraud, connectionScore, creditToDeposit, creditUsd, cumUniqueDeviceId, cumUniqueDigitalId, cumUniqueOs, cumUniqueSessionId, cumUniqueSymbolUnderlying, cumUniqueWebSessionId, cumavgAmountUsdDeposit, cumavgAmountUsdDepositCard, cumavgAmountUsdDepositCrypto, cumavgAmountUsdDepositFail, cumavgAmountUsdDepositSuccess, cumavgAmountUsdWithdrawal, cumavgAmountUsdWithdrawalCard, cumavgAmountUsdWithdrawalCrypto, cumavgAmountUsdWithdrawalFail, cumavgAmountUsdWithdrawalSuccess, cumavgCreditUsd, cumavgDigitalIdTrustScore, cumavgNotionalValueUsd, cumavgNotionalValueUsdAction0, cumavgNotionalValueUsdAction1, cumavgNotionalValueUsdEntry0, cumavgNotionalValueUsdEntry1, cumavgPolicyScore, cumavgProfitUsd, cumavgTimeDiffMinute, cumavgVolumeLots, cumcountAction, cumcountAction0, cumcountAction1, cumcountAction2, cumcountAction3, cumcountAction4, cumcountAction5, cumcountAction6, cumcountActionTrade, cumcountDepositCard, cumcountDepositCrypto, cumcountDepositP2p, cumcountDepositSuccess, cumcountDepositFail, cumcountDepositTransfer, cumcountEntry0, cumcountEntry1, cumcountWithdrawalCard, cumcountWithdrawalCrypto, cumcountWithdrawalP2p, cumcountWithdrawalSuccess, cumcountWithdrawalFail, cumcountWithdrawalTransfer, cumsumAmountUsdAction, cumsumAmountUsdDeposit, cumsumAmountUsdDepositCard, cumsumAmountUsdDepositCrypto, cumsumAmountUsdDepositFail, cumsumAmountUsdDepositSuccess, cumsumAmountUsdWithdrawal, cumsumAmountUsdWithdrawalCard, cumsumAmountUsdWithdrawalCrypto, cumsumAmountUsdWithdrawalFail, cumsumAmountUsdWithdrawalSuccess, cumsumCreditUsd, cumsumLotsSymbol, cumsumNotionalValueUsdAction0, cumsumNotionalValueUsdAction1, cumsumNotionalValueUsdEntry0, cumsumNotionalValueUsdEntry1, cumsumProfitSymbol, cumsumProfitUsd, cumsumToxicityUsd, cumsumVolumeLots, deviceId, digitalId, digitalIdTrustScore, entry, id, name, notionalValueUsd, notionalValueUsdAction0, notionalValueUsdAction1, notionalValueUsdEntry0, notionalValueUsdEntry1, os, paymentChannelId, paymentTypeId, policyScore, profitSymbolToProfit, profitToCredit, profitToDeposit, profitToDepositCredit, profitUsd, sessionId, shareSymbol, statusId, symbolUnderlying, time, lastUpdated, timeDiff, timeDiffMinute, timeDiffReg, timeDiffRegMinute, toxicityUsd, ucid, volumeLots, webSessionId);
    }

    @Override
    public String toString() {
        return "MirrorDataWithStat{" + "accountEmailResult='" + accountEmailResult + '\'' + ", accountTelephoneResult='" + accountTelephoneResult + '\'' + ", action='" + action + '\'' + ", amountUsdAction=" + amountUsdAction + ", amountUsdDeposit=" + amountUsdDeposit + ", amountUsdDepositCard=" + amountUsdDepositCard + ", amountUsdDepositCrypto=" + amountUsdDepositCrypto + ", amountUsdDepositFail=" + amountUsdDepositFail + ", amountUsdDepositSuccess=" + amountUsdDepositSuccess + ", amountUsdWithdrawal=" + amountUsdWithdrawal + ", amountUsdWithdrawalCard=" + amountUsdWithdrawalCard + ", amountUsdWithdrawalCrypto=" + amountUsdWithdrawalCrypto + ", amountUsdWithdrawalFail=" + amountUsdWithdrawalFail + ", amountUsdWithdrawalSuccess=" + amountUsdWithdrawalSuccess + ", commentLabel='" + commentLabel + '\'' + ", countConnection=" + countConnection + ", countFraud=" + countFraud + ", connectionScore=" + connectionScore + ", creditToDeposit=" + creditToDeposit + ", creditUsd=" + creditUsd + ", cumUniqueDeviceId=" + cumUniqueDeviceId + ", cumUniqueDigitalId=" + cumUniqueDigitalId + ", cumUniqueOs=" + cumUniqueOs + ", cumUniqueSessionId=" + cumUniqueSessionId + ", cumUniqueSymbolUnderlying=" + cumUniqueSymbolUnderlying + ", cumUniqueWebSessionId=" + cumUniqueWebSessionId + ", cumavgAmountUsdDeposit=" + cumavgAmountUsdDeposit + ", cumavgAmountUsdDepositCard=" + cumavgAmountUsdDepositCard + ", cumavgAmountUsdDepositCrypto=" + cumavgAmountUsdDepositCrypto + ", cumavgAmountUsdDepositFail=" + cumavgAmountUsdDepositFail + ", cumavgAmountUsdDepositSuccess=" + cumavgAmountUsdDepositSuccess + ", cumavgAmountUsdWithdrawal=" + cumavgAmountUsdWithdrawal + ", cumavgAmountUsdWithdrawalCard=" + cumavgAmountUsdWithdrawalCard + ", cumavgAmountUsdWithdrawalCrypto=" + cumavgAmountUsdWithdrawalCrypto + ", cumavgAmountUsdWithdrawalFail=" + cumavgAmountUsdWithdrawalFail + ", cumavgAmountUsdWithdrawalSuccess=" + cumavgAmountUsdWithdrawalSuccess + ", cumavgCreditUsd=" + cumavgCreditUsd + ", cumavgDigitalIdTrustScore=" + cumavgDigitalIdTrustScore + ", cumavgNotionalValueUsd=" + cumavgNotionalValueUsd + ", cumavgNotionalValueUsdAction0=" + cumavgNotionalValueUsdAction0 + ", cumavgNotionalValueUsdAction1=" + cumavgNotionalValueUsdAction1 + ", cumavgNotionalValueUsdEntry0=" + cumavgNotionalValueUsdEntry0 + ", cumavgNotionalValueUsdEntry1=" + cumavgNotionalValueUsdEntry1 + ", cumavgPolicyScore=" + cumavgPolicyScore + ", cumavgProfitUsd=" + cumavgProfitUsd + ", cumavgTimeDiffMinute=" + cumavgTimeDiffMinute + ", cumavgVolumeLots=" + cumavgVolumeLots + ", cumcountAction=" + cumcountAction + ", cumcountAction0=" + cumcountAction0 + ", cumcountAction1=" + cumcountAction1 + ", cumcountAction2=" + cumcountAction2 + ", cumcountAction3=" + cumcountAction3 + ", cumcountAction4=" + cumcountAction4 + ", cumcountAction5=" + cumcountAction5 + ", cumcountAction6=" + cumcountAction6 + ", cumcountActionTrade=" + cumcountActionTrade + ", cumcountDepositCard=" + cumcountDepositCard + ", cumcountDepositCrypto=" + cumcountDepositCrypto + ", cumcountDepositP2p=" + cumcountDepositP2p + ", cumcountDepositSuccess=" + cumcountDepositSuccess + ", cumcountDepositFail=" + cumcountDepositFail + ", cumcountDepositTransfer=" + cumcountDepositTransfer + ", cumcountEntry0=" + cumcountEntry0 + ", cumcountEntry1=" + cumcountEntry1 + ", cumcountWithdrawalCard=" + cumcountWithdrawalCard + ", cumcountWithdrawalCrypto=" + cumcountWithdrawalCrypto + ", cumcountWithdrawalP2p=" + cumcountWithdrawalP2p + ", cumcountWithdrawalSuccess=" + cumcountWithdrawalSuccess + ", cumcountWithdrawalFail=" + cumcountWithdrawalFail + ", cumcountWithdrawalTransfer=" + cumcountWithdrawalTransfer + ", cumsumAmountUsdAction=" + cumsumAmountUsdAction + ", cumsumAmountUsdDeposit=" + cumsumAmountUsdDeposit + ", cumsumAmountUsdDepositCard=" + cumsumAmountUsdDepositCard + ", cumsumAmountUsdDepositCrypto=" + cumsumAmountUsdDepositCrypto + ", cumsumAmountUsdDepositFail=" + cumsumAmountUsdDepositFail + ", cumsumAmountUsdDepositSuccess=" + cumsumAmountUsdDepositSuccess + ", cumsumAmountUsdWithdrawal=" + cumsumAmountUsdWithdrawal + ", cumsumAmountUsdWithdrawalCard=" + cumsumAmountUsdWithdrawalCard + ", cumsumAmountUsdWithdrawalCrypto=" + cumsumAmountUsdWithdrawalCrypto + ", cumsumAmountUsdWithdrawalFail=" + cumsumAmountUsdWithdrawalFail + ", cumsumAmountUsdWithdrawalSuccess=" + cumsumAmountUsdWithdrawalSuccess + ", cumsumCreditUsd=" + cumsumCreditUsd + ", cumsumLotsSymbol=" + cumsumLotsSymbol + ", cumsumNotionalValueUsdAction0=" + cumsumNotionalValueUsdAction0 + ", cumsumNotionalValueUsdAction1=" + cumsumNotionalValueUsdAction1 + ", cumsumNotionalValueUsdEntry0=" + cumsumNotionalValueUsdEntry0 + ", cumsumNotionalValueUsdEntry1=" + cumsumNotionalValueUsdEntry1 + ", cumsumProfitSymbol=" + cumsumProfitSymbol + ", cumsumProfitUsd=" + cumsumProfitUsd + ", cumsumToxicityUsd=" + cumsumToxicityUsd + ", cumsumVolumeLots=" + cumsumVolumeLots + ", deviceId='" + deviceId + '\'' + ", digitalId='" + digitalId + '\'' + ", digitalIdTrustScore=" + digitalIdTrustScore + ", entry='" + entry + '\'' + ", id='" + id + '\'' + ", name='" + name + '\'' + ", notionalValueUsd=" + notionalValueUsd + ", notionalValueUsdAction0=" + notionalValueUsdAction0 + ", notionalValueUsdAction1=" + notionalValueUsdAction1 + ", notionalValueUsdEntry0=" + notionalValueUsdEntry0 + ", notionalValueUsdEntry1=" + notionalValueUsdEntry1 + ", os='" + os + '\'' + ", paymentChannelId='" + paymentChannelId + '\'' + ", paymentTypeId='" + paymentTypeId + '\'' + ", policyScore=" + policyScore + ", profitSymbolToProfit=" + profitSymbolToProfit + ", profitToCredit=" + profitToCredit + ", profitToDeposit=" + profitToDeposit + ", profitToDepositCredit=" + profitToDepositCredit + ", profitUsd=" + profitUsd + ", sessionId='" + sessionId + '\'' + ", shareSymbol=" + shareSymbol + ", statusId='" + statusId + '\'' + ", symbolUnderlying='" + symbolUnderlying + '\'' + ", time=" + time + ", lastUpdated=" + lastUpdated + ", timeDiff=" + timeDiff + ", timeDiffMinute=" + timeDiffMinute + ", timeDiffReg=" + timeDiffReg + ", timeDiffRegMinute=" + timeDiffRegMinute + ", toxicityUsd=" + toxicityUsd + ", ucid='" + ucid + '\'' + ", volumeLots=" + volumeLots + ", webSessionId='" + webSessionId + '\'' + '}';
    }

    public String getAccountEmailResult() {
        return accountEmailResult;
    }

    public void setAccountEmailResult(String accountEmailResult) {
        this.accountEmailResult = accountEmailResult;
    }

    public String getAccountTelephoneResult() {
        return accountTelephoneResult;
    }

    public void setAccountTelephoneResult(String accountTelephoneResult) {
        this.accountTelephoneResult = accountTelephoneResult;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Double getAmountUsdAction() {
        return amountUsdAction;
    }

    public void setAmountUsdAction(Double amountUsdAction) {
        this.amountUsdAction = amountUsdAction;
    }

    public Double getAmountUsdDeposit() {
        return amountUsdDeposit;
    }

    public void setAmountUsdDeposit(Double amountUsdDeposit) {
        this.amountUsdDeposit = amountUsdDeposit;
    }

    public Double getAmountUsdDepositCard() {
        return amountUsdDepositCard;
    }

    public void setAmountUsdDepositCard(Double amountUsdDepositCard) {
        this.amountUsdDepositCard = amountUsdDepositCard;
    }

    public Double getAmountUsdDepositCrypto() {
        return amountUsdDepositCrypto;
    }

    public void setAmountUsdDepositCrypto(Double amountUsdDepositCrypto) {
        this.amountUsdDepositCrypto = amountUsdDepositCrypto;
    }

    public Double getAmountUsdDepositFail() {
        return amountUsdDepositFail;
    }

    public void setAmountUsdDepositFail(Double amountUsdDepositFail) {
        this.amountUsdDepositFail = amountUsdDepositFail;
    }

    public Double getAmountUsdDepositSuccess() {
        return amountUsdDepositSuccess;
    }

    public void setAmountUsdDepositSuccess(Double amountUsdDepositSuccess) {
        this.amountUsdDepositSuccess = amountUsdDepositSuccess;
    }

    public Double getAmountUsdWithdrawal() {
        return amountUsdWithdrawal;
    }

    public void setAmountUsdWithdrawal(Double amountUsdWithdrawal) {
        this.amountUsdWithdrawal = amountUsdWithdrawal;
    }

    public Double getAmountUsdWithdrawalCard() {
        return amountUsdWithdrawalCard;
    }

    public void setAmountUsdWithdrawalCard(Double amountUsdWithdrawalCard) {
        this.amountUsdWithdrawalCard = amountUsdWithdrawalCard;
    }

    public Double getAmountUsdWithdrawalCrypto() {
        return amountUsdWithdrawalCrypto;
    }

    public void setAmountUsdWithdrawalCrypto(Double amountUsdWithdrawalCrypto) {
        this.amountUsdWithdrawalCrypto = amountUsdWithdrawalCrypto;
    }

    public Double getAmountUsdWithdrawalFail() {
        return amountUsdWithdrawalFail;
    }

    public void setAmountUsdWithdrawalFail(Double amountUsdWithdrawalFail) {
        this.amountUsdWithdrawalFail = amountUsdWithdrawalFail;
    }

    public Double getAmountUsdWithdrawalSuccess() {
        return amountUsdWithdrawalSuccess;
    }

    public void setAmountUsdWithdrawalSuccess(Double amountUsdWithdrawalSuccess) {
        this.amountUsdWithdrawalSuccess = amountUsdWithdrawalSuccess;
    }

    public String getCommentLabel() {
        return commentLabel;
    }

    public void setCommentLabel(String commentLabel) {
        this.commentLabel = commentLabel;
    }

    public Integer getCountConnection() {
        return countConnection;
    }

    public void setCountConnection(Integer countConnection) {
        this.countConnection = countConnection;
    }

    public Integer getCountFraud() {
        return countFraud;
    }

    public void setCountFraud(Integer countFraud) {
        this.countFraud = countFraud;
    }

    public Double getConnectionScore() {
        return connectionScore;
    }

    public void setConnectionScore(Double connectionScore) {
        this.connectionScore = connectionScore;
    }

    public Double getCreditToDeposit() {
        return creditToDeposit;
    }

    public void setCreditToDeposit(Double creditToDeposit) {
        this.creditToDeposit = creditToDeposit;
    }

    public Double getCreditUsd() {
        return creditUsd;
    }

    public void setCreditUsd(Double creditUsd) {
        this.creditUsd = creditUsd;
    }

    public Integer getCumUniqueDeviceId() {
        return cumUniqueDeviceId;
    }

    public void setCumUniqueDeviceId(Integer cumUniqueDeviceId) {
        this.cumUniqueDeviceId = cumUniqueDeviceId;
    }

    public Integer getCumUniqueDigitalId() {
        return cumUniqueDigitalId;
    }

    public void setCumUniqueDigitalId(Integer cumUniqueDigitalId) {
        this.cumUniqueDigitalId = cumUniqueDigitalId;
    }

    public Integer getCumUniqueOs() {
        return cumUniqueOs;
    }

    public void setCumUniqueOs(Integer cumUniqueOs) {
        this.cumUniqueOs = cumUniqueOs;
    }

    public Integer getCumUniqueSessionId() {
        return cumUniqueSessionId;
    }

    public void setCumUniqueSessionId(Integer cumUniqueSessionId) {
        this.cumUniqueSessionId = cumUniqueSessionId;
    }

    public Integer getCumUniqueSymbolUnderlying() {
        return cumUniqueSymbolUnderlying;
    }

    public void setCumUniqueSymbolUnderlying(Integer cumUniqueSymbolUnderlying) {
        this.cumUniqueSymbolUnderlying = cumUniqueSymbolUnderlying;
    }

    public Integer getCumUniqueWebSessionId() {
        return cumUniqueWebSessionId;
    }

    public void setCumUniqueWebSessionId(Integer cumUniqueWebSessionId) {
        this.cumUniqueWebSessionId = cumUniqueWebSessionId;
    }

    public Double getCumavgAmountUsdDeposit() {
        return cumavgAmountUsdDeposit;
    }

    public void setCumavgAmountUsdDeposit(Double cumavgAmountUsdDeposit) {
        this.cumavgAmountUsdDeposit = cumavgAmountUsdDeposit;
    }

    public Double getCumavgAmountUsdDepositCard() {
        return cumavgAmountUsdDepositCard;
    }

    public void setCumavgAmountUsdDepositCard(Double cumavgAmountUsdDepositCard) {
        this.cumavgAmountUsdDepositCard = cumavgAmountUsdDepositCard;
    }

    public Double getCumavgAmountUsdDepositCrypto() {
        return cumavgAmountUsdDepositCrypto;
    }

    public void setCumavgAmountUsdDepositCrypto(Double cumavgAmountUsdDepositCrypto) {
        this.cumavgAmountUsdDepositCrypto = cumavgAmountUsdDepositCrypto;
    }

    public Double getCumavgAmountUsdDepositFail() {
        return cumavgAmountUsdDepositFail;
    }

    public void setCumavgAmountUsdDepositFail(Double cumavgAmountUsdDepositFail) {
        this.cumavgAmountUsdDepositFail = cumavgAmountUsdDepositFail;
    }

    public Double getCumavgAmountUsdDepositSuccess() {
        return cumavgAmountUsdDepositSuccess;
    }

    public void setCumavgAmountUsdDepositSuccess(Double cumavgAmountUsdDepositSuccess) {
        this.cumavgAmountUsdDepositSuccess = cumavgAmountUsdDepositSuccess;
    }

    public Double getCumavgAmountUsdWithdrawal() {
        return cumavgAmountUsdWithdrawal;
    }

    public void setCumavgAmountUsdWithdrawal(Double cumavgAmountUsdWithdrawal) {
        this.cumavgAmountUsdWithdrawal = cumavgAmountUsdWithdrawal;
    }

    public Double getCumavgAmountUsdWithdrawalCard() {
        return cumavgAmountUsdWithdrawalCard;
    }

    public void setCumavgAmountUsdWithdrawalCard(Double cumavgAmountUsdWithdrawalCard) {
        this.cumavgAmountUsdWithdrawalCard = cumavgAmountUsdWithdrawalCard;
    }

    public Double getCumavgAmountUsdWithdrawalCrypto() {
        return cumavgAmountUsdWithdrawalCrypto;
    }

    public void setCumavgAmountUsdWithdrawalCrypto(Double cumavgAmountUsdWithdrawalCrypto) {
        this.cumavgAmountUsdWithdrawalCrypto = cumavgAmountUsdWithdrawalCrypto;
    }

    public Double getCumavgAmountUsdWithdrawalFail() {
        return cumavgAmountUsdWithdrawalFail;
    }

    public void setCumavgAmountUsdWithdrawalFail(Double cumavgAmountUsdWithdrawalFail) {
        this.cumavgAmountUsdWithdrawalFail = cumavgAmountUsdWithdrawalFail;
    }

    public Double getCumavgAmountUsdWithdrawalSuccess() {
        return cumavgAmountUsdWithdrawalSuccess;
    }

    public void setCumavgAmountUsdWithdrawalSuccess(Double cumavgAmountUsdWithdrawalSuccess) {
        this.cumavgAmountUsdWithdrawalSuccess = cumavgAmountUsdWithdrawalSuccess;
    }

    public Double getCumavgCreditUsd() {
        return cumavgCreditUsd;
    }

    public void setCumavgCreditUsd(Double cumavgCreditUsd) {
        this.cumavgCreditUsd = cumavgCreditUsd;
    }

    public Double getCumavgDigitalIdTrustScore() {
        return cumavgDigitalIdTrustScore;
    }

    public void setCumavgDigitalIdTrustScore(Double cumavgDigitalIdTrustScore) {
        this.cumavgDigitalIdTrustScore = cumavgDigitalIdTrustScore;
    }

    public Double getCumavgNotionalValueUsd() {
        return cumavgNotionalValueUsd;
    }

    public void setCumavgNotionalValueUsd(Double cumavgNotionalValueUsd) {
        this.cumavgNotionalValueUsd = cumavgNotionalValueUsd;
    }

    public Double getCumavgNotionalValueUsdAction0() {
        return cumavgNotionalValueUsdAction0;
    }

    public void setCumavgNotionalValueUsdAction0(Double cumavgNotionalValueUsdAction0) {
        this.cumavgNotionalValueUsdAction0 = cumavgNotionalValueUsdAction0;
    }

    public Double getCumavgNotionalValueUsdAction1() {
        return cumavgNotionalValueUsdAction1;
    }

    public void setCumavgNotionalValueUsdAction1(Double cumavgNotionalValueUsdAction1) {
        this.cumavgNotionalValueUsdAction1 = cumavgNotionalValueUsdAction1;
    }

    public Double getCumavgNotionalValueUsdEntry0() {
        return cumavgNotionalValueUsdEntry0;
    }

    public void setCumavgNotionalValueUsdEntry0(Double cumavgNotionalValueUsdEntry0) {
        this.cumavgNotionalValueUsdEntry0 = cumavgNotionalValueUsdEntry0;
    }

    public Double getCumavgNotionalValueUsdEntry1() {
        return cumavgNotionalValueUsdEntry1;
    }

    public void setCumavgNotionalValueUsdEntry1(Double cumavgNotionalValueUsdEntry1) {
        this.cumavgNotionalValueUsdEntry1 = cumavgNotionalValueUsdEntry1;
    }

    public Double getCumavgPolicyScore() {
        return cumavgPolicyScore;
    }

    public void setCumavgPolicyScore(Double cumavgPolicyScore) {
        this.cumavgPolicyScore = cumavgPolicyScore;
    }

    public Double getCumavgProfitUsd() {
        return cumavgProfitUsd;
    }

    public void setCumavgProfitUsd(Double cumavgProfitUsd) {
        this.cumavgProfitUsd = cumavgProfitUsd;
    }

    public Double getCumavgTimeDiffMinute() {
        return cumavgTimeDiffMinute;
    }

    public void setCumavgTimeDiffMinute(Double cumavgTimeDiffMinute) {
        this.cumavgTimeDiffMinute = cumavgTimeDiffMinute;
    }

    public Double getCumavgVolumeLots() {
        return cumavgVolumeLots;
    }

    public void setCumavgVolumeLots(Double cumavgVolumeLots) {
        this.cumavgVolumeLots = cumavgVolumeLots;
    }

    public Integer getCumcountAction() {
        return cumcountAction;
    }

    public void setCumcountAction(Integer cumcountAction) {
        this.cumcountAction = cumcountAction;
    }

    public Integer getCumcountAction0() {
        return cumcountAction0;
    }

    public void setCumcountAction0(Integer cumcountAction0) {
        this.cumcountAction0 = cumcountAction0;
    }

    public Integer getCumcountAction1() {
        return cumcountAction1;
    }

    public void setCumcountAction1(Integer cumcountAction1) {
        this.cumcountAction1 = cumcountAction1;
    }

    public Integer getCumcountAction2() {
        return cumcountAction2;
    }

    public void setCumcountAction2(Integer cumcountAction2) {
        this.cumcountAction2 = cumcountAction2;
    }

    public Integer getCumcountAction3() {
        return cumcountAction3;
    }

    public void setCumcountAction3(Integer cumcountAction3) {
        this.cumcountAction3 = cumcountAction3;
    }

    public Integer getCumcountAction4() {
        return cumcountAction4;
    }

    public void setCumcountAction4(Integer cumcountAction4) {
        this.cumcountAction4 = cumcountAction4;
    }

    public Integer getCumcountAction5() {
        return cumcountAction5;
    }

    public void setCumcountAction5(Integer cumcountAction5) {
        this.cumcountAction5 = cumcountAction5;
    }

    public Integer getCumcountAction6() {
        return cumcountAction6;
    }

    public void setCumcountAction6(Integer cumcountAction6) {
        this.cumcountAction6 = cumcountAction6;
    }

    public Integer getCumcountActionTrade() {
        return cumcountActionTrade;
    }

    public void setCumcountActionTrade(Integer cumcountActionTrade) {
        this.cumcountActionTrade = cumcountActionTrade;
    }

    public Integer getCumcountDepositCard() {
        return cumcountDepositCard;
    }

    public void setCumcountDepositCard(Integer cumcountDepositCard) {
        this.cumcountDepositCard = cumcountDepositCard;
    }

    public Integer getCumcountDepositCrypto() {
        return cumcountDepositCrypto;
    }

    public void setCumcountDepositCrypto(Integer cumcountDepositCrypto) {
        this.cumcountDepositCrypto = cumcountDepositCrypto;
    }

    public Integer getCumcountDepositP2p() {
        return cumcountDepositP2p;
    }

    public void setCumcountDepositP2p(Integer cumcountDepositP2p) {
        this.cumcountDepositP2p = cumcountDepositP2p;
    }

    public Integer getCumcountDepositSuccess() {
        return cumcountDepositSuccess;
    }

    public void setCumcountDepositSuccess(Integer cumcountDepositSuccess) {
        this.cumcountDepositSuccess = cumcountDepositSuccess;
    }

    public Integer getCumcountDepositFail() {
        return cumcountDepositFail;
    }

    public void setCumcountDepositFail(Integer cumcountDepositFail) {
        this.cumcountDepositFail = cumcountDepositFail;
    }

    public Integer getCumcountDepositTransfer() {
        return cumcountDepositTransfer;
    }

    public void setCumcountDepositTransfer(Integer cumcountDepositTransfer) {
        this.cumcountDepositTransfer = cumcountDepositTransfer;
    }

    public Integer getCumcountEntry0() {
        return cumcountEntry0;
    }

    public void setCumcountEntry0(Integer cumcountEntry0) {
        this.cumcountEntry0 = cumcountEntry0;
    }

    public Integer getCumcountEntry1() {
        return cumcountEntry1;
    }

    public void setCumcountEntry1(Integer cumcountEntry1) {
        this.cumcountEntry1 = cumcountEntry1;
    }

    public Integer getCumcountWithdrawalCard() {
        return cumcountWithdrawalCard;
    }

    public void setCumcountWithdrawalCard(Integer cumcountWithdrawalCard) {
        this.cumcountWithdrawalCard = cumcountWithdrawalCard;
    }

    public Integer getCumcountWithdrawalCrypto() {
        return cumcountWithdrawalCrypto;
    }

    public void setCumcountWithdrawalCrypto(Integer cumcountWithdrawalCrypto) {
        this.cumcountWithdrawalCrypto = cumcountWithdrawalCrypto;
    }

    public Integer getCumcountWithdrawalP2p() {
        return cumcountWithdrawalP2p;
    }

    public void setCumcountWithdrawalP2p(Integer cumcountWithdrawalP2p) {
        this.cumcountWithdrawalP2p = cumcountWithdrawalP2p;
    }

    public Integer getCumcountWithdrawalSuccess() {
        return cumcountWithdrawalSuccess;
    }

    public void setCumcountWithdrawalSuccess(Integer cumcountWithdrawalSuccess) {
        this.cumcountWithdrawalSuccess = cumcountWithdrawalSuccess;
    }

    public Integer getCumcountWithdrawalFail() {
        return cumcountWithdrawalFail;
    }

    public void setCumcountWithdrawalFail(Integer cumcountWithdrawalFail) {
        this.cumcountWithdrawalFail = cumcountWithdrawalFail;
    }

    public Integer getCumcountWithdrawalTransfer() {
        return cumcountWithdrawalTransfer;
    }

    public void setCumcountWithdrawalTransfer(Integer cumcountWithdrawalTransfer) {
        this.cumcountWithdrawalTransfer = cumcountWithdrawalTransfer;
    }

    public Double getCumsumAmountUsdAction() {
        return cumsumAmountUsdAction;
    }

    public void setCumsumAmountUsdAction(Double cumsumAmountUsdAction) {
        this.cumsumAmountUsdAction = cumsumAmountUsdAction;
    }

    public Double getCumsumAmountUsdDeposit() {
        return cumsumAmountUsdDeposit;
    }

    public void setCumsumAmountUsdDeposit(Double cumsumAmountUsdDeposit) {
        this.cumsumAmountUsdDeposit = cumsumAmountUsdDeposit;
    }

    public Double getCumsumAmountUsdDepositCard() {
        return cumsumAmountUsdDepositCard;
    }

    public void setCumsumAmountUsdDepositCard(Double cumsumAmountUsdDepositCard) {
        this.cumsumAmountUsdDepositCard = cumsumAmountUsdDepositCard;
    }

    public Double getCumsumAmountUsdDepositCrypto() {
        return cumsumAmountUsdDepositCrypto;
    }

    public void setCumsumAmountUsdDepositCrypto(Double cumsumAmountUsdDepositCrypto) {
        this.cumsumAmountUsdDepositCrypto = cumsumAmountUsdDepositCrypto;
    }

    public Double getCumsumAmountUsdDepositFail() {
        return cumsumAmountUsdDepositFail;
    }

    public void setCumsumAmountUsdDepositFail(Double cumsumAmountUsdDepositFail) {
        this.cumsumAmountUsdDepositFail = cumsumAmountUsdDepositFail;
    }

    public Double getCumsumAmountUsdDepositSuccess() {
        return cumsumAmountUsdDepositSuccess;
    }

    public void setCumsumAmountUsdDepositSuccess(Double cumsumAmountUsdDepositSuccess) {
        this.cumsumAmountUsdDepositSuccess = cumsumAmountUsdDepositSuccess;
    }

    public Double getCumsumAmountUsdWithdrawal() {
        return cumsumAmountUsdWithdrawal;
    }

    public void setCumsumAmountUsdWithdrawal(Double cumsumAmountUsdWithdrawal) {
        this.cumsumAmountUsdWithdrawal = cumsumAmountUsdWithdrawal;
    }

    public Double getCumsumAmountUsdWithdrawalCard() {
        return cumsumAmountUsdWithdrawalCard;
    }

    public void setCumsumAmountUsdWithdrawalCard(Double cumsumAmountUsdWithdrawalCard) {
        this.cumsumAmountUsdWithdrawalCard = cumsumAmountUsdWithdrawalCard;
    }

    public Double getCumsumAmountUsdWithdrawalCrypto() {
        return cumsumAmountUsdWithdrawalCrypto;
    }

    public void setCumsumAmountUsdWithdrawalCrypto(Double cumsumAmountUsdWithdrawalCrypto) {
        this.cumsumAmountUsdWithdrawalCrypto = cumsumAmountUsdWithdrawalCrypto;
    }

    public Double getCumsumAmountUsdWithdrawalFail() {
        return cumsumAmountUsdWithdrawalFail;
    }

    public void setCumsumAmountUsdWithdrawalFail(Double cumsumAmountUsdWithdrawalFail) {
        this.cumsumAmountUsdWithdrawalFail = cumsumAmountUsdWithdrawalFail;
    }

    public Double getCumsumAmountUsdWithdrawalSuccess() {
        return cumsumAmountUsdWithdrawalSuccess;
    }

    public void setCumsumAmountUsdWithdrawalSuccess(Double cumsumAmountUsdWithdrawalSuccess) {
        this.cumsumAmountUsdWithdrawalSuccess = cumsumAmountUsdWithdrawalSuccess;
    }

    public Double getCumsumCreditUsd() {
        return cumsumCreditUsd;
    }

    public void setCumsumCreditUsd(Double cumsumCreditUsd) {
        this.cumsumCreditUsd = cumsumCreditUsd;
    }

    public Double getCumsumLotsSymbol() {
        return cumsumLotsSymbol;
    }

    public void setCumsumLotsSymbol(Double cumsumLotsSymbol) {
        this.cumsumLotsSymbol = cumsumLotsSymbol;
    }

    public Double getCumsumNotionalValueUsdAction0() {
        return cumsumNotionalValueUsdAction0;
    }

    public void setCumsumNotionalValueUsdAction0(Double cumsumNotionalValueUsdAction0) {
        this.cumsumNotionalValueUsdAction0 = cumsumNotionalValueUsdAction0;
    }

    public Double getCumsumNotionalValueUsdAction1() {
        return cumsumNotionalValueUsdAction1;
    }

    public void setCumsumNotionalValueUsdAction1(Double cumsumNotionalValueUsdAction1) {
        this.cumsumNotionalValueUsdAction1 = cumsumNotionalValueUsdAction1;
    }

    public Double getCumsumNotionalValueUsdEntry0() {
        return cumsumNotionalValueUsdEntry0;
    }

    public void setCumsumNotionalValueUsdEntry0(Double cumsumNotionalValueUsdEntry0) {
        this.cumsumNotionalValueUsdEntry0 = cumsumNotionalValueUsdEntry0;
    }

    public Double getCumsumNotionalValueUsdEntry1() {
        return cumsumNotionalValueUsdEntry1;
    }

    public void setCumsumNotionalValueUsdEntry1(Double cumsumNotionalValueUsdEntry1) {
        this.cumsumNotionalValueUsdEntry1 = cumsumNotionalValueUsdEntry1;
    }

    public Double getCumsumProfitSymbol() {
        return cumsumProfitSymbol;
    }

    public void setCumsumProfitSymbol(Double cumsumProfitSymbol) {
        this.cumsumProfitSymbol = cumsumProfitSymbol;
    }

    public Double getCumsumProfitUsd() {
        return cumsumProfitUsd;
    }

    public void setCumsumProfitUsd(Double cumsumProfitUsd) {
        this.cumsumProfitUsd = cumsumProfitUsd;
    }

    public Double getCumsumToxicityUsd() {
        return cumsumToxicityUsd;
    }

    public void setCumsumToxicityUsd(Double cumsumToxicityUsd) {
        this.cumsumToxicityUsd = cumsumToxicityUsd;
    }

    public Double getCumsumVolumeLots() {
        return cumsumVolumeLots;
    }

    public void setCumsumVolumeLots(Double cumsumVolumeLots) {
        this.cumsumVolumeLots = cumsumVolumeLots;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDigitalId() {
        return digitalId;
    }

    public void setDigitalId(String digitalId) {
        this.digitalId = digitalId;
    }

    public Double getDigitalIdTrustScore() {
        return digitalIdTrustScore;
    }

    public void setDigitalIdTrustScore(Double digitalIdTrustScore) {
        this.digitalIdTrustScore = digitalIdTrustScore;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getNotionalValueUsd() {
        return notionalValueUsd;
    }

    public void setNotionalValueUsd(Double notionalValueUsd) {
        this.notionalValueUsd = notionalValueUsd;
    }

    public Double getNotionalValueUsdAction0() {
        return notionalValueUsdAction0;
    }

    public void setNotionalValueUsdAction0(Double notionalValueUsdAction0) {
        this.notionalValueUsdAction0 = notionalValueUsdAction0;
    }

    public Double getNotionalValueUsdAction1() {
        return notionalValueUsdAction1;
    }

    public void setNotionalValueUsdAction1(Double notionalValueUsdAction1) {
        this.notionalValueUsdAction1 = notionalValueUsdAction1;
    }

    public Double getNotionalValueUsdEntry0() {
        return notionalValueUsdEntry0;
    }

    public void setNotionalValueUsdEntry0(Double notionalValueUsdEntry0) {
        this.notionalValueUsdEntry0 = notionalValueUsdEntry0;
    }

    public Double getNotionalValueUsdEntry1() {
        return notionalValueUsdEntry1;
    }

    public void setNotionalValueUsdEntry1(Double notionalValueUsdEntry1) {
        this.notionalValueUsdEntry1 = notionalValueUsdEntry1;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getPaymentChannelId() {
        return paymentChannelId;
    }

    public void setPaymentChannelId(String paymentChannelId) {
        this.paymentChannelId = paymentChannelId;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public Double getPolicyScore() {
        return policyScore;
    }

    public void setPolicyScore(Double policyScore) {
        this.policyScore = policyScore;
    }

    public Double getProfitSymbolToProfit() {
        return profitSymbolToProfit;
    }

    public void setProfitSymbolToProfit(Double profitSymbolToProfit) {
        this.profitSymbolToProfit = profitSymbolToProfit;
    }

    public Double getProfitToCredit() {
        return profitToCredit;
    }

    public void setProfitToCredit(Double profitToCredit) {
        this.profitToCredit = profitToCredit;
    }

    public Double getProfitToDeposit() {
        return profitToDeposit;
    }

    public void setProfitToDeposit(Double profitToDeposit) {
        this.profitToDeposit = profitToDeposit;
    }

    public Double getProfitToDepositCredit() {
        return profitToDepositCredit;
    }

    public void setProfitToDepositCredit(Double profitToDepositCredit) {
        this.profitToDepositCredit = profitToDepositCredit;
    }

    public Double getProfitUsd() {
        return profitUsd;
    }

    public void setProfitUsd(Double profitUsd) {
        this.profitUsd = profitUsd;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Double getShareSymbol() {
        return shareSymbol;
    }

    public void setShareSymbol(Double shareSymbol) {
        this.shareSymbol = shareSymbol;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getSymbolUnderlying() {
        return symbolUnderlying;
    }

    public void setSymbolUnderlying(String symbolUnderlying) {
        this.symbolUnderlying = symbolUnderlying;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Double getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(Double timeDiff) {
        this.timeDiff = timeDiff;
    }

    public Double getTimeDiffMinute() {
        return timeDiffMinute;
    }

    public void setTimeDiffMinute(Double timeDiffMinute) {
        this.timeDiffMinute = timeDiffMinute;
    }

    public Double getTimeDiffReg() {
        return timeDiffReg;
    }

    public void setTimeDiffReg(Double timeDiffReg) {
        this.timeDiffReg = timeDiffReg;
    }

    public Double getTimeDiffRegMinute() {
        return timeDiffRegMinute;
    }

    public void setTimeDiffRegMinute(Double timeDiffRegMinute) {
        this.timeDiffRegMinute = timeDiffRegMinute;
    }

    public Double getToxicityUsd() {
        return toxicityUsd;
    }

    public void setToxicityUsd(Double toxicityUsd) {
        this.toxicityUsd = toxicityUsd;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Double getVolumeLots() {
        return volumeLots;
    }

    public void setVolumeLots(Double volumeLots) {
        this.volumeLots = volumeLots;
    }

    public String getWebSessionId() {
        return webSessionId;
    }

    public void setWebSessionId(String webSessionId) {
        this.webSessionId = webSessionId;
    }
}
