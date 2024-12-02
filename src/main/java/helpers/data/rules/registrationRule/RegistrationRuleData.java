package helpers.data.rules.registrationRule;

import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.csTbConnectionTableV3.ConnectionTableEntryV3;
import businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObject;
import businessObjects.db.clickhouse.mtTbUserTable.MtTbUserObject;
import businessObjects.kafka.crmEvents.RegistrationEvent;
import helpers.data.ClientHelper;

import java.util.List;

public class RegistrationRuleData {
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public LnSessionParsedObject lnSessionParsedObject;
    public List<ConnectionTableEntryV3> connections;
    public List<CrmTbUserObject> connectedUsers;
    public RegistrationEvent registrationEvent;
    public List<BoClientFraudTypesObject> clientFraudTypes;
    public MtTbUserObject mtTbUserObject;

    public RegistrationRuleData() {
    }

    public RegistrationRuleData(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject, LnSessionParsedObject lnSessionParsedObject, List<ConnectionTableEntryV3> connections, List<CrmTbUserObject> connectedUsers, RegistrationEvent registrationEvent, List<BoClientFraudTypesObject> clientFraudTypes, MtTbUserObject mtTbUserObject) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = crmTbUserObject;
        this.lnSessionParsedObject = lnSessionParsedObject;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
        this.registrationEvent = registrationEvent;
        this.clientFraudTypes = clientFraudTypes;
        this.mtTbUserObject = mtTbUserObject;
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
                ", mtTbUserObject=" + mtTbUserObject +
                '}';
    }
}
