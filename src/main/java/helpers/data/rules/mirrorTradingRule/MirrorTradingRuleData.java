package helpers.data.rules.mirrorTradingRule;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.csTbConnectionTableV2.ConnectionTableEntryV2;
import businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObject;
import helpers.data.ClientHelper;

import java.util.List;

public class MirrorTradingRuleData {
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public LnSessionParsedObject lnSessionParsedObject;
    public List<ConnectionTableEntryV2> connections;
    public List<CrmTbUserObject> connectedUsers;

    public MirrorTradingRuleData() {
    }

    public MirrorTradingRuleData(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject, LnSessionParsedObject lnSessionParsedObject, List<ConnectionTableEntryV2> connections, List<CrmTbUserObject> connectedUsers) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = crmTbUserObject;
        this.lnSessionParsedObject = lnSessionParsedObject;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
    }

    @Override
    public String toString() {
        return "RegistrationRuleData{" +
                "clientHelper=" + clientHelper +
                ", crmTbUserObject=" + crmTbUserObject +
                ", lnSessionParsedObject=" + lnSessionParsedObject +
                ", connections=" + connections +
                ", connectedUsers=" + connectedUsers +
                '}';
    }
}
