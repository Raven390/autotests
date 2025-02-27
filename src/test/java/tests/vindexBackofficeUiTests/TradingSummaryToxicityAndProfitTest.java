package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
import businessObjects.db.clickhouse.mtMt5DealsCoercedToxicity.Mt5DealsCoercedToxicityObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static businessObjects.db.clickhouse.mtMt5DealsCoercedToxicity.Mt5DealsCoercedToxicityFactory.generateMt5DealsCoercedToxicityAdditionalByClient;
import static businessObjects.db.clickhouse.mtMt5DealsCoercedToxicity.Mt5DealsCoercedToxicityFactory.generateMt5DealsCoercedToxicityByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.*;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

public class TradingSummaryToxicityAndProfitTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account2 = generateAdditionalCrmTbAccountData(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);
    private static final Mt5DealsCoercedToxicityObject trade1 = generateMt5DealsCoercedToxicityByClient(client);
    private static final Mt5DealsCoercedToxicityObject trade2 = generateMt5DealsCoercedToxicityByClient(client);
    private static final Mt5DealsCoercedToxicityObject trade3 = generateMt5DealsCoercedToxicityAdditionalByClient(client);
    private static final Mt5DealsCoercedToxicityObject trade4 = generateMt5DealsCoercedToxicityByClient(client);
    private static final Mt5DealsCoercedToxicityObject trade5 = generateMt5DealsCoercedToxicityAdditionalByClient(client);
    private static final Mt5DealsCoercedToxicityObject trade6 = generateMt5DealsCoercedToxicityByClient(client);
    private static final Mt5DealsCoercedToxicityObject trade7 = generateMt5DealsCoercedToxicityAdditionalByClient(client);
    private static final Mt5DealsCoercedToxicityObject trade8 = generateMt5DealsCoercedToxicityByClient(client);
    private static final Mt5DealsCoercedToxicityObject trade9 = generateMt5DealsCoercedToxicityAdditionalByClient(client);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        trade1.profitUsd = -5555.5;
        trade1.toxicityUsd = 3545.534;
        trade1.timeUtc = "2024-09-05 12:34:56";
        trade2.profitUsd = -6666.66;
        trade2.toxicityUsd = 9482.12;
        trade2.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7, 0, 0);
        trade3.profitUsd = -2313.534;
        trade3.toxicityUsd = -1331.09;
        trade3.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6, 0, 0);
        trade4.profitUsd = 3890.23;
        trade4.toxicityUsd = 3487.95;
        trade4.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        trade5.profitUsd = 34_244.89;
        trade5.toxicityUsd = -39_811.319;
        trade5.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 4, 0, 0);
        trade6.profitUsd = -12_309.09;
        trade6.toxicityUsd = 7231.90;
        trade6.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 3, 0, 0);
        trade7.profitUsd = -1233.09;
        trade7.toxicityUsd = 43_299.13;
        trade7.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 2, 0, 0);
        trade8.profitUsd = 5675.132;
        trade8.toxicityUsd = -18_787.465;
        trade8.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        trade9.profitUsd = -22_987.0;
        trade9.toxicityUsd = 24_573.0;
        trade9.timeUtc = getCurrentTimestampDbFormat();
        crmTbUser.registrationDate = transformDate(trade1.timeUtc, DATE_AND_TIME, DATE);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account, account2));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, mtAccount2));
        insertObjectsToDb(MT5_DEALS_COERCED_TOXICITY_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9));
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1007")
    @DisplayName("Verify Toxicity & Profit chart in Trading - Summary")
    public void verifyTradingSummaryToxicityAndProfitTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify Toxicity & Profit chart title", tradingPage.getToxicityAndProfitChartTitle(), is("Toxicity and profitUSD"));
        assertThat("Verify Toxicity & Profit Y axis label", tradingPage.getToxicityAndProfitYAxisLabel(), is("45k"));
        String maxToxicity = "28,144";
        String maxProfit = "29,155";
        assertThat("Verify Toxicity & Profit max toxicity value", tradingPage.getToxicityAndProfitMaxToxicityValue(), is(maxToxicity));
        assertThat("Verify Toxicity & Profit max toxicity label", tradingPage.getToxicityAndProfitMaxToxicityLabel(), is("Max toxicity"));
        assertThat("Verify Toxicity & Profit max profit value", tradingPage.getToxicityAndProfitMaxProfitValue(), is(maxProfit));
        assertThat("Verify Toxicity & Profit max profit label", tradingPage.getToxicityAndProfitMaxProfitLabel(), is("Max profit"));
        assertThat("Verify Toxicity & Profit max toxicity graph dot value", tradingPage.getToxicityAndProfitMaxToxicityGraphDot(), is(maxToxicity));
        assertThat("Verify Toxicity & Profit x axis labels match expected pattern", tradingPage.getToxicityAndProfitXAxisLabels(), contains("Deals", "1", "2", "3", "4", "5", "6", "7"));
        assertThat("Verify Toxicity & Profit tooltip", tradingPage.getToxicityAndProfitTooltip(), is("The chart only displays data available from September 6, 2024, onwards.  Data is updated once per hour."));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        cleanMt5CoercedToxicityTableByUcid(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }
}
