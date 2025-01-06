package BankingManagementSystem.BankingManagementSystem.account;

import BankingManagementSystem.BankingManagementSystem.Card.Card;
import BankingManagementSystem.BankingManagementSystem.customer.Customer;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String email;
    @Column( length = 20, nullable = false, unique = true)
    private String accountNumber;
    @Column( nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @ManyToOne()
    @JoinColumn(name ="customer_id",nullable = false)
    private Customer customer;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Card> cards =new ArrayList<>();
    @Column(nullable = false)
    private String password;
    @Column( nullable = false)
    private BigDecimal balance=BigDecimal.ZERO;

    @Column( nullable = false)
    private LocalDateTime createdDate;

    @Column( nullable = false)
    private boolean isDeleted;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {return email;}

    public void setEmail(String username) {this.email = username;}

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

}
