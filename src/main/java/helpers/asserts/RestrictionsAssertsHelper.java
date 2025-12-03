package helpers.asserts;


import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;

import java.util.List;

import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.MITIGATION_CLIENT_GENERAL_RESTRICTION;

public class RestrictionsAssertsHelper {

    public static void checkManualWithdrawalRestrictionApplied(ClientHelper client, String comment) throws Exception {
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", client.getUcid()), ClientGeneralRestriction.class, 30);
        assertThat("Check ucid", clientGeneralRestrictions.getFirst().getUcid(), is(client.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.getFirst().getRegulator(), is(client.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.getFirst().getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.getFirst().getComment(), is(comment));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));
    }
}
