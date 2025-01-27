package helpers.data.rules.mirrorTradingRule;

import businessObjects.db.clickhouse.aggrCreditEquityRate.AggrCreditEquityRateObject;
import businessObjects.db.clickhouse.aggrMirrorAccountsByTrades.MirrorLoginObject;
import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObject;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.db.clickhouse.mtTbCredits.MtTbCreditsObject;
import businessObjects.kafka.mtEvents.CloseTradeMtEvent;
import helpers.data.ClientHelper;

import java.util.List;

public class MirrorTradingRuleData {
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public LnSessionParsedObject lnSessionParsedObjectRegistration;
    public LnSessionParsedObject lnSessionParsedObjectLogin;
    public List<ConnectionTableEntry> connections;
    public List<CrmTbUserObject> connectedUsers;
    public CloseTradeMtEvent closeTradeMtEvent;
    public List<BoClientFraudTypesObject> clientFraudTypes;
    public CrmTbAccountObject crmTbAccountObject;
    public List<MtTbCreditsObject> mtTbCreditsObjects;
    public List<CrmTbWithdrawalObject> crmTbWithdrawalObjects;
    public List<CrmTbDepositObject> crmTbDepositObjects;
    public List<CrmTbBonusObject> crmTbBonusObjects;
    public List<Mt5DealsCoercedObject> mt5DealsObjects;
    public AggrCreditEquityRateObject aggrCreditEquityRate;
    public MirrorLoginObject aggrMirrorAccountsByTrades;

    public MirrorTradingRuleData() {
    }

    public MirrorTradingRuleData(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject,
            LnSessionParsedObject lnSessionParsedObjectRegistration, LnSessionParsedObject lnSessionParsedObjectLogin,
            List<ConnectionTableEntry> connections, List<CrmTbUserObject> connectedUsers,
            CloseTradeMtEvent closeTradeMtEvent, List<BoClientFraudTypesObject> clientFraudTypes,
            CrmTbAccountObject crmTbAccountObject, List<MtTbCreditsObject> mtTbCreditsObjects,
            List<CrmTbWithdrawalObject> crmTbWithdrawalObjects, List<CrmTbDepositObject> crmTbDepositObjects,
            List<CrmTbBonusObject> crmTbBonusObjects, List<Mt5DealsCoercedObject> mt5DealsObjects,
            AggrCreditEquityRateObject aggrCreditEquityRate,
            MirrorLoginObject aggrMirrorAccountsByTrades) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = crmTbUserObject;
        this.lnSessionParsedObjectRegistration = lnSessionParsedObjectRegistration;
        this.lnSessionParsedObjectLogin = lnSessionParsedObjectLogin;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
        this.closeTradeMtEvent = closeTradeMtEvent;
        this.clientFraudTypes = clientFraudTypes;
        this.crmTbAccountObject = crmTbAccountObject;
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
        return "MirrorTradingRuleData{" + "clientHelper=" + clientHelper + ", crmTbUserObject=" + crmTbUserObject + ", lnSessionParsedObjectRegistration=" + lnSessionParsedObjectRegistration + ", lnSessionParsedObjectLogin=" + lnSessionParsedObjectLogin + ", connections=" + connections + ", connectedUsers=" + connectedUsers + ", closeTradeMtEvent=" + closeTradeMtEvent + ", clientFraudTypes=" + clientFraudTypes + ", mtTbUserObject=" + crmTbAccountObject + ", mtTbCreditsObjects=" + mtTbCreditsObjects + ", crmTbWithdrawalObjects=" + crmTbWithdrawalObjects + ", crmTbDepositObjects=" + crmTbDepositObjects + ", crmTbBonusObjects=" + crmTbBonusObjects + ", mt5DealsObjects=" + mt5DealsObjects + ", aggrCreditEquityRate=" + aggrCreditEquityRate + ", aggrMirrorAccountsByTrades=" + aggrMirrorAccountsByTrades + '}';
    }
}
