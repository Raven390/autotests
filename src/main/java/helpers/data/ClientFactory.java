
package helpers.data;

import net.datafaker.Faker;

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

    public static ClientHelper getRandomVantageClientAllFields() {
        Faker faker = new Faker();
        return new ClientHelper(
                getRandomIntPositive(),
                getRandomUuidString(),
                Brand.VANTAGE,
                getRandomIntPositive(),
                new Random().nextInt(1, 50),
                faker.internet().emailAddress(),
                faker.phoneNumber().cellPhoneInternational()
                        .replace("+", "")
                        .replace(" ","")
                        .replace("-",""),
                faker.internet().ipV4Address(),
                faker.country().countryCode2().toUpperCase()
        );
    }
}
