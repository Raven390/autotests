package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import helpers.data.enums.Brand;
import helpers.data.enums.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import static helpers.data.enums.Brand.*;

public class Utils {

    public static Integer getRandomInt() {
        return new Random().nextInt();
    }

    public static String startFromUpper(String lowerCase) {
        return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
    }

    public static Integer getRandomIntPositive() {
        return new Random().nextInt(Integer.MAX_VALUE) + 1;
    }

    public static Long getRandomLongPositive() {
        return new Random().nextLong(Long.MAX_VALUE) + 1;
    }

    public static String getRandomUuidString() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomEmail() {
        String email = "testmail" + getRandomInt() + "@mail.com";
        System.out.println("Generated email: " + email);
        return email;
    }

    public static long getCurrentTimestamp() {
        return Instant.now().getEpochSecond();
    }

    public static String getCurrentTimestampMinusOffsetFormatted(String format, int years, int months, int days,
            int hours, int minutes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.US);
        return LocalDateTime.now(ZoneOffset.UTC).minusYears(years).minusMonths(months).minusDays(days).minusHours(hours).minusMinutes(minutes).format(formatter);
    }

    public static String getCurrentTimestampMinusOffsetFormatted(DateTimeFormat format, int years, int months, int days,
            int hours, int minutes) {
        return getCurrentTimestampMinusOffsetFormatted(format.getDisplayName(), years, months, days, hours, minutes);
    }

    public static String getCurrentTimestampDbFormat() {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 0);
    }

    @Deprecated
    public static String getCurrentTimestampDbFormatMinusDays(int days) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, days, 0, 0);
    }

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

    @Deprecated
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

    public static String getPreviousDateByIntYearMonthDay(int step) {
        return getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, step, 0, 0, 0, 0);
    }

    public static Double getRandomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
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
                randomInt = new Random().nextInt();
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
        int x = new Random().nextInt(clazz.getEnumConstants().length);
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

    public static String timestampFromDbToIso(String timestampDb) {
        return timestampDb.replace(" ", "T") + "Z";
    }

    public static long getDifferenceTimeMinutes(String dateTimeString, String dateTimeString2) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateTimeString2, formatter);

        long time = ChronoUnit.MINUTES.between(dateTime, dateTime2);

        System.out.println("TIME DIFFERENCE IN MINUTES IS " + time);

        return time;
    }

    public static String getUcidByUserIdAndBrand(Integer userId, Brand brand) {
        Map<Brand, String> brandToUcidBrandMap = new HashMap<>();
        brandToUcidBrandMap.put(VANTAGE, "vantage");
        brandToUcidBrandMap.put(VJP, "vjp");
        brandToUcidBrandMap.put(VT, "vt");
        brandToUcidBrandMap.put(PU_PRIME, "puprime");
        brandToUcidBrandMap.put(STAR_TRADER, "startrader");
        brandToUcidBrandMap.put(MONETA, "moneta");
        brandToUcidBrandMap.put(ULTIMA_MARKETS, "ultimamarkets");
        brandToUcidBrandMap.put(INFINOX, "infinox");
        return String.format("%s-%s", brandToUcidBrandMap.get(brand), userId);
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

    public static String calculatePercentageFromList(List<Long> numerators, List<Long> denominators) {
        if (numerators.size() != denominators.size()) {
            throw new IllegalArgumentException("Lists must have the same size"); // Handle mismatch in list sizes
        }
        double totalNumerator = 0;
        double totalDenominator = 0;
        // Calculate the sum of numerators and denominators
        for (int i = 0; i < numerators.size(); i++) {
            totalNumerator += numerators.get(i);
            totalDenominator += denominators.get(i);
        }

        // Check for division by zero
        if (totalDenominator == 0) {
            throw new IllegalArgumentException("Division by zero!"); // Handle division by zero
        }

        // Perform the division, multiply by 100, round to 1 decimal place
        double result = (totalNumerator / totalDenominator) * 100;
        result = Math.round(result * 10.0) / 10.0; // Round to 1 decimal place

        // Use DecimalFormat to format the result without decimals when unnecessary
        DecimalFormat formatter = new DecimalFormat(result % 1 == 0 ? "#,###" : "#,###.0");

        // Return the formatted result as a percentage string
        return formatter.format(result) + "%";
    }
}
