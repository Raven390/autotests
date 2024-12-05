package helpers.data.rules.mirrorTradingRule;

import businessObjects.db.clickhouse.aggrCreditEquityRate.AggrCreditEquityRateObject;
import businessObjects.db.clickhouse.aggrMirrorAccountsByTrades.AggrMirrorAccountsByTradesObject;
import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObject;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.crmTbWithdrawalTable.CrmTbWithdrawalObject;
import businessObjects.db.clickhouse.csTbConnectionTableV3.ConnectionTableEntryV3;
import businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObject;
import businessObjects.db.clickhouse.mtMt5DealsTable.Mt5DealsObject;
import businessObjects.db.clickhouse.mtTbCreditsTable.MtTbCreditsObject;
import businessObjects.db.clickhouse.mtTbUserTable.MtTbUserObject;
import businessObjects.kafka.mtEvents.CloseTradeMtEvent;
import helpers.data.ClientHelper;

import java.util.List;

public class MirrorTradingRuleData {
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public LnSessionParsedObject lnSessionParsedObjectRegistration;
    public LnSessionParsedObject lnSessionParsedObjectLogin;
    public List<ConnectionTableEntryV3> connections;
    public List<CrmTbUserObject> connectedUsers;
    public CloseTradeMtEvent closeTradeMtEvent;
    public List<BoClientFraudTypesObject> clientFraudTypes;
    public MtTbUserObject mtTbUserObject;
    public List<MtTbCreditsObject> mtTbCreditsObjects;
    public List<CrmTbWithdrawalObject> crmTbWithdrawalObjects;
    public List<CrmTbDepositObject> crmTbDepositObjects;
    public List<CrmTbBonusObject> crmTbBonusObjects;
    public List<Mt5DealsObject> mt5DealsObjects;
    public AggrCreditEquityRateObject aggrCreditEquityRate;
    public AggrMirrorAccountsByTradesObject aggrMirrorAccountsByTrades;

    public MirrorTradingRuleData() {
    }

    public MirrorTradingRuleData(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject,
            LnSessionParsedObject lnSessionParsedObjectRegistration, LnSessionParsedObject lnSessionParsedObjectLogin,
            List<ConnectionTableEntryV3> connections, List<CrmTbUserObject> connectedUsers,
            CloseTradeMtEvent closeTradeMtEvent, List<BoClientFraudTypesObject> clientFraudTypes,
            MtTbUserObject mtTbUserObject, List<MtTbCreditsObject> mtTbCreditsObjects,
            List<CrmTbWithdrawalObject> crmTbWithdrawalObjects, List<CrmTbDepositObject> crmTbDepositObjects,
            List<CrmTbBonusObject> crmTbBonusObjects, List<Mt5DealsObject> mt5DealsObjects,
            AggrCreditEquityRateObject aggrCreditEquityRate,
            AggrMirrorAccountsByTradesObject aggrMirrorAccountsByTrades) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = crmTbUserObject;
        this.lnSessionParsedObjectRegistration = lnSessionParsedObjectRegistration;
        this.lnSessionParsedObjectLogin = lnSessionParsedObjectLogin;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
        this.closeTradeMtEvent = closeTradeMtEvent;
        this.clientFraudTypes = clientFraudTypes;
        this.mtTbUserObject = mtTbUserObject;
        this.mtTbCreditsObjects = mtTbCreditsObjects;
        this.crmTbWithdrawalObjects = crmTbWithdrawalObjects;
        this.crmTbDepositObjects = crmTbDepositObjects;
        this.crmTbBonusObjects = crmTbBonusObjects;
        this.mt5DealsObjects = mt5DealsObjects;
        this.aggrCreditEquityRate = aggrCreditEquityRate;
        this.aggrMirrorAccountsByTrades = aggrMirrorAccountsByTrades;
    }

    @Override
    public String toString() {
        return "MirrorTradingRuleData{" + "clientHelper=" + clientHelper + ", crmTbUserObject=" + crmTbUserObject + ", lnSessionParsedObjectRegistration=" + lnSessionParsedObjectRegistration + ", lnSessionParsedObjectLogin=" + lnSessionParsedObjectLogin + ", connections=" + connections + ", connectedUsers=" + connectedUsers + ", closeTradeMtEvent=" + closeTradeMtEvent + ", clientFraudTypes=" + clientFraudTypes + ", mtTbUserObject=" + mtTbUserObject + ", mtTbCreditsObjects=" + mtTbCreditsObjects + ", crmTbWithdrawalObjects=" + crmTbWithdrawalObjects + ", crmTbDepositObjects=" + crmTbDepositObjects + ", crmTbBonusObjects=" + crmTbBonusObjects + ", mt5DealsObjects=" + mt5DealsObjects + ", aggrCreditEquityRate=" + aggrCreditEquityRate + ", aggrMirrorAccountsByTrades=" + aggrMirrorAccountsByTrades + '}';
    }
}
