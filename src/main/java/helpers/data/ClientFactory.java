
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
                getRandomIntPositive(), getRandomUuidString(), Regulator.VFSC2, Brand.VANTAGE, getRandomIntPositive(), getRandomIntPositive(), new Random().nextInt(1, 50), faker.internet().emailAddress(), faker.phoneNumber().cellPhoneInternational().replace("+", "").replace(" ", "").replace("-", ""), faker.internet().ipV4Address(), "CY", getRandomIntPositive(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString()
        );
    }

    public static ClientHelper getRandomVantageClientNoCpa() {
        Faker faker = new Faker();
        return new ClientHelper(
                getRandomIntPositive(), getRandomUuidString(), Regulator.VFSC2, Brand.VANTAGE, getRandomIntPositive(), getRandomIntPositive(), new Random().nextInt(1, 50), faker.internet().emailAddress(), faker.phoneNumber().cellPhoneInternational().replace("+", "").replace(" ", "").replace("-", ""), faker.internet().ipV4Address(), "CY", null, null, null, null, null, null
        );
    }

    public static ClientHelper getRandomVjpClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.VJP);
        return client;
    }

    public static ClientHelper getRandomVtClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.VT);
        return client;
    }

    public static ClientHelper getRandomPuPrimeClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.PU_PRIME);
        return client;
    }

    public static ClientHelper getRandomStarTraderClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.STAR_TRADER);
        return client;
    }

    public static ClientHelper getRandomMonetaClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.MONETA);
        return client;
    }

    public static ClientHelper getRandomUltimaMarketsClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.ULTIMA_MARKETS);
        return client;
    }

    public static ClientHelper getRandomInfinoxClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.INFINOX);
        return client;
    }
}
