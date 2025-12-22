package business_objects.db.clickhouse.reporting_test;

import java.util.Objects;

public class ZeebeRulesStarted {
    // Columns we care about; DbHelper maps by name ignoring underscores
    private String timestampStart;
    private String ruleName;
    private String runId;
    private String eventId;
    private String tradeId;
    private String serverId;
    private String tradingAccount;
    private String ucid;
    private String ruleVersion;
    private String mtEventDate;
    private String timestampCreate;

    public ZeebeRulesStarted() {}

    public ZeebeRulesStarted(
            String timestampStart,
            String ruleName,
            String runId,
            String eventId,
            String tradeId,
            String serverId,
            String tradingAccount,
            String ucid,
            String ruleVersion,
            String mtEventDate,
            String timestampCreate) {
        this.timestampStart = timestampStart;
        this.ruleName = ruleName;
        this.runId = runId;
        this.eventId = eventId;
        this.tradeId = tradeId;
        this.serverId = serverId;
        this.tradingAccount = tradingAccount;
        this.ucid = ucid;
        this.ruleVersion = ruleVersion;
        this.mtEventDate = mtEventDate;
        this.timestampCreate = timestampCreate;
    }

    public String getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(String timestampStart) {
        this.timestampStart = timestampStart;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(String tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getRuleVersion() {
        return ruleVersion;
    }

    public void setRuleVersion(String ruleVersion) {
        this.ruleVersion = ruleVersion;
    }

    public String getMtEventDate() {
        return mtEventDate;
    }

    public void setMtEventDate(String mtEventDate) {
        this.mtEventDate = mtEventDate;
    }

    public String getTimestampCreate() {
        return timestampCreate;
    }

    public void setTimestampCreate(String timestampCreate) {
        this.timestampCreate = timestampCreate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ZeebeRulesStarted that)) return false;
        return Objects.equals(timestampStart, that.timestampStart)
                && Objects.equals(ruleName, that.ruleName)
                && Objects.equals(runId, that.runId)
                && Objects.equals(eventId, that.eventId)
                && Objects.equals(tradeId, that.tradeId)
                && Objects.equals(serverId, that.serverId)
                && Objects.equals(tradingAccount, that.tradingAccount)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(ruleVersion, that.ruleVersion)
                && Objects.equals(mtEventDate, that.mtEventDate)
                && Objects.equals(timestampCreate, that.timestampCreate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                timestampStart,
                ruleName,
                runId,
                eventId,
                tradeId,
                serverId,
                tradingAccount,
                ucid,
                ruleVersion,
                mtEventDate,
                timestampCreate);
    }

    @Override
    public String toString() {
        return "ZeebeRulesStarted{" + "timestampStart='" + timestampStart + '\'' + ", ruleName='" + ruleName + '\''
                + ", runId='" + runId + '\'' + ", eventId='" + eventId + '\'' + ", tradeId='" + tradeId + '\''
                + ", serverId='" + serverId + '\'' + ", tradingAccount='" + tradingAccount + '\'' + ", ucid='" + ucid
                + '\'' + ", ruleVersion='" + ruleVersion + '\'' + ", mtEventDate='" + mtEventDate + '\''
                + ", timestampCreate='" + timestampCreate + '\'' + '}';
    }
}
