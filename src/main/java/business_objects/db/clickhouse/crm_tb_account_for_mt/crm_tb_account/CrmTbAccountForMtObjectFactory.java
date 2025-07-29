package business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import java.util.Currency;

import static utils.Constants.*;
import static utils.Utils.*;

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
                10,// sourceIdString
                5,// brandUid
                client.getBrand(),// brand
                client.getRegulator(), // regulator
                client.getUserId(), // userId
                client.getUcid(),// ucid
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
}