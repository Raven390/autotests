package business_objects.api.payment_gate.payments;


import java.math.BigDecimal;

import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

public class PostPaymentsRequestBodyFactory {

    public static PostPaymentsRequestBody createPostPaymentsRequestBody() {
        PostPaymentsRequestBody body = new PostPaymentsRequestBody();

        // Map fields from the provided issue description JSON to the request body via setters
        body.setSchemaVersion("1.0");
        body.setBrand("vantage");
        body.setRegulator("CIMA");
        body.setType("withdrawal");
        body.setId(getRandomUuidString());
        body.setClientId(getRandomIntPositive());
        body.setWithdrawalId(getRandomIntPositive());
        body.setMerchantOrderId("VTSG1115142220250202132259");
        body.setMt4Account(getRandomIntPositive());
        body.setAccountType("MT5");
        body.setPlatform("WEB");
        body.setCheckName("WR_Blacklist");
        body.setStatusId(1);
        body.setStatus("Success");
        body.setEventDate("2025-05-20T14:30:00Z");
        body.setWithdrawalApplicationTime("2025-07-15 07:38:05");
        body.setWithdrawalCurrency("USD");
        body.setWithdrawalAmount(new BigDecimal("1500.00"));
        body.setWithdrawalAmountUSD(new BigDecimal("1500.00"));
        body.setPaymentMethodCode("CREDIT_CARD");
        body.setPaymentChannelCode(1);
        body.setPaymentChannelName("Credit card");
        body.setPaymentTypeName("Credit card");
        body.setPaymentTypeCode(2);
        body.setIp("121.233.122.82");
        body.setStatusKYC("Confirmed");
        body.setCost(new BigDecimal("0.56"));

        // Card details - using constructor since nested class is private, populate via public constructor in the outer class
        // Build card via the all-args constructor inside PostPaymentsRequestBody and assign
        try {
            java.lang.reflect.Constructor<?> ctor = Class.forName("business_objects.api.payment_gate.payments.PostPaymentsRequestBody$Card").getDeclaredConstructor(String.class, String.class, String.class, String.class, String.class, Integer.class);
            ctor.setAccessible(true);
            Object card = ctor.newInstance(
                    "654321", "1225", "4", "2029", "sheryar shah", 0
            );
            // use reflection to call setCard to avoid referencing private nested type in signature
            Class<?> cardClass = Class.forName("business_objects.api.payment_gate.payments.PostPaymentsRequestBody$Card");
            java.lang.reflect.Method setCard = PostPaymentsRequestBody.class.getDeclaredMethod("setCard", cardClass);
            setCard.invoke(body, card);
        } catch (Exception e) {
            throw new RuntimeException("Failed to construct Card for PostPaymentsRequestBody", e);
        }

        return body;
    }

}
