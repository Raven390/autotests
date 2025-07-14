package helpers.api;

import business_objects.api.abuse_registry.PostFraudTypesRequestBody;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeStatus;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static business_objects.api.abuse_registry.AbuseRegistryRequest.postFraudTypes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class AbuseRegistryHelper {

    private AbuseRegistryHelper() {
    }

    static Logger ln = Logger.getLogger(AbuseRegistryHelper.class.getName());

    public static void addFraudsForClient(ClientHelper client, List<FraudType> fraudTypes, FraudTypeStatus status)
            throws IOException {
        assertThat("Check that fraudTypes list is not empty", fraudTypes.size(), greaterThan(0));
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                "Auto Test", "BO", "Set by autotest", fraudTypes.stream().map(fraudType -> new PostFraudTypesRequestBody.FraudTypeWithStatus(status.getStatus(), fraudType.getCode())).toList());
        assertThat("Check that request was successful", postFraudTypes(client, requestBody).code(), is(200));
        ln.info("frauds successfully sent for client " + client.getUcid());
    }

    public static void addFraudsForClient(String ucid, List<FraudType> fraudTypes, FraudTypeStatus status)
            throws IOException {
        assertThat("Check that fraudTypes list is not empty", fraudTypes.size(), greaterThan(0));
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                "Auto Test", "BO", "Set by autotest", fraudTypes.stream().map(fraudType -> new PostFraudTypesRequestBody.FraudTypeWithStatus(status.getStatus(), fraudType.getCode())).toList());
        assertThat("Check that request was successful", postFraudTypes(ucid, requestBody).code(), is(200));
        ln.info("frauds successfully sent for client " + ucid);
    }

    public static void addFraudForClient(ClientFraudTypes fraud)
            throws IOException {
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                "Auto Test", "BO", "Set by autotest", List.of(new PostFraudTypesRequestBody.FraudTypeWithStatus("CONFIRMED", fraud.getFraudTypeCode())));
        assertThat("Check that request was successful", postFraudTypes(fraud.getUcid(), requestBody).code(), is(200));
        ln.info("fraud " + fraud.getFraudTypeCode() + " successfully sent for client " + fraud.getUcid());
    }

    public static void addFraudsForClient(ClientFraudTypes... frauds) throws IOException {
        for (ClientFraudTypes fraud : frauds) {
            addFraudForClient(fraud);
        }
    }

    public static void addFraudForClient(ClientFraudTypes fraud, String status)
            throws IOException {
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                "Auto Test", "BO", "Set by autotest", List.of(new PostFraudTypesRequestBody.FraudTypeWithStatus(status, fraud.getFraudTypeCode())));
        assertThat("Check that request was successful", postFraudTypes(fraud.getUcid(), requestBody).code(), is(200));
    }


}
