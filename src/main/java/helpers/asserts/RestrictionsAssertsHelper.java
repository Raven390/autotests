package helpers.asserts;

import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.MITIGATION_CLIENT_GENERAL_RESTRICTION;

import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import helpers.data.ClientHelper;
import helpers.data.enums.Restriction;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import java.util.List;

public class RestrictionsAssertsHelper {

    public static void checkManualWithdrawalRestrictionApplied(ClientHelper client, String comment) throws Exception {
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                String.format(
                        "ucid = '%s' AND restriction_id = %d",
                        client.getUcid(), Restriction.MANUAL_WITHDRAWAL_REVIEW.getId()),
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
                is(Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong()));
        assertThat(
                "Restriction.comment should match provided comment",
                clientGeneralRestrictions.getFirst().getComment(),
                is(comment));
        assertThat(
                "Restriction.status should be 'APPLIED'",
                clientGeneralRestrictions.getFirst().getStatus(),
                is("APPLIED"));
    }

    public static void checkNoBonusRestrictionApplied(ClientHelper client, String comment) throws Exception {
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                String.format(
                        "ucid = '%s' AND restriction_id = %d", client.getUcid(), Restriction.CREDIT_AND_BONUS.getId()),
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
                "Restriction.restrictionId should be 9 (No Bonus)",
                clientGeneralRestrictions.getFirst().getRestrictionId(),
                is(Restriction.CREDIT_AND_BONUS.getIdLong()));
        assertThat(
                "Restriction.comment should match provided comment",
                clientGeneralRestrictions.getFirst().getComment(),
                is(comment));
        assertThat(
                "Restriction.status should be 'APPLIED'",
                clientGeneralRestrictions.getFirst().getStatus(),
                is("APPLIED"));
    }

    public static void checkBonusRestrictionNotExists(ClientHelper client, String comment) throws Exception {
        Allure.step("Check that bonus restriction does not exist");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                String.format(
                        "ucid = '%s' AND restriction_id = %d AND comment='%s'",
                        client.getUcid(), Restriction.CREDIT_AND_BONUS.getId(), comment),
                ClientGeneralRestriction.class,
                30);
        assertThat("Bonus restriction should not exist in DB", clientGeneralRestrictions, hasSize(0));
    }
}
