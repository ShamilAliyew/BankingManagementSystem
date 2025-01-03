package BankingManagementSystem.BankingManagementSystem.account;

import BankingManagementSystem.BankingManagementSystem.Card.CardDTO;
import BankingManagementSystem.BankingManagementSystem.Card.CardRepository;
import BankingManagementSystem.BankingManagementSystem.customer.Customer;
import BankingManagementSystem.BankingManagementSystem.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountServiceImp {

    private  AccountRepository accountRepository;
    @Autowired
    private  CustomerRepository customerRepository;

    public AccountServiceImp(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    private CardRepository cardRepository;

    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder randomPart = new StringBuilder();
        for(int i=0;i<12;i++){
            randomPart.append(random.nextInt(10));
        }
        return randomPart.toString();
    }


    public AccountDto createAccount(CreateAccountDto createAccountDto) {
        Optional<Customer> optionalCustomer = customerRepository.findById(createAccountDto.getCustomerId());
               Customer customer = optionalCustomer .orElseThrow(()-> new RuntimeException("Customer not found"));

    Account account = new Account();

    account.setCustomer(customer);
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
        accountDto.setAccountId(account.getId());
        accountDto.setAccountNumber(account.getAccountNumber());
        //accountDto.setPassword(account.getPassword());
        accountDto.setAccountType(String.valueOf(account.getAccountType()));
        accountDto.setBalance(account.getBalance());
        accountDto.setCreatedDate(account.getCreatedDate());
        accountDto.setDeleted(account.isDeleted());

        return accountDto;
    }

    public AccountDto getAccountDetails(Long accountId){
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        Account account = optionalAccount.orElseThrow(()-> new RuntimeException("Account not found"));
        List<CardDTO> cardDTOList = optionalAccount.get().getCards().stream()
                .map(card -> new CardDTO(card.getId(),card.getCardNumber(),card.getExpirationDate(),card.getCardType().name()))
                .toList();

        AccountDto accountDto = new AccountDto();
        accountDto.setCustomerId(account.getCustomer().getId());
        accountDto.setAccountId(account.getId());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setAccountType(String.valueOf(account.getAccountType()));
        accountDto.setBalance(account.getBalance());
        accountDto.setCards(cardDTOList);
        accountDto.setCreatedDate(account.getCreatedDate());
        accountDto.setDeleted(account.isDeleted());
        return accountDto;
    }
}
