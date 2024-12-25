package businessObjects.api.connectionSearchApi;

import helpers.data.ClientHelper;

public class GetConnectionsResponseFactory {

    public static GetConnectionsResponse getConnectionsResponseSuccess(ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344")}, "Same Person", 1, null, 1d, 1d, 1d
        );
    }

    public static GetConnectionsResponse getConnectionsByClientLvl2ResponseSuccess(ClientHelper userFrom,
            ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(), userTo.getUcid(), 0.5d, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344")}, "Same Person", 2, null, 0.5d, 0.5d, 0.5d
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
                userFrom.getUcid(), userTo.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344")}, "Same Person", 2, null, 1d, 1d, 1d
        );
    }

    public static GetConnectionsResponse[] getConnectionsForFiltrationByParams(ClientHelper userFrom,
            ClientHelper userTo1, ClientHelper userTo2) {
        return new GetConnectionsResponse[]{new GetConnectionsResponse(
                userFrom.getUcid(), userTo1.getUcid(), 1.0, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payoutId", "535456**** **0344")}, "Same Person", 1, null, 1d, 1d, 1d
        ), new GetConnectionsResponse(
                userTo1.getUcid(), userTo2.getUcid(), 0.2d, new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("emailAddress", "wcJSyCAcOAT2WPuuoN+t6Z/WaBiHSPPa")}, "Same Network", 2, null, 0.2d, 0.2d, 0.2d
        )
        };
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorIncorrectConnectionAttributes() {
        return new GetConnectionsResponseError(
                null, 400, "Unknown attribute provided: test. Valid arguments are: [payoutId, emailAddress, phoneNumber, ipAddress, documentType, documentNumber, documentCountryId, customAttribute, digital, device, session, webSession, nameBirth]", null, null, null, null, null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorConnectionScoreToBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, null, null, "about:blank", "Bad Request", "Failed to convert 'connectionScoreTo' with value: 'test'", "/v1/connections/byClientId"
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorConnectionDepthBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, null, null, "about:blank", "Bad Request", "Failed to convert 'connectionDepth' with value: 'test'", "/v1/connections/byClientId"
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorConnectionScoreFromBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, null, null, "about:blank", "Bad Request", "Failed to convert 'connectionScoreFrom' with value: 'test'", "/v1/connections/byClientId"
        );
    }

    public static GetConnectionsResponseError getConnectionsByAttributesResponseErrorConnectionScoreToBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, null, null, "about:blank", "Bad Request", "Failed to convert 'connectionScoreTo' with value: 'test'", "/v1/connections/byAttributes"
        );
    }

    public static GetConnectionsResponseError getConnectionsByAttributesResponseErrorConnectionScoreFromBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, null, null, "about:blank", "Bad Request", "Failed to convert 'connectionScoreFrom' with value: 'test'", "/v1/connections/byAttributes"
        );
    }
}
