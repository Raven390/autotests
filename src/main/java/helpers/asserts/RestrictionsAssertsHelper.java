package helpers.asserts;

import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static utils.Constants.MITIGATION_CLIENT_GENERAL_RESTRICTION;
import static utils.Constants.MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION;

import business_objects.api.mitigation_service.CorrelationType;
import business_objects.api.mitigation_service.RestrictionStatus;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.db.mitigation_service_db.client_trading_environment_restriction.ClientTradingEnvironmentRestrictionEntity;
import helpers.data.DataHelper;
import helpers.data.enums.Restriction;
import helpers.database.DbName;
import io.qameta.allure.Step;
import java.util.List;

public class RestrictionsAssertsHelper {
    private static final String RESTRICTION_QUERY = "ucid = '%s' AND restriction_id = %d";
    private static final String ASSERT_RESTRICTION_TITLE = "Assert restriction";

    @Step("Check that manual withdrawal restriction is applied")
    public static void checkManualWithdrawalRestrictionApplied(DataHelper data, String comment) throws Exception {
        List<ClientGeneralRestriction> restrictions = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                String.format(
                        RESTRICTION_QUERY, data.clientHelper.getUcid(), Restriction.MANUAL_WITHDRAWAL_REVIEW.getId()),
                ClientGeneralRestriction.class,
                30);
        assertThat(
                "Restriction.ucid should match client's ucid",
                restrictions.getFirst().getUcid(),
                is(data.clientHelper.getUcid()));
        assertThat(
                "Restriction.regulator should match client's regulator",
                restrictions.getFirst().getRegulator(),
                is(data.clientHelper.getRegulator()));
        assertThat(
                "Restriction.restrictionId should be 8 (Manual Withdrawal)",
                restrictions.getFirst().getRestrictionId(),
                is(Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong()));
        assertThat(
                "Restriction.comment should match provided comment",
                restrictions.getFirst().getComment(),
                is(comment));
        assertThat(
                "Restriction.status should be 'APPLIED'",
                restrictions.getFirst().getStatus(),
                is(RestrictionStatus.APPLIED.getValue()));
    }

    @Step("Check that worse trading restriction is applied")
    public static void checkWorseTradingRestrictionApplied(DataHelper data, String comment) throws Exception {
        List<ClientTradingEnvironmentRestrictionEntity> restriction = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION,
                String.format(RESTRICTION_QUERY, data.clientHelper.getUcid(), Restriction.WORSE_TRADING.getId()),
                ClientTradingEnvironmentRestrictionEntity.class,
                30);
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getId(), is(notNullValue()));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat(
                ASSERT_RESTRICTION_TITLE, restriction.getFirst().getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat(
                ASSERT_RESTRICTION_TITLE,
                restriction.getFirst().getAccountId(),
                is(Long.valueOf(data.clientHelper.getTradingAccount())));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getServerId(), is(data.clientHelper.getServerId()));
        assertThat(
                ASSERT_RESTRICTION_TITLE,
                restriction.getFirst().getRestrictionId(),
                is(Restriction.WORSE_TRADING.getId()));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getLevel(), is("LOW"));
        assertThat(
                ASSERT_RESTRICTION_TITLE, restriction.getFirst().getStatus(), is(RestrictionStatus.APPLIED.getValue()));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getComment(), is(comment));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getApplicationReason(), is(nullValue()));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getCancellationReason(), is(nullValue()));
        assertThat(
                ASSERT_RESTRICTION_TITLE,
                restriction.getFirst().getCorrelationType(),
                is(CorrelationType.RULE_ENGINE.getValue()));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getCorrelationId(), is(instanceOf(String.class)));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getFailReason(), is(nullValue()));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getCreatedAt(), is(instanceOf(String.class)));
        assertThat(ASSERT_RESTRICTION_TITLE, restriction.getFirst().getUpdatedAt(), is(instanceOf(String.class)));
    }

    @Step("Check that no bonus restriction is applied")
    public static void checkNoBonusRestrictionApplied(DataHelper data, String comment) throws Exception {
        List<ClientGeneralRestriction> restrictions = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                String.format(RESTRICTION_QUERY, data.clientHelper.getUcid(), Restriction.CREDIT_AND_BONUS.getId()),
                ClientGeneralRestriction.class,
                30);
        assertThat(
                "Restriction.ucid should match client's ucid",
                restrictions.getFirst().getUcid(),
                is(data.clientHelper.getUcid()));
        assertThat(
                "Restriction.regulator should match client's regulator",
                restrictions.getFirst().getRegulator(),
                is(data.clientHelper.getRegulator()));
        assertThat(
                "Restriction.restrictionId should be 9 (No Bonus)",
                restrictions.getFirst().getRestrictionId(),
                is(Restriction.CREDIT_AND_BONUS.getIdLong()));
        assertThat(
                "Restriction.comment should match provided comment",
                restrictions.getFirst().getComment(),
                is(comment));
        assertThat(
                "Restriction.status should be 'APPLIED'",
                restrictions.getFirst().getStatus(),
                is(RestrictionStatus.APPLIED.getValue()));
    }

    @Step("Check that bonus restriction does not exist")
    public static void checkBonusRestrictionNotExists(DataHelper data, String comment) throws Exception {
        List<ClientGeneralRestriction> restrictions = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                String.format(
                        "ucid = '%s' AND restriction_id = %d AND comment='%s'",
                        data.clientHelper.getUcid(), Restriction.CREDIT_AND_BONUS.getId(), comment),
                ClientGeneralRestriction.class,
                30);
        assertThat("Bonus restriction should not exist in DB", restrictions, hasSize(0));
    }
}
