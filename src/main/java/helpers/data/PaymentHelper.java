package helpers.data;

import static utils.Constants.*;

import io.qameta.allure.Allure;
import java.util.Objects;

public class PaymentHelper {
    public static void createPayment(String type, String paymentSystem, Integer amount) {
        if (Objects.equals(type, PAYMENT_TYPE_DEPOSIT)) {

        } else if (Objects.equals(type, PAYMENT_TYPE_WITHDRAWAL)) {

        } else if (Objects.equals(type, PAYMENT_TYPE_BONUS)) {

        }
        Allure.step(String.format("Payment: %s through %s with %d amount is created", type, paymentSystem, amount));
    }
}
