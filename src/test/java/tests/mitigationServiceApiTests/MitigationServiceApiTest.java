// package tests.mitigationServiceApiTests;

// import businessObjects.api.mitigationService.*;
// import businessObjects.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventMt4;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import helpers.kafka.KafkaHelper;
// import io.qameta.allure.Allure;
// import io.qameta.allure.AllureId;
// import io.qameta.allure.Feature;
// import io.qameta.allure.Story;
// import okhttp3.Response;
// import org.junit.jupiter.api.*;
// import tests.TestBaseApi;

// import java.io.IOException;
// import java.util.Arrays;

// import static businessObjects.api.mitigationService.MitigationServiceRequest.*;
// import static businessObjects.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt4;
// import static utils.Constants.*;

// @Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
// @Story(STORY_CONNECTION_SEARCH_BY_CLIENT_ID)
// @Tag(TEAM_CORE)
// @Tag(LAYER_API)
// @Tag(SUITE_MITIGATION_SERVICE)
// public class MitigationServiceApiTest extends TestBaseApi {

//     @Test
//     @DisplayName("Mitigation service tests")
//     @AllureId("")
//     public void getRestrictionsByUcidTest() throws IOException{

//         Response response = getRestrictionsByUcid("vantage-10079867");
//         GetRestrictionResponseBody[] restrictionBody = objectMapper.readValue(
//                 response.body().string(),
//                 GetRestrictionResponseBody[].class
//         );
//         System.out.println(Arrays.toString(restrictionBody));
//         System.out.println(response.code());
//     }

//     @Test
//     @DisplayName("Mitigation service tests")
//     @AllureId("")
//     public void postRestrictionsTest() throws IOException {

//         PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
//                 "vantage-10079867",
//                 "05",
//                 "GENERAL",
//                 null,
//                 null,
//                 "Integration test",
//                 new PostRestrictionRequestBody.UpdatedBy("string", "string")
//         );

//         Response response = postRestriction(postRestrictionRequestBody);
//         PostRestrictionResponse restrictionBody = objectMapper.readValue(
//                 response.body().string(),
//                 PostRestrictionResponse.class
//         );
//         System.out.println(restrictionBody);
//         System.out.println(response.code());
//     }

//     @Test
//     @DisplayName("Mitigation service tests")
//     @AllureId("")
//     public void cancelRestrictionTest() throws IOException{

//         // confirm apply
//         KafkaHelper kafka = new KafkaHelper();
//         ObjectMapper objectMapper = new ObjectMapper();
//         ApplyConfirmedKafkaMessage applyConfirmedKafkaMessage = new ApplyConfirmedKafkaMessage(
//                 "2024-09-11T12:00:00Z",
//                 new ApplyConfirmedKafkaMessage.Restriction[]{
//                         new ApplyConfirmedKafkaMessage.Restriction(31, "Applied", "")
//                 });

//         kafka.produceMessage("13", objectMapper.writeValueAsString(applyConfirmedKafkaMessage), "client.restrictions.applyConfirmed");

// //        // cancel
// //        CancelRestrictionRequestBody cancelRestrictionRequestBody = new CancelRestrictionRequestBody(
// //                "string",
// //                new CancelRestrictionRequestBody.UpdatedBy("string", "string")
// //        );
// //
// //        Response response = cancelRestrictionById(31, cancelRestrictionRequestBody);
// //        System.out.println(response.body().string());
// //        System.out.println(response.code());
// //
// //        // confirm cancel
// //        KafkaHelper kafka = new KafkaHelper();
// //        ObjectMapper objectMapper = new ObjectMapper();
// //        CancelConfirmedKafkaMessage cancelConfirmedKafkaMessage = new CancelConfirmedKafkaMessage(
// //                "2024-09-11T12:00:00Z",
// //                new CancelConfirmedKafkaMessage.Restriction[]{
// //                        new CancelConfirmedKafkaMessage.Restriction(31, "Canceled")
// //                });
// //
// //        kafka.produceMessage("13", objectMapper.writeValueAsString(cancelConfirmedKafkaMessage), "client.restrictions.cancelConfirmed");
//     }

//     @Test
//     @DisplayName("Mitigation service tests")
//     @AllureId("")
//     public void getRestrictionCatalogTest() throws IOException{

//         Response response = getRestrictionCatalog();
//         RestrictionCatalogEntry[] restrictionCatalog = objectMapper.readValue(
//                 response.body().string(),
//                 RestrictionCatalogEntry[].class
//         );
//         System.out.println(Arrays.toString(restrictionCatalog));
//         System.out.println(response.code());
//     }
// }
