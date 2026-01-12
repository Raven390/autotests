package business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account;

import static utils.Constants.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import java.util.Currency;

public class CrmTbAccountForMtObjectFactory {

    @Step("Generate crm account for mt table data")
    public static CrmTbAccountForMtObject generateAccountForMtByClient(ClientHelper client, Boolean isSwapFree) {
        int swapFree;
        String accountType = "";
        if (isSwapFree) {
            swapFree = 1;
            accountType = ACCOUNT_TYPE_STANDARD;
        } else {
            swapFree = 0;
            accountType = ACCOUNT_TYPE_SWAP_FREE;
        }
        return new CrmTbAccountForMtObject(
                10, // sourceIdString
                5, // brandUid
                client.getBrand(), // brand
                client.getRegulator(), // regulator
                client.getUserId(), // userId
                client.getUcid(), // ucid
                client.getTradingAccount(), // account
                client.getServerId(), // serverIdSt
                "ST", // server
                1, // accountTypeId
                accountType, // accountType
                "M_SVS_0000_USD", // accountGroup
                PLATFORM_MT_4, // platform
                "2022-11-16 05:30:00", // createTime
                "2022-11-16 05:30:00", // createTimeUtc
                "2022-11-16", // createDate
                "2022-11-16", // createDateUtc
                ACCOUNT_STATUS_ACTIVE, // accountStatus
                "2024-11-16 05:30:00", // last login
                "2024-11-16 05:30:00", // lastLoginUtc
                "2024-11-16 05:30:00", // lastOrder
                "2024-11-16 05:30:00", // lastOrderUtc
                1d, // balance
                Currency.getInstance("USD").getCurrencyCode(), // currency
                1d, // balanceUsd
                2d, // equity
                3d, // credit
                4d, // pnl
                4, // leverage
                5d, // marginFree
                0, // isRebateAccount
                0, // rebateAccountNr
                943_793, // ibId
                943_793, // pId
                swapFree, // isSwapFree
                0, // isPamm
                0, // isCent
                1, // isArchive
                0, // isHidden
                0, // isDel
                0, // isDelete
                "2024-07-09 02:21:39", // lastUpdated
                COMMENT_AUTOMATION_TESTS, // internalComment
                0 // isTest
                );
    }

    @Step("Generate crm account for mt table data")
    public static CrmTbAccountForMtObject generateAccountForMtByAccount(CrmTbAccountObject account) {
        CrmTbAccountForMtObject accountForMtObject = new CrmTbAccountForMtObject();
        accountForMtObject.setSourceIdSt(account.sourceIdSt);
        accountForMtObject.setBrandUid(account.brandUid);
        accountForMtObject.setBrand(account.brand);
        accountForMtObject.setRegulator(account.regulator);
        accountForMtObject.setUserId(account.userId);
        accountForMtObject.setUcid(account.ucid);
        accountForMtObject.setAccount(account.account);
        accountForMtObject.setServerIdSt(account.serverIdSt);
        accountForMtObject.setServerName(account.serverName);
        accountForMtObject.setAccountTypeId(account.accountTypeId);
        accountForMtObject.setAccountType(account.accountType);
        accountForMtObject.setAccountGroup(account.accountGroup);
        accountForMtObject.setPlatform(account.platform);
        accountForMtObject.setCreateTime(account.createTime);
        accountForMtObject.setCreateTimeUtc(account.createTimeUtc);
        accountForMtObject.setAccountStatus(account.accountStatus);
        accountForMtObject.setLastLogin(account.lastLogin);
        accountForMtObject.setLastLoginUtc(account.lastLoginUtc);
        accountForMtObject.setLastOrder(account.lastOrder);
        accountForMtObject.setLastOrderUtc(account.lastOrderUtc);
        accountForMtObject.setBalance(account.balance);
        accountForMtObject.setCurrency(account.currency);
        accountForMtObject.setBalanceUsd(account.balanceUsd);
        accountForMtObject.setEquity(account.equity);
        accountForMtObject.setCredit(account.credit);
        accountForMtObject.setPnl(account.pnl);
        accountForMtObject.setLeverage(account.leverage);
        accountForMtObject.setMarginFree(account.marginFree);
        accountForMtObject.setIsRebateAccount(account.isRebateAccount);
        accountForMtObject.setRebateAccountNr(account.rebateAccountNr);
        accountForMtObject.setIbId(account.ibId);
        accountForMtObject.setpId(account.pId);
        accountForMtObject.setIsSwapFree(account.isSwapFree);
        accountForMtObject.setIsPamm(account.isPamm);
        accountForMtObject.setIsCent(account.isCent);
        accountForMtObject.setIsArchive(account.isArchive);
        accountForMtObject.setIsHidden(account.isHidden);
        accountForMtObject.setIsDel(account.isDel);
        accountForMtObject.setIsDeleted(account.isDeleted);
        accountForMtObject.setInternalComment(account.internalComment);
        accountForMtObject.setLastUpdated(account.lastUpdated);
        accountForMtObject.setIsTest(0);
        return accountForMtObject;
    }
}
