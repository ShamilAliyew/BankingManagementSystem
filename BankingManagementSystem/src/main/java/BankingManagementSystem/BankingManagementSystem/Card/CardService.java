package BankingManagementSystem.BankingManagementSystem.Card;

import BankingManagementSystem.BankingManagementSystem.Transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
    Card createCard(Long accountId, CardRequestDTO cardRequestDTO);
    Card getCardById(Long cardId);
    List<Card> getAllCardsByCustomerId(Long customerId);
    void makePayment(String cardNumber, BigDecimal amount);
    void updateCardLimit(String cardNumber, BigDecimal newLimit);

    void withdrawFromCard(String cardNumber, BigDecimal amount);
    void deposit (String cardNumber,BigDecimal amount);
    void transferFromCard(String sourceCardNumber, String receiveCardNumber, BigDecimal amount);
    void updateCardPin(Long cardId, String oldPin, String newPin);

    List<Transaction> getTransactionHistory(Long cardId);
    String generateCardNumber();
    String generateCvv();
    // boolean validateCardNumber(String cardNumber);
    // void updateCardStatus(Long cardId, boolean isActive);

}
