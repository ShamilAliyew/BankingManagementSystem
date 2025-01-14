package BankingManagementSystem.BankingManagementSystem.Card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DebitCardRequest{
    private Long accountId;
    private String currency;
    private String pin;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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
