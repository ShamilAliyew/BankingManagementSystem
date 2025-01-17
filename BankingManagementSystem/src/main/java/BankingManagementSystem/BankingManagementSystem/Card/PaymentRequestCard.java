package BankingManagementSystem.BankingManagementSystem.Card;

import java.math.BigDecimal;

public class PaymentRequestCard {
    private String cardNumber;
    private String cvv;
    private BigDecimal amount;

    public String getCardNumber(){
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
