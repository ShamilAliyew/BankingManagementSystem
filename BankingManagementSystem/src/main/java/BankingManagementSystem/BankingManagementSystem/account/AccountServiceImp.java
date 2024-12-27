package BankingManagementSystem.BankingManagementSystem.account;

import BankingManagementSystem.BankingManagementSystem.Card.CardRepository;
import BankingManagementSystem.BankingManagementSystem.customer.Customer;
import BankingManagementSystem.BankingManagementSystem.customer.CustomerRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImp {
    private AccountRepository accountRepository;
    public AccountServiceImp(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private CustomerRepository customerRepository;
    private CardRepository cardRepository;


    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    
    public AccountDto createAccount(CreateAccountDto createAccountDto) {
        Optional<Customer> optionalCustomer = customerRepository.findById(createAccountDto.getCustomerId());
               Customer Customer = optionalCustomer .orElseThrow(()-> new RuntimeException("Customer not found"));

    Account account = new Account();

    account.setCustomer(Customer);
    account.setAccountNumber(generateAccountNumber());
    account.setPassword(createAccountDto.getPassword());
    account.setAccountType(AccountType.valueOf(createAccountDto.getAccountType()));
    account.setBalance(BigDecimal.ZERO);
    account.setCreatedDate(LocalDateTime.now());
    account.setDeleted(false);

    Account savedAccount = accountRepository.save(account);

        return convertToAccountDto(savedAccount) ;
    }
    public AccountDto convertToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setCustomerId(account.getCustomer().getId());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setPassword(account.getPassword());
        accountDto.setAccountType(String.valueOf(account.getAccountType()));
        accountDto.setBalance(account.getBalance());
        accountDto.setCreatedDate(account.getCreatedDate());
        accountDto.setDeleted(account.isDeleted());

        return accountDto;
    }

}
