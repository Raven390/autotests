package helpers.data.enums.rule_engine;

import lombok.Getter;

@Getter
public enum Event {
    CRM_DEPOSIT_EVENT("deposit"),
    CRM_LOGIN_EVENT("login"),
    CRM_REGISTRATION_EVENT("registration"),
    CRM_WITHDRAWAL_EVENT("withdrawal"),
    CRM_TRANSFER_TO_WA_EVENT("transferToWA"),
    EG_LOGIN_EVENT("egLoginToWeb"),
    EG_RAF_BALANCE_EVENT("egRaf"),
    REGISTRATION_EVENT("registration"),
    EG_WITHDRAWAL_EVENT("egWithdrawal"),
    MT_CLOSE_TRADE_EVENT("closeTrade"),
    MT_OPEN_TRADE_EVENT("openTrade"),
    MT_RAF_BALANCE_EVENT("raf");

    private final String name;

    Event(String displayName) {
        this.name = displayName;
    }
}
