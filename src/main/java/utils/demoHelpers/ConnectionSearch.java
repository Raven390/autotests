package utils.demoHelpers;


import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static helpers.database.BoHelper.cleanUserFraudsDb;
import static helpers.database.BoHelper.createUserFraudsDb;
import static helpers.database.DbHelper.*;
import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
import static utils.Utils.getRandomIntPositive;
import static utils.demoHelpers.ClickHouse.createSimpleClient;


public class ConnectionSearch {

    @Step("create test data to CS tab in BO")
    public static void createTestDataConnectionSearch() throws Exception {
        Allure.step("create test data to CS tab in BO");
        String statement = "INSERT INTO vindex_test.connection_table" + "(user_from, user_to, degree_connection, connection_score, connection_info)" + "VALUES('infinox-424201', 'infinox-424202', 'Same Identity', 0.01, '[{\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"charleskatako5@gmail.com\", \"sourceAttributeValue\": \"charlesabako@gmail.com\", \"relationType\": \"similar\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424203', 'Same Identity', 0.16, '[{\"connectionAttributeName\": \"phone\", \"connectionAttributeValue\": \"5551829\", \"sourceAttributeValue\": \"qI5rXCKvGkpqOcNZPXKLNg==\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424204', 'Same Identity', 0.17, '[{\"connectionAttributeName\": \"device\", \"connectionAttributeValue\": \"6f198e5dae004f009553631a400e4c38\", \"sourceAttributeValue\": \"6f198e5dae004f009553631a400e4c38\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424205', 'Same Identity', 0.33, '[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"TPUpcw55c1QgBLTck456vTinLe11X9nj\", \"sourceAttributeValue\": \"TPUpcw55c1QgBLTckVyYW9vTinLe11X9nj\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424206', 'Same Network', 0.34, '[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"testomattos\", \"sourceAttributeValue\": \"TPUpcw55c1QgBLTckVyYW9vTinLe11X9nj\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424207', 'Same Identity', 0.49, '[{\"connectionAttributeName\": \"device\", \"connectionAttributeValue\": \"testDevices\", \"sourceAttributeValue\": \"TPUpcw55c1QgBLTckVyYW9vTinLe11X9nj\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424208', 'Same Identity', 0.5, '[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"TPUpcw55c1QgBLTck456vTinLe11X9nj\", \"sourceAttributeValue\": \"TPUpcw55c1QgBLTckVyYW9vTinLe11X9nj\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424209', 'Same Identity', 0.66, '[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"TPUpcw55c1QgBLTck456vTinLe11X9nj\", \"sourceAttributeValue\": \"TPUpcw55c1QgBLTckVyYW9vTinLe11X9nj\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424210', 'Same Person', 0.67, '[{\"connectionAttributeName\": \"device\", \"connectionAttributeValue\": \"testDevices1\", \"sourceAttributeValue\": \"30303bee5a4b4133ad16f9fb48f23585\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424211', 'Same Identity', 0.83, '[{\"connectionAttributeName\": \"ip\", \"connectionAttributeValue\": \"45.249.87.7\", \"sourceAttributeValue\": \"45.249.86.7\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"device\", \"connectionAttributeValue\": \"6f1d8424e9834ff989a028dc800f40a8\", \"sourceAttributeValue\": \"6f1d8424e9834ff989a028dc800f40a8\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424201', 'infinox-424212', 'Same Identity', 0.84, '[{\"connectionAttributeName\": \"digital\", \"connectionAttributeValue\": \"5d709c8f4564860b06037bf6966a37f\", \"sourceAttributeValue\": \"5d709c8fc40c4860b06737bf6966a37f\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"YO5zqombFcKm2KlaPJFwz29o9kWHhVUI\", \"sourceAttributeValue\": \"YO5zqombFcKm2KlaPJFwz29o9kWHhVUI\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"lamdoanforex@gmail.com\", \"sourceAttributeValue\": \"sulaimanforex@hotmail.com\", \"relationType\": \"similar\"}, {\"connectionAttributeName\": \"phone\", \"connectionAttributeValue\": \"z/bMEgqOYkTlEmHTEhFPuQ==\", \"sourceAttributeValue\": \"z/bMEgqOYkTlEmHTEhFPuQ==\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424210', 'infinox-424213', 'Same Identity', 0.84, '[{\"connectionAttributeName\": \"digital\", \"connectionAttributeValue\": \"5d709c8f4564860b06037bf6966a37f\", \"sourceAttributeValue\": \"5d709c8fc40c4860b06737bf6966a37f\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"YO5zqombFcKm2KlaPJFwz29o9kWHhVUI\", \"sourceAttributeValue\": \"YO5zqombFcKm2KlaPJFwz29o9kWHhVUI\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"lamdoanforex@gmail.com\", \"sourceAttributeValue\": \"sulaimanforex@hotmail.com\", \"relationType\": \"similar\"}, {\"connectionAttributeName\": \"phone\", \"connectionAttributeValue\": \"z/bMEgqOYkTlEmHTEhFPuQ==\", \"sourceAttributeValue\": \"z/bMEgqOYkTlEmHTEhFPuQ==\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424213', 'infinox-424214', 'Same Identity', 0.84, '[{\"connectionAttributeName\": \"digital\", \"connectionAttributeValue\": \"5d709c8f4564860b06037bf6966a37f\", \"sourceAttributeValue\": \"5d709c8fc40c4860b06737bf6966a37f\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"YO5zqombFcKm2KlaPJFwz29o9kWHhVUI\", \"sourceAttributeValue\": \"YO5zqombFcKm2KlaPJFwz29o9kWHhVUI\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"lamdoanforex@gmail.com\", \"sourceAttributeValue\": \"sulaimanforex@hotmail.com\", \"relationType\": \"similar\"}, {\"connectionAttributeName\": \"phone\", \"connectionAttributeValue\": \"z/bMEgqOYkTlEmHTEhFPuQ==\", \"sourceAttributeValue\": \"z/bMEgqOYkTlEmHTEhFPuQ==\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424213', 'infinox-424215', 'Same Identity', 0.84, '[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"TPUpcw55c1QgBL5469vT3nLe11X9nj\", \"sourceAttributeValue\": \"TPUpcw55c1QgBLTckVyYW9vTinLe11X9nj\", \"relationType\": \"exact\"}]');\n" + "INSERT INTO vindex_test.connection_table\n" + "(user_from, user_to, degree_connection, connection_score, connection_info)\n" + "VALUES('infinox-424213', 'infinox-424216', 'Same Identity', 0.84, '[{\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"charleskatako6@gmail.com\", \"sourceAttributeValue\": \"charlesabako@gmail.com\", \"relationType\": \"similar\"}]');\n";
        executeQueryToDb(DbName.CLICKHOUSE, statement);
    }

    public static void createDemoDataConnectionSearch(String userFromUcid) throws Exception {
        Allure.step("create demo data to CS tab in BO");
        Faker faker = new Faker();

        String fame = faker.name().firstName();

        createSimpleClient("infinox-424302", "Leopold", "Catz");
        createSimpleClient("infinox-424303", "Peter", "Lean");
        createSimpleClient("infinox-424304", "Paul", "Nixon");
        createSimpleClient("infinox-424305", "Valentino", "Petrucchi");
        createSimpleClient("infinox-424306", "Jacob", "Peters");
        createSimpleClient("infinox-424307", "Vernon", "Small");
        createSimpleClient("infinox-424308", "Jerry", "Jumpman");
        createSimpleClient("infinox-424309", "Peter", "Leopold");
        createSimpleClient("infinox-424310", "Paul", "Fakeman");
        createSimpleClient("infinox-424311", "Chris", "Nowhere");

        String modificator = getRandomIntPositive().toString();

        ConnectionTableEntry connectionTableEntry1 = new ConnectionTableEntry(userFromUcid, "infinox-424302", "Same Identity", 0.01, "[{\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"kot.leopold@soyuzmultfilm.com\", \"sourceAttributeValue\": \"cat.leopold@soyuzmultfilm.com\", \"relationType\": \"similar\"}]");
        ConnectionTableEntry connectionTableEntry2 = new ConnectionTableEntry(userFromUcid, "infinox-424303", "Same Identity", 0.17, "[{\"connectionAttributeName\": \"document\", \"connectionAttributeValue\": \"1271566157\", \"sourceAttributeValue\": \"1271566157\", \"relationType\": \"exact\"}]");
        ConnectionTableEntry connectionTableEntry3 = new ConnectionTableEntry(userFromUcid, "infinox-424304", "Same Identity", 0.34, "[{\"connectionAttributeName\": \"device\", \"connectionAttributeValue\": \"lgAndr121276" + modificator + "\", \"sourceAttributeValue\": \"lgAndr121276" + modificator + "\", \"relationType\": \"exact\"}]");

        ConnectionTableEntry connectionTableEntry4 = new ConnectionTableEntry("infinox-424302", "infinox-424305", "Same Identity", 0.5, "[{\"connectionAttributeName\": \"phone\", \"connectionAttributeValue\": \"+46700975436\", \"sourceAttributeValue\": \"+46700975435\", \"relationType\": \"similar\"}]");
        ConnectionTableEntry connectionTableEntry5 = new ConnectionTableEntry("infinox-424302", "infinox-424306", "Same Identity", 0.66, "[{\"connectionAttributeName\": \"ip\", \"connectionAttributeValue\": \"124.12.12.42\", \"sourceAttributeValue\": \"124.12.12.46" + modificator + "\", \"relationType\": \"similar\"}]");
        ConnectionTableEntry connectionTableEntry6 = new ConnectionTableEntry("infinox-424302", "infinox-424307", "Same Identity", 0.83, "[{\"connectionAttributeName\": \"session\", \"connectionAttributeValue\": \"1298u98ds98t" + modificator + "\", \"sourceAttributeValue\": \"1298u98ds98t" + modificator + "\", \"relationType\": \"exact\"}]");
        ConnectionTableEntry connectionTableEntry7 = new ConnectionTableEntry("infinox-424304", "infinox-424308", "Same Identity", 0.82, "[{\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"ETUU6t2aFDAdy7mnCBxhbr+UJiA7TDi2CRUvpqQ4/kw=\", \"sourceAttributeValue\": \"ETUU6t2aFDAdy7mnCBxhbr+UJiA7TDi2CRUvpqQ4/kw=\", \"relationType\": \"exact\"}]");
        ConnectionTableEntry connectionTableEntry8 = new ConnectionTableEntry("infinox-424303", "infinox-424309", "Same Identity", 0.34, "[{\"connectionAttributeName\": \"phone\", \"connectionAttributeValue\": \"x3UCHwqCv3LDMrLP5XtrbA==\", \"sourceAttributeValue\": \"x3UCHwqCv3LDMrLP5XtrbA==\", \"relationType\": \"exact\"}]");

        ConnectionTableEntry connectionTableEntry9 = new ConnectionTableEntry("infinox-424307", "infinox-424310", "Same Identity", 0.17, "[{\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"4uxgXPyW5dBcjcXSPMcrsr/OWk7qylxy\", \"sourceAttributeValue\": \"4uxgXPyW5dBcjcXSPMcrsr/OWk7qylxy\", \"relationType\": \"exact\"}]");
        ConnectionTableEntry connectionTableEntry10 = new ConnectionTableEntry("infinox-424308", "infinox-424311", "Same Identity", 0.17, "[{\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"yjpB7jkXAMId8vRlXynlTk8cdqTtCvP0\", \"sourceAttributeValue\": \"7ZoSwJTKIG+OoGHfBgxdv7do7rC3aFRx\", \"relationType\": \"exact\"}]");

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
        createSimpleAlert("infinox-424303", "MARKET_MANIPULATION");
        createUserFraudsDb("infinox-424303", 3);

        //second floor
        createSimpleAlert("infinox-424306", "CPA");
        createUserFraudsDb("infinox-424306", 7);
//        setGeneralRestrictionApi("infinox-424306", "03"); todo implement for trade

        createSimpleAlert("infinox-424308", "HEDGING");
        createUserFraudsDb("infinox-424308", 1);
//        setGeneralRestrictionApi("infinox-424308", "03"); todo implement for trade

        createSimpleAlert("infinox-424309", "HEDGING");

        //third floor
        createSimpleAlert("infinox-424310", "POTENTIAL_ABUSE");
        createUserFraudsDb("infinox-424310", 14);
//        setGeneralRestrictionApi("infinox-424306", "03"); todo implement for trade

        stopSshTunnel();


    }

    public static void cleanDemoDataConnectionSearch() throws Exception {
        cleanUserFraudsDb("infinox-424303");
        cleanUserFraudsDb("infinox-424306");
        cleanUserFraudsDb("infinox-424308");
        cleanUserFraudsDb("infinox-424309");
        cleanUserFraudsDb("infinox-424310");
        deleteEntryFromDb("vindex_test.connection_table", "user_to in ('infinox-424302', 'infinox-424303', 'infinox-424304', 'infinox-424305', 'infinox-424306', 'infinox-424307', 'infinox-424308', 'infinox-424309', 'infinox-424310', 'infinox-424311')");
    }

    @Test
    void createDemoDataConnectionSearchTest() throws Exception {
        createDemoDataConnectionSearch("vantage-518248");
    }

}
