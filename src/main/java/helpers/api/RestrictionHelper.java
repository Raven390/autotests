package helpers.api;

import static business_objects.api.mitigation_service.CorrelationType.RESTRICTION_MANAGEMENT;
import static business_objects.api.mitigation_service.MitigationServiceRequest.cancelRestriction;
import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static helpers.data.enums.Restriction.WORSE_TRADING;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbName.POSTGRES;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION;
import static utils.Constants.VINDEX_BO_SYSTEM;

import business_objects.api.mitigation_service.*;
import business_objects.db.mitigation_service_db.client_trading_environment_restriction.ClientTradingEnvironmentRestrictionEntity;
import helpers.data.ClientHelper;
import helpers.data.enums.TradingEnvironmentLevel;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import okhttp3.Response;

public class RestrictionHelper {
    @Step("Set restriction through API")
    public static void setRestrictionAPIGeneral(String ucid, String code) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "GENERAL",
                null,
                null,
                "Integration test",
                new PostRestrictionRequestBody.UpdatedBy("test", "automation"));
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Step("Set restriction through API")
    public static void setRestrictionAPIGeneral(String ucid, String code, String comment) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "GENERAL",
                null,
                null,
                comment,
                new PostRestrictionRequestBody.UpdatedBy("test", "automation"));
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Step
    public static void setRestrictionAPITrade(String ucid, int accId, int serverId, String code) throws IOException {
        Allure.step("Set restriction through API TRADE");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "TRADING",
                accId,
                serverId,
                "Integration test",
                new PostRestrictionRequestBody.UpdatedBy("string", "string"));
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    public static void setRestrictionAPIGeneral(
            String ucid, String code, String applyReason, String updatedBySystem, String updatedByUser)
            throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "GENERAL",
                null,
                null,
                applyReason,
                new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser));
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
    }

    @Deprecated
    public static String setRestrictionAPIGeneralResponse(
            String ucid, String code, String applyReason, String updatedBySystem, String updatedByUser)
            throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "GENERAL",
                null,
                null,
                applyReason,
                new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser));
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(response.code(), 200);
        assertThat(response.body(), is(notNullValue()));
        return response.body().string();
    }

    @Step
    public static String setRestrictionAPITradeResponse(
            String ucid,
            String code,
            int accId,
            int serverId,
            String applyReason,
            String updatedBySystem,
            String updatedByUser)
            throws IOException {
        Allure.step("Set restriction through API TRADE");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "TRADING",
                accId,
                serverId,
                applyReason,
                new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser));
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
        assertThat(response.body(), is(notNullValue()));
        return response.body().string();
    }

    public static void addCancelledRestriction(ClientHelper client, String type, String code) throws IOException {
        // add bonus restriction
        Integer restrictionId = postRestriction(client, type, code).id;
        // cancel bonus restriction
        assertThat("Assert response code", cancelRestriction(restrictionId).code(), is(204));
    }

    @Step
    public static Response putRestrictionV3(NewTradingEnvRestrictionRequestBody putRestrictionRequestBody)
            throws IOException {
        Allure.step("Set trading env restriction through API");
        Response response = MitigationServiceRequest.putRestrictionV3(putRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }

    @Step
    public static Response getClientRestrictionsV3(String ucid) throws IOException {
        Allure.step("Get client's restrictions through API");
        Response response = MitigationServiceRequest.getRestrictionsByUcidV3(ucid);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }

    @Step("Set trading env restriction through API")
    public static Response postRestrictionV3(NewTradingEnvRestrictionRequestBody postRestrictionRequestBody)
            throws IOException {
        Response response = MitigationServiceRequest.postRestrictionV3(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }

    @Step("Delete trading env restriction through API")
    public static Response deleteRestrictionV3(DeleteTradingEnvRestrictionRequestBody deleteRestrictionRequestBody)
            throws IOException {
        Response response = MitigationServiceRequest.deleteRestrictionV3(deleteRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }

    @Step("Add general restriction through API")
    public static Response addGeneralRestrictionV3(NewGeneralRestriction newGeneralRestriction) throws IOException {
        Response response = MitigationServiceRequest.postGeneralRestrictionV3(newGeneralRestriction);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }

    @Step("Cancel general restriction through API")
    public static Response cancelGeneralRestrictionV3(CancelGeneralRestriction cancelGeneralRestriction)
            throws IOException {
        Response response = MitigationServiceRequest.deleteGeneralRestrictionV3(cancelGeneralRestriction);
        assertNotNull(response);
        assertEquals(204, response.code());
        return response;
    }

    @Step("Add trading restriction through API")
    public static Response addTradingRestrictionV3(NewTradingRestriction newTradingRestriction) throws IOException {
        Response response = MitigationServiceRequest.postTradingRestrictionV3(newTradingRestriction);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }

    @Step("Cancel general restriction through API")
    public static Response cancelTradingRestrictionV3(CancelTradingRestriction cancelTradingRestriction)
            throws IOException {
        Response response = MitigationServiceRequest.deleteTradingRestrictionV3(cancelTradingRestriction);
        assertNotNull(response);
        assertEquals(204, response.code());
        return response;
    }

    public static void putWorseTradingRestriction(
            String clientUCid,
            BigInteger accountID,
            String correlationId,
            Integer serverIdSt,
            TradingEnvironmentLevel level,
            String comment,
            String user)
            throws IOException {
        NewTradingEnvRestrictionRequestBody putRestriction = new NewTradingEnvRestrictionRequestBody();
        putRestriction.setType(RestrictionType.TRADING_ENVIRONMENT);
        putRestriction.setUcid(clientUCid);
        putRestriction.setCode(WORSE_TRADING.getCode());
        putRestriction.setComment(comment);
        putRestriction.setUpdatedBy(new UpdatedBy().system(VINDEX_BO_SYSTEM).user(user));
        putRestriction.setAccountId(accountID);
        putRestriction.setCorrelationId(correlationId);
        putRestriction.setCorrelationType(RESTRICTION_MANAGEMENT);
        putRestriction.setServerId(serverIdSt);
        putRestriction.setLevel(level);
        putRestrictionV3(putRestriction);
    }

    public static void deleteWorseTradingRestriction(
            String clientUCid,
            BigInteger accountID,
            String correlationId,
            Integer serverIdSt,
            String comment,
            String user)
            throws IOException {
        DeleteTradingEnvRestrictionRequestBody deleteRestriction = DeleteTradingEnvRestrictionRequestBody.builder()
                .type(RestrictionType.TRADING_ENVIRONMENT)
                .ucid(clientUCid)
                .code(WORSE_TRADING.getCode())
                .cancelReason(comment)
                .correlationType(RESTRICTION_MANAGEMENT)
                .correlationId(correlationId)
                .updatedBy(new UpdatedBy().system(VINDEX_BO_SYSTEM).user(user))
                .accountId(accountID)
                .serverId(serverIdSt)
                .build();
        deleteRestrictionV3(deleteRestriction);
    }

    public static void waitUntilWorseTradingRestrictionHasStatusAndLevel(
            BigInteger accountID, TradingEnvironmentLevel level, RestrictionStatus status) {
        await().atMost(Duration.ofSeconds(60))
                .pollInterval(Duration.ofMillis(200))
                .until(() -> {
                    List<ClientTradingEnvironmentRestrictionEntity> entities = getObjectsFromDB(
                            POSTGRES,
                            MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION,
                            String.format("account_id=%s", accountID),
                            ClientTradingEnvironmentRestrictionEntity.class);

                    if (entities == null || entities.isEmpty()) {
                        return false;
                    }

                    return entities.stream().anyMatch(e -> e.getStatus() == status && e.getLevel() == level);
                });
    }
}
