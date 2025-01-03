package BankingManagementSystem.BankingManagementSystem.Card;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class CardRequestDTO {
    @JsonProperty("accountId")
    private Long accountId;
    @JsonProperty("cardName")
    private String cardName;
    @JsonProperty("cardType")
    private String cardType;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("pin")
    private String pin;
    @JsonProperty("limit")
    private BigDecimal limit;

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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
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

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }
}
