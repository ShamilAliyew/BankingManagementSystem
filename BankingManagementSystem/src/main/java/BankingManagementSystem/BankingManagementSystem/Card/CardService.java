package BankingManagementSystem.BankingManagementSystem.Card;

import BankingManagementSystem.BankingManagementSystem.Transaction.Transaction;
import BankingManagementSystem.BankingManagementSystem.Transaction.TransactionRepository;
import BankingManagementSystem.BankingManagementSystem.Transaction.TransactionStatus;
import BankingManagementSystem.BankingManagementSystem.Transaction.TransactionType;
import BankingManagementSystem.BankingManagementSystem.account.Account;
import BankingManagementSystem.BankingManagementSystem.account.AccountRepository;
import BankingManagementSystem.BankingManagementSystem.customer.Customer;
import org.springframework.transaction.annotation.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public CardService(CardRepository cardRepository,
                       AccountRepository accountRepository,
                       TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    public String generateCardNumber() {
        String fixedPart = "20051223";
        Random random = new Random();
        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            randomPart.append(random.nextInt(10));
        }

        return fixedPart + randomPart.toString();
    }

    public String generateCvv() {
        return String.format("%03d", new Random().nextInt(1000));
    }

    public void validatePin(String newPin) {
        if (newPin == null || newPin.isEmpty()) {
            throw new IllegalArgumentException("Pin can not be empty or null");
        }
        if (!newPin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be 4 long and contain only numbers");

        }
    }

    public CardDTO orderDebitCard(DebitCardRequest debitCardRequest) {
        if (debitCardRequest == null) {
            throw new IllegalArgumentException("Card request data cannot be null");
        }
        if (debitCardRequest.getAccountId() == null) {
            throw new IllegalArgumentException("Account ID must not be null");
        }
        Account account = accountRepository.findById(debitCardRequest.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Customer customer = account.getCustomer();
        if (customer == null) {
            throw new IllegalStateException("Account is not associated with any customer");
        }

        Card card = new Card();
        card.setCustomer(customer);
        card.setAccount(account);
        card.setCardHolderName("Mr."+ customer.getFirstName()+" "+customer.getLastName());
        card.setCurrency(Currency.valueOf(debitCardRequest.getCurrency()));
        card.setCardType(CardType.DEBIT);

        String cardNumValue;
        do {
            cardNumValue = generateCardNumber();
        } while (cardRepository.findByCardNumber(cardNumValue).isPresent());
        card.setCardNumber(cardNumValue);
        card.setExpirationDate(LocalDate.now().plusYears(5));
        card.setCvv(generateCvv());
        card.setPin(debitCardRequest.getPin());
        card.setCardBalance(BigDecimal.ZERO);
        card.setActive(true);
        Card savedCard = cardRepository.save(card);
        return convertToCardDto(savedCard);

    }

    public CardDTO orderCreditCard(CreditCardRequest creditCardRequest){
        if (creditCardRequest == null) {
            throw new IllegalArgumentException("Card request data cannot be null");
        }
        if (creditCardRequest.getAccountId() == null){
            throw new IllegalArgumentException("Account ID must not be null");
        }
        Account account = accountRepository.findById(creditCardRequest.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Customer customer = account.getCustomer();
        if (customer == null) {
            throw new IllegalStateException("Account is not associated with any customer");
        }

        Card card = new Card();
        card.setCustomer(customer);
        card.setAccount(account);
        card.setCardHolderName("Mr."+customer.getFirstName()+" "+customer.getLastName());
        card.setCurrency(Currency.valueOf(creditCardRequest.getCurrency()));
        card.setCardType(CardType.CREDIT);

        String cardNumValue;
        do {
            cardNumValue = generateCardNumber();
        } while (cardRepository.findByCardNumber(cardNumValue).isPresent());
        card.setCardNumber(cardNumValue);
        card.setExpirationDate(LocalDate.now().plusYears(5));
        card.setCvv(generateCvv());
        card.setPin(creditCardRequest.getPin());
        card.setCardBalance(BigDecimal.ZERO);
        card.setCardLimit(BigDecimal.valueOf(5000));
        card.setActive(true);
        Card savedCard = cardRepository.save(card);
        return convertToCardDto(savedCard);

    }

    public CardDTO convertToCardDto(Card card) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setCustomerId(card.getCustomer().getId());
        cardDTO.setAccountId(card.getAccount().getId());
        cardDTO.setCardNumber(card.getCardNumber());
        cardDTO.setId(card.getId());
        cardDTO.setCurrency(String.valueOf(card.getCurrency()));
        cardDTO.setCardHolderName(card.getCardHolderName());
        cardDTO.setCardType(String.valueOf(card.getCardType()));
        cardDTO.setBalance(card.getCardBalance());
        cardDTO.setExpirationDate(card.getExpirationDate());
        cardDTO.setActive(card.isActive());
        return cardDTO;
    }

    public CardDTO getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found!"));

        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(card.getId());
        cardDTO.setCardNumber("**** **** **** " + card.getCardNumber().substring(12));
        cardDTO.setAccountId(card.getAccount().getId());
        cardDTO.setCardHolderName(card.getCardHolderName());
        cardDTO.setCurrency(card.getCurrency().name());
        cardDTO.setCustomerId(card.getAccount().getCustomer().getId());
        cardDTO.setCardType(card.getCardType().name());
        cardDTO.setBalance(card.getCardBalance());
        cardDTO.setLimit(card.getCardLimit());
        cardDTO.setExpirationDate(card.getExpirationDate());
        cardDTO.setActive(card.isActive());

        return cardDTO;

    }

    public List<CardDTO> getAllCardsByCustomerId(Long customerId) {
        List<Card> cards = cardRepository.findByCustomerId(customerId);


        return cards.stream()
                .map(card -> {
                    CardDTO cardDTO = new CardDTO();
                    cardDTO.setId(card.getId());
                    cardDTO.setCardNumber("**** **** **** " + card.getCardNumber().substring(12));
                    cardDTO.setCardHolderName(card.getCardHolderName());
                    cardDTO.setCurrency(card.getCurrency().name());
                    cardDTO.setAccountId(card.getAccount().getId());
                    cardDTO.setCardType(card.getCardType().name());
                    cardDTO.setBalance(card.getCardBalance());
                    cardDTO.setExpirationDate(card.getExpirationDate());
                    cardDTO.setActive(card.isActive());
                    return cardDTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    void makePayment(String cardNumber,String cvv ,BigDecimal amount) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        if(!card.isActive()){
            throw new IllegalArgumentException("Card is not active.You can not perform financial operations");
        }
        if(card.getCvv().equals(cvv)){
            if (card.getCardType().name().equals(CardType.DEBIT.name())) {
                if (card.getCardBalance().compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Insufficient card balance");
                }
                BigDecimal cashbask = amount.multiply(BigDecimal.valueOf(0.05));
                card.setCardBalance(card.getCardBalance().subtract(amount).add(cashbask));
            } else if (card.getCardType().name().equals(CardType.CREDIT.name())) {
                card.setCardBalance(card.getCardBalance().subtract(amount));
                if (card.getCardLimit().compareTo(card.getCardBalance()) < 0) {
                    throw new IllegalArgumentException("You have exceeded the limit assigned to the card.");
                }

            }

        }else{
            throw new IllegalArgumentException("Incorrect card cvv");
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setSourceNumber(cardNumber);
        transaction.setDestinationNumber(null);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDescription(null);
        cardRepository.save(card);
        transactionRepository.save(transaction);
    }
    @Transactional
    public void transfer(String senderCardNum, String receiverCardNum, BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        Card senderCard = cardRepository.findByCardNumber(senderCardNum)
                .orElseThrow(()->new IllegalArgumentException("Sender card not found"));
        Card receiverCard = cardRepository.findByCardNumber(receiverCardNum)
                .orElseThrow(()->new IllegalArgumentException("Receiver Card not found"));
        if(!senderCard.isActive()){
            throw new IllegalArgumentException("Sender card is not active.You can not perform financial operations");
        }
        if(!receiverCard.isActive()){
            throw new IllegalArgumentException("Receiver card is not active.You can not perform financial operations");
        }
        if(senderCard.getCardType().name().equals(CardType.DEBIT.name())) {
            if(senderCard.getCardBalance().compareTo(amount)< 0 ){
                throw new IllegalArgumentException("Insufficient balance for transfer");
            }
            senderCard.setCardBalance(senderCard.getCardBalance().subtract(amount));

        }
        if(senderCard.getCardType().name().equals(CardType.CREDIT.name())){
            if(senderCard.getCardLimit().compareTo(senderCard.getCardBalance().abs()) <= 0 ){
                throw new IllegalArgumentException("Transaction declined: Your card balance exceeds the allowed limit.");
            }
            senderCard.setCardBalance(senderCard.getCardBalance().subtract(amount));
        }
        receiverCard.setCardBalance(receiverCard.getCardBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setSourceNumber(senderCardNum);
        transaction.setDestinationNumber(receiverCardNum);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDescription(null);
        cardRepository.save(senderCard);
        cardRepository.save(receiverCard);
        transactionRepository.save(transaction);

    }

    @Transactional
    public void withdrawFromCard(String cardNumber, BigDecimal amount, String pin) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        if(!card.isActive()){
            throw new IllegalArgumentException("Card is not active.You can not perform financial operations");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("Amount can't be lower than one");
        }
        if(!card.getPin().equals(pin)) {
            throw new IllegalArgumentException("Incorrect pin");
        }
        if (!card.getCardType().equals(CardType.DEBIT)) {
            throw new IllegalArgumentException("You cannot withdraw money from a card other than a debit card");
        }
        if (card.getCardBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient card balance!");
        }
        card.setCardBalance(card.getCardBalance().subtract(amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setSourceNumber(cardNumber);
        transaction.setDestinationNumber("null");
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDescription("null");

        cardRepository.save(card);
        transactionRepository.save(transaction);

    }

    public BigDecimal showBalance(String cardNum){
        Card card=cardRepository.findByCardNumber(cardNum)
                .orElseThrow(()->new IllegalArgumentException("Card not found"));

        BigDecimal balance = card.getCardBalance();

        return balance;
    }


    @Transactional
    void deposit(String cardNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if(!card.isActive()){
            throw new IllegalArgumentException("Card is not active.You can not perform financial operations");
        }
        if (card.getCardType().name().equals(CardType.DEBIT.name())){
            card.setCardBalance(card.getCardBalance().add(amount));
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setTransactionType(TransactionType.DEPOSIT);
            transaction.setSourceNumber(null);
            transaction.setDestinationNumber(cardNumber);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setDescription(null);

            cardRepository.save(card);
            transactionRepository.save(transaction);
        } else {
            throw new IllegalArgumentException("You cannot deposit money from a card other than a debit card");
        }
    }

    void updateCardPin(Long cardId, String oldPin, String newPin) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        if (!card.getPin().equals(oldPin)) {
            throw new IllegalArgumentException("Old pin is incorrect,come again");
        }
        validatePin(newPin);
        card.setPin(newPin);
        cardRepository.save(card);
    }


    void updateCardLimit(String cardNumber, BigDecimal newLimit) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Card limit cannot be negative");
        }
        card.setCardLimit(newLimit);
        cardRepository.save(card);
    }

    public void blockCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        if(!card.isActive()){
           throw new IllegalArgumentException("Card is already blocked");        }
        else{
            card.setActive(false);
        }
        cardRepository.save(card);
    }

    public void activateCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        if(card.isActive()){
            throw new IllegalArgumentException("Card already activated");
        }
        else{
            card.setActive(true);
        }
        cardRepository.save(card);
    }

}
