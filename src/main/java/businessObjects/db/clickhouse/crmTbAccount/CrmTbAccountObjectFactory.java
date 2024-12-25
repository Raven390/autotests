package businessObjects.db.clickhouse.crmTbAccount;

import helpers.data.ClientHelper;

public class CrmTbAccountObjectFactory {

    public static CrmTbAccountObject generateAccountByClient(ClientHelper client, Boolean isSwapFree) {
        int swapFree;
        String accountType = "";
        if (isSwapFree) {
            swapFree = 1;
            accountType = "Swap free";
        } else {
            swapFree = 0;
            accountType = "Standard";
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
                18,// serverIdSt
                "ST",// server
                1,// accountTypeId
                accountType,// accountType
                "M_SVS_0000_USD",// accountGroup
                "MT4",// platform
                "2022-11-16 05:30:00",// createTime
                "2022-11-16 05:30:00",// createTimeUtc
                "2022-11-16",// createDate
                "2022-11-16",// createDateUtc
                "Inactive",// accountStatus
                "2024-11-16 05:30:00",// last login
                "2024-11-16 05:30:00",// lastLoginUtc
                "2024-11-16 05:30:00",// lastOrder
                "2024-11-16 05:30:00",// lastOrderUtc
                1d,// balance
                "USD",// currency
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
                "Automation tests"// internalComment
        );
    }
}