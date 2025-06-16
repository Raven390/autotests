package tests.rule_engine_service_tests.rules;

import helpers.data.rules.RuleDataHelper;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.no_slippage_rule.NoSlippageRuleDataFactory.setupNoSlippageRuleData;
import static helpers.database.DbHelper.*;

@Disabled("Temporarily disabling this test class because rule is in development")
class NoSlippageRuleTest {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupNoSlippageRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        stopSshTunnel();
        deleteRuleData(dbDataMap);
    }

    @Test
    @DisplayName("No slippage rule Event_end_1")
    void noSlippageRuleTest1() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_end_6")
    void noSlippageRuleTest2() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_0yv2mku")
    void noSlippageRuleTest3() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_0tlx8d8")
    void noSlippageRuleTest4() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_0n3wjq9")
    void noSlippageRuleTest5() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_0m5x0nm")
    void noSlippageRuleTest6() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_05pqy7v")
    void noSlippageRuleTest7() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_0b9or7o")
    void noSlippageRuleTest8() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_0505ghq")
    void noSlippageRuleTest9() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_0aw1lki")
    void noSlippageRuleTest10() throws IOException, InterruptedException {

    }

    @Test
    @DisplayName("No slippage rule Event_0oa6zyc")
    void noSlippageRuleTest11() throws IOException, InterruptedException {

    }


}
