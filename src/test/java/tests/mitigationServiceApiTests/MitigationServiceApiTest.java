package tests.mitigationServiceApiTests;

import businessObjects.api.mitigationService.*;
import businessObjects.db.auditServiceDB.Event;
import helpers.database.AuditHelper;
import helpers.database.DbName;
import helpers.database.MitigationHelper;
import io.qameta.allure.Muted;
import io.qameta.allure.Owner;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.List;

import static businessObjects.api.mitigationService.MitigationServiceRequest.*;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.*;

//@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
//@Story(STORY_CONNECTION_SEARCH_BY_CLIENT_ID)
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_API)
@Tag(SUITE_MITIGATION_SERVICE)
@Muted
public class MitigationServiceApiTest extends TestBaseApi {

    @BeforeAll
    public static void CRMEmulation() throws IOException {
        Response response = enableCRMEmulator();
        assertNotNull(response);

    }

    @BeforeEach
    @Test
    public void before() throws Exception {
        MitigationHelper.cleanUserRestriction("vantage-10081449");
        AuditHelper.cleanUserAudit("vantage-10081449");
    }

//    @Test
//    @DisplayName("Mitigation service tests")
//    @AllureId("")
//    public void getRestrictionsByUcidTest() throws IOException {
//
//        Response response = getRestrictionsByUcid("vantage-1370903308");
//        GetRestrictionResponseBody[] restrictionBody = objectMapper.readValue(
//                response.body().string(),
//                GetRestrictionResponseBody[].class
//        );
//        System.out.println(Arrays.toString(restrictionBody));
//        System.out.println(response.code());
//    }
//
//    @Test
//    @DisplayName("Mitigation service tests")
//    @AllureId("")
//    public void postRestrictionsTest() throws IOException {
//
//        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
//                "vantage-10079867",
//                "05",
//                "GENERAL",
//                null,
//                null,
//                "Integration test",
//                new PostRestrictionRequestBody.UpdatedBy("string", "string")
//        );
//
//        Response response = postRestriction(postRestrictionRequestBody);
//        PostRestrictionResponse restrictionBody = objectMapper.readValue(
//                response.body().string(),
//                PostRestrictionResponse.class
//        );
//        System.out.println(restrictionBody);
//        System.out.println(response.code());
//    }
//
//    @Test
//    @DisplayName("Mitigation service tests")
//    @AllureId("")
//    public void cancelRestrictionTest() throws IOException {
//
//        KafkaHelper kafka = new KafkaHelper();
//        ObjectMapper objectMapper = new ObjectMapper();
//
////         // confirm apply
////         ApplyConfirmedKafkaMessage applyConfirmedKafkaMessage = new ApplyConfirmedKafkaMessage(
////                 "2024-09-11T12:00:00Z",
////                 new ApplyConfirmedKafkaMessage.Restriction[]{
////                         new ApplyConfirmedKafkaMessage.Restriction(183, "Applied", "")
////                 });
////
////         kafka.produceMessage("13", objectMapper.writeValueAsString(applyConfirmedKafkaMessage), "client.restrictions.applyConfirmed");
//
//        // cancel
//        CancelRestrictionRequestBody cancelRestrictionRequestBody = new CancelRestrictionRequestBody(
//                "string",
//                new CancelRestrictionRequestBody.UpdatedBy("string", "string")
//        );
//
//        Response response = cancelRestrictionById(184, cancelRestrictionRequestBody);
//        System.out.println(response.body().string());
//        System.out.println(response.code());
//
//        // confirm cancel
//        CancelConfirmedKafkaMessage cancelConfirmedKafkaMessage = new CancelConfirmedKafkaMessage(
//                "2024-09-11T12:00:00Z",
//                new CancelConfirmedKafkaMessage.Restriction[]{
//                        new CancelConfirmedKafkaMessage.Restriction(184, "Canceled")
//                });
//
//        kafka.produceMessage("13", objectMapper.writeValueAsString(cancelConfirmedKafkaMessage), "client.restrictions.cancelConfirmed");
//    }
//
//    @Test
//    @DisplayName("Mitigation service tests")
//    @AllureId("")
//    public void getRestrictionCatalogTest() throws IOException {
//
//        Response response = getRestrictionCatalog();
//        RestrictionCatalogEntry[] restrictionCatalog = objectMapper.readValue(
//                response.body().string(),
//                RestrictionCatalogEntry[].class
//        );
//        System.out.println(Arrays.toString(restrictionCatalog));
//        System.out.println(response.code());
//    }

    @Test
    @DisplayName("Mitigation service tests")
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionRecordInAudit() throws Exception {

        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                "vantage-10081449",
                "05",
                "GENERAL",
                null,
                null,
                "Integration test",
                new PostRestrictionRequestBody.UpdatedBy("string", "string")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        PostRestrictionResponse restrictionBody = objectMapper.readValue(
                response.body().string(),
                PostRestrictionResponse.class
        );
        String restrictionID = restrictionBody.id.toString();
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "au.au.event", "ucid = 'vantage-10081449'", Event.class);
        String type1 = event.get(0).getType();
        assertEquals(type1, "RESTRICTION_REQUESTED");
        String type2 = event.get(1).getType();
        assertEquals(type2, "RESTRICTION_APPLIED");
    }


}
