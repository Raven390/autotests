package businessObjects.api.connectionSearchApi;

public class GetConnectionsResponseFactory {

    public static GetConnectionsResponse getConnectionsByClientResponseSuccess(){
        return new GetConnectionsResponse(
                "vantage-99996",
                "vantage-99999",
                1.0,
                new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payout", "463344**** **5603")},
                "Same Person",
                1,
                null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorBadRequest(){
        return new GetConnectionsResponseError(
                null,
                400,
                "Bad Request",
                "/v1/connections/byClientId"
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorClientIdBadRequest(){
        return new GetConnectionsResponseError(
                null,
                400,
                "Invalid &quot;clientId&quot; property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030",
                null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentTypeBadRequest(){
        return new GetConnectionsResponseError(
                null,
                400,
                "DocumentType must be specified once DocumentNumber or DocumentCountryId provided",
                null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentNumberBadRequest(){
        return new GetConnectionsResponseError(
                null,
                400,
                "DocumentNumber must be specified once DocumentType or DocumentCountryId provided",
                null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentCountryIdBadRequest(){
        return new GetConnectionsResponseError(
                null,
                400,
                "DocumentCountryId must be specified once DocumentType or DocumentNumber provided",
                null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorNoSearchParameters(){
        return new GetConnectionsResponseError(
                null,
                400,
                "No search parameters specified",
                null
        );
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentCountryIdNotInt(){
        return new GetConnectionsResponseError(
                null,
                400,
                "Country code must be decimal number consist 1 to 4 digits",
                null
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesDocumentResponseSuccess(){
        return new GetConnectionsResponse(
                "vantage-99991",
                "vantage-99999",
                1.0,
                new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payout", "463344**** **5603")},
                "Same Person",
                1,
                null
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesConnDepthResponseSuccess(){
        return new GetConnectionsResponse(
                "vantage-99991",
                "vantage-99999",
                1.0,
                new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payout", "463344**** **5603")},
                "Same Person",
                2,
                null
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesEmailResponseSuccess(){
        return new GetConnectionsResponse(
                "vantage-99992",
                "vantage-99999",
                1.0,
                new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payout", "463344**** **5603")},
                "Same Person",
                1,
                null
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesIpResponseSuccess(){
        return new GetConnectionsResponse(
                "vantage-99993",
                "vantage-99999",
                1.0,
                new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payout", "463344**** **5603")},
                "Same Person",
                1,
                null
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesPhoneResponseSuccess(){
        return new GetConnectionsResponse(
                "vantage-99994",
                "vantage-99999",
                1.0,
                new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payout", "463344**** **5603")},
                "Same Person",
                1,
                null
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesPayoutResponseSuccess(){
        return new GetConnectionsResponse(
                "vantage-99995",
                "vantage-99999",
                1.0,
                new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payout", "463344**** **5603")},
                "Same Person",
                1,
                null
        );
    }

    public static GetConnectionsResponse getConnectionsByAttributesForDepth(){
        return new GetConnectionsResponse(
                "vantage-99991",
                "vantage-99999",
                1.0,
                new GetConnectionsResponse.ConnectionDetail[]{new GetConnectionsResponse.ConnectionDetail("payout", "463344**** **5603")},
                "Same Person",
                2,
                null
        );
    }
}
