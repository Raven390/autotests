package helpers.api;

import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static utils.Constants.*;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.db.mitigation_service_db.ClientTradingRestriction;
import helpers.data.ClientHelper;
import helpers.data.enums.Restriction;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Response;

public class MitigationHelper {

    public static void setGeneralRestrictionApi(String ucid, String restrictionCode) throws Exception {

        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                restrictionCode,
                "GENERAL",
                null,
                null,
                "set test data",
                new PostRestrictionRequestBody.UpdatedBy("API", "QA"));

        Allure.step("Send and check first request");
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(response.code(), 200);
    }

    public static void checkAppliedRestriction(
            List<ClientGeneralRestriction> clientGeneralRestrictions, Integer restrictionCode) {
        for (ClientGeneralRestriction clientGeneralRestriction : clientGeneralRestrictions) {
            if ("APPLIED".equals(clientGeneralRestriction.getStatus())
                    && clientGeneralRestriction.getRestrictionId() == restrictionCode.longValue()) {
                return;
            }
            fail("expected restriction is not in applied restrictions");
        }
    }

    public static List<Restriction> getClientRestrictionListFromDb(ClientHelper client) throws Exception {
        List<Restriction> restrictionList = new ArrayList<>();
        List<ClientGeneralRestriction> restrictionsGeneral = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                String.format("ucid = '%s' and status = '%s'", client.getUcid(), APPLIED_STATUS),
                ClientGeneralRestriction.class);
        for (ClientGeneralRestriction restriction : restrictionsGeneral) {
            restrictionList.add(Restriction.getRestrictionById(
                    restriction.getRestrictionId().intValue()));
        }
        String where = String.format(
                """
                %s.ucid = '%s'
                    AND NOT EXISTS (
                        SELECT 1
                        FROM %s ctrs
                        WHERE ctrs.client_restriction_id = %s.id
                            AND ctrs.status <> '%s'
                     )
                """,
                MITIGATION_CLIENT_TRADING_RESTRICTION,
                client.getUcid(),
                MITIGATION_CLIENT_TRADING_RESTRICTION_STATUS_BY_SITE,
                MITIGATION_CLIENT_TRADING_RESTRICTION,
                APPLIED_STATUS);
        List<ClientTradingRestriction> restrictionsTrading = getObjectsFromDB(
                DbName.POSTGRES, MITIGATION_CLIENT_TRADING_RESTRICTION, where, ClientTradingRestriction.class);
        for (ClientTradingRestriction restriction : restrictionsTrading) {
            restrictionList.add(Restriction.getRestrictionById(
                    restriction.getRestrictionId().intValue()));
        }
        return restrictionList;
    }

    public static void deleteTradingEnvironmentRestrictions(String ucid) throws Exception {
        String whereRestrictionId = String.format(
                "client_restriction_id IN (SELECT id from %s where ucid = '%s')",
                MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION, ucid);
        String whereUcid = String.format("ucid = '%s'", ucid);
        deleteEntryFromDb(
                DbName.POSTGRES, MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION_KAFKA_REQUEST, whereRestrictionId);
        deleteEntryFromDb(
                DbName.POSTGRES, MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION_KAFKA_RESPONSE, whereRestrictionId);
        deleteEntryFromDb(
                DbName.POSTGRES, MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION_ACTION, whereRestrictionId);
        deleteEntryFromDb(DbName.POSTGRES, MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION_QUEUE, whereUcid);
        deleteEntryFromDb(DbName.POSTGRES, MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION, whereUcid);
    }
}
