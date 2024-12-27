package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import helpers.data.enums.Brands;
import org.json.JSONArray;
import org.json.JSONObject;

import static helpers.data.enums.Brands.*;

public class Utils {

    public static Integer getRandomInt() {
        return new Random().nextInt();
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

    public static String getCurrentTimestampDbFormat() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public static String getTomorrowTimestampDbFormat() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return tomorrow.format(formatter);
    }

    public static String getYesterdayTimestampDbFormat() {
        LocalDateTime tomorrow = LocalDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return tomorrow.format(formatter);
    }

    public static String getPreviousWeekTimestampDbFormat() {
        LocalDateTime tomorrow = LocalDateTime.now().minusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return tomorrow.format(formatter);
    }

    public static String getNextYearTimestampDbFormat() {
        LocalDateTime tomorrow = LocalDateTime.now().plusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return tomorrow.format(formatter);
    }

    public static String getPreviousYearTimestampDbFormat() {
        LocalDateTime tomorrow = LocalDateTime.now().minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return tomorrow.format(formatter);
    }

    public static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return formatter.format(currentDateTime);
    }

    public static String getCurrentDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(currentDateTime);
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

    public static String getUcidByUserIdAndBrand(Integer userId, Brands brand) {
        Map<Brands, String> brandToUcidBrandMap = new HashMap<>();
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

    public static String getCurrentDateUtc() {
        return LocalDate.now(ZoneOffset.UTC).toString();
    }

    public static String getYesterdayDateUtc() {
        return LocalDate.now(ZoneOffset.UTC).minusDays(1).toString();
    }

    public static String getPreviousWeekDateUtc() {
        return LocalDate.now(ZoneOffset.UTC).minusDays(6).toString();
    }

    public static String getPrevious30DaysDateUtc() {
        return LocalDate.now(ZoneOffset.UTC).minusDays(29).toString();
    }

    public static String formatTimeToUtc(String time) {
        return time.split("\\.")[0].replace(" ", "T") + "Z";
    }
}
