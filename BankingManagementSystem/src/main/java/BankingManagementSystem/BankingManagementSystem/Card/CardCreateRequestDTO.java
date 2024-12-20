package BankingManagementSystem.BankingManagementSystem.Card;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CardCreateRequestDTO {
    private Long accountId; // Hesaba bağlı ID
    private String cardType; // Debit və ya Kredit
    private BigDecimal initialBalance; // İlkin balans
    private BigDecimal limit; // Kredit kartı üçün limit
}
