package business_objects.db.abuse_registry_db;

import java.sql.Timestamp;
import java.util.Objects;

import static utils.Utils.compareDoubles;

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
    private Timestamp deductionDate;
    private String commentDeduction;
    private Double approvedDeduction;
    private Double approvedDeductionUsd;
    private String crmId;
    private Double balanceAtResolution;
    private Double balanceAtResolutionUsd;
    private Boolean sendToLark;
    private String deductionType;

    public String getDeductionType() {
        return deductionType;
    }

    public void setDeductionType(String deductionType) {
        this.deductionType = deductionType;
    }

    public AbuserDeduction() {
    }

    public AbuserDeduction(Integer id, String ucid, Integer abuserHistoryId, String account, Integer serverId,
            String serverName, String currency, String brandGroup, String statusOpenPositions, String statusEmail,
            String statusDeduction, String statusApproval, String comment, Double illegalProfit,
            Double illegalProfitUsd, Double suggestedDeduction, Double suggestedDeductionUsd, Double actualDeduction,
            Double actualDeductionUsd, Timestamp createdAt, Timestamp updatedAt, String modifiedByUser,
            String modifiedBySystem, String typeAccount, Timestamp deductionDate, String commentDeduction,
            Double approvedDeduction, Double approvedDeductionUsd, String crmId, Double balanceAtResolution,
            Double balanceAtResolutionUsd, Boolean sendToLark) {
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
        this.commentDeduction = commentDeduction;
        this.approvedDeduction = approvedDeduction;
        this.approvedDeductionUsd = approvedDeductionUsd;
        this.crmId = crmId;
        this.balanceAtResolution = balanceAtResolution;
        this.balanceAtResolutionUsd = balanceAtResolutionUsd;
        this.sendToLark = sendToLark;
    }

    public AbuserDeduction(String ucid, Integer abuserHistoryId, String account, Integer serverId, String serverName,
            String currency, String brandGroup, String statusOpenPositions, String statusEmail, String statusDeduction,
            String statusApproval, String comment, Double illegalProfit, Double illegalProfitUsd,
            Double suggestedDeduction, Double suggestedDeductionUsd, Double actualDeduction, Double actualDeductionUsd,
            Timestamp createdAt, Timestamp updatedAt, String modifiedByUser, String modifiedBySystem,
            String typeAccount, Timestamp deductionDate, String commentDeduction, Double approvedDeduction,
            Double approvedDeductionUsd, String crmId, Double balanceAtResolution, Double balanceAtResolutionUsd,
            Boolean sendToLark) {
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
        this.commentDeduction = commentDeduction;
        this.approvedDeduction = approvedDeduction;
        this.approvedDeductionUsd = approvedDeductionUsd;
        this.crmId = crmId;
        this.balanceAtResolution = balanceAtResolution;
        this.balanceAtResolutionUsd = balanceAtResolutionUsd;
        this.sendToLark = sendToLark;
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

    public Timestamp getDeductionDate() {
        return deductionDate;
    }

    public void setDeductionDate(Timestamp deductionDate) {
        this.deductionDate = deductionDate;
    }

    public String getCommentDeduction() {
        return commentDeduction;
    }

    public void setCommentDeduction(String commentDeduction) {
        this.commentDeduction = commentDeduction;
    }

    public Double getApprovedDeduction() {
        return approvedDeduction;
    }

    public void setApprovedDeduction(Double approvedDeduction) {
        this.approvedDeduction = approvedDeduction;
    }

    public Double getApprovedDeductionUsd() {
        return approvedDeductionUsd;
    }

    public void setApprovedDeductionUsd(Double approvedDeductionUsd) {
        this.approvedDeductionUsd = approvedDeductionUsd;
    }

    public String getCrmId() {
        return crmId;
    }

    public void setCrmId(String crmId) {
        this.crmId = crmId;
    }

    public Double getBalanceAtResolution() {
        return balanceAtResolution;
    }

    public void setBalanceAtResolution(Double balanceAtResolution) {
        this.balanceAtResolution = balanceAtResolution;
    }

    public Double getBalanceAtResolutionUsd() {
        return balanceAtResolutionUsd;
    }

    public void setBalanceAtResolutionUsd(Double balanceAtResolutionUsd) {
        this.balanceAtResolutionUsd = balanceAtResolutionUsd;
    }

    public Boolean getSendToLark() {
        return sendToLark;
    }

    public void setSendToLark(Boolean sendToLark) {
        this.sendToLark = sendToLark;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbuserDeduction deduction = (AbuserDeduction) o;
        return Objects.equals(ucid, deduction.ucid) && Objects.equals(account, deduction.account) && Objects.equals(serverId, deduction.serverId) && Objects.equals(serverName, deduction.serverName) && Objects.equals(currency, deduction.currency) && Objects.equals(brandGroup, deduction.brandGroup) && Objects.equals(statusOpenPositions, deduction.statusOpenPositions) && Objects.equals(statusEmail, deduction.statusEmail) && Objects.equals(statusDeduction, deduction.statusDeduction) && Objects.equals(statusApproval, deduction.statusApproval) && Objects.equals(comment, deduction.comment) && compareDoubles(illegalProfit, deduction.illegalProfit) && compareDoubles(suggestedDeduction, deduction.suggestedDeduction) && compareDoubles(actualDeduction, deduction.actualDeduction) && Objects.equals(modifiedByUser, deduction.modifiedByUser) && Objects.equals(modifiedBySystem, deduction.modifiedBySystem) && Objects.equals(typeAccount, deduction.typeAccount) && Objects.equals(commentDeduction, deduction.commentDeduction) && compareDoubles(approvedDeduction, deduction.approvedDeduction) && Objects.equals(crmId, deduction.crmId) && compareDoubles(balanceAtResolution, deduction.balanceAtResolution) && Objects.equals(sendToLark, deduction.sendToLark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, account, serverId, serverName, currency, brandGroup, statusOpenPositions, statusEmail, statusDeduction, statusApproval, comment, illegalProfit, suggestedDeduction, actualDeduction, modifiedByUser, modifiedBySystem, typeAccount, commentDeduction, approvedDeduction, crmId, balanceAtResolution, sendToLark);
    }

    @Override
    public String toString() {
        return "AbuserDeduction{" + "id=" + id + ", ucid='" + ucid + '\'' + ", abuserHistoryId=" + abuserHistoryId + ", account='" + account + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", currency='" + currency + '\'' + ", brandGroup='" + brandGroup + '\'' + ", statusOpenPositions='" + statusOpenPositions + '\'' + ", statusEmail='" + statusEmail + '\'' + ", statusDeduction='" + statusDeduction + '\'' + ", statusApproval='" + statusApproval + '\'' + ", comment='" + comment + '\'' + ", illegalProfit=" + illegalProfit + ", illegalProfitUsd=" + illegalProfitUsd + ", suggestedDeduction=" + suggestedDeduction + ", suggestedDeductionUsd=" + suggestedDeductionUsd + ", actualDeduction=" + actualDeduction + ", actualDeductionUsd=" + actualDeductionUsd + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", modifiedByUser='" + modifiedByUser + '\'' + ", modifiedBySystem='" + modifiedBySystem + '\'' + ", typeAccount='" + typeAccount + '\'' + ", deductionDate='" + deductionDate + '\'' + ", commentDeduction='" + commentDeduction + '\'' + ", approvedDeduction=" + approvedDeduction + ", approvedDeductionUsd=" + approvedDeductionUsd + ", crmId='" + crmId + '\'' + ", balanceAtResolution=" + balanceAtResolution + ", balanceAtResolutionUsd=" + balanceAtResolutionUsd + ", sendToLark=" + sendToLark + '}';
    }
}
