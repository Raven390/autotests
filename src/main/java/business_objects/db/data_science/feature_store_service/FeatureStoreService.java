package business_objects.db.data_science.feature_store_service;

import java.math.BigDecimal;

public class FeatureStoreService {
    private String ucid;
    private String brand;
    private String timeUtc;
    private String insertTimeUtc;
    private Integer action;
    private String id;
    private Integer entry;
    private BigDecimal notionalValueUsd;
    private BigDecimal notionalValueUsdAction0;
    private BigDecimal notionalValueUsdAction1;
    private BigDecimal notionalValueUsdEntry0;
    private BigDecimal notionalValueUsdEntry1;
    private BigDecimal amountUsdAction;
    private String symbolUnderlying;
    private BigDecimal volumeLots;
    private BigDecimal profitUsd;
    private BigDecimal storageUsd;
    private BigDecimal commissionUsd;
    private BigDecimal sl;
    private BigDecimal tp;
    private String name;
    private Integer paymentChannelId;
    private Integer paymentTypeId;
    private Integer statusId;
    private BigDecimal amountUsdDeposit;
    private BigDecimal amountUsdDepositCard;
    private BigDecimal amountUsdDepositCrypto;
    private BigDecimal amountUsdDepositFail;
    private BigDecimal amountUsdDepositSuccess;
    private BigDecimal amountUsdWithdrawal;
    private BigDecimal amountUsdWithdrawalCard;
    private BigDecimal amountUsdWithdrawalCrypto;
    private BigDecimal amountUsdWithdrawalFail;
    private BigDecimal amountUsdWithdrawalSuccess;
    private Integer commentLabel;
    private BigDecimal creditUsd;
    private String deviceId;
    private String digitalId;
    private String os;
    private String sessionId;
    private String webSessionId;
    private Double digitalIdTrustScore;
    private Double policyScore;
    private Integer accountEmailResult;
    private Integer accountTelephoneResult;
    private Double connectionScore;
    private Long countConnection;
    private Long countFraud;
    private Long countPotential;
    private Double ratioFraud;
    private Double ratioPotential;
    private Long timeDiff;
    private Long timeDiffReg;
    private BigDecimal sumCpa;
    private BigDecimal sumRebate;
    private BigDecimal sumSwapFreePositive;
    private BigDecimal sumSwapFreeNegative;
    private BigDecimal sumSpread;
    private Long cumUniqueDeviceId;
    private Long cumUniqueDigitalId;
    private Long cumUniqueOs;
    private Long cumUniqueSessionId;
    private Long cumUniqueSymbolUnderlying;
    private Long cumUniqueWebSessionId;
    private BigDecimal cumavgAmountUsdDeposit;
    private BigDecimal cumavgAmountUsdDepositCard;
    private BigDecimal cumavgAmountUsdDepositCrypto;
    private BigDecimal cumavgAmountUsdDepositFail;
    private BigDecimal cumavgAmountUsdDepositSuccess;
    private BigDecimal cumavgAmountUsdWithdrawal;
    private BigDecimal cumavgAmountUsdWithdrawalCard;
    private BigDecimal cumavgAmountUsdWithdrawalCrypto;
    private BigDecimal cumavgAmountUsdWithdrawalFail;
    private BigDecimal cumavgAmountUsdWithdrawalSuccess;
    private BigDecimal cumavgCreditUsd;
    private Double cumavgDigitalIdTrustScore;
    private BigDecimal cumavgNotionalValueUsd;
    private BigDecimal cumavgNotionalValueUsdAction0;
    private BigDecimal cumavgNotionalValueUsdAction1;
    private BigDecimal cumavgNotionalValueUsdEntry0;
    private BigDecimal cumavgNotionalValueUsdEntry1;
    private Double cumavgPolicyScore;
    private BigDecimal cumavgProfitUsd;
    private BigDecimal cumavgTimeDiff;
    private BigDecimal cumavgVolumeLots;
    private Long cumcountAction;
    private Long cumcountAction0;
    private Long cumcountAction1;
    private Long cumcountAction2;
    private Long cumcountAction3;
    private Long cumcountAction5;
    private Long cumcountAction6;
    private Long cumcountActionTrade;
    private Long cumcountEntry0;
    private Long cumcountEntry1;
    private Long cumcountDepositCard;
    private Long cumcountDepositCrypto;
    private Long cumcountDepositP2p;
    private Long cumcountDepositTransfer;
    private Long cumcountDepositFail;
    private Long cumcountDepositSuccess;
    private Long cumcountWithdrawalCard;
    private Long cumcountWithdrawalCrypto;
    private Long cumcountWithdrawalP2p;
    private Long cumcountWithdrawalTransfer;
    private Long cumcountWithdrawalFail;
    private Long cumcountWithdrawalSuccess;
    private BigDecimal cumsumAmountUsdAction;
    private BigDecimal cumsumAmountUsdDeposit;
    private BigDecimal cumsumAmountUsdDepositCard;
    private BigDecimal cumsumAmountUsdDepositCrypto;
    private BigDecimal cumsumAmountUsdDepositFail;
    private BigDecimal cumsumAmountUsdDepositSuccess;
    private BigDecimal cumsumAmountUsdWithdrawal;
    private BigDecimal cumsumAmountUsdWithdrawalCard;
    private BigDecimal cumsumAmountUsdWithdrawalCrypto;
    private BigDecimal cumsumAmountUsdWithdrawalFail;
    private BigDecimal cumsumAmountUsdWithdrawalSuccess;
    private BigDecimal cumsumCreditUsd;
    private BigDecimal cumsumVolumeLots;
    private BigDecimal cumsumLotsSymbol;
    private BigDecimal cumsumNotionalValueUsdAction0;
    private BigDecimal cumsumNotionalValueUsdAction1;
    private BigDecimal cumsumNotionalValueUsdEntry0;
    private BigDecimal cumsumNotionalValueUsdEntry1;
    private BigDecimal cumsumProfitSymbol;
    private BigDecimal cumsumProfitUsd;
    private BigDecimal cumsumStorageUsd;
    private BigDecimal cumsumCommissionUsd;
    private Double creditToDeposit;
    private Double profitSymbolToProfit;
    private Double profitToCredit;
    private Double profitToDeposit;
    private Double profitToDepositCredit;
    private Double shareSymbol;
    private Double ratioAction0;
    private Double ratioAction1;
    private Double ratioAction2;
    private Double ratioAction3;
    private Double ratioAction5;
    private Double ratioAction6;
    private Double ratioActionTrade;
    private Double ratioEntry0;
    private Double ratioEntry1;
    private Double ratioDepositCard;
    private Double ratioDepositCrypto;
    private Double ratioDepositP2p;
    private Double ratioDepositTransfer;
    private Double ratioDepositFail;
    private Double ratioDepositSuccess;
    private Double ratioWithdrawalCard;
    private Double ratioWithdrawalCrypto;
    private Double ratioWithdrawalP2p;
    private Double ratioWithdrawalTransfer;
    private Double ratioWithdrawalFail;
    private Double ratioWithdrawalSuccess;

    public FeatureStoreService() {
    }

    public FeatureStoreService(
            String ucid, String brand, String timeUtc, String insertTimeUtc, Integer action, String id) {
        this.ucid = ucid;
        this.brand = brand;
        this.timeUtc = timeUtc;
        this.insertTimeUtc = insertTimeUtc;
        this.action = action;
        this.id = id;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTimeUtc() {
        return timeUtc;
    }

    public void setTimeUtc(String timeUtc) {
        this.timeUtc = timeUtc;
    }

    public String getInsertTimeUtc() {
        return insertTimeUtc;
    }

    public void setInsertTimeUtc(String insertTimeUtc) {
        this.insertTimeUtc = insertTimeUtc;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getEntry() {
        return entry;
    }

    public void setEntry(Integer entry) {
        this.entry = entry;
    }

    public BigDecimal getNotionalValueUsd() {
        return notionalValueUsd;
    }

    public void setNotionalValueUsd(BigDecimal notionalValueUsd) {
        this.notionalValueUsd = notionalValueUsd;
    }

    public BigDecimal getNotionalValueUsdAction0() {
        return notionalValueUsdAction0;
    }

    public void setNotionalValueUsdAction0(BigDecimal notionalValueUsdAction0) {
        this.notionalValueUsdAction0 = notionalValueUsdAction0;
    }

    public BigDecimal getNotionalValueUsdAction1() {
        return notionalValueUsdAction1;
    }

    public void setNotionalValueUsdAction1(BigDecimal notionalValueUsdAction1) {
        this.notionalValueUsdAction1 = notionalValueUsdAction1;
    }

    public BigDecimal getNotionalValueUsdEntry0() {
        return notionalValueUsdEntry0;
    }

    public void setNotionalValueUsdEntry0(BigDecimal notionalValueUsdEntry0) {
        this.notionalValueUsdEntry0 = notionalValueUsdEntry0;
    }

    public BigDecimal getNotionalValueUsdEntry1() {
        return notionalValueUsdEntry1;
    }

    public void setNotionalValueUsdEntry1(BigDecimal notionalValueUsdEntry1) {
        this.notionalValueUsdEntry1 = notionalValueUsdEntry1;
    }

    public BigDecimal getAmountUsdAction() {
        return amountUsdAction;
    }

    public void setAmountUsdAction(BigDecimal amountUsdAction) {
        this.amountUsdAction = amountUsdAction;
    }

    public String getSymbolUnderlying() {
        return symbolUnderlying;
    }

    public void setSymbolUnderlying(String symbolUnderlying) {
        this.symbolUnderlying = symbolUnderlying;
    }

    public BigDecimal getVolumeLots() {
        return volumeLots;
    }

    public void setVolumeLots(BigDecimal volumeLots) {
        this.volumeLots = volumeLots;
    }

    public BigDecimal getProfitUsd() {
        return profitUsd;
    }

    public void setProfitUsd(BigDecimal profitUsd) {
        this.profitUsd = profitUsd;
    }

    public BigDecimal getStorageUsd() {
        return storageUsd;
    }

    public void setStorageUsd(BigDecimal storageUsd) {
        this.storageUsd = storageUsd;
    }

    public BigDecimal getCommissionUsd() {
        return commissionUsd;
    }

    public void setCommissionUsd(BigDecimal commissionUsd) {
        this.commissionUsd = commissionUsd;
    }

    public BigDecimal getSl() {
        return sl;
    }

    public void setSl(BigDecimal sl) {
        this.sl = sl;
    }

    public BigDecimal getTp() {
        return tp;
    }

    public void setTp(BigDecimal tp) {
        this.tp = tp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPaymentChannelId() {
        return paymentChannelId;
    }

    public void setPaymentChannelId(Integer paymentChannelId) {
        this.paymentChannelId = paymentChannelId;
    }

    public Integer getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Integer paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public BigDecimal getAmountUsdDeposit() {
        return amountUsdDeposit;
    }

    public void setAmountUsdDeposit(BigDecimal amountUsdDeposit) {
        this.amountUsdDeposit = amountUsdDeposit;
    }

    public BigDecimal getAmountUsdDepositCard() {
        return amountUsdDepositCard;
    }

    public void setAmountUsdDepositCard(BigDecimal amountUsdDepositCard) {
        this.amountUsdDepositCard = amountUsdDepositCard;
    }

    public BigDecimal getAmountUsdDepositCrypto() {
        return amountUsdDepositCrypto;
    }

    public void setAmountUsdDepositCrypto(BigDecimal amountUsdDepositCrypto) {
        this.amountUsdDepositCrypto = amountUsdDepositCrypto;
    }

    public BigDecimal getAmountUsdDepositFail() {
        return amountUsdDepositFail;
    }

    public void setAmountUsdDepositFail(BigDecimal amountUsdDepositFail) {
        this.amountUsdDepositFail = amountUsdDepositFail;
    }

    public BigDecimal getAmountUsdDepositSuccess() {
        return amountUsdDepositSuccess;
    }

    public void setAmountUsdDepositSuccess(BigDecimal amountUsdDepositSuccess) {
        this.amountUsdDepositSuccess = amountUsdDepositSuccess;
    }

    public BigDecimal getAmountUsdWithdrawal() {
        return amountUsdWithdrawal;
    }

    public void setAmountUsdWithdrawal(BigDecimal amountUsdWithdrawal) {
        this.amountUsdWithdrawal = amountUsdWithdrawal;
    }

    public BigDecimal getAmountUsdWithdrawalCard() {
        return amountUsdWithdrawalCard;
    }

    public void setAmountUsdWithdrawalCard(BigDecimal amountUsdWithdrawalCard) {
        this.amountUsdWithdrawalCard = amountUsdWithdrawalCard;
    }

    public BigDecimal getAmountUsdWithdrawalCrypto() {
        return amountUsdWithdrawalCrypto;
    }

    public void setAmountUsdWithdrawalCrypto(BigDecimal amountUsdWithdrawalCrypto) {
        this.amountUsdWithdrawalCrypto = amountUsdWithdrawalCrypto;
    }

    public BigDecimal getAmountUsdWithdrawalFail() {
        return amountUsdWithdrawalFail;
    }

    public void setAmountUsdWithdrawalFail(BigDecimal amountUsdWithdrawalFail) {
        this.amountUsdWithdrawalFail = amountUsdWithdrawalFail;
    }

    public BigDecimal getAmountUsdWithdrawalSuccess() {
        return amountUsdWithdrawalSuccess;
    }

    public void setAmountUsdWithdrawalSuccess(BigDecimal amountUsdWithdrawalSuccess) {
        this.amountUsdWithdrawalSuccess = amountUsdWithdrawalSuccess;
    }

    public Integer getCommentLabel() {
        return commentLabel;
    }

    public void setCommentLabel(Integer commentLabel) {
        this.commentLabel = commentLabel;
    }

    public BigDecimal getCreditUsd() {
        return creditUsd;
    }

    public void setCreditUsd(BigDecimal creditUsd) {
        this.creditUsd = creditUsd;
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

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getWebSessionId() {
        return webSessionId;
    }

    public void setWebSessionId(String webSessionId) {
        this.webSessionId = webSessionId;
    }

    public Double getDigitalIdTrustScore() {
        return digitalIdTrustScore;
    }

    public void setDigitalIdTrustScore(Double digitalIdTrustScore) {
        this.digitalIdTrustScore = digitalIdTrustScore;
    }

    public Double getPolicyScore() {
        return policyScore;
    }

    public void setPolicyScore(Double policyScore) {
        this.policyScore = policyScore;
    }

    public Integer getAccountEmailResult() {
        return accountEmailResult;
    }

    public void setAccountEmailResult(Integer accountEmailResult) {
        this.accountEmailResult = accountEmailResult;
    }

    public Integer getAccountTelephoneResult() {
        return accountTelephoneResult;
    }

    public void setAccountTelephoneResult(Integer accountTelephoneResult) {
        this.accountTelephoneResult = accountTelephoneResult;
    }

    public Double getConnectionScore() {
        return connectionScore;
    }

    public void setConnectionScore(Double connectionScore) {
        this.connectionScore = connectionScore;
    }

    public Long getCountConnection() {
        return countConnection;
    }

    public void setCountConnection(Long countConnection) {
        this.countConnection = countConnection;
    }

    public Long getCountFraud() {
        return countFraud;
    }

    public void setCountFraud(Long countFraud) {
        this.countFraud = countFraud;
    }

    public Long getCountPotential() {
        return countPotential;
    }

    public void setCountPotential(Long countPotential) {
        this.countPotential = countPotential;
    }

    public Double getRatioFraud() {
        return ratioFraud;
    }

    public void setRatioFraud(Double ratioFraud) {
        this.ratioFraud = ratioFraud;
    }

    public Double getRatioPotential() {
        return ratioPotential;
    }

    public void setRatioPotential(Double ratioPotential) {
        this.ratioPotential = ratioPotential;
    }

    public Long getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(Long timeDiff) {
        this.timeDiff = timeDiff;
    }

    public Long getTimeDiffReg() {
        return timeDiffReg;
    }

    public void setTimeDiffReg(Long timeDiffReg) {
        this.timeDiffReg = timeDiffReg;
    }

    public BigDecimal getSumCpa() {
        return sumCpa;
    }

    public void setSumCpa(BigDecimal sumCpa) {
        this.sumCpa = sumCpa;
    }

    public BigDecimal getSumRebate() {
        return sumRebate;
    }

    public void setSumRebate(BigDecimal sumRebate) {
        this.sumRebate = sumRebate;
    }

    public BigDecimal getSumSwapFreePositive() {
        return sumSwapFreePositive;
    }

    public void setSumSwapFreePositive(BigDecimal sumSwapFreePositive) {
        this.sumSwapFreePositive = sumSwapFreePositive;
    }

    public BigDecimal getSumSwapFreeNegative() {
        return sumSwapFreeNegative;
    }

    public void setSumSwapFreeNegative(BigDecimal sumSwapFreeNegative) {
        this.sumSwapFreeNegative = sumSwapFreeNegative;
    }

    public BigDecimal getSumSpread() {
        return sumSpread;
    }

    public void setSumSpread(BigDecimal sumSpread) {
        this.sumSpread = sumSpread;
    }

    public Long getCumUniqueDeviceId() {
        return cumUniqueDeviceId;
    }

    public void setCumUniqueDeviceId(Long cumUniqueDeviceId) {
        this.cumUniqueDeviceId = cumUniqueDeviceId;
    }

    public Long getCumUniqueDigitalId() {
        return cumUniqueDigitalId;
    }

    public void setCumUniqueDigitalId(Long cumUniqueDigitalId) {
        this.cumUniqueDigitalId = cumUniqueDigitalId;
    }

    public Long getCumUniqueOs() {
        return cumUniqueOs;
    }

    public void setCumUniqueOs(Long cumUniqueOs) {
        this.cumUniqueOs = cumUniqueOs;
    }

    public Long getCumUniqueSessionId() {
        return cumUniqueSessionId;
    }

    public void setCumUniqueSessionId(Long cumUniqueSessionId) {
        this.cumUniqueSessionId = cumUniqueSessionId;
    }

    public Long getCumUniqueSymbolUnderlying() {
        return cumUniqueSymbolUnderlying;
    }

    public void setCumUniqueSymbolUnderlying(Long cumUniqueSymbolUnderlying) {
        this.cumUniqueSymbolUnderlying = cumUniqueSymbolUnderlying;
    }

    public Long getCumUniqueWebSessionId() {
        return cumUniqueWebSessionId;
    }

    public void setCumUniqueWebSessionId(Long cumUniqueWebSessionId) {
        this.cumUniqueWebSessionId = cumUniqueWebSessionId;
    }

    public BigDecimal getCumavgAmountUsdDeposit() {
        return cumavgAmountUsdDeposit;
    }

    public void setCumavgAmountUsdDeposit(BigDecimal cumavgAmountUsdDeposit) {
        this.cumavgAmountUsdDeposit = cumavgAmountUsdDeposit;
    }

    public BigDecimal getCumavgAmountUsdDepositCard() {
        return cumavgAmountUsdDepositCard;
    }

    public void setCumavgAmountUsdDepositCard(BigDecimal cumavgAmountUsdDepositCard) {
        this.cumavgAmountUsdDepositCard = cumavgAmountUsdDepositCard;
    }

    public BigDecimal getCumavgAmountUsdDepositCrypto() {
        return cumavgAmountUsdDepositCrypto;
    }

    public void setCumavgAmountUsdDepositCrypto(BigDecimal cumavgAmountUsdDepositCrypto) {
        this.cumavgAmountUsdDepositCrypto = cumavgAmountUsdDepositCrypto;
    }

    public BigDecimal getCumavgAmountUsdDepositFail() {
        return cumavgAmountUsdDepositFail;
    }

    public void setCumavgAmountUsdDepositFail(BigDecimal cumavgAmountUsdDepositFail) {
        this.cumavgAmountUsdDepositFail = cumavgAmountUsdDepositFail;
    }

    public BigDecimal getCumavgAmountUsdDepositSuccess() {
        return cumavgAmountUsdDepositSuccess;
    }

    public void setCumavgAmountUsdDepositSuccess(BigDecimal cumavgAmountUsdDepositSuccess) {
        this.cumavgAmountUsdDepositSuccess = cumavgAmountUsdDepositSuccess;
    }

    public BigDecimal getCumavgAmountUsdWithdrawal() {
        return cumavgAmountUsdWithdrawal;
    }

    public void setCumavgAmountUsdWithdrawal(BigDecimal cumavgAmountUsdWithdrawal) {
        this.cumavgAmountUsdWithdrawal = cumavgAmountUsdWithdrawal;
    }

    public BigDecimal getCumavgAmountUsdWithdrawalCard() {
        return cumavgAmountUsdWithdrawalCard;
    }

    public void setCumavgAmountUsdWithdrawalCard(BigDecimal cumavgAmountUsdWithdrawalCard) {
        this.cumavgAmountUsdWithdrawalCard = cumavgAmountUsdWithdrawalCard;
    }

    public BigDecimal getCumavgAmountUsdWithdrawalCrypto() {
        return cumavgAmountUsdWithdrawalCrypto;
    }

    public void setCumavgAmountUsdWithdrawalCrypto(BigDecimal cumavgAmountUsdWithdrawalCrypto) {
        this.cumavgAmountUsdWithdrawalCrypto = cumavgAmountUsdWithdrawalCrypto;
    }

    public BigDecimal getCumavgAmountUsdWithdrawalFail() {
        return cumavgAmountUsdWithdrawalFail;
    }

    public void setCumavgAmountUsdWithdrawalFail(BigDecimal cumavgAmountUsdWithdrawalFail) {
        this.cumavgAmountUsdWithdrawalFail = cumavgAmountUsdWithdrawalFail;
    }

    public BigDecimal getCumavgAmountUsdWithdrawalSuccess() {
        return cumavgAmountUsdWithdrawalSuccess;
    }

    public void setCumavgAmountUsdWithdrawalSuccess(BigDecimal cumavgAmountUsdWithdrawalSuccess) {
        this.cumavgAmountUsdWithdrawalSuccess = cumavgAmountUsdWithdrawalSuccess;
    }

    public BigDecimal getCumavgCreditUsd() {
        return cumavgCreditUsd;
    }

    public void setCumavgCreditUsd(BigDecimal cumavgCreditUsd) {
        this.cumavgCreditUsd = cumavgCreditUsd;
    }

    public Double getCumavgDigitalIdTrustScore() {
        return cumavgDigitalIdTrustScore;
    }

    public void setCumavgDigitalIdTrustScore(Double cumavgDigitalIdTrustScore) {
        this.cumavgDigitalIdTrustScore = cumavgDigitalIdTrustScore;
    }

    public BigDecimal getCumavgNotionalValueUsd() {
        return cumavgNotionalValueUsd;
    }

    public void setCumavgNotionalValueUsd(BigDecimal cumavgNotionalValueUsd) {
        this.cumavgNotionalValueUsd = cumavgNotionalValueUsd;
    }

    public BigDecimal getCumavgNotionalValueUsdAction0() {
        return cumavgNotionalValueUsdAction0;
    }

    public void setCumavgNotionalValueUsdAction0(BigDecimal cumavgNotionalValueUsdAction0) {
        this.cumavgNotionalValueUsdAction0 = cumavgNotionalValueUsdAction0;
    }

    public BigDecimal getCumavgNotionalValueUsdAction1() {
        return cumavgNotionalValueUsdAction1;
    }

    public void setCumavgNotionalValueUsdAction1(BigDecimal cumavgNotionalValueUsdAction1) {
        this.cumavgNotionalValueUsdAction1 = cumavgNotionalValueUsdAction1;
    }

    public BigDecimal getCumavgNotionalValueUsdEntry0() {
        return cumavgNotionalValueUsdEntry0;
    }

    public void setCumavgNotionalValueUsdEntry0(BigDecimal cumavgNotionalValueUsdEntry0) {
        this.cumavgNotionalValueUsdEntry0 = cumavgNotionalValueUsdEntry0;
    }

    public BigDecimal getCumavgNotionalValueUsdEntry1() {
        return cumavgNotionalValueUsdEntry1;
    }

    public void setCumavgNotionalValueUsdEntry1(BigDecimal cumavgNotionalValueUsdEntry1) {
        this.cumavgNotionalValueUsdEntry1 = cumavgNotionalValueUsdEntry1;
    }

    public Double getCumavgPolicyScore() {
        return cumavgPolicyScore;
    }

    public void setCumavgPolicyScore(Double cumavgPolicyScore) {
        this.cumavgPolicyScore = cumavgPolicyScore;
    }

    public BigDecimal getCumavgProfitUsd() {
        return cumavgProfitUsd;
    }

    public void setCumavgProfitUsd(BigDecimal cumavgProfitUsd) {
        this.cumavgProfitUsd = cumavgProfitUsd;
    }

    public BigDecimal getCumavgTimeDiff() {
        return cumavgTimeDiff;
    }

    public void setCumavgTimeDiff(BigDecimal cumavgTimeDiff) {
        this.cumavgTimeDiff = cumavgTimeDiff;
    }

    public BigDecimal getCumavgVolumeLots() {
        return cumavgVolumeLots;
    }

    public void setCumavgVolumeLots(BigDecimal cumavgVolumeLots) {
        this.cumavgVolumeLots = cumavgVolumeLots;
    }

    public Long getCumcountAction() {
        return cumcountAction;
    }

    public void setCumcountAction(Long cumcountAction) {
        this.cumcountAction = cumcountAction;
    }

    public Long getCumcountAction0() {
        return cumcountAction0;
    }

    public void setCumcountAction0(Long cumcountAction0) {
        this.cumcountAction0 = cumcountAction0;
    }

    public Long getCumcountAction1() {
        return cumcountAction1;
    }

    public void setCumcountAction1(Long cumcountAction1) {
        this.cumcountAction1 = cumcountAction1;
    }

    public Long getCumcountAction2() {
        return cumcountAction2;
    }

    public void setCumcountAction2(Long cumcountAction2) {
        this.cumcountAction2 = cumcountAction2;
    }

    public Long getCumcountAction3() {
        return cumcountAction3;
    }

    public void setCumcountAction3(Long cumcountAction3) {
        this.cumcountAction3 = cumcountAction3;
    }

    public Long getCumcountAction5() {
        return cumcountAction5;
    }

    public void setCumcountAction5(Long cumcountAction5) {
        this.cumcountAction5 = cumcountAction5;
    }

    public Long getCumcountAction6() {
        return cumcountAction6;
    }

    public void setCumcountAction6(Long cumcountAction6) {
        this.cumcountAction6 = cumcountAction6;
    }

    public Long getCumcountActionTrade() {
        return cumcountActionTrade;
    }

    public void setCumcountActionTrade(Long cumcountActionTrade) {
        this.cumcountActionTrade = cumcountActionTrade;
    }

    public Long getCumcountEntry0() {
        return cumcountEntry0;
    }

    public void setCumcountEntry0(Long cumcountEntry0) {
        this.cumcountEntry0 = cumcountEntry0;
    }

    public Long getCumcountEntry1() {
        return cumcountEntry1;
    }

    public void setCumcountEntry1(Long cumcountEntry1) {
        this.cumcountEntry1 = cumcountEntry1;
    }

    public Long getCumcountDepositCard() {
        return cumcountDepositCard;
    }

    public void setCumcountDepositCard(Long cumcountDepositCard) {
        this.cumcountDepositCard = cumcountDepositCard;
    }

    public Long getCumcountDepositCrypto() {
        return cumcountDepositCrypto;
    }

    public void setCumcountDepositCrypto(Long cumcountDepositCrypto) {
        this.cumcountDepositCrypto = cumcountDepositCrypto;
    }

    public Long getCumcountDepositP2p() {
        return cumcountDepositP2p;
    }

    public void setCumcountDepositP2p(Long cumcountDepositP2p) {
        this.cumcountDepositP2p = cumcountDepositP2p;
    }

    public Long getCumcountDepositTransfer() {
        return cumcountDepositTransfer;
    }

    public void setCumcountDepositTransfer(Long cumcountDepositTransfer) {
        this.cumcountDepositTransfer = cumcountDepositTransfer;
    }

    public Long getCumcountDepositFail() {
        return cumcountDepositFail;
    }

    public void setCumcountDepositFail(Long cumcountDepositFail) {
        this.cumcountDepositFail = cumcountDepositFail;
    }

    public Long getCumcountDepositSuccess() {
        return cumcountDepositSuccess;
    }

    public void setCumcountDepositSuccess(Long cumcountDepositSuccess) {
        this.cumcountDepositSuccess = cumcountDepositSuccess;
    }

    public Long getCumcountWithdrawalCard() {
        return cumcountWithdrawalCard;
    }

    public void setCumcountWithdrawalCard(Long cumcountWithdrawalCard) {
        this.cumcountWithdrawalCard = cumcountWithdrawalCard;
    }

    public Long getCumcountWithdrawalCrypto() {
        return cumcountWithdrawalCrypto;
    }

    public void setCumcountWithdrawalCrypto(Long cumcountWithdrawalCrypto) {
        this.cumcountWithdrawalCrypto = cumcountWithdrawalCrypto;
    }

    public Long getCumcountWithdrawalP2p() {
        return cumcountWithdrawalP2p;
    }

    public void setCumcountWithdrawalP2p(Long cumcountWithdrawalP2p) {
        this.cumcountWithdrawalP2p = cumcountWithdrawalP2p;
    }

    public Long getCumcountWithdrawalTransfer() {
        return cumcountWithdrawalTransfer;
    }

    public void setCumcountWithdrawalTransfer(Long cumcountWithdrawalTransfer) {
        this.cumcountWithdrawalTransfer = cumcountWithdrawalTransfer;
    }

    public Long getCumcountWithdrawalFail() {
        return cumcountWithdrawalFail;
    }

    public void setCumcountWithdrawalFail(Long cumcountWithdrawalFail) {
        this.cumcountWithdrawalFail = cumcountWithdrawalFail;
    }

    public Long getCumcountWithdrawalSuccess() {
        return cumcountWithdrawalSuccess;
    }

    public void setCumcountWithdrawalSuccess(Long cumcountWithdrawalSuccess) {
        this.cumcountWithdrawalSuccess = cumcountWithdrawalSuccess;
    }

    public BigDecimal getCumsumAmountUsdAction() {
        return cumsumAmountUsdAction;
    }

    public void setCumsumAmountUsdAction(BigDecimal cumsumAmountUsdAction) {
        this.cumsumAmountUsdAction = cumsumAmountUsdAction;
    }

    public BigDecimal getCumsumAmountUsdDeposit() {
        return cumsumAmountUsdDeposit;
    }

    public void setCumsumAmountUsdDeposit(BigDecimal cumsumAmountUsdDeposit) {
        this.cumsumAmountUsdDeposit = cumsumAmountUsdDeposit;
    }

    public BigDecimal getCumsumAmountUsdDepositCard() {
        return cumsumAmountUsdDepositCard;
    }

    public void setCumsumAmountUsdDepositCard(BigDecimal cumsumAmountUsdDepositCard) {
        this.cumsumAmountUsdDepositCard = cumsumAmountUsdDepositCard;
    }

    public BigDecimal getCumsumAmountUsdDepositCrypto() {
        return cumsumAmountUsdDepositCrypto;
    }

    public void setCumsumAmountUsdDepositCrypto(BigDecimal cumsumAmountUsdDepositCrypto) {
        this.cumsumAmountUsdDepositCrypto = cumsumAmountUsdDepositCrypto;
    }

    public BigDecimal getCumsumAmountUsdDepositFail() {
        return cumsumAmountUsdDepositFail;
    }

    public void setCumsumAmountUsdDepositFail(BigDecimal cumsumAmountUsdDepositFail) {
        this.cumsumAmountUsdDepositFail = cumsumAmountUsdDepositFail;
    }

    public BigDecimal getCumsumAmountUsdDepositSuccess() {
        return cumsumAmountUsdDepositSuccess;
    }

    public void setCumsumAmountUsdDepositSuccess(BigDecimal cumsumAmountUsdDepositSuccess) {
        this.cumsumAmountUsdDepositSuccess = cumsumAmountUsdDepositSuccess;
    }

    public BigDecimal getCumsumAmountUsdWithdrawal() {
        return cumsumAmountUsdWithdrawal;
    }

    public void setCumsumAmountUsdWithdrawal(BigDecimal cumsumAmountUsdWithdrawal) {
        this.cumsumAmountUsdWithdrawal = cumsumAmountUsdWithdrawal;
    }

    public BigDecimal getCumsumAmountUsdWithdrawalCard() {
        return cumsumAmountUsdWithdrawalCard;
    }

    public void setCumsumAmountUsdWithdrawalCard(BigDecimal cumsumAmountUsdWithdrawalCard) {
        this.cumsumAmountUsdWithdrawalCard = cumsumAmountUsdWithdrawalCard;
    }

    public BigDecimal getCumsumAmountUsdWithdrawalCrypto() {
        return cumsumAmountUsdWithdrawalCrypto;
    }

    public void setCumsumAmountUsdWithdrawalCrypto(BigDecimal cumsumAmountUsdWithdrawalCrypto) {
        this.cumsumAmountUsdWithdrawalCrypto = cumsumAmountUsdWithdrawalCrypto;
    }

    public BigDecimal getCumsumAmountUsdWithdrawalFail() {
        return cumsumAmountUsdWithdrawalFail;
    }

    public void setCumsumAmountUsdWithdrawalFail(BigDecimal cumsumAmountUsdWithdrawalFail) {
        this.cumsumAmountUsdWithdrawalFail = cumsumAmountUsdWithdrawalFail;
    }

    public BigDecimal getCumsumAmountUsdWithdrawalSuccess() {
        return cumsumAmountUsdWithdrawalSuccess;
    }

    public void setCumsumAmountUsdWithdrawalSuccess(BigDecimal cumsumAmountUsdWithdrawalSuccess) {
        this.cumsumAmountUsdWithdrawalSuccess = cumsumAmountUsdWithdrawalSuccess;
    }

    public BigDecimal getCumsumCreditUsd() {
        return cumsumCreditUsd;
    }

    public void setCumsumCreditUsd(BigDecimal cumsumCreditUsd) {
        this.cumsumCreditUsd = cumsumCreditUsd;
    }

    public BigDecimal getCumsumVolumeLots() {
        return cumsumVolumeLots;
    }

    public void setCumsumVolumeLots(BigDecimal cumsumVolumeLots) {
        this.cumsumVolumeLots = cumsumVolumeLots;
    }

    public BigDecimal getCumsumLotsSymbol() {
        return cumsumLotsSymbol;
    }

    public void setCumsumLotsSymbol(BigDecimal cumsumLotsSymbol) {
        this.cumsumLotsSymbol = cumsumLotsSymbol;
    }

    public BigDecimal getCumsumNotionalValueUsdAction0() {
        return cumsumNotionalValueUsdAction0;
    }

    public void setCumsumNotionalValueUsdAction0(BigDecimal cumsumNotionalValueUsdAction0) {
        this.cumsumNotionalValueUsdAction0 = cumsumNotionalValueUsdAction0;
    }

    public BigDecimal getCumsumNotionalValueUsdAction1() {
        return cumsumNotionalValueUsdAction1;
    }

    public void setCumsumNotionalValueUsdAction1(BigDecimal cumsumNotionalValueUsdAction1) {
        this.cumsumNotionalValueUsdAction1 = cumsumNotionalValueUsdAction1;
    }

    public BigDecimal getCumsumNotionalValueUsdEntry0() {
        return cumsumNotionalValueUsdEntry0;
    }

    public void setCumsumNotionalValueUsdEntry0(BigDecimal cumsumNotionalValueUsdEntry0) {
        this.cumsumNotionalValueUsdEntry0 = cumsumNotionalValueUsdEntry0;
    }

    public BigDecimal getCumsumNotionalValueUsdEntry1() {
        return cumsumNotionalValueUsdEntry1;
    }

    public void setCumsumNotionalValueUsdEntry1(BigDecimal cumsumNotionalValueUsdEntry1) {
        this.cumsumNotionalValueUsdEntry1 = cumsumNotionalValueUsdEntry1;
    }

    public BigDecimal getCumsumProfitSymbol() {
        return cumsumProfitSymbol;
    }

    public void setCumsumProfitSymbol(BigDecimal cumsumProfitSymbol) {
        this.cumsumProfitSymbol = cumsumProfitSymbol;
    }

    public BigDecimal getCumsumProfitUsd() {
        return cumsumProfitUsd;
    }

    public void setCumsumProfitUsd(BigDecimal cumsumProfitUsd) {
        this.cumsumProfitUsd = cumsumProfitUsd;
    }

    public BigDecimal getCumsumStorageUsd() {
        return cumsumStorageUsd;
    }

    public void setCumsumStorageUsd(BigDecimal cumsumStorageUsd) {
        this.cumsumStorageUsd = cumsumStorageUsd;
    }

    public BigDecimal getCumsumCommissionUsd() {
        return cumsumCommissionUsd;
    }

    public void setCumsumCommissionUsd(BigDecimal cumsumCommissionUsd) {
        this.cumsumCommissionUsd = cumsumCommissionUsd;
    }

    public Double getCreditToDeposit() {
        return creditToDeposit;
    }

    public void setCreditToDeposit(Double creditToDeposit) {
        this.creditToDeposit = creditToDeposit;
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

    public Double getShareSymbol() {
        return shareSymbol;
    }

    public void setShareSymbol(Double shareSymbol) {
        this.shareSymbol = shareSymbol;
    }

    public Double getRatioAction0() {
        return ratioAction0;
    }

    public void setRatioAction0(Double ratioAction0) {
        this.ratioAction0 = ratioAction0;
    }

    public Double getRatioAction1() {
        return ratioAction1;
    }

    public void setRatioAction1(Double ratioAction1) {
        this.ratioAction1 = ratioAction1;
    }

    public Double getRatioAction2() {
        return ratioAction2;
    }

    public void setRatioAction2(Double ratioAction2) {
        this.ratioAction2 = ratioAction2;
    }

    public Double getRatioAction3() {
        return ratioAction3;
    }

    public void setRatioAction3(Double ratioAction3) {
        this.ratioAction3 = ratioAction3;
    }

    public Double getRatioAction5() {
        return ratioAction5;
    }

    public void setRatioAction5(Double ratioAction5) {
        this.ratioAction5 = ratioAction5;
    }

    public Double getRatioAction6() {
        return ratioAction6;
    }

    public void setRatioAction6(Double ratioAction6) {
        this.ratioAction6 = ratioAction6;
    }

    public Double getRatioActionTrade() {
        return ratioActionTrade;
    }

    public void setRatioActionTrade(Double ratioActionTrade) {
        this.ratioActionTrade = ratioActionTrade;
    }

    public Double getRatioEntry0() {
        return ratioEntry0;
    }

    public void setRatioEntry0(Double ratioEntry0) {
        this.ratioEntry0 = ratioEntry0;
    }

    public Double getRatioEntry1() {
        return ratioEntry1;
    }

    public void setRatioEntry1(Double ratioEntry1) {
        this.ratioEntry1 = ratioEntry1;
    }

    public Double getRatioDepositCard() {
        return ratioDepositCard;
    }

    public void setRatioDepositCard(Double ratioDepositCard) {
        this.ratioDepositCard = ratioDepositCard;
    }

    public Double getRatioDepositCrypto() {
        return ratioDepositCrypto;
    }

    public void setRatioDepositCrypto(Double ratioDepositCrypto) {
        this.ratioDepositCrypto = ratioDepositCrypto;
    }

    public Double getRatioDepositP2p() {
        return ratioDepositP2p;
    }

    public void setRatioDepositP2p(Double ratioDepositP2p) {
        this.ratioDepositP2p = ratioDepositP2p;
    }

    public Double getRatioDepositTransfer() {
        return ratioDepositTransfer;
    }

    public void setRatioDepositTransfer(Double ratioDepositTransfer) {
        this.ratioDepositTransfer = ratioDepositTransfer;
    }

    public Double getRatioDepositFail() {
        return ratioDepositFail;
    }

    public void setRatioDepositFail(Double ratioDepositFail) {
        this.ratioDepositFail = ratioDepositFail;
    }

    public Double getRatioDepositSuccess() {
        return ratioDepositSuccess;
    }

    public void setRatioDepositSuccess(Double ratioDepositSuccess) {
        this.ratioDepositSuccess = ratioDepositSuccess;
    }

    public Double getRatioWithdrawalCard() {
        return ratioWithdrawalCard;
    }

    public void setRatioWithdrawalCard(Double ratioWithdrawalCard) {
        this.ratioWithdrawalCard = ratioWithdrawalCard;
    }

    public Double getRatioWithdrawalCrypto() {
        return ratioWithdrawalCrypto;
    }

    public void setRatioWithdrawalCrypto(Double ratioWithdrawalCrypto) {
        this.ratioWithdrawalCrypto = ratioWithdrawalCrypto;
    }

    public Double getRatioWithdrawalP2p() {
        return ratioWithdrawalP2p;
    }

    public void setRatioWithdrawalP2p(Double ratioWithdrawalP2p) {
        this.ratioWithdrawalP2p = ratioWithdrawalP2p;
    }

    public Double getRatioWithdrawalTransfer() {
        return ratioWithdrawalTransfer;
    }

    public void setRatioWithdrawalTransfer(Double ratioWithdrawalTransfer) {
        this.ratioWithdrawalTransfer = ratioWithdrawalTransfer;
    }

    public Double getRatioWithdrawalFail() {
        return ratioWithdrawalFail;
    }

    public void setRatioWithdrawalFail(Double ratioWithdrawalFail) {
        this.ratioWithdrawalFail = ratioWithdrawalFail;
    }

    public Double getRatioWithdrawalSuccess() {
        return ratioWithdrawalSuccess;
    }

    public void setRatioWithdrawalSuccess(Double ratioWithdrawalSuccess) {
        this.ratioWithdrawalSuccess = ratioWithdrawalSuccess;
    }
}
