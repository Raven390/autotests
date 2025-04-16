package utils.demo_helpers;


import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static helpers.database.BoHelper.cleanUserFraudsBo;
import static helpers.database.BoHelper.createUserFraudsBo;
import static helpers.database.DbHelper.*;
import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;
import static utils.demo_helpers.ClickHouse.createSimpleClient;

public class ConnectionSearch {

    private static String ucid1 = "infinox-424302";
    private static String ucid2 = "infinox-424303";
    private static String ucid3 = "infinox-424304";
    private static String ucid4 = "infinox-424305";
    private static String ucid5 = "infinox-424306";
    private static String ucid6 = "infinox-424307";
    private static String ucid7 = "infinox-424308";
    private static String ucid8 = "infinox-424309";
    private static String ucid9 = "infinox-424310";
    private static String ucid10 = "infinox-424311";

    public static void createDemoDataConnectionSearch(String userFromUcid) throws Exception {
        Allure.step("create demo data to CS tab in BO");

        createSimpleClient(ucid1, "Leopold", "Catz");
        createSimpleClient(ucid2, "Peter", "Lean");
        createSimpleClient(ucid3, "Paul", "Nixon");
        createSimpleClient(ucid4, "Valentino", "Petrucchi");
        createSimpleClient(ucid5, "Jacob", "Peters");
        createSimpleClient(ucid6, "Vernon", "Small");
        createSimpleClient(ucid7, "Jerry", "Jumpman");
        createSimpleClient(ucid8, "Peter", "Leopold");
        createSimpleClient(ucid9, "Paul", "Fakeman");
        createSimpleClient(ucid10, "Chris", "Nowhere");

        String modificator = getRandomIntPositive().toString();

        ConnectionTableEntry connectionTableEntry1 = new ConnectionTableEntry(userFromUcid, ucid1, CONNECTION_TYPE_SAME_IDENTITY, 0.01, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_EMAIL, "kot.leopold@soyuzmultfilm.com", "cat.leopold@soyuzmultfilm.com", CONNECTION_TYPE_RELATION_TYPE_SIMILAR)), getCurrentTimestampDbFormat());
        ConnectionTableEntry connectionTableEntry2 = new ConnectionTableEntry(userFromUcid, ucid2, CONNECTION_TYPE_SAME_IDENTITY, 0.17, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DOCUMENT, "1271566157", "1271566157", CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat());
        ConnectionTableEntry connectionTableEntry3 = new ConnectionTableEntry(userFromUcid, ucid3, CONNECTION_TYPE_SAME_IDENTITY, 0.34, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, "lgAndr121276 + modificator", "lgAndr121276 + modificator", CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat());
        ConnectionTableEntry connectionTableEntry4 = new ConnectionTableEntry(ucid1, ucid4, CONNECTION_TYPE_SAME_IDENTITY, 0.5, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PHONE, "+46700975436", "+46700975435", CONNECTION_TYPE_RELATION_TYPE_SIMILAR)), getCurrentTimestampDbFormat());
        ConnectionTableEntry connectionTableEntry5 = new ConnectionTableEntry(ucid1, ucid5, CONNECTION_TYPE_SAME_IDENTITY, 0.66, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_IP, CONNECTION_SEARCH_DATA_IP4, CONNECTION_SEARCH_DATA_IP5 + modificator, CONNECTION_TYPE_RELATION_TYPE_SIMILAR)), getCurrentTimestampDbFormat());
        ConnectionTableEntry connectionTableEntry6 = new ConnectionTableEntry(ucid1, ucid6, CONNECTION_TYPE_SAME_IDENTITY, 0.83, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_SESSION, "1298u98ds98t" + modificator, "1298u98ds98t" + modificator, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat());
        ConnectionTableEntry connectionTableEntry7 = new ConnectionTableEntry(ucid3, ucid7, CONNECTION_TYPE_SAME_IDENTITY, 0.82, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_EMAIL, "ETUU6t2aFDAdy7mnCBxhbr+UJiA7TDi2CRUvpqQ4/kw=", "ETUU6t2aFDAdy7mnCBxhbr+UJiA7TDi2CRUvpqQ4/kw=", CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat());
        ConnectionTableEntry connectionTableEntry8 = new ConnectionTableEntry(ucid2, ucid8, CONNECTION_TYPE_SAME_IDENTITY, 0.34, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PHONE, "x3UCHwqCv3LDMrLP5XtrbA==", "x3UCHwqCv3LDMrLP5XtrbA==", CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat());
        ConnectionTableEntry connectionTableEntry9 = new ConnectionTableEntry(ucid6, ucid9, CONNECTION_TYPE_SAME_IDENTITY, 0.17, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_EMAIL, "4uxgXPyW5dBcjcXSPMcrsr/OWk7qylxy", "4uxgXPyW5dBcjcXSPMcrsr/OWk7qylxy", CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat());
        ConnectionTableEntry connectionTableEntry10 = new ConnectionTableEntry(ucid7, ucid10, CONNECTION_TYPE_SAME_IDENTITY, 0.17, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_EMAIL, "yjpB7jkXAMId8vRlXynlTk8cdqTtCvP0", "7ZoSwJTKIG+OoGHfBgxdv7do7rC3aFRx", CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat());

        List<ConnectionTableEntry> list = new ArrayList<>();
        list.add(connectionTableEntry1);
        list.add(connectionTableEntry2);
        list.add(connectionTableEntry3);
        list.add(connectionTableEntry4);
        list.add(connectionTableEntry5);
        list.add(connectionTableEntry6);
        list.add(connectionTableEntry7);
        list.add(connectionTableEntry8);
        list.add(connectionTableEntry9);
        list.add(connectionTableEntry10);

        insertObjectsToDb("vindex_test.connection_table", list);
        startSshTunnel();

        //first floor
        createSimpleAlert(ucid2, "MARKET_MANIPULATION");
        createUserFraudsBo(ucid2, 3);

        //second floor
        createSimpleAlert(ucid5, "CPA");
        createUserFraudsBo(ucid5, 7);
//        setGeneralRestrictionApi(ucid5, "03"); todo implement for trade

        createSimpleAlert(ucid7, "HEDGING");
        createUserFraudsBo(ucid7, 1);
//        setGeneralRestrictionApi(ucid7, "03"); todo implement for trade

        createSimpleAlert(ucid8, "HEDGING");

        //third floor
        createSimpleAlert(ucid9, "POTENTIAL_ABUSE");
        createUserFraudsBo(ucid9, 14);
//        setGeneralRestrictionApi(ucid5, "03"); todo implement for trade

        stopSshTunnel();
    }

    public static void cleanDemoDataConnectionSearch() throws Exception {
        cleanUserFraudsBo(ucid2);
        cleanUserFraudsBo(ucid5);
        cleanUserFraudsBo(ucid7);
        cleanUserFraudsBo(ucid8);
        cleanUserFraudsBo(ucid9);
        deleteEntryFromDb("vindex_test.connection_table", "user_to in (" + ucid1 + ", " + ucid2 + ", " + ucid3 + ", " + ucid4 + ", " + ucid5 + ", " + ucid6 + ", " + ucid7 + ", " + ucid8 + ", " + ucid9 + ", " + ucid10 + ")");
    }

    @Test
    void createDemoDataConnectionSearchTest() throws Exception {
        createDemoDataConnectionSearch("vantage-518248");
    }
}
