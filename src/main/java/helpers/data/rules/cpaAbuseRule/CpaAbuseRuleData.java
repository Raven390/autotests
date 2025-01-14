package helpers.data.rules.cpaAbuseRule;

import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.mtTbUser.MtTbUserObject;
import businessObjects.kafka.crmEvents.WithdrawalEvent;
import helpers.data.ClientHelper;

import java.util.List;

public class CpaAbuseRuleData {
    public WithdrawalEvent withdrawalEvent;
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public List<ConnectionTableEntry> connections;
    public List<CrmTbUserObject> connectedUsers;
    public List<ClientHelper> connectedClientHelpers;
    public List<BoClientFraudTypesObject> clientFraudTypes;
    public MtTbUserObject mtTbUserObject;

    public CpaAbuseRuleData() {
    }

    public CpaAbuseRuleData(WithdrawalEvent withdrawalEvent, ClientHelper clientHelper,
            List<ConnectionTableEntry> connections,
            List<CrmTbUserObject> connectedUsers, List<ClientHelper> connectedClientHelpers,
            List<BoClientFraudTypesObject> clientFraudTypes, CrmTbUserObject crmTbUserObject
    ) {
        this.withdrawalEvent = withdrawalEvent;
        this.clientHelper = clientHelper;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
        this.connectedClientHelpers = connectedClientHelpers;
        this.clientFraudTypes = clientFraudTypes;
        this.crmTbUserObject = crmTbUserObject;
    }

    @Override
    public String toString() {
        return "MirrorTradingRuleData{" + "clientHelper=" + clientHelper + ", connections=" + connections + ", connectedUsers=" + connectedUsers + ", withdrawalDbEvent=" + withdrawalEvent + ", clientFraudTypes=" + clientFraudTypes + '}';
    }
}
