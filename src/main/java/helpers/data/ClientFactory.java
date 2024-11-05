package helpers.data;

import java.util.Random;

import static utils.Utils.*;

public class ClientFactory {

    public static ClientHelper getRandomClient() {
        return new ClientHelper(
                getRandomIntPositive(),
                getRandomUuidString(),
                randomEnum(Brand.class),
                getRandomIntPositive(),
                new Random().nextInt(1, 50)
        );
    }

    public static ClientHelper getRandomVantageClient() {
        return new ClientHelper(
                getRandomIntPositive(),
                getRandomUuidString(),
                Brand.VANTAGE,
                getRandomIntPositive(),
                new Random().nextInt(1, 50)
        );
    }
}
