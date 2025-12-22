package helpers.asserts;

import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.MITIGATION_CLIENT_GENERAL_RESTRICTION;

import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import java.util.List;

public class RestrictionsAssertsHelper {

    public static void checkManualWithdrawalRestrictionApplied(ClientHelper client, String comment) throws Exception {
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                String.format("ucid = '%s'", client.getUcid()),
                ClientGeneralRestriction.class,
                30);
        assertThat(
                "Restriction.ucid should match client's ucid",
                clientGeneralRestrictions.getFirst().getUcid(),
                is(client.getUcid()));
        assertThat(
                "Restriction.regulator should match client's regulator",
                clientGeneralRestrictions.getFirst().getRegulator(),
                is(client.getRegulator()));
        assertThat(
                "Restriction.restrictionId should be 8 (Manual Withdrawal)",
                clientGeneralRestrictions.getFirst().getRestrictionId(),
                is(8L));
        assertThat(
                "Restriction.comment should match provided comment",
                clientGeneralRestrictions.getFirst().getComment(),
                is(comment));
        assertThat(
                "Restriction.status should be 'APPLIED'",
                clientGeneralRestrictions.getFirst().getStatus(),
                is("APPLIED"));
    }
}
