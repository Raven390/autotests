package business_objects.db.abuse_registry_db;

import java.sql.Timestamp;
import java.util.Objects;

public class AbuserDeduction {
    private Integer id;
    private String ucid;
    private Integer abuserHistoryId;
    private String account;
    private Integer serverId;
    private String serverName;
    private String currency;
    private String brandGroup;
    private String statusOpenPositions;
    private String statusEmail;
    private String statusDeduction;
    private String statusApproval;
    private String comment;
    private Double illegalProfit;
    private Double illegalProfitUsd;
    private Double suggestedDeduction;
    private Double suggestedDeductionUsd;
    private Double actualDeduction;
    private Double actualDeductionUsd;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String modifiedByUser;
    private String modifiedBySystem;
    private String typeAccount;

    public AbuserDeduction() {
    }

    public AbuserDeduction(Integer id, String ucid, Integer abuserHistoryId, String account, Integer serverId,
            String serverName, String currency, String brandGroup, String statusOpenPositions, String statusEmail,
            String statusDeduction, String statusApproval, String comment, Double illegalProfit,
            Double illegalProfitUsd, Double suggestedDeduction, Double suggestedDeductionUsd, Double actualDeduction,
            Double actualDeductionUsd, Timestamp createdAt, Timestamp updatedAt, String modifiedByUser,
            String modifiedBySystem, String typeAccount) {
        this.id = id;
        this.ucid = ucid;
        this.abuserHistoryId = abuserHistoryId;
        this.account = account;
        this.serverId = serverId;
        this.serverName = serverName;
        this.currency = currency;
        this.brandGroup = brandGroup;
        this.statusOpenPositions = statusOpenPositions;
        this.statusEmail = statusEmail;
        this.statusDeduction = statusDeduction;
        this.statusApproval = statusApproval;
        this.comment = comment;
        this.illegalProfit = illegalProfit;
        this.illegalProfitUsd = illegalProfitUsd;
        this.suggestedDeduction = suggestedDeduction;
        this.suggestedDeductionUsd = suggestedDeductionUsd;
        this.actualDeduction = actualDeduction;
        this.actualDeductionUsd = actualDeductionUsd;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.modifiedByUser = modifiedByUser;
        this.modifiedBySystem = modifiedBySystem;
        this.typeAccount = typeAccount;
    }

    public AbuserDeduction(String ucid, Integer abuserHistoryId, String account, Integer serverId, String serverName,
            String currency, String brandGroup, String statusOpenPositions, String statusEmail, String statusDeduction,
            String statusApproval, String comment, Double illegalProfit, Double illegalProfitUsd,
            Double suggestedDeduction, Double suggestedDeductionUsd, Double actualDeduction, Double actualDeductionUsd,
            Timestamp createdAt, Timestamp updatedAt, String modifiedByUser, String modifiedBySystem,
            String typeAccount) {
        this.ucid = ucid;
        this.abuserHistoryId = abuserHistoryId;
        this.account = account;
        this.serverId = serverId;
        this.serverName = serverName;
        this.currency = currency;
        this.brandGroup = brandGroup;
        this.statusOpenPositions = statusOpenPositions;
        this.statusEmail = statusEmail;
        this.statusDeduction = statusDeduction;
        this.statusApproval = statusApproval;
        this.comment = comment;
        this.illegalProfit = illegalProfit;
        this.illegalProfitUsd = illegalProfitUsd;
        this.suggestedDeduction = suggestedDeduction;
        this.suggestedDeductionUsd = suggestedDeductionUsd;
        this.actualDeduction = actualDeduction;
        this.actualDeductionUsd = actualDeductionUsd;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.modifiedByUser = modifiedByUser;
        this.modifiedBySystem = modifiedBySystem;
        this.typeAccount = typeAccount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getAbuserHistoryId() {
        return abuserHistoryId;
    }

    public void setAbuserHistoryId(Integer abuserHistoryId) {
        this.abuserHistoryId = abuserHistoryId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBrandGroup() {
        return brandGroup;
    }

    public void setBrandGroup(String brandGroup) {
        this.brandGroup = brandGroup;
    }

    public String getStatusOpenPositions() {
        return statusOpenPositions;
    }

    public void setStatusOpenPositions(String statusOpenPositions) {
        this.statusOpenPositions = statusOpenPositions;
    }

    public String getStatusEmail() {
        return statusEmail;
    }

    public void setStatusEmail(String statusEmail) {
        this.statusEmail = statusEmail;
    }

    public String getStatusDeduction() {
        return statusDeduction;
    }

    public void setStatusDeduction(String statusDeduction) {
        this.statusDeduction = statusDeduction;
    }

    public String getStatusApproval() {
        return statusApproval;
    }

    public void setStatusApproval(String statusApproval) {
        this.statusApproval = statusApproval;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getIllegalProfit() {
        return illegalProfit;
    }

    public void setIllegalProfit(Double illegalProfit) {
        this.illegalProfit = illegalProfit;
    }

    public Double getIllegalProfitUsd() {
        return illegalProfitUsd;
    }

    public void setIllegalProfitUsd(Double illegalProfitUsd) {
        this.illegalProfitUsd = illegalProfitUsd;
    }

    public Double getSuggestedDeduction() {
        return suggestedDeduction;
    }

    public void setSuggestedDeduction(Double suggestedDeduction) {
        this.suggestedDeduction = suggestedDeduction;
    }

    public Double getSuggestedDeductionUsd() {
        return suggestedDeductionUsd;
    }

    public void setSuggestedDeductionUsd(Double suggestedDeductionUsd) {
        this.suggestedDeductionUsd = suggestedDeductionUsd;
    }

    public Double getActualDeduction() {
        return actualDeduction;
    }

    public void setActualDeduction(Double actualDeduction) {
        this.actualDeduction = actualDeduction;
    }

    public Double getActualDeductionUsd() {
        return actualDeductionUsd;
    }

    public void setActualDeductionUsd(Double actualDeductionUsd) {
        this.actualDeductionUsd = actualDeductionUsd;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(String modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public String getModifiedBySystem() {
        return modifiedBySystem;
    }

    public void setModifiedBySystem(String modifiedBySystem) {
        this.modifiedBySystem = modifiedBySystem;
    }

    public String getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(String typeAccount) {
        this.typeAccount = typeAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbuserDeduction that = (AbuserDeduction) o;
        return Objects.equals(id, that.id) && Objects.equals(ucid, that.ucid) && Objects.equals(abuserHistoryId, that.abuserHistoryId) && Objects.equals(account, that.account) && Objects.equals(serverId, that.serverId) && Objects.equals(serverName, that.serverName) && Objects.equals(currency, that.currency) && Objects.equals(brandGroup, that.brandGroup) && Objects.equals(statusOpenPositions, that.statusOpenPositions) && Objects.equals(statusEmail, that.statusEmail) && Objects.equals(statusDeduction, that.statusDeduction) && Objects.equals(statusApproval, that.statusApproval) && Objects.equals(comment, that.comment) && Objects.equals(illegalProfit, that.illegalProfit) && Objects.equals(illegalProfitUsd, that.illegalProfitUsd) && Objects.equals(suggestedDeduction, that.suggestedDeduction) && Objects.equals(suggestedDeductionUsd, that.suggestedDeductionUsd) && Objects.equals(actualDeduction, that.actualDeduction) && Objects.equals(actualDeductionUsd, that.actualDeductionUsd) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(modifiedByUser, that.modifiedByUser) && Objects.equals(modifiedBySystem, that.modifiedBySystem) && Objects.equals(typeAccount, that.typeAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ucid, abuserHistoryId, account, serverId, serverName, currency, brandGroup, statusOpenPositions, statusEmail, statusDeduction, statusApproval, comment, illegalProfit, illegalProfitUsd, suggestedDeduction, suggestedDeductionUsd, actualDeduction, actualDeductionUsd, createdAt, updatedAt, modifiedByUser, modifiedBySystem, typeAccount);
    }

    @Override
    public String toString() {
        return "AbuserDeduction{" + "id=" + id + ", ucid='" + ucid + '\'' + ", abuserHistoryId=" + abuserHistoryId + ", account='" + account + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", currency='" + currency + '\'' + ", brandGroup='" + brandGroup + '\'' + ", statusOpenPositions='" + statusOpenPositions + '\'' + ", statusEmail='" + statusEmail + '\'' + ", statusDeduction='" + statusDeduction + '\'' + ", statusApproval='" + statusApproval + '\'' + ", comment='" + comment + '\'' + ", illegalProfit=" + illegalProfit + ", illegalProfitUsd=" + illegalProfitUsd + ", suggestedDeduction=" + suggestedDeduction + ", suggestedDeductionUsd=" + suggestedDeductionUsd + ", actualDeduction=" + actualDeduction + ", actualDeductionUsd=" + actualDeductionUsd + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", modifiedByUser='" + modifiedByUser + '\'' + ", modifiedBySystem='" + modifiedBySystem + '\'' + ", typeAccount='" + typeAccount + '\'' + '}';
    }
}
