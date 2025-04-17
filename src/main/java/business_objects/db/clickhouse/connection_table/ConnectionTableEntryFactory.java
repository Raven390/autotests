package business_objects.db.clickhouse.connection_table;

import helpers.data.ClientHelper;

import java.util.List;

import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;

public class ConnectionTableEntryFactory {

    public static ConnectionTableEntry getConnectionTableEntry(ClientHelper userFrom, ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntry(ClientHelper userFrom, ClientHelper userTo,
            String ipAddress) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_IP, ipAddress, ipAddress, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryLvl2(ClientHelper userFrom, ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 0.5d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForFiltration(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_NETWORK, 0.2d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_EMAIL, CONNECTION_SEARCH_DATA_EMAIL1, CONNECTION_SEARCH_DATA_EMAIL2, CONNECTION_TYPE_RELATION_TYPE_SIMILAR)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUi(ClientHelper userFrom, ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_IP, CONNECTION_SEARCH_DATA_IP1, CONNECTION_SEARCH_DATA_IP1, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DIGITAL, "5784170d787442f8945926a2c9b24c40", "5784170d787442f8945926a2c9b24c40", CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_EMAIL, "matisse@gmx.net", "matisse@gmx.net", CONNECTION_TYPE_RELATION_TYPE_SIMILAR), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PHONE, "cTsGbMYzhsD5SxSOhmgpmQ==", "cTsGbMYzhsD5SxSOhmgpmQ==", CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DOCUMENT, "3110200460092", "3110200460092", CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_NAME_AND_BIRTH, "kanjana sirajindapirom 1978-02-17", "kanjana sirajindapirom 1978-02-17", CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_SESSION, "63767f3e2a9340efafd32c354660b128", "63767f3e2a9340efafd32c354660b128", CONNECTION_TYPE_RELATION_TYPE_EXACT)
                ), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration1(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration3(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 0.2d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration4(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 0.7d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE2, CONNECTION_SEARCH_DATA_DEVICE2, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration5(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_NETWORK, 0.2d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE2, CONNECTION_SEARCH_DATA_DEVICE2, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration6(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 0.5d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DOCUMENT, CONNECTION_SEARCH_DATA_DOCUMENT, CONNECTION_SEARCH_DATA_DOCUMENT, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntry(ClientHelper userFrom, ClientHelper userTo,
            Double connectionScore) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, connectionScore, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat()
        );
    }
}
