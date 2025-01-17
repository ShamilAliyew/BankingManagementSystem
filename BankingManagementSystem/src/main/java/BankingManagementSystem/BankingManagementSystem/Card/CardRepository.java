package BankingManagementSystem.BankingManagementSystem.Card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByCustomerId(Long customerId);
//    @Query("SELECT c FROM Card c WHERE c.cardNumber = :cardNumber")
    Optional<Card> findByCardNumber(String cardNumber);
}
