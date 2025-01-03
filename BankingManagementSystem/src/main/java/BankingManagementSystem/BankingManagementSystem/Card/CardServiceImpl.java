package BankingManagementSystem.BankingManagementSystem.Card;

import BankingManagementSystem.BankingManagementSystem.account.Account;
import BankingManagementSystem.BankingManagementSystem.account.AccountRepository;
import BankingManagementSystem.BankingManagementSystem.customer.Customer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl {
    private final CardRepository cardRepository;
    public CardServiceImpl(CardRepository cardRepository, AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }
    private final AccountRepository accountRepository;


    public String generateCardNumber(){
        String fixedPart = "20051223";
        Random random = new Random();
        StringBuilder randomPart = new StringBuilder();
        for(int i=0;i<8;i++){
            randomPart.append(random.nextInt(10));
        }

        return fixedPart + randomPart.toString();
    }
    public String generateCvv() {
        return String.format("%03d", new Random().nextInt(1000));
    }

    public void validatePin(String newPin){
        if(newPin == null || newPin.isEmpty()){
            throw new IllegalArgumentException("Pin can not be empty or null");
        }
        if(newPin.matches("\\d{4}")){
            throw new IllegalArgumentException("PIN must be 4 long and contain only numbers");

        }
    }

    public CardDTO createCard(CardRequestDTO cardRequestDTO) {
        if (cardRequestDTO == null) {
            throw new IllegalArgumentException("Card request data cannot be null");
        }
        if (cardRequestDTO.getAccountId() == null){
            throw new IllegalArgumentException("Account ID must not be null");
        }
        Account account = accountRepository.findById(cardRequestDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Customer customer = account.getCustomer();
        if (customer == null) {
            throw new IllegalStateException("Account is not associated with any customer");
        }

        Card card = new Card();
        card.setCustomer(customer);
        card.setAccount(account);
        card.setCardHolderName(cardRequestDTO.getCardName());
        card.setCurrency(Currency.valueOf(cardRequestDTO.getCurrency()));
        card.setCardType(CardType.valueOf(cardRequestDTO.getCardType()));

        String cardNumValue;
        do{
            cardNumValue = generateCardNumber();
        }while (cardRepository.findByCardNumber(cardNumValue).isPresent());
        card.setCardNumber(cardNumValue);

        card.setPin(cardRequestDTO.getPin());
        card.setCvv(generateCvv());
        card.setExpirationDate(LocalDate.now().plusYears(5));
        card.setActive(true);

        if (CardType.DEBIT.name().equalsIgnoreCase(cardRequestDTO.getCardType())) {
            card.setCardBalance(account.getBalance());
        } else if (CardType.CREDIT.name().equalsIgnoreCase(cardRequestDTO.getCardType())) {
            card.setCardBalance(BigDecimal.ZERO);
            card.setCardLimit(cardRequestDTO.getLimit());
        } else {
            throw new IllegalArgumentException("Invalid card type: " + cardRequestDTO.getCardType());
        }
        Card savedCard = cardRepository.save(card);
        return convertToCardDto(savedCard);
    }

    public CardDTO convertToCardDto(Card card) {
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


    public CardDTO getCardById(Long cardId){
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
    void makePayment(String cardNumber, BigDecimal amount){
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        if(card.getCardType().name().equals(CardType.DEBIT.name())){
            if(card.getCardBalance().compareTo(amount)<0) {
                throw new IllegalArgumentException("Insufficent card balance");
            }
            card.setCardBalance(card.getCardBalance().subtract(amount));

            Account account = card.getAccount();
            account.setBalance(account.getBalance().subtract(amount));
            cardRepository.save(card);
            accountRepository.save(account);
        }
        else if(card.getCardType().name().equals(CardType.CREDIT.name())){
            card.setCardBalance(card.getCardBalance().add(amount));
            if(card.getCardLimit().compareTo(card.getCardBalance())<0) {
                throw new IllegalArgumentException("You have exceeded the limit assigned to the card.");
            }
            cardRepository.save(card);
        }
    }
    @Transactional
    void updateCardLimit(String cardNumber, BigDecimal newLimit){
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Card limit cannot be negative");
        }
        card.setCardLimit(newLimit);
        cardRepository.save(card);
    }
    @Transactional
    public void withdrawFromCard(String cardNumber, BigDecimal amount){
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() ->  new RuntimeException("Card not found"));
        if(amount.compareTo(BigDecimal.ZERO)<0)  {
            throw new IllegalArgumentException("Amount can't be lower than zero");
        }
        if(card.getCardType().name().equals(CardType.DEBIT.name())){
            if(card.getCardBalance().compareTo(amount)>=0){
                card.setCardBalance(card.getCardBalance().subtract(amount));

                Account account=card.getAccount();
                account.setBalance(account.getBalance().subtract(amount));

                cardRepository.save(card);
                accountRepository.save(account);
            }
            else{
                throw new IllegalArgumentException("Insufficent card balance!");
            }
        }
        else{
            throw new IllegalArgumentException("You cannot withdraw money from a card other than a debit card");
        }

    }
    @Transactional
    void deposit (String cardNumber,BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() ->  new RuntimeException("Card not found"));
        if(card.getCardType().name().equals(CardType.DEBIT.name())){
                card.setCardBalance(card.getCardBalance().add(amount));

            Account account=card.getAccount();
            account.setBalance(account.getBalance().subtract(amount));

            cardRepository.save(card);
            accountRepository.save(account);
        }
        else{
            throw new IllegalArgumentException("You cannot deposit money from a card other than a debit card");
        }
    }

    void updateCardPin(Long cardId, String oldPin, String newPin){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()->new IllegalArgumentException("Card not found"));
        if(!card.getPin().equals(oldPin)){
            throw new IllegalArgumentException("Old pin is incorrect,come again");
        }
        validatePin(newPin);
        card.setPin(newPin);
        cardRepository.save(card);
    }


}
