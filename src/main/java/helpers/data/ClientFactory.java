
package helpers.data;

import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import net.datafaker.Faker;


import java.util.ArrayList;
import java.util.List;

import static utils.Utils.*;
import static utils.Utils.getRandomUuidString;

public class ClientFactory {

    public static ClientHelper getRandomClient() {
        ClientHelper client = new ClientHelper(getRandomIntPositive(), getRandomUuidString(), randomEnum(Brand.class), getRandomIntPositive(), getRandomIntPositiveWithBounds(1, 50));
        client.setRegulator(Regulator.VFSC);
        return client;
    }

    public static ClientHelper getRandomVantageClient() {
        ClientHelper client = new ClientHelper(getRandomIntPositive(), getRandomUuidString(), Brand.VANTAGE, getRandomIntPositive(), getRandomIntPositiveWithBounds(1, 50));
        client.setRegulator(Regulator.VFSC);
        return client;
    }

    public static ClientHelper getRandomBybitClient() {
        ClientHelper client = new ClientHelper(getRandomIntPositive(), getRandomUuidString(), Brand.BYBIT, getRandomIntPositive(), getRandomIntPositiveWithBounds(1, 50));
        client.setRegulator(Regulator.VFSC);
        return client;
    }

    public static ClientHelper getRandomVantageClientAllFields() {
        Faker faker = new Faker();
        return new ClientHelper(
                getRandomIntPositive(), getRandomUuidString(), Brand.VANTAGE, Regulator.VFSC2, getRandomIntPositive(), getRandomIntPositive(), getRandomIntPositiveWithBounds(1, 50), faker.internet().emailAddress(), faker.phoneNumber().cellPhoneInternational().replace("+", "").replace(" ", "").replace("-", ""), faker.internet().ipV4Address(), "CY", getRandomIntPositive(), getRandomIntPositive(), getRandomIntPositive(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString(), "1990-01-01", faker.name().firstName(), faker.name().lastName(), "CYPRUS"
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

    public static ClientHelper getRandomInfinoxClientAllFields() {
        ClientHelper client = getRandomVantageClientAllFields();
        client.setBrand(Brand.INFINOX);
        return client;
    }

    public static ClientHelper getRandomVantageClientNoCpaIbRef() {
        Faker faker = new Faker();
        return new ClientHelper(
                getRandomIntPositive(), getRandomUuidString(), Brand.VANTAGE, Regulator.VFSC2, getRandomIntPositive(), getRandomIntPositive(), getRandomIntPositiveWithBounds(1, 50), faker.internet().emailAddress(), faker.phoneNumber().cellPhoneInternational().replace("+", "").replace(" ", "").replace("-", ""), faker.internet().ipV4Address(), "CY", null, null, null, null, null, null, null, "1990-01-01", faker.name().firstName(), faker.name().lastName(), "CYPRUS"
        );
    }

    public static List<ClientHelper> getRandomClientWithCpa(Integer size, Integer cpa) {
        List<ClientHelper> clients = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ClientHelper client = getRandomClient();
            client.setCpaId(cpa);
            clients.add(client);
        }
        return clients;
    }

    public static ClientHelper getRandomClientByBrandAndCountry(Brand brand, String country) {
        Faker faker = new Faker();
        return new ClientHelper(
                getRandomIntPositive(), getRandomUuidString(), brand, Regulator.VFSC2, getRandomIntPositive(), getRandomIntPositive(), getRandomIntPositiveWithBounds(1, 50), faker.internet().emailAddress(), faker.phoneNumber().cellPhoneInternational().replace("+", "").replace(" ", "").replace("-", ""), faker.internet().ipV4Address(), "CN", getRandomIntPositive(), getRandomIntPositive(), getRandomIntPositive(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString(), getRandomUuidString(), "1990-01-01", faker.name().firstName(), faker.name().lastName(), country
        );
    }
}
