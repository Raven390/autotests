package utils;

import java.util.Random;

public class Utils {

    public static Integer getRandomInt() {
        Random random = new Random();
        int randomInt = random.nextInt(Integer.MAX_VALUE);
        return randomInt;
    }

    public static String getRandomEmail() {
        String email = "testmail" + getRandomInt() + "@mail.com";
        System.out.println("Generated email: " + email);
        return email;
    }
}
