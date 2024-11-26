package helpers.data.rules.registrationRule;

import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.csTbConnectionTableV2.ConnectionTableEntry;
import businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObject;
import businessObjects.kafka.crmEvents.RegistrationEvent;
import helpers.data.ClientHelper;

import java.util.List;

public class RegistrationRuleData {
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public LnSessionParsedObject lnSessionParsedObject;
    public List<ConnectionTableEntry> connections;
    public List<CrmTbUserObject> connectedUsers;
    public RegistrationEvent registrationEvent;
    public List<BoClientFraudTypesObject> clientFraudTypes;

    public RegistrationRuleData() {
    }

    public RegistrationRuleData(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject, LnSessionParsedObject lnSessionParsedObject, List<ConnectionTableEntry> connections, List<CrmTbUserObject> connectedUsers, RegistrationEvent registrationEvent, List<BoClientFraudTypesObject> clientFraudTypes) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = crmTbUserObject;
        this.lnSessionParsedObject = lnSessionParsedObject;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
        this.registrationEvent = registrationEvent;
        this.clientFraudTypes = clientFraudTypes;
    }

    @Override
    public String toString() {
        return "RegistrationRuleData{" +
                "clientHelper=" + clientHelper +
                ", crmTbUserObject=" + crmTbUserObject +
                ", lnSessionParsedObject=" + lnSessionParsedObject +
                ", connections=" + connections +
                ", connectedUsers=" + connectedUsers +
                ", registrationEvent=" + registrationEvent +
                ", clientFraudTypes=" + clientFraudTypes +
                '}';
    }
}
