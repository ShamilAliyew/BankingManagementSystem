package BankingManagementSystem.BankingManagementSystem.Card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> getAllCardsByCustomerId(Long customerId);
}
