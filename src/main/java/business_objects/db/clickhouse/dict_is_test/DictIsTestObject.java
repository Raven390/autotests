package business_objects.db.clickhouse.dict_is_test;

import java.util.Objects;

public class DictIsTestObject {
    // Declare variables
    public Integer serverIdSt;
    public Integer account;
    public String accountGroup;
    public Integer isTest;
    public String lastUpdated;

    public DictIsTestObject() {}

    public DictIsTestObject(
            Integer serverIdSt, Integer account, String accountGroup, Integer isTest, String lastUpdated) {
        this.serverIdSt = serverIdSt;
        this.account = account;
        this.accountGroup = accountGroup;
        this.isTest = isTest;
        this.lastUpdated = lastUpdated;
    }

    public Integer getserverIdSt() {
        return serverIdSt;
    }

    public void setserverIdSt(Integer serverIdSt) {
        this.serverIdSt = serverIdSt;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public Integer getIsTest() {
        return isTest;
    }

    public void setIsTest(Integer isTest) {
        this.isTest = isTest;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DictIsTestObject that)) return false;
        return Objects.equals(serverIdSt, that.serverIdSt)
                && Objects.equals(account, that.account)
                && Objects.equals(accountGroup, that.accountGroup)
                && Objects.equals(isTest, that.isTest)
                && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverIdSt, account, accountGroup, isTest, lastUpdated);
    }

    @Override
    public String toString() {
        return "DictIsTestObject{" + "serverIdSt=" + serverIdSt + ", account=" + account + ", accountGroup='"
                + accountGroup + '\'' + ", isTest=" + isTest + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
