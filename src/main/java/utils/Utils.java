package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

import business_objects.api.connection_search_api.get_connections.GetConnectionsResponse;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.ticks.rates_usd_current.RatesUsdCurrentObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.DateTimeFormat;
import helpers.database.DbName;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.logging.Logger;

import static business_objects.api.connection_search_api.get_connections.GetConnectionsRequest.getConnectionsByClientId;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntry;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.BoHelper.getUserIdByUser;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.CLICKHOUSE;
import static helpers.database.DbName.POSTGRES;
import static org.junit.jupiter.api.Assertions.fail;
import static utils.Constants.*;

public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    static SecureRandom random = new SecureRandom();

    public static Integer getRandomInt() {
        return random.nextInt();
    }

    public static Double getRandomDouble() {
        return random.nextDouble();
    }

    public static String startFromUpper(String lowerCase) {
        return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
    }

    public static Integer getRandomIntPositive() {
        return random.nextInt(Integer.MAX_VALUE) + 1;
    }

    public static Integer getRandomIntPositiveWithBounds(Integer boundLow, Integer boundHigh) {
        return random.nextInt(boundLow, boundHigh);
    }

    public static Long getRandomLongPositive() {
        return random.nextLong(Long.MAX_VALUE) + 1;
    }

    public static String getRandomUuidString() {
        return UUID.randomUUID().toString();
    }

    public static UUID getRandomUuid() {
        return UUID.randomUUID();
    }

    public static String getRandomEmail() {
        String email = "testmail" + getRandomInt() + "@mail.com";
        logger.info("Generated email: " + email);
        return email;
    }

    public static long getCurrentTimestampSeconds() {
        return Instant.now().getEpochSecond();
    }

    public static long getCurrentTimestampMillis() {
        return Instant.now().toEpochMilli();
    }

    /**
     * Converts a timestamp in milliseconds to ISO 8601 format with milliseconds and Z timezone indicator.
     * Example: 2025-08-01T07:16:56.099Z
     *
     * @param timestampMillis timestamp in milliseconds
     * @return formatted date-time string
     */
    public static String convertTimestampToIsoFormat(long timestampMillis) {
        return Instant.ofEpochMilli(timestampMillis).toString();
    }

    /**
     * Returns a random ISO 8601 UTC date-time string between now minus 365 days and now.
     * Example: 2025-05-20T14:30:00Z
     */
    public static String getRandomDateTimeIsoUtc() {
        long now = Instant.now().toEpochMilli();
        long yearAgo = Instant.now().minus(365, ChronoUnit.DAYS).toEpochMilli();
        long randomMillis = ThreadLocalRandom.current().nextLong(yearAgo, now);
        return Instant.ofEpochMilli(randomMillis).truncatedTo(ChronoUnit.SECONDS).toString();
    }

    public static String getCurrentTimestampMinusOffsetFormatted(String format, int years, int months, int days,
            int hours, int minutes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.US);
        return LocalDateTime.now(ZoneOffset.UTC).minusYears(years).minusMonths(months).minusDays(days).minusHours(hours).minusMinutes(minutes).format(formatter);
    }

    public static String getCurrentTimestampMinusOffsetFormatted(String format, int years, int months, int days,
            int hours, int minutes, int seconds) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.US);
        return LocalDateTime.now(ZoneOffset.UTC).minusYears(years).minusMonths(months).minusDays(days).minusHours(hours).minusMinutes(minutes).minusSeconds(seconds).format(formatter);
    }

    public static String getCurrentTimestampMinusOffsetFormatted(DateTimeFormat format, int years, int months, int days,
            int hours, int minutes) {
        return getCurrentTimestampMinusOffsetFormatted(format.getDisplayName(), years, months, days, hours, minutes, 0);
    }

    public static String getCurrentTimestampMinusOffsetFormatted(DateTimeFormat format, int years, int months, int days,
            int hours) {
        return getCurrentTimestampMinusOffsetFormatted(format.getDisplayName(), years, months, days, hours, 0, 0);
    }

    public static String getCurrentTimestampMinusOffsetFormatted(DateTimeFormat format, int years, int months,
            int days) {
        return getCurrentTimestampMinusOffsetFormatted(format.getDisplayName(), years, months, days, 0, 0, 0);
    }

    public static String getCurrentTimestampMinusOffsetFormatted(DateTimeFormat format, int years, int months) {
        return getCurrentTimestampMinusOffsetFormatted(format.getDisplayName(), years, months, 0, 0, 0, 0);
    }

    public static String getCurrentTimestampMinusOffsetFormatted(DateTimeFormat format, int years) {
        return getCurrentTimestampMinusOffsetFormatted(format.getDisplayName(), years, 0, 0, 0, 0, 0);
    }

    public static String getCurrentTimestampMinusOffsetFormatted(DateTimeFormat format, int years, int months, int days,
            int hours, int minutes, int seconds) {
        return getCurrentTimestampMinusOffsetFormatted(format.getDisplayName(), years, months, days, hours, minutes, seconds);
    }

    public static String getCurrentTimestampDbFormat() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 0);
    }

    @Deprecated
    public static String getCurrentTimestampDbFormatMinusDays(int days) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, days, 0, 0);
    }

    @Deprecated
    public static String getTomorrowTimestampDbFormat() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, -1, 0, 0);
    }

    @Deprecated
    public static String getYesterdayTimestampDbFormat() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0);
    }

    @Deprecated
    public static String getPreviousWeekTimestampDbFormat() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 6, 0, 0);
    }

    @Deprecated
    public static String getNextYearTimestampDbFormat() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, -1, 0, 0, 0, 0);
    }

    @Deprecated
    public static String getPreviousYearTimestampDbFormat() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 1, 0, 0, 0, 0);
    }

    public static String getPreviousWeekDate() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 6, 0, 0);
    }

    @Deprecated
    public static String getPrevious90DaysTimestampYearMonthDay() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 90, 0, 0);
    }

    @Deprecated
    public static String getPreviousMonthTimestampYearMonthDay() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 1, 0, 0, 0);
    }

    @Deprecated
    public static String getPrevious6MonthTimestampYearMonthDay() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 6, 0, 0, 0);
    }

    @Deprecated
    public static String getPreviousYearTimestampYearMonthDay() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 1, 0, 0, 0, 0);
    }

    public static String getYesterdayDate() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0);
    }

    @Deprecated
    public static String getCurrentDateTime() {
        return getCurrentTimestampMinusOffsetFormatted("yyyy-MM-dd_HH-mm-ss", 0, 0, 0, 0, 0);
    }

    @Deprecated
    public static String getCurrentDateMonthDay() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.MONTH_TEXT_AND_DAY, 0, 0, 0, 0, 0);
    }

    public static String getPreviousDayMonthDayByIntDay(int step) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.MONTH_TEXT_AND_DAY, 0, 0, step, 0, 0);
    }

    @Deprecated
    public static String getPreviousDayMonthDay() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.MONTH_TEXT_AND_DAY, 0, 0, 1, 0, 0);
    }

    @Deprecated
    public static String getPreviousDateMonthYearIntYears(int step) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.MONTH_TEXT_AND_YEAR, step, 0, 0, 0, 0);
    }

    public static String getPreviousDateMonthYearIntMonth(int step) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.MONTH_TEXT_AND_YEAR, 0, step, 1, 0, 0);
    }

    public static String getCurrentDateMonthYear() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.MONTH_TEXT_AND_YEAR, 0, 0, 0, 0, 0);
    }

    @Deprecated
    public static String getCurrentYear() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.YEAR, 0, 0, 0, 0, 0);
    }

    @Deprecated
    public static String getPreviousYearByInt(int step) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.YEAR, step, 0, 0, 0, 0);
    }

    public static String getCurrentDate() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 0, 0, 0);
    }

    @Deprecated
    public static String getPreviousDateYearMonthDayByIntMonthMinus1Day(int step) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, step, 1, 0, 0);
    }

    public static String getPreviousDayByIntDaysYearMonthDay(int step) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, step, 0, 0);
    }

    public static String getPreviousYearByIntYearMonthDay(int step) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, step, 0, 0, 0, 0);
    }

    public static Double getRandomRoundedDouble(double min, double max) {
        double random = ThreadLocalRandom.current().nextDouble(min, max);
        double rounded = roundDouble(random, 2);
        return rounded;
    }

    public static Integer getRandomIntNotInRange(int lowerBound, int upperBound) {
        if (lowerBound < upperBound) {
            int randomInt;
            do {
                randomInt = random.nextInt();
            } while (randomInt > lowerBound && randomInt < upperBound);
            return randomInt;
        } else {
            return null;
        }
    }

    public static String removeKeyFromJson(String jsonString, String keyToRemove) {
        // Parse the input string as a JSONObject
        JSONObject jsonObject = new JSONObject(jsonString);

        // Recursively remove the key from the JSON object
        removeKeyRecursively(jsonObject, keyToRemove);

        // Return the updated JSON string
        return jsonObject.toString();
    }

    // Helper method to recursively remove a key from a JSONObject
    private static void removeKeyRecursively(JSONObject jsonObject, String keyToRemove) {
        // Use an iterator to avoid ConcurrentModificationException
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            // If the key matches the key to remove, remove it
            if (key.equals(keyToRemove)) {
                keys.remove();
            }
            // If the value is a JSONObject, recurse into it
            else if (value instanceof JSONObject) {
                removeKeyRecursively((JSONObject) value, keyToRemove);
            }
            // If the value is a JSONArray, check for JSONObjects inside it
            else if (value instanceof JSONArray array) {
                for (int i = 0; i < array.length(); i++) {
                    Object arrayElement = array.get(i);
                    if (arrayElement instanceof JSONObject) {
                        removeKeyRecursively((JSONObject) arrayElement, keyToRemove);
                    }
                }
            }
        }
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static double roundDouble(double value, int decimals) {
        if (decimals < 0) {
            throw new IllegalArgumentException("Decimals must be non-negative.");
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean compareDoubles(Double a, Double b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return BigDecimal.valueOf(a).setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.valueOf(b).setScale(2, RoundingMode.HALF_UP)) == 0;
    }

    public static String timestampFromDbToIso(String timestampDb) {
        return timestampDb.replace(" ", "T") + "Z";
    }

    public static String timestampFromIsoToDb(String timestampIso) {
        return timestampIso.replace("T", " ").replace("Z", "");
    }

    public static long getDifferenceTimeMinutes(String dateTimeString, String dateTimeString2) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateTimeString2, formatter);

        long time = ChronoUnit.MINUTES.between(dateTime, dateTime2);

        logger.info("TIME DIFFERENCE IN MINUTES IS " + time);

        return time;
    }

    public static String getUcidByUserIdAndBrand(Integer userId, Brand brand) {
        return String.format("%s-%s", brand.getUcidBrand(), userId);
    }

    public static String convertDateTimeDbToDate(String dtDbFormat) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime dateTime = LocalDateTime.parse(dtDbFormat, inputFormatter);
        return dateTime.format(outputFormatter);
    }

    @Deprecated
    public static String getPrevious30DaysDate() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 29, 0, 0);
    }

    public static String formatTimeToUtc(String time) {
        return time.split("\\.")[0].replace(" ", "T") + "Z";
    }

    @Deprecated
    public static String getPrevious14DaysDate() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 13, 0, 0);
    }

    @Deprecated
    public static String getPrevious90DaysDateUtc() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 89, 0, 0);
    }

    public static String transformDate(String dateTimeString, DateTimeFormat formatFrom, DateTimeFormat formatTo) {
        try {
            DateTimeFormatter sourceFormatter = DateTimeFormatter.ofPattern(formatFrom.getDisplayName(), Locale.US);
            DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern(formatTo.getDisplayName(), Locale.US);
            // Determine if the input format is for a date or date-time
            if (DateTimeFormat.DATE.equals(formatFrom) || DateTimeFormat.MONTH_TEXT_AND_DAY.equals(formatFrom) || DateTimeFormat.MONTH_TEXT_AND_YEAR.equals(formatFrom) || DateTimeFormat.YEAR.equals(formatFrom)) {
                // Parse as LocalDate if only a date is present
                LocalDate date = LocalDate.parse(dateTimeString, sourceFormatter);
                return date.format(targetFormatter);
            } else {
                // Parse as LocalDateTime if time is present
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, sourceFormatter);
                return dateTime.format(targetFormatter);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date string, formatFrom or formatTo is incorrect!");
        }
    }

    public static void waitForConnectionSearchToUpdate() throws Exception {
        ClientHelper userFrom1 = getRandomVantageClient();
        ClientHelper userTo1 = getRandomVantageClient();
        ConnectionTableEntry connectionTableEntry11 = getConnectionTableEntry(userFrom1, userTo1);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry11);
        waitForConnectionSearchToUpdate(userFrom1.getUcid());
        connectionTableEntry11.datetime = getCurrentTimestampDbFormat();
        connectionTableEntry11.status = "delete";
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry11);
    }

    public static void waitForConnectionSearchToUpdate(ClientHelper client) throws Exception {
        waitForConnectionSearchToUpdate(client.getUcid());
    }

    public static void waitForConnectionSearchToUpdate(String ucid) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", ucid);
        boolean updated = false;
        for (int i = 0; i < 60; i++) {
            Response response = getConnectionsByClientId(queryParams);
            GetConnectionsResponse[] responseBody = objectMapper.readValue(
                    response.body().string(), GetConnectionsResponse[].class
            );
            if (responseBody.length > 0) {
                updated = true;
                break;
            }
            Thread.sleep(1000);
        }
        if (!updated) {
            try {
                throw new TimeoutException("Connection search did not provide a non empty response while requesting connection for " + ucid + " !");
            } catch (TimeoutException e) {
                fail("Connection search did not provide a non empty response while requesting connection for " + ucid + " !");//fail test if there no response
            }
        }
    }

    public static void insertConnectionToDb(ConnectionTableEntry... connections) throws Exception {
        for (ConnectionTableEntry connection : connections) {
            connection.datetime = getCurrentTimestampDbFormat();
            insertObjectToDb(CONNECTIONS_TABLE_NAME, connection);
            Thread.sleep(1000);
        }
        waitForConnectionSearchToUpdate(connections[connections.length - 1].userFrom);
    }

    public static void closeAllAlertsBo() throws Exception {
        executeQueryToDb(POSTGRES, String.format("UPDATE %s SET closed_at ='%s', status = 'CLOSED', alert_resolution = 'CONFIRMED' WHERE status = 'OPEN';", BO_ALERT_TABLE_NAME, getCurrentTimestampDbFormat()));
        String userId = getUserIdByUser(autotestUserOne());
        executeQueryToDb(
                DbName.POSTGRES, String.format("UPDATE %s SET assigned_user_id ='%s', completed_by_user_id = '%s', started_at = '%s', completed_at = '%s', status = 'COMPLETED' WHERE status IN ('NEW', 'ACTIVE')", BO_INVESTIGATION_TABLE_NAME, userId, userId, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat()
                )
        );
    }

    public static double convertToUsd(double amount, String symbol) {
        RatesUsdCurrentObject rate = getObjectsFromDBFinal(CLICKHOUSE, RATES_USD_CURRENT, "currency = '" + symbol + "'", RatesUsdCurrentObject.class).getFirst();
        logger.info("rate is " + rate.getRate());
        return amount * rate.getRate();
    }

    public static String buildUcid(String brand, Integer clientId) {
        return brand + "-" + clientId.toString();
    }
}
