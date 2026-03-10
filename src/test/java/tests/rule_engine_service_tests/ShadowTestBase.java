package tests.rule_engine_service_tests;

import business_objects.db.clickhouse.reporting_test.AuditRaw;
import business_objects.db.clickhouse.reporting_test.ZeebeErroneousRulesEnds;
import business_objects.db.clickhouse.reporting_test.ZeebeRulesElements;
import business_objects.db.clickhouse.reporting_test.ZeebeRulesStarted;
import helpers.database.DbHelper;
import helpers.database.DbName;
import io.qameta.allure.Step;
import org.awaitility.Awaitility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import tests.TestBaseRule;

public abstract class ShadowTestBase extends TestBaseRule {

    public interface PathNormalizer {
        List<String> normalizeCamundaPath(List<String> camundaPath);
        List<String> normalizeNewEnginePath(List<String> newEnginePath);
    }

    @Step("Wait for engines to complete execution")
    protected void waitForEnginesCompletion(String correlationId, String finalCamundaNode) {
        Awaitility.await()
                .atMost(60, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> isCamundaFinished(correlationId, finalCamundaNode) && isNewEngineFinished(correlationId));
    }

    private boolean isCamundaFinished(String correlationId, String finalCamundaNode) throws Exception {
        String runIdQuery = String.format("SELECT run_id FROM reporting.zeebe_rules_started WHERE event_id = '%s' LIMIT 1", correlationId);
        List<ZeebeRulesStarted> startedList = DbHelper.getObjectsFromDB(DbName.CLICKHOUSE, runIdQuery, ZeebeRulesStarted.class);

        if (startedList == null || startedList.isEmpty() || startedList.get(0).getRunId() == null) {
            return false;
        }
        String runId = startedList.get(0).getRunId();

        String elementsQuery = String.format("SELECT element_id FROM reporting.zeebe_rules_elements WHERE run_id = '%s' AND element_id = '%s'", runId, finalCamundaNode);
        List<ZeebeRulesElements> elementsList = DbHelper.getObjectsFromDB(DbName.CLICKHOUSE, elementsQuery, ZeebeRulesElements.class);
        if (elementsList != null && !elementsList.isEmpty()) {
            return true;
        }

        String errorsQuery = String.format("SELECT run_id FROM reporting.zeebe_erroneous_rules_ends WHERE run_id = '%s'", runId);
        List<ZeebeErroneousRulesEnds> errorsList = DbHelper.getObjectsFromDB(DbName.CLICKHOUSE, errorsQuery, ZeebeErroneousRulesEnds.class);
        return errorsList != null && !errorsList.isEmpty();
    }

    private boolean isNewEngineFinished(String correlationId) throws Exception {
        String instanceIdQuery = String.format("SELECT instanceId FROM reporting.lt_tre___audit_raw WHERE JSONExtractString(variables, 'event', 'id') = '%s' LIMIT 1", correlationId);
        List<AuditRaw> instanceList = DbHelper.getObjectsFromDB(DbName.CLICKHOUSE, instanceIdQuery, AuditRaw.class);

        if (instanceList == null || instanceList.isEmpty() || instanceList.get(0).getInstanceId() == null) {
            return false;
        }
        String instanceId = instanceList.get(0).getInstanceId();

        String query = String.format("SELECT instanceId FROM reporting.lt_tre___audit_raw WHERE instanceId = '%s' AND nodePhase = 'exit' LIMIT 1", instanceId);
        List<AuditRaw> auditList = DbHelper.getObjectsFromDB(DbName.CLICKHOUSE, query, AuditRaw.class);
        return auditList != null && !auditList.isEmpty();
    }

    @Step("Extract Camunda Execution Path")
    protected List<String> extractCamundaPath(String correlationId) throws Exception {
        String runIdQuery = String.format("SELECT run_id FROM reporting.zeebe_rules_started WHERE event_id = '%s' LIMIT 1", correlationId);
        List<ZeebeRulesStarted> startedList = DbHelper.getObjectsFromDB(DbName.CLICKHOUSE, runIdQuery, ZeebeRulesStarted.class);

        if (startedList == null || startedList.isEmpty() || startedList.get(0).getRunId() == null) {
            return new ArrayList<>();
        }

        String runId = startedList.get(0).getRunId();
        String pathQuery = String.format("SELECT element_id FROM reporting.zeebe_rules_elements WHERE run_id = '%s' ORDER BY position ASC", runId);

        List<ZeebeRulesElements> elementsList = DbHelper.getObjectsFromDB(DbName.CLICKHOUSE, pathQuery, ZeebeRulesElements.class);
        if (elementsList == null) return new ArrayList<>();

        return elementsList.stream()
                .map(ZeebeRulesElements::getElementId)
                .collect(Collectors.toList());
    }

    @Step("Assert Execution Path Match")
    protected void assertShadowExecutionPath(String correlationId, String finalCamundaNode, PathNormalizer normalizer) throws Exception {
        waitForEnginesCompletion(correlationId, finalCamundaNode);

        List<String> rawCamundaPath = extractCamundaPath(correlationId);
        List<String> rawNewEnginePath = extractNewEnginePath(correlationId);

        List<String> normalizedCamundaPath = normalizer.normalizeCamundaPath(rawCamundaPath);
        List<String> normalizedNewEnginePath = normalizer.normalizeNewEnginePath(rawNewEnginePath);

        org.hamcrest.MatcherAssert.assertThat("Normalized execution paths should strictly match",
                normalizedNewEnginePath, org.hamcrest.Matchers.equalTo(normalizedCamundaPath));
    }

    @Step("Extract New Engine Execution Path")
    protected List<String> extractNewEnginePath(String correlationId) throws Exception {
        String instanceIdQuery = String.format("SELECT instanceId FROM reporting.lt_tre___audit_raw WHERE JSONExtractString(variables, 'event', 'id') = '%s' LIMIT 1", correlationId);
        List<AuditRaw> instanceList = DbHelper.getObjectsFromDB(DbName.CLICKHOUSE, instanceIdQuery, AuditRaw.class);

        if (instanceList == null || instanceList.isEmpty() || instanceList.get(0).getInstanceId() == null) {
            return new ArrayList<>();
        }

        String instanceId = instanceList.get(0).getInstanceId();
        String pathQuery = String.format("SELECT fromId, toId FROM reporting.lt_tre___audit_raw WHERE instanceId = '%s' AND nodePhase = 'transition' ORDER BY startedAt ASC, ts ASC", instanceId);

        List<AuditRaw> transitionsList = DbHelper.getObjectsFromDB(DbName.CLICKHOUSE, pathQuery, AuditRaw.class);
        if (transitionsList == null || transitionsList.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> path = new ArrayList<>();
        for (AuditRaw transition : transitionsList) {
            if (path.isEmpty() || !path.get(path.size() - 1).equals(transition.getFromId())) {
                path.add(transition.getFromId());
            }
            path.add(transition.getToId());
        }

        return path;
    }
}