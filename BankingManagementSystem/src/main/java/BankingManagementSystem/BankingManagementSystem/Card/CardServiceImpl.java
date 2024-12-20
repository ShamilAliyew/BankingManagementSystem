package BankingManagementSystem.BankingManagementSystem.Card;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl {
    private final CardRepository cardRepository;
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public CardDTO getCardDetails(Long cardId) throws Exception  {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new Exception("Card not found!"));

        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(card.getId());
        cardDTO.setCardNumber("**** **** **** " + card.getCardNumber().substring(12));
        cardDTO.setAccountId(card.getAccount().getId());
        cardDTO.setCustomerId(card.getAccount().getCustomer().getId());
        cardDTO.setCardType(card.getCardType().toString());
        cardDTO.setBalance(card.getCardBalance());
        cardDTO.setLimit(card.getLimit());
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
                    cardDTO.setAccountId(card.getAccount().getId());
                    cardDTO.setCardType(card.getCardType().toString());
                    cardDTO.setBalance(card.getCardBalance());
                    cardDTO.setExpirationDate(card.getExpirationDate());
                    cardDTO.setActive(card.isActive());
                    return cardDTO;
                })
                .collect(Collectors.toList());
    }


}
