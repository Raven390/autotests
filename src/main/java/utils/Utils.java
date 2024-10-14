package utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class Utils {

    public static Integer getRandomInt() {
        Random random = new Random();
        int randomInt = random.nextInt(Integer.MAX_VALUE);
        return randomInt;
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

    public static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return formatter.format(currentDateTime);
    }
}
