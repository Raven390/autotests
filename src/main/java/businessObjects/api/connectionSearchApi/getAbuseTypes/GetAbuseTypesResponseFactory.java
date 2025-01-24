package businessObjects.api.connectionSearchApi.getAbuseTypes;


import businessObjects.api.connectionSearchApi.ConnectionSearchResponseError;

public class GetAbuseTypesResponseFactory {

    public static ConnectionSearchResponseError getConnectionsResponseErrorConnectionScoreToBadRequest() {
        return new ConnectionSearchResponseError(
                null, 400, "Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.", null, null, null, null, null
        );
    }

    public static ConnectionSearchResponseError getConnectionsResponseErrorConnectionScoreFromBadRequest() {
        return new ConnectionSearchResponseError(
                null, 400, "Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.", null, null, null, null, null
        );
    }

    public static ConnectionSearchResponseError getAbuseTypesResponseErrorClientIdMissingBadRequest() {
        return new ConnectionSearchResponseError(
                null, 400, null, null, "about:blank", "Bad Request", "Required parameter 'clientId' is not present.", "/v1/abuseTypes/byClientId"
        );
    }

    public static ConnectionSearchResponseError getAbuseTypesResponseErrorUnknownAttributeBadRequest() {
        return new ConnectionSearchResponseError(
                null, 400, "Unknown attribute provided: payout. Valid arguments are: [payoutId, emailAddress, phoneNumber, ipAddress, documentType, documentNumber, documentCountryId, customAttribute, digital, device, session, webSession, nameBirth]", null, null, null, null, null
        );
    }

    public static ConnectionSearchResponseError getConnectionsResponseErrorClientIdBadRequest() {
        return new ConnectionSearchResponseError(
                null, 400, "Invalid &quot;clientId&quot; property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030", null, null, null, null, null
        );
    }
}
