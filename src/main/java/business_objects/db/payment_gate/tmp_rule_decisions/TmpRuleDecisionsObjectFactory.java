package business_objects.db.payment_gate.tmp_rule_decisions;


import helpers.data.ClientHelper;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class TmpRuleDecisionsObjectFactory {

    public static TmpRuleDecisionsObject generateTmpRuleDecisionsObject(ClientHelper client) {
        return new TmpRuleDecisionsObject(
                getRandomIntPositive(),                                           // id
                client != null ? client.getUcid() : null,       // paymentId
                "UNKNOWN",                                     // decision
                getCurrentTimestampDbFormat()                   // dateCreated
        );
    }


}
