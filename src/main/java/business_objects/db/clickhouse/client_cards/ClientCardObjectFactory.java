package business_objects.db.clickhouse.client_cards;

import static utils.Utils.getRandomIntPositive;

import helpers.data.ClientHelper;
import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class ClientCardObjectFactory {
    private ClientCardObjectFactory() {}

    public static ClientCardsObject generateClientCard(ClientHelper client) {
        return ClientCardsObject.builder()
                .sourceIdSt(client.getServerId().shortValue())
                .brandUid((short) 1)
                .brand(client.getBrand())
                .regulator(client.getRegulator())
                .id(getRandomIntPositive().longValue())
                .userId(client.getUserId().longValue())
                .ucid(client.getUcid())
                .createTime(OffsetDateTime.now())
                .updateTime(OffsetDateTime.now())
                .isDel((short) 0)
                .usedForDeposit((short) 0)
                .usedForWithdrawal((short) 0)
                .cardBeginSixDigits(
                        String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000)))
                .cardLastFourDigits(
                        String.format("%04d", ThreadLocalRandom.current().nextInt(0, 10_000)))
                .cardHolderName(client.getFirstName() + " " + client.getLastName())
                .expiryMonth((short) 12)
                .expiryYear(2099)
                .threeDomainSecure((short) 0)
                .paymentType((short) 1)
                .status((short) 1)
                .lastUpdated(OffsetDateTime.now())
                .build();
    }

    public static ClientCardsObject generateClientCardsObject(ClientHelper client) {
        return generateClientCard(client);
    }
}
