package helpers.data.rules.registration_rule;

import business_objects.db.clickhouse.bo_client_fraud_types.BoClientFraudTypesObject;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.email_table.EmailTableEntry;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.db.clickhouse.session_id.SessionIdTableEntry;
import business_objects.kafka.crm_events.RegistrationEvent;
import helpers.data.ClientHelper;

import java.util.List;

public class RegistrationRuleData {
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public LnSessionParsedObject lnSessionParsedObject;
    public List<ConnectionTableEntry> connections;
    public List<CrmTbUserObject> connectedUsers;
    public List<ClientHelper> connectedClientHelpers;
    public RegistrationEvent registrationEvent;
    public List<BoClientFraudTypesObject> clientFraudTypes;
    public CrmTbAccountObject crmTbAccountObject;
    public List<SessionIdTableEntry> sessionIdTableEntries;
    public List<EmailTableEntry> emailTableEntries;

    public RegistrationRuleData() {
    }

    public RegistrationRuleData(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject,
            LnSessionParsedObject lnSessionParsedObject, List<ConnectionTableEntry> connections,
            List<CrmTbUserObject> connectedUsers, List<ClientHelper> connectedClientHelpers,
            RegistrationEvent registrationEvent, List<BoClientFraudTypesObject> clientFraudTypes,
            CrmTbAccountObject crmTbAccountObject, List<SessionIdTableEntry> sessionIdTableEntries,
            List<EmailTableEntry> emailTableEntries) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = crmTbUserObject;
        this.lnSessionParsedObject = lnSessionParsedObject;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
        this.connectedClientHelpers = connectedClientHelpers;
        this.registrationEvent = registrationEvent;
        this.clientFraudTypes = clientFraudTypes;
        this.crmTbAccountObject = crmTbAccountObject;
        this.sessionIdTableEntries = sessionIdTableEntries;
        this.emailTableEntries = emailTableEntries;
    }

    @Override
    public String toString() {
        return "RegistrationRuleData{" + "clientHelper=" + clientHelper + ", crmTbUserObject=" + crmTbUserObject + ", lnSessionParsedObject=" + lnSessionParsedObject + ", connections=" + connections + ", connectedUsers=" + connectedUsers + ", connectedClientHelpers=" + connectedClientHelpers + ", registrationEvent=" + registrationEvent + ", clientFraudTypes=" + clientFraudTypes + ", crmTbAccountObject=" + crmTbAccountObject + ", sessionIdTableEntries=" + sessionIdTableEntries + ", emailTableEntries=" + emailTableEntries + '}';
    }
}
