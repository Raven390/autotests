
package helpers.data;

import helpers.data.enums.Brands;
import helpers.data.enums.Regulators;
import net.datafaker.Faker;

import java.util.Random;

import static utils.Utils.*;

public class ClientFactory {

    public static ClientHelper getRandomClient() {
        ClientHelper client = new ClientHelper(getRandomIntPositive(), getRandomUuidString(), randomEnum(Brands.class), getRandomIntPositive(), new Random().nextInt(1, 50));
        client.setRegulator(Regulators.VFSC);
        return client;
    }

    public static ClientHelper getRandomVantageClient() {
        ClientHelper client = new ClientHelper(getRandomIntPositive(), getRandomUuidString(), Brands.VANTAGE, getRandomIntPositive(), new Random().nextInt(1, 50));
        client.setRegulator(Regulators.VFSC);
        return client;
    }

    public static ClientHelper getRandomVantageClientAllFields() {
        Faker faker = new Faker();
        return new ClientHelper(
                getRandomIntPositive(), getRandomUuidString(), Regulators.VFSC2, Brands.VANTAGE, getRandomIntPositive(), getRandomIntPositive(), new Random().nextInt(1, 50), faker.internet().emailAddress(), faker.phoneNumber().cellPhoneInternational().replace("+", "").replace(" ", "").replace("-", ""), faker.internet().ipV4Address(), faker.country().countryCode2().toUpperCase()
        );
    }

    public static ClientHelper getRandomVjpClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brands.VJP);
        return client;
    }

    public static ClientHelper getRandomVtClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brands.VT);
        return client;
    }

    public static ClientHelper getRandomPuPrimeClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brands.PU_PRIME);
        return client;
    }

    public static ClientHelper getRandomStarTraderClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brands.STAR_TRADER);
        return client;
    }

    public static ClientHelper getRandomMonetaClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brands.MONETA);
        return client;
    }

    public static ClientHelper getRandomUltimaMarketsClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brands.ULTIMA_MARKETS);
        return client;
    }

    public static ClientHelper getRandomInfinoxClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brands.INFINOX);
        return client;
    }
}
