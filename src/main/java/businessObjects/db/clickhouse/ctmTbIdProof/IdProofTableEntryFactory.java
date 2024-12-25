package businessObjects.db.clickhouse.ctmTbIdProof;

import helpers.data.ClientHelper;

import static utils.Utils.*;

public class IdProofTableEntryFactory {

    public static IdProofTableEntry getIdProof(ClientHelper client) {
        return new IdProofTableEntry(777, getRandomUuidString(), client.getUcid(), client.getUserId(), client.getBrand(), client.getRegulator(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), "Jason", null, "Sample", "1980-12-14", 1, "REJECTED", "Tomas Timberman", "not all photos provided", "not all photos provided", "ARC", "0500300333", 1, "IND", 21, "Audit Type", 12, "ID_PROOF", getCurrentTimestampDbFormat());
    }
}
