package business_objects.api.connection_search_api.get_abuse_types_v1;

import business_objects.api.connection_search_api.ConnectionSearchResponseError;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;

public class GetAbuseTypesResponseFactoryV1 {

    public static ConnectionSearchResponseError getConnectionsResponseErrorConnectionScoreToBadRequest() {
        return new ConnectionSearchResponseError(
                null,
                400,
                "Method parameter 'connectionScoreTo': Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
                null,
                null,
                null,
                null,
                null);
    }

    public static ConnectionSearchResponseError getConnectionsResponseErrorConnectionScoreFromBadRequest() {
        return new ConnectionSearchResponseError(
                null,
                400,
                "Method parameter 'connectionScoreFrom': Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
                null,
                null,
                null,
                null,
                null);
    }

    public static ConnectionSearchResponseError getAbuseTypesResponseErrorClientIdMissingBadRequest() {
        return new ConnectionSearchResponseError(
                null,
                400,
                null,
                null,
                "about:blank",
                "Bad Request",
                "Required parameter 'clientId' is not present.",
                "/v1/abuseTypes/byClientId");
    }

    public static ConnectionSearchResponseError getAbuseTypesResponseErrorUnknownAttributeBadRequest() {
        return new ConnectionSearchResponseError(
                null,
                400,
                "Unknown attribute provided: payout. Valid values are: [payoutId, emailAddress, phoneNumber, ipAddress, documentType, documentNumber, documentCountryId, customAttribute, digital, device, session, webSession, nameBirth, nameBirthNoKyc, fuzzyDevice, browserStringHash]",
                null,
                null,
                null,
                null,
                null);
    }

    public static ConnectionSearchResponseError getConnectionsResponseErrorClientIdBadRequest() {
        return new ConnectionSearchResponseError(
                null,
                400,
                "Invalid &quot;clientId&quot; property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetAbuseTypesResponseV1 getAbuseTypesResponseByFraud(ClientFraudTypes fraud, String status) {
        GetAbuseTypesResponseV1 response = new GetAbuseTypesResponseV1();
        response.abuseType = fraud.getFraudTypeCode();
        response.fraudTypeStatus = status;
        response.maxScoreToInitial = 1.0;
        response.maxScoreClientId = fraud.getUcid();
        return response;
    }
}
