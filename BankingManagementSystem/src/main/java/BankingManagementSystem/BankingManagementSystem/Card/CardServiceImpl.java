package BankingManagementSystem.BankingManagementSystem.Card;

import BankingManagementSystem.BankingManagementSystem.account.Account;
import BankingManagementSystem.BankingManagementSystem.account.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl {
    private  CardRepository cardRepository;
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    private AccountRepository accountRepository;

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

    public CardDTO createCard(CardCreateRequestDTO cardRequestDTO) {
        Account account = accountRepository.findById(cardRequestDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Card card = new Card();
        card.setCustomer(account.getCustomer());
        card.setAccount(account);
        card.setCardHolderName(cardRequestDTO.getCardName());
        card.setCurrency(Currency.valueOf(cardRequestDTO.getCurrency()));
        card.setCardType(CartType.valueOf(cardRequestDTO.getCardType()));

        String cardNumValue;
        do{
            cardNumValue = generateCardNumber();
        }while (cardRepository.findByCardNumber(cardNumValue).isPresent());
        card.setCardNumber(cardNumValue);

        card.setPin(cardRequestDTO.getPin());
        card.setCvv(generateCvv());
        card.setExpirationDate(LocalDate.now().plusYears(5));
        card.setActive(true);

        if (CartType.DEBIT.name().equalsIgnoreCase(cardRequestDTO.getCardType())) {
            card.setCardBalance(account.getBalance());
        } else if (CartType.CREDIT.name().equalsIgnoreCase(cardRequestDTO.getCardType())) {
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


    public CardDTO getCardDetails(Long cardId) throws Exception  {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new Exception("Card not found!"));

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
        List<Card> cards = cardRepository.getAllCardsByCustomerId(customerId);

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
        if(card.getCardType().name().equals(CartType.DEBIT.name())){
            if(card.getCardBalance().compareTo(amount)<0) {
                throw new IllegalArgumentException("Insufficent card balance");
            }
            card.setCardBalance(card.getCardBalance().subtract(amount));

            Account account = card.getAccount();
            account.setBalance(account.getBalance().subtract(amount));
            cardRepository.save(card);
            accountRepository.save(account);
        }
        else if(card.getCardType().name().equals(CartType.CREDIT.name())){
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



}
