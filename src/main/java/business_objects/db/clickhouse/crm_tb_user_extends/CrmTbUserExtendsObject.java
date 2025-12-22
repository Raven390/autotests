package business_objects.db.clickhouse.crm_tb_user_extends;

import java.util.Objects;

public class CrmTbUserExtendsObject {
    public Integer sourceIdSt;
    public Integer brandUid;
    public String brand;
    public String regulator;
    public Long userId;
    public String ucid;
    public Long cpaId;
    public String webSource;
    public String income;
    public String tradingExperience;
    public String gender;
    public Integer isDel;
    public String lastUpdated;

    public CrmTbUserExtendsObject() {}

    public CrmTbUserExtendsObject(
            Integer sourceIdSt,
            Integer brandUid,
            String brand,
            String regulator,
            Long userId,
            String ucid,
            Long cpaId,
            String webSource,
            String income,
            String tradingExperience,
            String gender,
            Integer isDel,
            String lastUpdated) {
        this.sourceIdSt = sourceIdSt;
        this.brandUid = brandUid;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.ucid = ucid;
        this.cpaId = cpaId;
        this.webSource = webSource;
        this.income = income;
        this.tradingExperience = tradingExperience;
        this.gender = gender;
        this.isDel = isDel;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbUserExtendsObject that = (CrmTbUserExtendsObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt)
                && Objects.equals(brandUid, that.brandUid)
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(userId, that.userId)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(cpaId, that.cpaId)
                && Objects.equals(webSource, that.webSource)
                && Objects.equals(income, that.income)
                && Objects.equals(tradingExperience, that.tradingExperience)
                && Objects.equals(gender, that.gender)
                && Objects.equals(isDel, that.isDel)
                && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                sourceIdSt,
                brandUid,
                brand,
                regulator,
                userId,
                ucid,
                cpaId,
                webSource,
                income,
                tradingExperience,
                gender,
                isDel,
                lastUpdated);
    }

    @Override
    public String toString() {
        return "CrmTbUserExtendsObject{" + "sourceIdSt=" + sourceIdSt + ", brandUid=" + brandUid + ", brand='" + brand
                + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\''
                + ", cpaId=" + cpaId + ", webSource='" + webSource + '\'' + ", income='" + income + '\''
                + ", tradingExperience='" + tradingExperience + '\'' + ", gender='" + gender + '\'' + ", isDel=" + isDel
                + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
