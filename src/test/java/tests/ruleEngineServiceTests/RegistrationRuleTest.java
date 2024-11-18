//package tests.ruleEngineServiceTests;
//
//import businessObjects.db.crmTbUserTable.CrmTbUserObject;
//import businessObjects.db.csTbEmailTable.EmailTableEntry;
//import businessObjects.db.lnSessionParsedTable.LnSessionParsedObject;
//import businessObjects.kafka.crmDbEvents.registration.RegistrationDbEvent;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import helpers.data.rules.registrationRule.RegistrationRuleData;
//import helpers.database.DbName;
//import helpers.kafka.KafkaHelper;
//import io.qameta.allure.*;
//import org.junit.jupiter.api.*;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Locale;
//
//import static businessObjects.kafka.crmDbEvents.registration.RegistrationDbEventFactory.generateRegistrationDbEvent;
//import static helpers.data.rules.registrationRule.RegistrationRuleDataFactory.*;
//import static helpers.database.DbHelper.*;
//import static utils.Constants.*;
//import static utils.Utils.getCurrentTimestampDbFormat;
//
//@Feature(FEATURE_RULE_ENGINE_SERVICE)
//@Story(STORY_RULE_ENGINE_REGISTRATION_RULE)
//@Tag(TEAM_CORE)
//@Tag(LAYER_API)
//@Tag(SUITE_RULE_ENGINE_SERVICE)
//public class RegistrationRuleTest {
//
//    public static RegistrationRuleData registrationRuleData1 = getRegistrationRuleExitEventEnd1Data();
//    public static RegistrationRuleData registrationRuleData7v1 = getRegistrationRuleExitEventEnd7Version1Data();
//    public static RegistrationRuleData registrationRuleData7v2 = getRegistrationRuleExitEventEnd7Version2Data();
//    public static RegistrationRuleData registrationRuleData7v3 = getRegistrationRuleExitEventEnd7Version3Data();
//
////    @BeforeAll
////    public static void setupDbData() throws ReflectiveOperationException, SQLException {
////        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, registrationRuleData1.lnSessionParsedObject);
////        insertObjectToDb(CRM_USER_TABLE_NAME, registrationRuleData1.crmTbUserObject);
////    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_1")
//    @AllureId("155")
//    public void registrationRuleExitEventEnd1Test() throws JsonProcessingException, ReflectiveOperationException, SQLException {
//        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, registrationRuleData1.lnSessionParsedObject);
//        insertObjectToDb(CRM_USER_TABLE_NAME, registrationRuleData1.crmTbUserObject);
//
//        KafkaHelper kafka = new KafkaHelper();
//        ObjectMapper objectMapper = new ObjectMapper();
//        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData1.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);
//
//        Allure.step("No toxic accounts linked");
//        Allure.step("No different identity connections");
//        Allure.step("IP country == address country");
//        Allure.step("LN score != high");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_2")
//    @AllureId("156")
//    public void registrationRuleExitEventEnd2Test() throws Exception {
////        deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.clients_restriction", "id = 184");
//        List<LnSessionParsedObject> list = getObjectsFromDB(LEXIS_NEXIS_TABLE_NAME, null, LnSessionParsedObject.class);
//        System.out.println(list.get(5));
//        Allure.step("No toxic accounts linked");
//        Allure.step("No different identity connections");
//        Allure.step("IP country == address country");
//        Allure.step("LN score == high");
//        Allure.step("Generate alert");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_3")
//    @AllureId("157")
//    public void registrationRuleExitEventEnd3Test() {
//        Allure.step("No toxic accounts linked");
//        Allure.step("No different identity connections");
//        Allure.step("IP country != address country");
//        Allure.step("Generate alert");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_4")
//    @AllureId("158")
//    public void registrationRuleExitEventEnd4Test() {
//        Allure.step("No toxic accounts linked");
//        Allure.step("Different identity connections");
//        Allure.step("Linked to IB account OR Same referrer");
//        Allure.step("Generate alert");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_5")
//    @AllureId("159")
//    public void registrationRuleExitEventEnd5Test() {
//        Allure.step("No toxic accounts linked");
//        Allure.step("Different identity connections");
//        Allure.step("Not linked to IB account OR Same referrer");
//        Allure.step("LN score == Low");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_6")
//    @AllureId("160")
//    public void registrationRuleExitEventEnd6Test() {
//        Allure.step("No toxic accounts linked");
//        Allure.step("Different identity connections");
//        Allure.step("Not linked to IB account OR Same referrer");
//        Allure.step("LN score != Low");
//        Allure.step("Set no bonus, promotions, CPA");
//        Allure.step("Generate alert");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 all available alerts/restrictions")
//    @AllureId("161")
//    public void registrationRuleExitEventEnd7Version1Test() throws ReflectiveOperationException, SQLException, JsonProcessingException {
////        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, registrationRuleData7v1.lnSessionParsedObject);
////        insertObjectToDb(CRM_USER_TABLE_NAME, registrationRuleData7v1.crmTbUserObject);
////        insertObjectToDb(CRM_USER_TABLE_NAME, registrationRuleData7v1.connectedUsers.getFirst());
////        insertObjectToDb(CONNECTIONS_TABLE_NAME, registrationRuleData7v1.connections.getFirst());
////        CrmTbUserObject connectedUser = registrationRuleData7v1.connectedUsers.getFirst();
////        EmailTableEntry emailTableEntry = new EmailTableEntry(registrationRuleData7v1.crmTbUserObject.ucid, registrationRuleData7v1.crmTbUserObject.userId, connectedUser.brand.toLowerCase(), connectedUser.email, getCurrentTimestampDbFormat());
////        insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntry);
//
//        KafkaHelper kafka = new KafkaHelper();
//        ObjectMapper objectMapper = new ObjectMapper();
//        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v1.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);
////
////        Allure.step("Toxic accounts linked");
////        Allure.step("Any of the connected users is a CPA abuser");
////        Allure.step("Set no rebates");
////        Allure.step("LN == High");
////        Allure.step("Set bad trading environment");
////        Allure.step("Generate alert");
////        Allure.step("Connected user is a bonus abuser");
////        Allure.step("Set no bonuses, promotions");
////        Allure.step("Not Vjp");
////        Allure.step("Set Login CRM Restriction");
////        Allure.step("Generate alert");
////        Allure.step("Is a voucher abuser");
////        Allure.step("Set no vouchers");
////        Allure.step("Set bad trading environment");
////        Allure.step("Generate alert");
////        Allure.step("Set bad trading environment");
////        Allure.step("News trader");
////        Allure.step("Not Vjp");
////        Allure.step("Set bad trading environment");
////        Allure.step("Generate alert");
////        Allure.step("Is TLS");
////        Allure.step("Block user");
////        Allure.step("Generate alert");
////        Allure.step("Swap abuse");
////        Allure.step("Set no swap free option");
////        Allure.step("Generate alert");
////        Allure.step("Market manipulation");
////        Allure.step("A-book the new account");
////        Allure.step("Set no bonuses, promotions");
////        Allure.step("Generate alert");
////        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set no rebates")
//    @AllureId("162")
//    public void registrationRuleExitEventEnd7Version2Test() throws JsonProcessingException {
//        KafkaHelper kafka = new KafkaHelper();
//        ObjectMapper objectMapper = new ObjectMapper();
//        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v2.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);
//
//        Allure.step("Toxic accounts linked");
//        Allure.step("Any of the connected users is a CPA abuser");
//        Allure.step("Set no rebates");
//        Allure.step("LN == Low");
//        Allure.step("Generate alert");
//        Allure.step("Connected user is NOT a bonus abuser");
//        Allure.step("NOT a voucher abuser");
//        Allure.step("NOT a News trader");
//        Allure.step("NOT TLS");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set no rebates + Set bad trading environment")
//    @AllureId("163")
//    public void registrationRuleExitEventEnd7Version3Test() throws JsonProcessingException {
//
//        KafkaHelper kafka = new KafkaHelper();
//        ObjectMapper objectMapper = new ObjectMapper();
//        kafka.produceMessage("123", objectMapper.writeValueAsString(registrationRuleData7v3.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);
//
//        Allure.step("Toxic accounts linked");
//        Allure.step("Any of the connected users is a CPA abuser");
//        Allure.step("Set no rebates");
//        Allure.step("LN == High");
//        Allure.step("Set bad trading environment");
//        Allure.step("Generate alert");
//        Allure.step("Connected user is NOT a bonus abuser");
//        Allure.step("NOT a voucher abuser");
//        Allure.step("NOT a News trader");
//        Allure.step("NOT TLS");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions (Bonus abuser)")
//    @AllureId("164")
//    public void registrationRuleExitEventEnd7Version4Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is a bonus abuser");
//        Allure.step("Set no bonuses, promotions");
//        Allure.step("Is Vjp");
//        Allure.step("NOT a voucher abuser");
//        Allure.step("NOT a News trader");
//        Allure.step("NOT TLS");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions + Set bad trading environment")
//    @AllureId("165")
//    public void registrationRuleExitEventEnd7Version5Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is a bonus abuser");
//        Allure.step("Set no bonuses, promotions");
//        Allure.step("NOT Vjp");
//        Allure.step("Low LN score");
//        Allure.step("Set bad trading environment");
//        Allure.step("Generate alert");
//        Allure.step("NOT a voucher abuser");
//        Allure.step("NOT a News trader");
//        Allure.step("NOT TLS");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions + Block user")
//    @AllureId("166")
//    public void registrationRuleExitEventEnd7Version6Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is a bonus abuser");
//        Allure.step("Set no bonuses, promotions");
//        Allure.step("NOT Vjp");
//        Allure.step("High LN score");
//        Allure.step("Block user");
//        Allure.step("Generate alert");
//        Allure.step("NOT a voucher abuser");
//        Allure.step("NOT a News trader");
//        Allure.step("NOT TLS");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers")
//    @AllureId("167")
//    public void registrationRuleExitEventEnd7Version7Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is NOT a bonus abuser");
//        Allure.step("Is a voucher abuser");
//        Allure.step("Set no vouchers");
//        Allure.step("Low LN score");
//        Allure.step("Generate alert");
//        Allure.step("NOT a News trader");
//        Allure.step("NOT TLS");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers + Set bad trading environment")
//    @AllureId("168")
//    public void registrationRuleExitEventEnd7Version8Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is NOT a bonus abuser");
//        Allure.step("Is a voucher abuser");
//        Allure.step("Set no vouchers");
//        Allure.step("High LN score");
//        Allure.step("Set bad trading environment");
//        Allure.step("Generate alert");
//        Allure.step("NOT a News trader");
//        Allure.step("NOT TLS");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers + Set bad trading environment")
//    @AllureId("169")
//    public void registrationRuleExitEventEnd7Version9Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is NOT a bonus abuser");
//        Allure.step("NOT a voucher abuser");
//        Allure.step("Is a News trader");
//        Allure.step("Is Vjp");
//        Allure.step("Wipeout blacklist");
//        Allure.step("Generate alert");
//        Allure.step("NOT TLS");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set bad trading environment (News trader)")
//    @AllureId("170")
//    public void registrationRuleExitEventEnd7Version10Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is NOT a bonus abuser");
//        Allure.step("NOT a voucher abuser");
//        Allure.step("Is a News trader");
//        Allure.step("NOT Vjp");
//        Allure.step("Set bad trading environment");
//        Allure.step("Generate alert");
//        Allure.step("NOT TLS");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Block user (TLS)")
//    @AllureId("171")
//    public void registrationRuleExitEventEnd7Version11Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is NOT a bonus abuser");
//        Allure.step("NOT a voucher abuser");
//        Allure.step("NOT a News trader");
//        Allure.step("Is TLS");
//        Allure.step("Block user");
//        Allure.step("NOT Swap abuse");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Manual withdrawal review (Swap abuse)")
//    @AllureId("209")
//    public void registrationRuleExitEventEnd7Version12Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is NOT a bonus abuser");
//        Allure.step("Is a voucher abuser");
//        Allure.step("Set no vouchers");
//        Allure.step("High LN score");
//        Allure.step("Set bad trading environment");
//        Allure.step("Generate alert");
//        Allure.step("NOT a News trader");
//        Allure.step("NOT TLS");
//        Allure.step("Swap abuse");
//        Allure.step("Set Manual Withdrawal Review restriction");
//        Allure.step("Generate alert");
//        Allure.step("NOT Market manipulation");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
//    @Test
//    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions (Market manipulation)")
//    @AllureId("172")
//    public void registrationRuleExitEventEnd7Version13Test() {
//        Allure.step("Toxic accounts linked");
//        Allure.step("All of the connected users are NOT CPA abusers");
//        Allure.step("Connected user is NOT a bonus abuser");
//        Allure.step("NOT a voucher abuser");
//        Allure.step("NOT a News trader");
//        Allure.step("NOT TLS");
//        Allure.step("Market manipulation");
//        Allure.step("A-book the new account");
//        Allure.step("Set no bonuses, promotions");
//        Allure.step("Generate alert");
//        Allure.step("Fraud");
//    }
//
////    @Test
////    public void deleteDbData() throws SQLException {
////        deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = '%s'", 1370903308));
////        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = '%s'", 1370903306));
////        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = '%s'", 1370903307));
////        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", "vantage-1370903306"));
////        deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = '%s'", registrationRuleData1.lnSessionParsedObject.userId));
////        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = '%s'", registrationRuleData1.crmTbUserObject.userId));
////    }
//}
