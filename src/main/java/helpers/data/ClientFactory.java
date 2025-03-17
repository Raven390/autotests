
package helpers.data;

import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import net.datafaker.Faker;

import java.util.Random;

import static utils.Utils.*;
import static utils.Utils.getRandomUuidString;

public class ClientFactory {

    public static ClientHelper getRandomClient() {
        ClientHelper client = new ClientHelper(getRandomIntPositive(), getRandomUuidString(), randomEnum(Brand.class), getRandomIntPositive(), new Random().nextInt(1, 50));
        client.setRegulator(Regulator.VFSC);
        return client;
    }

    public static ClientHelper getRandomVantageClient() {
        ClientHelper client = new ClientHelper(getRandomIntPositive(), getRandomUuidString(), Brand.VANTAGE, getRandomIntPositive(), new Random().nextInt(1, 50));
        client.setRegulator(Regulator.VFSC);
        return client;
    }

    public static ClientHelper getRandomVantageClientAllFields() {
        Faker faker = new Faker();
        return new ClientHelper(
                getRandomIntPositive(), getRandomUuidString(), Regulator.VFSC2, Brand.VANTAGE, getRandomIntPositive(), getRandomIntPositive(), new Random().nextInt(1, 50), faker.internet().emailAddress(), faker.phoneNumber().cellPhoneInternational().replace("+", "").replace(" ", "").replace("-", ""), faker.internet().ipV4Address(), "CY", getRandomIntPositive(), getRandomIntPositive(), getRandomIntPositive(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString(), faker.name().firstName(), faker.name().lastName()
        );
    }

    public static ClientHelper getRandomStarTraderClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.STAR_TRADER);
        return client;
    }

    public static ClientHelper getRandomUltimaMarketsClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.ULTIMA_MARKETS);
        return client;
    }

    public static ClientHelper getRandomVantageClientNoCpaIbRef() {
        Faker faker = new Faker();
        return new ClientHelper(
                getRandomIntPositive(), getRandomUuidString(), Regulator.VFSC2, Brand.VANTAGE, getRandomIntPositive(), getRandomIntPositive(), new Random().nextInt(1, 50), faker.internet().emailAddress(), faker.phoneNumber().cellPhoneInternational().replace("+", "").replace(" ", "").replace("-", ""), faker.internet().ipV4Address(), "CY", null, null, null, null, null, null, null, null, faker.name().firstName(), faker.name().lastName()
        );
    }
}
