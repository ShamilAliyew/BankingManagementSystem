package BankingManagementSystem.BankingManagementSystem.account;

import BankingManagementSystem.BankingManagementSystem.Card.CardDTO;
import BankingManagementSystem.BankingManagementSystem.Card.CardRepository;
import BankingManagementSystem.BankingManagementSystem.Transaction.Transaction;
import BankingManagementSystem.BankingManagementSystem.Transaction.TransactionRepository;
import BankingManagementSystem.BankingManagementSystem.Transaction.TransactionStatus;
import BankingManagementSystem.BankingManagementSystem.Transaction.TransactionType;
import BankingManagementSystem.BankingManagementSystem.customer.Customer;
import BankingManagementSystem.BankingManagementSystem.customer.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImp {

    private  AccountRepository accountRepository;
    @Autowired
    private  CustomerRepository customerRepository;
    private TransactionRepository transactionRepository;
    public AccountServiceImp(AccountRepository accountRepository,TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository=transactionRepository;
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
    account.setEmail(createAccountDto.getEmail());
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
        accountDto.setAccountName(account.getEmail());
        //accountDto.setPassword(account.getPassword());
        accountDto.setAccountType(String.valueOf(account.getAccountType()));
        accountDto.setBalance(account.getBalance());
        accountDto.setCreatedDate(account.getCreatedDate());
        accountDto.setDeleted(account.isDeleted());

        return accountDto;
    }

    public AccountDto login(AccountLoginDTO accountLoginDTO) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(accountLoginDTO.getEmail());
           Account account = optionalAccount.orElseThrow(()-> new RuntimeException("No account found matching the email."));
        if(account.getPassword().equals(accountLoginDTO.getPassword())){
            return convertToAccountDto(account);
        }
        else{
            throw new RuntimeException("Wrong password");
        }
    }

    public AccountDto getAccountDetails(Long accountId){
        Account account=accountRepository.findById(accountId)
                .orElseThrow(()->new IllegalArgumentException("Account not found"));
        List<CardDTO> cardDTOList = account.getCards().stream()
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
    @Transactional
    void makePayment(String accountNumber, BigDecimal amount){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
            if(amount.compareTo(BigDecimal.ONE)<0) {
                throw new IllegalArgumentException("Insufficient amount");
            }
                if(account.getBalance().compareTo(amount)<0) {
                    throw new IllegalArgumentException("Insufficient account balance");
            }
                BigDecimal cashback = amount.multiply(BigDecimal.valueOf(0.03));
                BigDecimal updatedBalance = account.getBalance().subtract(amount).add(cashback);
                account.setBalance(updatedBalance);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setSourceNumber(accountNumber);
        transaction.setDestinationNumber(null);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDescription(null);

        accountRepository.save(account);
        transactionRepository.save(transaction);

    }
    @Transactional
    void deposit (String accountNumber,BigDecimal amount){
        if (amount.compareTo(BigDecimal.ONE) <=  0) {
            throw new IllegalArgumentException("Deposit amount must be greater than one");
        }
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->  new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setSourceNumber(null);
        transaction.setDestinationNumber(accountNumber);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDescription(null);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    @Transactional
    void withdraw (String accountNumber,BigDecimal amount){
        if (amount.compareTo(BigDecimal.ONE) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero");
        }
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->  new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().subtract(amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setSourceNumber(accountNumber);
        transaction.setDestinationNumber(null);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDescription(null);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }
    @Transactional
    public void transfer(String senderAccountNumber,String receiverAccountNumber,BigDecimal amount ){
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber)
                .orElseThrow(() ->  new RuntimeException("Sender account not found"));
        Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber)
                .orElseThrow(() ->  new RuntimeException("Receiver account not found"));
        if(senderAccount.getBalance().compareTo(amount)<0){
            throw new IllegalArgumentException("Insufficient balance for transfer");
        }
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setSourceNumber(senderAccountNumber);
        transaction.setDestinationNumber(receiverAccountNumber);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDescription(null);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        transactionRepository.save(transaction);
    }


}
