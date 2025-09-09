package business_objects.db.clickhouse.app_tb_finindex_data;

import java.util.Objects;

public class AppTbFinindexData {

    private int brandUid;             // UInt8
    private String brand;             // LowCardinality(String)
    private long id;                  // UInt64
    private long dataId;              // UInt64
    private String dataName;          // String
    private String language;          // LowCardinality(String)
    private String title;             // LowCardinality(String)
    private String countryCode;       // LowCardinality(String)
    private String importance;        // LowCardinality(String)
    private String publishTime; // DateTime64(3)
    private String description;       // String
    private String createTime; // DateTime64(3)
    private String updateTime; // DateTime64(3)
    private String lastUpdated; // DateTime64(3)

    protected AppTbFinindexData() {
    }

    public int getBrandUid() {
        return brandUid;
    }

    public void setBrandUid(int brandUid) {
        this.brandUid = brandUid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AppTbFinindexData that = (AppTbFinindexData) o;
        return brandUid == that.brandUid && id == that.id && dataId == that.dataId && Objects.equals(brand, that.brand) && Objects.equals(dataName, that.dataName) && Objects.equals(language, that.language) && Objects.equals(title, that.title) && Objects.equals(countryCode, that.countryCode) && Objects.equals(importance, that.importance) && Objects.equals(publishTime, that.publishTime) && Objects.equals(description, that.description) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandUid, brand, id, dataId, dataName, language, title, countryCode, importance, publishTime, description, createTime, updateTime, lastUpdated);
    }

    @Override
    public String toString() {
        return "AppTbFinindexData{" + "brandUid=" + brandUid + ", brand='" + brand + '\'' + ", id=" + id + ", dataId=" + dataId + ", dataName='" + dataName + '\'' + ", language='" + language + '\'' + ", title='" + title + '\'' + ", countryCode='" + countryCode + '\'' + ", importance='" + importance + '\'' + ", publishTime='" + publishTime + '\'' + ", description='" + description + '\'' + ", createTime='" + createTime + '\'' + ", updateTime='" + updateTime + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
