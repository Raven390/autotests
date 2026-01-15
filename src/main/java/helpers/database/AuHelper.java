package helpers.database;

import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.db.audit_service_db.AuditEvent;
import io.qameta.allure.Allure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuHelper {
    public static void cleanClientAudit(String... ucid) throws Exception {
        String ucids = Arrays.stream(ucid).map(u -> "'" + u + "'").collect(Collectors.joining(", "));
        String condition = "ucid IN (" + ucids + ")";
        deleteObjectFromDb(DbName.POSTGRES, AUDIT_EVENT_TABLE, condition);
    }

    public static void checkRestrictionApplyAudit(String ucid) throws Exception {
        Allure.step("check that record about restriction apply appeared in the audit trail");
        List<AuditEvent> events = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            events = getObjectsFromDB(
                    POSTGRES, AUDIT_EVENT_TABLE, "ucid = '" + ucid + "' ORDER BY happened_at ASC", AuditEvent.class);
            if (events.size() >= 2) {
                break;
            } else if (i == 9) {
                assertThat("Assert that there are 3 events in audit", events.size(), greaterThanOrEqualTo(3));
            }
            Thread.sleep(1000);
        }
        AuditEvent event1 = events.getFirst();
        AuditEvent event2 = events.get(1);
        AuditEvent event3 = events.getLast();
        assertThat(
                event1.getType(),
                is(oneOf(RESTRICTION_REQUESTED_STATUS, RESTRICTION_APPLIED_STATUS, COMMENT_ADDED_TYPE)));
        assertThat(
                event2.getType(),
                is(oneOf(RESTRICTION_REQUESTED_STATUS, RESTRICTION_APPLIED_STATUS, COMMENT_ADDED_TYPE)));
        assertThat(
                event3.getType(),
                is(oneOf(RESTRICTION_REQUESTED_STATUS, RESTRICTION_APPLIED_STATUS, COMMENT_ADDED_TYPE)));
    }

    public static void checkRestrictionCancelAudit(String ucid) throws Exception {
        List<AuditEvent> events = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            events = getObjectsFromDB(
                    POSTGRES, AUDIT_EVENT_TABLE, "ucid = '" + ucid + "' ORDER BY happened_at DESC", AuditEvent.class);
            if (events.size() >= 6) {
                break;
            } else if (i == 9) {
                assertThat("Assert that there are 3 events in audit", events.size(), greaterThanOrEqualTo(3));
            }
            Thread.sleep(1000);
        }
        AuditEvent event1 = events.getFirst();
        AuditEvent event2 = events.get(1);
        AuditEvent event3 = events.get(2);
        assertThat(
                event1.getType(),
                is(oneOf(CANCELLATION_REQUESTED_STATUS, RESTRICTION_CANCELLED_STATUS, COMMENT_ADDED_TYPE)));
        assertThat(
                event2.getType(),
                is(oneOf(CANCELLATION_REQUESTED_STATUS, RESTRICTION_CANCELLED_STATUS, COMMENT_ADDED_TYPE)));
        assertThat(
                event3.getType(),
                is(oneOf(CANCELLATION_REQUESTED_STATUS, RESTRICTION_CANCELLED_STATUS, COMMENT_ADDED_TYPE)));
    }
}
