package BankingManagementSystem.BankingManagementSystem.Card;

import BankingManagementSystem.BankingManagementSystem.Transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
    Card createCard(Long accountId, CardCreateRequestDTO cardRequestDTO);
    void deactivateCard(Long cardId);

    Card getCardDetails(Long cardId);
    List<Card> getAllCardsByCustomerId(Long customerId);
    List<Card> getAllCardsByAccountId(Long accountId);

    void withdrawFromCard(Long cardId, BigDecimal amount);
    void transferFromCard(Long sourceCardId, Long destinationAccountId, BigDecimal amount);
    void makePayment(Long cardId, BigDecimal amount);
    void payCreditCardBill(Long cardId, BigDecimal paymentAmount);

    void updateCardLimit(Long cardId, BigDecimal newLimit);
    void updateCardStatus(Long cardId, boolean isActive);
    boolean validateCardNumber(String cardNumber);
    void updateCardPin(Long cardId, String oldPin, String newPin);

    List<Transaction> getTransactionHistory(Long cardId);
    String generateCardNumber();
    BigDecimal calculateCardBalance(Long cardId);


}
