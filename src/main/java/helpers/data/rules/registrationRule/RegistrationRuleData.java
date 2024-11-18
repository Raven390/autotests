package helpers.data.rules.registrationRule;

import businessObjects.db.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.csTbConnectionTableV2.ConnectionTableEntry;
import businessObjects.db.lnSessionParsedTable.LnSessionParsedObject;
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

    public RegistrationRuleData() {
    }

    public RegistrationRuleData(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject, LnSessionParsedObject lnSessionParsedObject, List<ConnectionTableEntry> connections, List<CrmTbUserObject> connectedUsers, RegistrationEvent registrationEvent) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = crmTbUserObject;
        this.lnSessionParsedObject = lnSessionParsedObject;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
        this.registrationEvent = registrationEvent;
    }

    @Override
    public String toString() {
        return "RegistrationRuleData{" +
                "clientHelper=" + clientHelper +
                ", crmTbUserObject=" + crmTbUserObject +
                ", lnSessionParsedObject=" + lnSessionParsedObject +
                ", connections=" + connections +
                ", connectedUsers=" + connectedUsers +
                ", registrationDbEvent=" + registrationEvent +
                '}';
    }
}
