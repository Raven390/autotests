package businessObjects.api.connectionSearchApi.getConnections;

import helpers.data.ClientHelper;

public class GetConnectionsResponseFactory {

    public static GetConnectionsResponse getConnectionsResponseSuccess(ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 1, null, 1d, 1d, 1d
        );
    }

    public static GetConnectionsResponse getConnectionsByClientLvl2ResponseSuccess(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 0.5d, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 0.5d, 0.5d, 0.5d
        );
    }

    public static GetConnectionsResponseError getConnectionsByAttributesResponseErrorBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "Bad Request", "/v1/connections/byAttributes", "about:blank", null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorClientIdBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "Invalid &quot;clientId&quot; property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorClientIdMissingBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, null, null, "about:blank", "Bad Request", "Required parameter 'clientId' is not present.", "/v1/connections/byClientId"
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentTypeBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "DocumentType must be specified once DocumentNumber or DocumentCountryId provided", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentNumberBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "DocumentNumber must be specified once DocumentType or DocumentCountryId provided", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentCountryIdBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "DocumentCountryId must be specified once DocumentType or DocumentNumber provided", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorNoSearchParameters() {
        return new GetConnectionsResponseError(
                null, 400, "No search parameters specified", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentCountryIdNotInt() {
        return new GetConnectionsResponseError(
                null, 400, "Country code must be decimal number consist 1 to 4 digits", null, null, null, null, null
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesForDepth(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 1d, 1d, 1d
        );
    }

    public static GetConnectionsResponse[] getConnectionsForFiltrationByParams(ClientHelper userFrom,
            ClientHelper userTo1, ClientHelper userTo2) {
        return new GetConnectionsResponse[]{new GetConnectionsResponse(null, userFrom.getUcid(), 0.5, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("emailAddress", "testfiltration@qatest.com", "testfiltration@qatest.com", "exact")}, "Same Person", 1, null, 0.5, 0.5, 0.5), new GetConnectionsResponse(userFrom.getUcid(), userTo1.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 0.5, 1d, 0.5), new GetConnectionsResponse(userTo1.getUcid(), userTo2.getUcid(), 0.200_000_002_980_232_24, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("emailAddress", "matisse@gmx.net", "maatiuss@gmail.com", "similar")}, "Same Network", 3, null, 0.200_000_002_980_232_24, 0.200_000_002_980_232_24, 0.200_000_002_980_232_24)
        };
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorIncorrectConnectionAttributes() {
        return new GetConnectionsResponseError(
                null, 400, "Unknown attribute provided: test. Valid arguments are: [payoutId, emailAddress, phoneNumber, ipAddress, documentType, documentNumber, documentCountryId, customAttribute, digital, device, session, webSession, nameBirth]", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorConnectionScoreToBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorConnectionDepthBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \"test\"", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorConnectionScoreFromBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsByAttributesResponseErrorConnectionScoreToBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsByAttributesResponseErrorConnectionScoreFromBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.", null, null, null, null, null
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDocumentInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("documentType", "passport", "passport", "exact"), new GetConnectionsResponse.ConnectionDetail("documentNumber", "testaccidnum", "testaccidnum", "exact"), new GetConnectionsResponse.ConnectionDetail("documentCountryId", "1", "1", "exact")}, "Same Identity", 1, null, 1d, 1d, 1d
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDocumentLvl2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 1d, 1d, 1d
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessEmailInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 0.5, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("emailAddress", "test@email.com", "test@email.com", "exact")}, "Same Person", 1, null, 0.5, 0.5, 0.5
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessEmailLvl2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 0.5, 1d, 0.5
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessIpInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 0.200_000_002_980_232_24, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("ipAddress", "111.111.111.111", "111.111.111.111", "exact")}, "Same Network", 1, null, 0.200_000_002_980_232_24, 0.200_000_002_980_232_24, 0.200_000_002_980_232_24
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessIpLvl2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 0.200_000_002_980_232_24, 1d, 0.200_000_002_980_232_24
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessPhoneInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 0.5, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("phoneNumber", user.getPhoneNumber(), user.getPhoneNumber(), "exact")}, "Same Person", 1, null, 0.5, 0.5, 0.5
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessPhoneLvl2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 0.5, 1d, 0.5
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessPayoutInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "testpayout", "testpayout", "exact")}, "Same Person", 1, null, 1.0, 1.0, 1.0
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessPayoutLvl2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 1.0, 1d, 1.0
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDeviceIdInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 0.699_999_988_079_071, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("device", user.getDeviceId(), user.getDeviceId(), "exact")}, "Same Person", 1, null, 0.699_999_988_079_071, 0.699_999_988_079_071, 0.699_999_988_079_071
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDeviceIdLvl2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 0.699_999_988_079_071, 1d, 0.699_999_988_079_071
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDigitalIdInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 0.699_999_988_079_071, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("digital", user.getDigitalId(), user.getDigitalId(), "exact")}, "Same Person", 1, null, 0.699_999_988_079_071, 0.699_999_988_079_071, 0.699_999_988_079_071
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDigitalIdLvl2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 0.699_999_988_079_071, 1d, 0.699_999_988_079_071
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessNameBirthInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 0.800_000_011_920_929, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("nameBirth", user.getNamedateofbirth(), user.getNamedateofbirth(), "exact")}, "Same Person", 1, null, 0.800_000_011_920_929, 0.800_000_011_920_929, 0.800_000_011_920_929
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessNameBirthLvl2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 0.800_000_011_920_929, 1d, 0.800_000_011_920_929
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessSessionIdInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("session", user.getSessionId(), user.getSessionId(), "exact")}, "Same Person", 1, null, 1.0, 1.0, 1.0
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessSessionIdLvl2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 1.0, 1d, 1.0
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessWebSessionIdInitial(
            ClientHelper user) {
        return new GetConnectionsResponse(
                null, user.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("webSession", user.getWebSessionId(), user.getWebSessionId(), "exact")}, "Same Person", 1, null, 1.0, 1.0, 1.0
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessWebSessionIdLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344", "535456**** **0344", "exact")}, "Same Person", 2, null, 1.0, 1d, 1.0
        );
    }
}
