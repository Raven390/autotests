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
    private String deductionDate;

    public AbuserDeduction() {
    }

    public AbuserDeduction(Integer id, String ucid, Integer abuserHistoryId, String account, Integer serverId,
            String serverName, String currency, String brandGroup, String statusOpenPositions, String statusEmail,
            String statusDeduction, String statusApproval, String comment, Double illegalProfit,
            Double illegalProfitUsd, Double suggestedDeduction, Double suggestedDeductionUsd, Double actualDeduction,
            Double actualDeductionUsd, Timestamp createdAt, Timestamp updatedAt, String modifiedByUser,
            String modifiedBySystem, String typeAccount, String deductionDate) {
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
        this.deductionDate = deductionDate;
    }

    public AbuserDeduction(String ucid, Integer abuserHistoryId, String account, Integer serverId, String serverName,
            String currency, String brandGroup, String statusOpenPositions, String statusEmail, String statusDeduction,
            String statusApproval, String comment, Double illegalProfit, Double illegalProfitUsd,
            Double suggestedDeduction, Double suggestedDeductionUsd, Double actualDeduction, Double actualDeductionUsd,
            Timestamp createdAt, Timestamp updatedAt, String modifiedByUser, String modifiedBySystem,
            String typeAccount, String deductionDate) {
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
        this.deductionDate = deductionDate;
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

    public String getDeductionDate() {
        return deductionDate;
    }

    public void setDeductionDate(String deductionDate) {
        this.deductionDate = deductionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbuserDeduction deduction = (AbuserDeduction) o;
        return Objects.equals(ucid, deduction.ucid) && Objects.equals(account, deduction.account) && Objects.equals(serverId, deduction.serverId) && Objects.equals(serverName, deduction.serverName) && Objects.equals(currency, deduction.currency) && Objects.equals(brandGroup, deduction.brandGroup) && Objects.equals(statusOpenPositions, deduction.statusOpenPositions) && Objects.equals(statusEmail, deduction.statusEmail) && Objects.equals(statusDeduction, deduction.statusDeduction) && Objects.equals(statusApproval, deduction.statusApproval) && Objects.equals(comment, deduction.comment) && Objects.equals(illegalProfitUsd, deduction.illegalProfitUsd) && Objects.equals(suggestedDeductionUsd, deduction.suggestedDeductionUsd) && Objects.equals(actualDeductionUsd, deduction.actualDeductionUsd) && Objects.equals(modifiedByUser, deduction.modifiedByUser) && Objects.equals(modifiedBySystem, deduction.modifiedBySystem) && Objects.equals(typeAccount, deduction.typeAccount) && Objects.equals(deductionDate, deduction.deductionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, account, serverId, serverName, currency, brandGroup, statusOpenPositions, statusEmail, statusDeduction, statusApproval, comment, illegalProfitUsd, suggestedDeductionUsd, actualDeductionUsd, modifiedByUser, modifiedBySystem, typeAccount, deductionDate);
    }

    @Override
    public String toString() {
        return "AbuserDeduction{" + "id=" + id + ", ucid='" + ucid + '\'' + ", abuserHistoryId=" + abuserHistoryId + ", account='" + account + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", currency='" + currency + '\'' + ", brandGroup='" + brandGroup + '\'' + ", statusOpenPositions='" + statusOpenPositions + '\'' + ", statusEmail='" + statusEmail + '\'' + ", statusDeduction='" + statusDeduction + '\'' + ", statusApproval='" + statusApproval + '\'' + ", comment='" + comment + '\'' + ", illegalProfit=" + illegalProfit + ", illegalProfitUsd=" + illegalProfitUsd + ", suggestedDeduction=" + suggestedDeduction + ", suggestedDeductionUsd=" + suggestedDeductionUsd + ", actualDeduction=" + actualDeduction + ", actualDeductionUsd=" + actualDeductionUsd + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", modifiedByUser='" + modifiedByUser + '\'' + ", modifiedBySystem='" + modifiedBySystem + '\'' + ", typeAccount='" + typeAccount + '\'' + ", deductionDate='" + deductionDate + '\'' + '}';
    }
}
