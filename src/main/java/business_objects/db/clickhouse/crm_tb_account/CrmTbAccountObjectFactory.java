package business_objects.db.clickhouse.crm_tb_account;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import java.util.Currency;

import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getCurrentTimestampDbFormat;

public class CrmTbAccountObjectFactory {

    public static CrmTbAccountObject generateAccountByClient(ClientHelper client, Boolean isSwapFree) {
        int swapFree;
        String accountType = "";
        if (isSwapFree) {
            swapFree = 1;
            accountType = ACCOUNT_TYPE_STANDARD;
        } else {
            swapFree = 0;
            accountType = ACCOUNT_TYPE_SWAP_FREE;
        }
        return new CrmTbAccountObject(
                10,// sourceIdString
                5,// brandUid
                client.getBrand(),// brand
                client.getRegulator(), // regulator
                client.getUserId(), // userId
                client.getUcid(),// ucid
                client.getUid(), // uid
                client.getTradingAccount(), // account
                client.getServerId(), // serverIdSt
                "ST",// server
                1,// accountTypeId
                accountType,// accountType
                "M_SVS_0000_USD",// accountGroup
                PLATFORM_MT_4,// platform
                "2022-11-16 05:30:00",// createTime
                "2022-11-16 05:30:00",// createTimeUtc
                "2022-11-16",// createDate
                "2022-11-16",// createDateUtc
                ACCOUNT_STATUS_INACTIVE,// accountStatus
                "2024-11-16 05:30:00",// last login
                "2024-11-16 05:30:00",// lastLoginUtc
                "2024-11-16 05:30:00",// lastOrder
                "2024-11-16 05:30:00",// lastOrderUtc
                1d,// balance
                Currency.getInstance("USD").getCurrencyCode(),// currency
                1d,// balanceUsd
                2d,// equity
                3d,// credit
                4d,// pnl
                4,// leverage
                5d,// marginFree
                0,// isRebateAccount
                0,// rebateAccountNr
                943_793,// ibId
                943_793,// pId
                swapFree,// isSwapFree
                0,// isPamm
                0,// isCent
                1,// isArchive
                0,// isHidden
                0,// isDel
                0,// isDelete
                "2024-07-09 02:21:39",// lastUpdated
                COMMENT_AUTOMATION_TESTS// internalComment
        );
    }

    @Step("Generate account data in crm___tb_account table")
    public static CrmTbAccountObject generateCrmTbAccountData(ClientHelper client) {
        return new CrmTbAccountObject(10, 5, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), getRandomUuidString(), client.getTradingAccount(), client.getServerId(), "server1", 1, ACCOUNT_TYPE_STANDARD, ACCOUNT_GROUP_S_VFX_EUR, PLATFORM_MT_4, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentDate(), getCurrentDate(), ACCOUNT_STATUS_ACTIVE, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 0.00, Currency.getInstance("USD").getCurrencyCode(), 100.0, 1.0, 2.0, 1.0, 3, 9.0, 0, 0, getRandomIntPositive(), getRandomIntPositive(), 0, 0, 0, 0, 0, 0, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }

    @Step("Generate additional account data in crm___tb_account table")
    public static CrmTbAccountObject generateAdditionalCrmTbAccountData(ClientHelper client) {
        return new CrmTbAccountObject(10, 5, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), getRandomUuidString(), client.getTradingAccount2(), client.getServerId(), "server1", 1, ACCOUNT_TYPE_STANDARD, ACCOUNT_GROUP_S_VFX_EUR, PLATFORM_MT_4, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentDate(), getCurrentDate(), ACCOUNT_STATUS_ACTIVE, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 0.00, Currency.getInstance("USD").getCurrencyCode(), 100.0, 1.0, 2.0, 1.0, 3, 9.0, 0, 0, getRandomIntPositive(), getRandomIntPositive(), 0, 0, 0, 0, 0, 0, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }

    public static CrmTbAccountObject generateCrmTbAccountDataForUi(ClientHelper client) {
        return new CrmTbAccountObject(10, 5, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), getRandomUuidString(), client.getTradingAccount(), client.getServerId(), "server1", 1, ACCOUNT_TYPE_STANDARD, ACCOUNT_GROUP_S_VFX_EUR, PLATFORM_MT_4, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentDate(), getCurrentDate(), ACCOUNT_STATUS_ACTIVE, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 101.5, Currency.getInstance("USD").getCurrencyCode(), 102.7, 11.1, 99.9, 22.2, 3, 7.77, 0, 0, getRandomIntPositive(), getRandomIntPositive(), 0, 0, 0, 0, 0, 0, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }

    public static CrmTbAccountObject generateAdditionalCrmTbAccountDataForUi(ClientHelper client) {
        return new CrmTbAccountObject(10, 5, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), getRandomUuidString(), client.getTradingAccount2(), 11, "server1", 1, ACCOUNT_TYPE_STANDARD, ACCOUNT_GROUP_S_VFX_EUR, PLATFORM_MT_4, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentDate(), getCurrentDate(), ACCOUNT_STATUS_ACTIVE, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 101.5, Currency.getInstance("USD").getCurrencyCode(), 102.7, 11.1, 99.9, 22.2, 3, 7.77, 0, 0, getRandomIntPositive(), getRandomIntPositive(), 0, 0, 0, 0, 0, 0, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }

    public static CrmTbAccountObject generateStaticCrmTbAccountActive(ClientHelper client) {
        return new CrmTbAccountObject(10, 5, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getUid(), client.getTradingAccount(), client.getServerId(), "server1", 1, ACCOUNT_TYPE_STANDARD, ACCOUNT_GROUP_S_VFX_EUR, PLATFORM_MT_4, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, "2015-12-29", "2015-12-29", ACCOUNT_STATUS_ACTIVE, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, 101.5, Currency.getInstance("USD").getCurrencyCode(), 102.7, 11.1, 99.9, 22.2, 3, 7.77, 0, 0, 42, 42, 0, 0, 0, 0, 0, 0, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }

    public static CrmTbAccountObject generateAdditionalStaticCrmTbAccountActive(ClientHelper client) {
        return new CrmTbAccountObject(10, 5, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getUid(), client.getTradingAccount2(), client.getServerId(), "server1", 1, ACCOUNT_TYPE_STANDARD, ACCOUNT_GROUP_S_VFX_EUR, PLATFORM_MT_4, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, "2015-12-29", "2015-12-29", ACCOUNT_STATUS_ACTIVE, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, 101.5, Currency.getInstance("USD").getCurrencyCode(), 102.7, 11.1, 99.9, 22.2, 3, 7.77, 0, 0, 42, 42, 0, 0, 0, 0, 0, 0, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }

    public static CrmTbAccountObject generateStaticCrmTbAccountInactive(ClientHelper client) {
        return new CrmTbAccountObject(10, 5, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getUid(), client.getTradingAccount(), client.getServerId(), "server1", 1, ACCOUNT_TYPE_STANDARD, ACCOUNT_GROUP_S_VFX_EUR, PLATFORM_MT_4, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, "2015-12-29", "2015-12-29", ACCOUNT_STATUS_INACTIVE, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, TIME_2024_2024_12_29_14_59_30_084000000, 101.5, Currency.getInstance("USD").getCurrencyCode(), 102.7, 11.1, 99.9, 22.2, 3, 7.77, 0, 0, 42, 42, 0, 0, 0, 0, 0, 0, 0, TIME_2024_2024_12_29_14_59_30_084000000, COMMENT_AUTOMATION_TESTS);
    }

}