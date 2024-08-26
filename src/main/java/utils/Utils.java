package utils;

import java.util.Random;

public class Utils {

    public Integer getRandomInt() {
        Random random = new Random();
        int randomInt = random.nextInt();
        System.out.println("Random integer: " + randomInt);
        return randomInt;
    }
}