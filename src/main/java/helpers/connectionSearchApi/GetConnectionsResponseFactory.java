package helpers.connectionSearchApi;

public class GetConnectionsResponseFactory {

    public static GetConnectionsResponse getConnectionsResponseSuccess(){
        return new GetConnectionsResponse(
                "vantage-autotest1",
                "vantage-autotest2",
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
}
