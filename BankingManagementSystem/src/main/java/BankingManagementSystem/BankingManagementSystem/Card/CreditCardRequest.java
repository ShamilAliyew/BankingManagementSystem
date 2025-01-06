package BankingManagementSystem.BankingManagementSystem.Card;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class CreditCardRequest {
    @JsonProperty("accountId")
    private Long accountId;
    @JsonProperty("cardName")
    private String cardName;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("pin")
    private String pin;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

}
