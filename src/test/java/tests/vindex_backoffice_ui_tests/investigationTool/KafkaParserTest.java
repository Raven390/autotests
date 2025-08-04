package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.kafka.crm_events.EgRegistrationEvent;
import helpers.data.ClientHelper;
import helpers.data.enums.Country;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.time.Instant;
import java.util.random.RandomGenerator;

import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getCurrentTimestampSeconds;


class KafkaParserTest extends TestBaseRule {

    Faker faker = new Faker();

    @Test
    @DisplayName("Registration kafka parser")
    @AllureId("")
    void oneTest() throws Exception {


        Country testCountry = Country.getRandomCountry();
        Country testCountry2 = Country.getRandomCountry();

        final ClientHelper client = getRandomVantageClientAllFields();
        client.setLastName("Kafka");


        LnSessionParsedObject lexis = generateLexisNexisDataByClient(client);
        lexis.setEventDatetime("2024-12-24 21:03:56");
        lexis.setEventType("account_creation");
        lexis.setConditionAttrib5("agent_mobile");
        lexis.setOs("android");
        lexis.setRiskRating("high");
        lexis.setPolicyScore(-50);
        lexis.setPolicyScore(99);
        lexis.setEmailageEmailriskscoreEascore(11);
        lexis.setEmailageEmailriskscoreEariskbandid(22);
        lexis.setEmailageEmailriskscoreEaadvice("test advice");
        lexis.setPolicyScore(RandomGenerator.getDefault().nextInt(0, 101));
        lexis.setTrueIp(faker.internet().ipV4Address());
        lexis.setTrueIpIsp("some true IP ISP" + getCurrentTimestampSeconds());
        lexis.setTrueIpPostalCode(faker.address().zipCode());
        lexis.setTrueIpCity(faker.address().city());
        lexis.setTrueIpRegion(faker.address().state());
        lexis.setTrueIpGeo(testCountry.getCountryCode());
        lexis.setTrueIpConnectionType("some true IP connection type" + getCurrentTimestampSeconds());
        lexis.setTrueIpRoutingType("some true IP routing type" + getCurrentTimestampSeconds());
        lexis.setProxyType("some true IP proxy type" + getCurrentTimestampSeconds());
        lexis.setInputIpAddress(faker.internet().ipV4Address());
        lexis.setInputIpIsp("some input IP ISP" + getCurrentTimestampSeconds());
        lexis.setInputIpCity(faker.address().city());
        lexis.setInputIpRegion(faker.address().state());
        lexis.setInputIpGeo(testCountry2.getCountryCode());
        lexis.setInputIpRoutingType("some input IP routing type" + getCurrentTimestampSeconds());
        lexis.setDeviceId("some smart id" + getCurrentTimestampSeconds());
        lexis.setFuzzyDeviceId("some exact id" + getCurrentTimestampSeconds());
        lexis.setBrowser("browser name" + getCurrentTimestampSeconds());
        lexis.setBrowserVersion("browser version" + getCurrentTimestampSeconds());
        lexis.setScreenResZoom(RandomGenerator.getDefault().nextDouble(0, 101));
        lexis.setBrowserLanguage("en-US,en;q=0.9,zh-CN;q=0.8,zh-TW;q=0.7,zh;q=0.6");
        lexis.setProfiledUrl("https://" + getCurrentTimestampSeconds() + ".com/login");
        lexis.setBrowserString("some user agent" + getCurrentTimestampSeconds());
        lexis.setPluginNumber(RandomGenerator.getDefault().nextInt(0, 101));
        lexis.setConditionAttrib5("Something");
        lexis.setOs("win");
        lexis.setOsVersion("win" + getCurrentTimestampSeconds());
        lexis.setAgentBrand("brand1, brand2 " + getCurrentTimestampSeconds());
        lexis.setAgentModel("agent model" + getCurrentTimestampSeconds());
        lexis.setDeviceModel("device model" + getCurrentTimestampSeconds());
        lexis.setDeviceName("human readable device name " + getCurrentTimestampSeconds());
        lexis.setAgentLanguage("it-IT");
        lexis.setScreenRes("1515x" + getCurrentTimestampSeconds());
        lexis.setDeviceId(null);

        EgRegistrationEvent registrationEvent = new EgRegistrationEvent();
        registrationEvent.lexisNexis = new EgRegistrationEvent.LexisNexis();
        registrationEvent.schemaVersion = "2.0";
        registrationEvent.clientId = client.getUserId();
        registrationEvent.brand = client.getBrand();
        registrationEvent.regulator = Regulator.VFSC.getDisplayName();
        registrationEvent.metaTraderAccount = 1;
        registrationEvent.id = getRandomUuidString();
        registrationEvent.createTime = Instant.now().toString();
        registrationEvent.type = CRM_REGISTRATION_EVENT;
        registrationEvent.ibId = 11;
        registrationEvent.cpaId = 12;
        registrationEvent.referrerId = 13;
        registrationEvent.firstName = client.getFirstName();
        registrationEvent.lastName = client.getLastName();
        registrationEvent.email = client.getEmail();
        registrationEvent.phoneNumber = "cTsGbMYzhsD5SxSOhmgpmQ==";
        registrationEvent.birthday = "1990-01-01";
        registrationEvent.firstLanguage = "fr_FR";
        registrationEvent.nationalityCode = "168";
        registrationEvent.residencyCode = "3382";
        registrationEvent.websiteUserType = "2";
        registrationEvent.lexisNexis.trueIp = "192.168.1.1";
        registrationEvent.lexisNexis.device = "device_001";
        registrationEvent.lexisNexis.digitalId = "digital_abc123";
        registrationEvent.lexisNexis.sessionId = "session_xyz789";
        registrationEvent.lexisNexis.webSessionId = "websession_456def";
        registrationEvent.lexisNexis.riskRating = "low";
        registrationEvent.lexisNexis.policyScore = 85;
        registrationEvent.lexisNexis.rawResponse = lexis.toStringRawResponse();
        registrationEvent.eventDate = getCurrentTimestampDbFormat();

        System.out.println("event is : \n" + registrationEvent);


        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

    }
}
