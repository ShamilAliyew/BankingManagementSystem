package BankingManagementSystem.BankingManagementSystem.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("Select c from Customer c where c.email=?1")
    Optional<Customer> findByEmail(String email);
}
