package tests;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataSetupHelper.setupData;

import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.Brand;
import jdk.jfr.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
@Description("Create user by given user id and brand")
class CreateUser {

    @Test
    void createUser() {
        ClientHelper client = getRandomVantageClientAllFields();

        // Select needed brand in enum
        client.setBrand(Brand.VANTAGE);
        // Paste user id
        client.setUserId(1);

        DataHelper data = new DataHelper();
        data.createClient(client);
        setupData(data);
    }
}
