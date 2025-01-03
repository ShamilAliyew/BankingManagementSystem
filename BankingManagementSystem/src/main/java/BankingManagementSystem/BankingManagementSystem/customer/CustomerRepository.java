package BankingManagementSystem.BankingManagementSystem.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
       @Query("Select c from Customer c where c.email=?1")
    Optional<Customer> findByEmail(String email);
}
