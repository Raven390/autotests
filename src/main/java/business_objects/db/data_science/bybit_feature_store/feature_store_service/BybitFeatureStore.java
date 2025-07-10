package business_objects.db.data_science.bybit_feature_store.feature_store_service;

import java.math.BigDecimal;
import java.util.Objects;

public class BybitFeatureStore {
    private String ucid;
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
    private BigDecimal commissionUsd;
    private BigDecimal sl;
    private BigDecimal tp;
    private BigDecimal depositUsd;
    private BigDecimal withdrawalUsd;
    private BigDecimal creditUsd;
    private BigDecimal bonusUsd;
    private Long timeDiff;
    private Long timeDiffReg;
    private BigDecimal cumavgTimeDiff;
    private Long cumUniqueSymbolUnderlying;
    private BigDecimal cumavgDepositUsd;
    private BigDecimal cumavgWithdrawalUsd;
    private BigDecimal cumavgCreditUsd;
    private BigDecimal cumavgBonusUsd;
    private BigDecimal cumavgNotionalValueUsd;
    private BigDecimal cumavgNotionalValueUsdAction0;
    private BigDecimal cumavgNotionalValueUsdAction1;
    private BigDecimal cumavgNotionalValueUsdEntry0;
    private BigDecimal cumavgNotionalValueUsdEntry1;
    private BigDecimal cumavgProfitUsd;
    private BigDecimal cumavgVolumeLots;
    private Long cumcountAction;
    private Long cumcountAction0;
    private Long cumcountAction1;
    private Long cumcountAction2;
    private Long cumcountAction3;
    private Long cumcountAction4;
    private Long cumcountAction6;
    private Long cumcountActionTrade;
    private Long cumcountEntry0;
    private Long cumcountEntry1;
    private BigDecimal cumsumAmountUsdAction;
    private BigDecimal cumsumDepositUsd;
    private BigDecimal cumsumWithdrawalUsd;
    private BigDecimal cumsumCreditUsd;
    private BigDecimal cumsumBonusUsd;
    private BigDecimal cumsumVolumeLots;
    private BigDecimal cumsumLotsSymbol;
    private BigDecimal cumsumNotionalValueUsdAction0;
    private BigDecimal cumsumNotionalValueUsdAction1;
    private BigDecimal cumsumNotionalValueUsdEntry0;
    private BigDecimal cumsumNotionalValueUsdEntry1;
    private BigDecimal cumsumProfitSymbol;
    private BigDecimal cumsumProfitUsd;
    private BigDecimal cumsumCommissionUsd;
    private Double creditToDeposit;
    private Double bonusToDeposit;
    private Double profitSymbolToProfit;
    private Double profitToCredit;
    private Double profitToBonus;
    private Double profitToDeposit;
    private Double profitToDepositCredit;
    private Double shareSymbol;
    private Double ratioAction0;
    private Double ratioAction1;
    private Double ratioAction2;
    private Double ratioAction3;
    private Double ratioAction4;
    private Double ratioAction6;
    private Double ratioActionTrade;
    private Double ratioEntry0;
    private Double ratioEntry1;

    public BybitFeatureStore() {
    }

    public BybitFeatureStore(
            String ucid, String timeUtc, String insertTimeUtc, Integer action, String id) {
        this.ucid = ucid;
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

    public BigDecimal getDepositUsd() {
        return depositUsd;
    }

    public void setDepositUsd(BigDecimal depositUsd) {
        this.depositUsd = depositUsd;
    }

    public BigDecimal getWithdrawalUsd() {
        return withdrawalUsd;
    }

    public void setWithdrawalUsd(BigDecimal withdrawalUsd) {
        this.withdrawalUsd = withdrawalUsd;
    }

    public BigDecimal getCreditUsd() {
        return creditUsd;
    }

    public void setCreditUsd(BigDecimal creditUsd) {
        this.creditUsd = creditUsd;
    }

    public BigDecimal getBonusUsd() {
        return bonusUsd;
    }

    public void setBonusUsd(BigDecimal bonusUsd) {
        this.bonusUsd = bonusUsd;
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

    public BigDecimal getCumavgTimeDiff() {
        return cumavgTimeDiff;
    }

    public void setCumavgTimeDiff(BigDecimal cumavgTimeDiff) {
        this.cumavgTimeDiff = cumavgTimeDiff;
    }

    public Long getCumUniqueSymbolUnderlying() {
        return cumUniqueSymbolUnderlying;
    }

    public void setCumUniqueSymbolUnderlying(Long cumUniqueSymbolUnderlying) {
        this.cumUniqueSymbolUnderlying = cumUniqueSymbolUnderlying;
    }

    public BigDecimal getCumavgDepositUsd() {
        return cumavgDepositUsd;
    }

    public void setCumavgDepositUsd(BigDecimal cumavgDepositUsd) {
        this.cumavgDepositUsd = cumavgDepositUsd;
    }

    public BigDecimal getCumavgWithdrawalUsd() {
        return cumavgWithdrawalUsd;
    }

    public void setCumavgWithdrawalUsd(BigDecimal cumavgWithdrawalUsd) {
        this.cumavgWithdrawalUsd = cumavgWithdrawalUsd;
    }

    public BigDecimal getCumavgCreditUsd() {
        return cumavgCreditUsd;
    }

    public void setCumavgCreditUsd(BigDecimal cumavgCreditUsd) {
        this.cumavgCreditUsd = cumavgCreditUsd;
    }

    public BigDecimal getCumavgBonusUsd() {
        return cumavgBonusUsd;
    }

    public void setCumavgBonusUsd(BigDecimal cumavgBonusUsd) {
        this.cumavgBonusUsd = cumavgBonusUsd;
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

    public BigDecimal getCumavgProfitUsd() {
        return cumavgProfitUsd;
    }

    public void setCumavgProfitUsd(BigDecimal cumavgProfitUsd) {
        this.cumavgProfitUsd = cumavgProfitUsd;
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

    public Long getCumcountAction4() {
        return cumcountAction4;
    }

    public void setCumcountAction4(Long cumcountAction4) {
        this.cumcountAction4 = cumcountAction4;
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

    public BigDecimal getCumsumAmountUsdAction() {
        return cumsumAmountUsdAction;
    }

    public void setCumsumAmountUsdAction(BigDecimal cumsumAmountUsdAction) {
        this.cumsumAmountUsdAction = cumsumAmountUsdAction;
    }

    public BigDecimal getCumsumDepositUsd() {
        return cumsumDepositUsd;
    }

    public void setCumsumDepositUsd(BigDecimal cumsumDepositUsd) {
        this.cumsumDepositUsd = cumsumDepositUsd;
    }

    public BigDecimal getCumsumWithdrawalUsd() {
        return cumsumWithdrawalUsd;
    }

    public void setCumsumWithdrawalUsd(BigDecimal cumsumWithdrawalUsd) {
        this.cumsumWithdrawalUsd = cumsumWithdrawalUsd;
    }

    public BigDecimal getCumsumCreditUsd() {
        return cumsumCreditUsd;
    }

    public void setCumsumCreditUsd(BigDecimal cumsumCreditUsd) {
        this.cumsumCreditUsd = cumsumCreditUsd;
    }

    public BigDecimal getCumsumBonusUsd() {
        return cumsumBonusUsd;
    }

    public void setCumsumBonusUsd(BigDecimal cumsumBonusUsd) {
        this.cumsumBonusUsd = cumsumBonusUsd;
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

    public Double getBonusToDeposit() {
        return bonusToDeposit;
    }

    public void setBonusToDeposit(Double bonusToDeposit) {
        this.bonusToDeposit = bonusToDeposit;
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

    public Double getProfitToBonus() {
        return profitToBonus;
    }

    public void setProfitToBonus(Double profitToBonus) {
        this.profitToBonus = profitToBonus;
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

    public Double getRatioAction4() {
        return ratioAction4;
    }

    public void setRatioAction4(Double ratioAction4) {
        this.ratioAction4 = ratioAction4;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BybitFeatureStore that = (BybitFeatureStore) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(timeUtc, that.timeUtc) && Objects.equals(insertTimeUtc, that.insertTimeUtc) && Objects.equals(action, that.action) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, timeUtc, insertTimeUtc, action, id);
    }

    @Override
    public String toString() {
        return "BybitFeatureStore{" + "ucid='" + ucid + '\'' + ", timeUtc='" + timeUtc + '\'' + ", insertTimeUtc='" + insertTimeUtc + '\'' + ", action=" + action + ", id='" + id + '\'' + '}';
    }
}