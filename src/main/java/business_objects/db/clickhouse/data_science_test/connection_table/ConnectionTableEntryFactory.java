package business_objects.db.clickhouse.data_science_test.connection_table;

import helpers.data.ClientHelper;
import helpers.data.enums.ConnectionAttributes;

import java.util.List;

import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;

public class ConnectionTableEntryFactory {

    public static ConnectionTableEntry getConnectionTableEntry(ClientHelper userFrom, ClientHelper userTo,
            ConnectionAttributes connectionAttrName) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(connectionAttrName.toString(), CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntry(ClientHelper userFrom, ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntry(ClientHelper userFrom, ClientHelper userTo,
            String ipAddress) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_IP, ipAddress, ipAddress, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryLvl2(ClientHelper userFrom, ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 0.5d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForFiltration(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_NETWORK, 0.2d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_EMAIL, CONNECTION_SEARCH_DATA_EMAIL1, CONNECTION_SEARCH_DATA_EMAIL2, CONNECTION_TYPE_RELATION_TYPE_SIMILAR)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUi(ClientHelper userFrom, ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_IP, CONNECTION_SEARCH_DATA_IP1, CONNECTION_SEARCH_DATA_IP1, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DIGITAL, CONNECTION_SEARCH_DATA_DIGITAL, CONNECTION_SEARCH_DATA_DIGITAL, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_EMAIL, CONNECTION_SEARCH_DATA_EMAIL1, CONNECTION_SEARCH_DATA_EMAIL1, CONNECTION_TYPE_RELATION_TYPE_SIMILAR), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PHONE, ENCODED_PHONE, ENCODED_PHONE, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DOCUMENT, CONNECTION_SEARCH_DATA_DOCUMENT, CONNECTION_SEARCH_DATA_DOCUMENT, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_NAME_AND_BIRTH, CONNECTION_SEARCH_DATA_NAME_BIRTH, CONNECTION_SEARCH_DATA_NAME_BIRTH, CONNECTION_TYPE_RELATION_TYPE_EXACT), new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_SESSION, CONNECTION_SEARCH_DATA_SESSION, CONNECTION_SEARCH_DATA_SESSION, CONNECTION_TYPE_RELATION_TYPE_EXACT)
                ), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration1(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration2(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration3(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 0.2d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration4(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, 0.7d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE2, CONNECTION_SEARCH_DATA_DEVICE2, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration5(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_NETWORK, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE2, CONNECTION_SEARCH_DATA_DEVICE2, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUiFiltration6(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 0.5d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DOCUMENT, CONNECTION_SEARCH_DATA_DOCUMENT, CONNECTION_SEARCH_DATA_DOCUMENT, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }

    public static ConnectionTableEntry getConnectionTableEntry(ClientHelper userFrom, ClientHelper userTo,
            Double connectionScore) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), CONNECTION_TYPE_SAME_PERSON, connectionScore, List.of(
                        new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat(), STATUS_NEW, 0
        );
    }


    public static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT
                        )), getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient,
            Double connectionScore) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, connectionScore, List.of(
                        new ConnectionTableEntry.ConnectionInfo(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT
                        )), getCurrentTimestampDbFormat()
        );
    }
}
