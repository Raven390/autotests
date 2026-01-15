package helpers.database;

import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;
import static utils.Constants.*;
import static utils.Utils.writeLog;

import business_objects.db.abuse_registry_db.Abuser;
import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.DeductionKafkaRequest;
import helpers.data.enums.FraudTypeStatus;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArHelper {

    private static final String WHERE_CONDITION = "ucid IN (%s)";
    private static final String NO_SUCH_CLIENT_IN_AR = "No such client(s) in AR";

    private ArHelper() {}

    public static void deleteUserFromAbuseRegistry(String... ucidList) throws Exception {
        try {
            String ucids = Arrays.stream(ucidList).map(u -> "'" + u + "'").collect(Collectors.joining(", "));
            String condition = String.format(WHERE_CONDITION, ucids);

            deleteDeductions(ucidList);
            deleteObjectFromDb(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, condition);
            deleteObjectFromDb(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, condition);
            deleteObjectFromDb(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, condition);
            deleteObjectFromDb(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, condition);

        } catch (NoSuchElementException e) {
            writeLog(NO_SUCH_CLIENT_IN_AR);
        }
    }

    public static void deleteDeductions(String... ucidList) throws Exception {
        try {
            String ucids = Arrays.stream(ucidList).map(u -> "'" + u + "'").collect(Collectors.joining(", "));
            String condition = String.format(WHERE_CONDITION, ucids);
            List<Integer> deductionIds =
                    getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, condition, AbuserDeduction.class)
                            .stream()
                            .map(AbuserDeduction::getId)
                            .toList();
            if (!deductionIds.isEmpty()) {
                String whereDeductionId = String.format(
                        "deduction_id IN (%s)",
                        deductionIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));
                List<String> messageIds = getObjectsFromDB(
                                DbName.POSTGRES,
                                AR_DEDUCTION_KAFKA_REQUEST_TABLE_NAME,
                                whereDeductionId,
                                DeductionKafkaRequest.class)
                        .stream()
                        .map(DeductionKafkaRequest::getMessageId)
                        .toList();
                if (!messageIds.isEmpty()) {
                    String whereMessageId = String.format(
                            "message_id IN ('%s')",
                            messageIds.stream().map(String::valueOf).collect(Collectors.joining("', '")));
                    deleteObjectFromDb(DbName.POSTGRES, AR_DEDUCTION_KAFKA_REQUEST_TABLE_NAME, whereMessageId);
                    deleteObjectFromDb(DbName.POSTGRES, AR_DEDUCTION_KAFKA_RESPONSE_TABLE_NAME, whereMessageId);
                }
            }
            deleteObjectFromDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, condition);
        } catch (NoSuchElementException e) {
            writeLog(NO_SUCH_CLIENT_IN_AR);
        }
    }

    public static List<String> deleteDeductionsKafkaResponse(String... ucidList) throws Exception {
        try {
            String ucids = Arrays.stream(ucidList).map(u -> "'" + u + "'").collect(Collectors.joining(", "));
            String condition = String.format(WHERE_CONDITION, ucids);
            List<Integer> deductionIds =
                    getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, condition, AbuserDeduction.class)
                            .stream()
                            .map(AbuserDeduction::getId)
                            .toList();
            if (!deductionIds.isEmpty()) {
                String whereDeductionId = String.format(
                        "deduction_id IN (%s)",
                        deductionIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));
                List<String> messageIds = getObjectsFromDB(
                                DbName.POSTGRES,
                                AR_DEDUCTION_KAFKA_REQUEST_TABLE_NAME,
                                whereDeductionId,
                                DeductionKafkaRequest.class)
                        .stream()
                        .map(DeductionKafkaRequest::getMessageId)
                        .toList();
                if (!messageIds.isEmpty()) {
                    String whereMessageId = String.format(
                            "message_id IN ('%s')",
                            messageIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));
                    deleteObjectFromDb(DbName.POSTGRES, AR_DEDUCTION_KAFKA_RESPONSE_TABLE_NAME, whereMessageId);
                    return messageIds;
                }
            }
        } catch (NoSuchElementException e) {
            writeLog(NO_SUCH_CLIENT_IN_AR);
        }
        return List.of();
    }

    public static void waitForClientToChangeStatus(String ucid, FraudTypeStatus status) throws Exception {
        for (int i = 0; i < 5; i++) {
            if (!getObjectsFromDB(
                            DbName.POSTGRES,
                            AR_ABUSER_TABLE_NAME,
                            String.format("ucid = '%s' AND status = '%s'", ucid, status.getStatus()),
                            Abuser.class)
                    .isEmpty()) {
                return;
            } else {
                Thread.sleep(1000);
            }
        }
    }
}
