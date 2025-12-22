package business_objects.api.connection_search_api.get_connections;

import static utils.Constants.*;

import helpers.data.ClientHelper;

public class GetConnectionsResponseFactory {

    public static GetConnectionsResponse getConnectionsResponseSuccess(ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                1d,
                1d,
                1d);
    }

    public static GetConnectionsResponse getConnectionsResponseSuccessWithLevel(
            ClientHelper userFrom, ClientHelper userTo, int level) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                level,
                null,
                1d,
                1d,
                1d);
    }

    public static GetConnectionsResponse getConnectionsByClientLvl2ResponseSuccess(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                0.5d,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                0.5d,
                0.5d,
                0.5d);
    }

    public static GetConnectionsResponseError getConnectionsByAttributesResponseErrorBadRequest() {
        return new GetConnectionsResponseError(
                null, 400, "Bad Request", "/v1/connections/byAttributes", "about:blank", null, null, null);
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorClientIdBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                "Invalid &quot;clientId&quot; property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorClientIdMissingBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                null,
                null,
                "about:blank",
                "Bad Request",
                "Required parameter 'clientId' is not present.",
                "/v1/connections/byClientId");
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentTypeBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                "DocumentType must be specified once DocumentNumber or DocumentCountryId provided",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentNumberBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                "DocumentNumber must be specified once DocumentType or DocumentCountryId provided",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentCountryIdBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                "DocumentCountryId must be specified once DocumentType or DocumentNumber provided",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorNoSearchParameters() {
        return new GetConnectionsResponseError(
                null, 400, "No search parameters specified", null, null, null, null, null);
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorDocumentCountryIdNotInt() {
        return new GetConnectionsResponseError(
                null, 400, "Country code must be decimal number consist 1 to 4 digits", null, null, null, null, null);
    }

    public static GetConnectionsResponse getConnectionsByAttributesForDepth(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                1d,
                1d,
                1d);
    }

    public static GetConnectionsResponse[] getConnectionsByClientForFiltrationByParams(
            ClientHelper userFrom, ClientHelper userTo1, ClientHelper userTo2) {
        return new GetConnectionsResponse[] {
            new GetConnectionsResponse(
                    userFrom.getUcid(),
                    userTo1.getUcid(),
                    1.0,
                    new GetConnectionsResponse.ConnectionDetail[] {
                        new GetConnectionsResponse.ConnectionDetail(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                                CONNECTION_TYPE_RELATION_TYPE_EXACT)
                    },
                    CONNECTION_TYPE_SAME_PERSON,
                    1,
                    null,
                    0.800_000_011_920_929,
                    0.800_000_011_920_929,
                    0.800_000_011_920_929),
            new GetConnectionsResponse(
                    userTo1.getUcid(),
                    userTo2.getUcid(),
                    0.800_000_011_920_929,
                    new GetConnectionsResponse.ConnectionDetail[] {
                        new GetConnectionsResponse.ConnectionDetail(
                                CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS,
                                "matisse@gmx.net",
                                "maatiuss@gmail.com",
                                "similar")
                    },
                    CONNECTION_TYPE_SAME_NETWORK,
                    3,
                    null,
                    0.200_000_002_980_232_24,
                    0.200_000_002_980_232_24,
                    0.200_000_002_980_232_24)
        };
    }

    public static GetConnectionsResponse[] getConnectionsForFiltrationByParams(
            ClientHelper userFrom, ClientHelper userTo1, ClientHelper userTo2) {
        return new GetConnectionsResponse[] {
            new GetConnectionsResponse(
                    null,
                    userFrom.getUcid(),
                    0.5,
                    new GetConnectionsResponse.ConnectionDetail[] {
                        new GetConnectionsResponse.ConnectionDetail(
                                CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS,
                                "testfiltration@qatest.com",
                                "testfiltration@qatest.com",
                                CONNECTION_TYPE_RELATION_TYPE_EXACT)
                    },
                    CONNECTION_TYPE_SAME_PERSON,
                    1,
                    null,
                    0.5,
                    0.5,
                    0.5),
            new GetConnectionsResponse(
                    userFrom.getUcid(),
                    userTo1.getUcid(),
                    1.0,
                    new GetConnectionsResponse.ConnectionDetail[] {
                        new GetConnectionsResponse.ConnectionDetail(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                                CONNECTION_TYPE_RELATION_TYPE_EXACT)
                    },
                    CONNECTION_TYPE_SAME_PERSON,
                    2,
                    null,
                    0.5,
                    1d,
                    0.5),
            new GetConnectionsResponse(
                    userTo1.getUcid(),
                    userTo2.getUcid(),
                    0.200_000_002_980_232_24,
                    new GetConnectionsResponse.ConnectionDetail[] {
                        new GetConnectionsResponse.ConnectionDetail(
                                CONNECTION_ATTRIBUTE_NAME_EMAIL, "matisse@gmx.net", "maatiuss@gmail.com", "similar")
                    },
                    CONNECTION_TYPE_SAME_NETWORK,
                    3,
                    null,
                    0.200_000_002_980_232_24,
                    0.200_000_002_980_232_24,
                    0.200_000_002_980_232_24)
        };
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorIncorrectConnectionAttributes() {
        return new GetConnectionsResponseError(
                null,
                400,
                "Unknown attribute provided: test. Valid values are: [payoutId, emailAddress, phoneNumber, ipAddress, documentType, documentNumber, documentCountryId, customAttribute, digital, device, session, webSession, nameBirth, nameBirthNoKyc, fuzzyDevice, browserStringHash, mtCid]",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorConnectionScoreToBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                "Method parameter 'connectionScoreTo': Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorConnectionDepthBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                "Method parameter 'connectionDepth': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \"test\"",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponseError getConnectionsResponseErrorConnectionScoreFromBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                "Method parameter 'connectionScoreFrom': Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponseError getConnectionsByAttributesResponseErrorConnectionScoreToBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                "Method parameter 'connectionScoreTo': Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponseError getConnectionsByAttributesResponseErrorConnectionScoreFromBadRequest() {
        return new GetConnectionsResponseError(
                null,
                400,
                "Method parameter 'connectionScoreFrom': Failed to convert value of type 'java.lang.String' to required type 'java.math.BigDecimal'; Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
                null,
                null,
                null,
                null,
                null);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDocumentInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_DOCUMENT_TYPE,
                            "passport",
                            "passport",
                            CONNECTION_TYPE_RELATION_TYPE_EXACT),
                    new GetConnectionsResponse.ConnectionDetail(
                            "documentNumber", "testaccidnum", "testaccidnum", CONNECTION_TYPE_RELATION_TYPE_EXACT),
                    new GetConnectionsResponse.ConnectionDetail(
                            "documentCountryId", "1", "1", CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                "Same Identity",
                1,
                null,
                1d,
                1d,
                1d);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDocumentLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_DOCUMENT_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                1d,
                1d,
                1d);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessEmailInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                0.5,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS,
                            user.getEmail(),
                            user.getEmail(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                0.5,
                0.5,
                0.5);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessEmailLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                0.5,
                1.0,
                0.5);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessIpInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                0.200_000_002_980_232_24,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_IP_ADDRESS,
                            CONNECTION_SEARCH_DATA_IP3,
                            CONNECTION_SEARCH_DATA_IP3,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_NETWORK,
                1,
                null,
                0.200_000_002_980_232_24,
                0.200_000_002_980_232_24,
                0.200_000_002_980_232_24);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessIpLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                0.200_000_002_980_232_24,
                1d,
                0.200_000_002_980_232_24);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessPhoneInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                0.5,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_PHONE_NUMBER,
                            user.getPhoneNumber(),
                            user.getPhoneNumber(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                0.5,
                0.5,
                0.5);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessPhoneLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_PHONE_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                0.5,
                1d,
                0.5);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessPayoutInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                            "testpayout",
                            "testpayout",
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                1.0,
                1.0,
                1.0);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessPayoutLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                1.0,
                1d,
                1.0);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDeviceIdInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                0.699_999_988_079_071,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_DEVICE,
                            user.getDeviceId(),
                            user.getDeviceId(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                0.699_999_988_079_071,
                0.699_999_988_079_071,
                0.699_999_988_079_071);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDeviceIdLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_DEVICE,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                0.699_999_988_079_071d,
                1d,
                0.699_999_988_079_071d);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDigitalIdInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                0.699_999_988_079_071,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_DIGITAL,
                            user.getDigitalId(),
                            user.getDigitalId(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                0.699_999_988_079_071,
                0.699_999_988_079_071,
                0.699_999_988_079_071);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessDigitalIdLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_DIGITAL,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                0.699_999_988_079_071,
                1d,
                0.699_999_988_079_071);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessNameBirthInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                0.800_000_011_920_929,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_NAME_BIRTH,
                            user.getNameDateOfBirth(),
                            user.getNameDateOfBirth(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                0.800_000_011_920_929,
                0.800_000_011_920_929,
                0.800_000_011_920_929);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessNameBirthLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_NAME_BIRTH,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                0.800_000_011_920_929d,
                1d,
                0.800_000_011_920_929d);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessSessionIdInitial(ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_SESSION,
                            user.getSessionId(),
                            user.getSessionId(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                1.0,
                1.0,
                1.0);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessSessionIdLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_SESSION,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                1.0,
                1d,
                1.0);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessWebSessionIdInitial(
            ClientHelper user) {
        return new GetConnectionsResponse(
                null,
                user.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_WEB_SESSION,
                            user.getWebSessionId(),
                            user.getWebSessionId(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                1.0,
                1.0,
                1.0);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessWebSessionIdLvl2(
            ClientHelper userFrom, ClientHelper userTo) {
        return new GetConnectionsResponse(
                userFrom.getUcid(),
                userTo.getUcid(),
                1.0,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_WEB_SESSION,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_SEARCH_DATA_CARD_NUMBER,
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                2,
                null,
                1.0,
                1d,
                1.0);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessMtCidLvl1(
            ClientHelper userFrom, String ucid) {
        return new GetConnectionsResponse(
                null,
                ucid,
                0.360_000_014_305_114_75,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_IP_ADDRESS,
                            userFrom.getIpAddress(),
                            userFrom.getIpAddress(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT),
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_MT_CID,
                            userFrom.getMtCid(),
                            userFrom.getMtCid(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_NETWORK,
                1,
                null,
                0.360_000_014_305_114_75,
                0.360_000_014_305_114_75,
                0.360_000_014_305_114_75);
    }

    public static GetConnectionsResponse getConnectionsByAttributesResponseSuccessMtCidEmailLvl1(
            ClientHelper userFrom, String ucid) {
        return new GetConnectionsResponse(
                null,
                ucid,
                0.600_000_023_841_857_9,
                new GetConnectionsResponse.ConnectionDetail[] {
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS,
                            userFrom.getEmail(),
                            userFrom.getEmail(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT),
                    new GetConnectionsResponse.ConnectionDetail(
                            CONNECTION_ATTRIBUTE_NAME_MT_CID,
                            userFrom.getMtCid(),
                            userFrom.getMtCid(),
                            CONNECTION_TYPE_RELATION_TYPE_EXACT)
                },
                CONNECTION_TYPE_SAME_PERSON,
                1,
                null,
                0.600_000_023_841_857_9,
                0.600_000_023_841_857_9,
                0.600_000_023_841_857_9);
    }
}
