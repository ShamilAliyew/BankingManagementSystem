package BankingManagementSystem.BankingManagementSystem.Card;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cards")
public class CardController {

    private final CardServiceImpl cardServiceImpl;

    public CardController(CardServiceImpl cardServiceImpl) {
        this.cardServiceImpl = cardServiceImpl;
    }

    @PostMapping("/create")
    public ResponseEntity<CardDTO> createCard( @RequestBody CardCreateRequestDTO cardRequestDTO) {
        CardDTO createdCard = cardServiceImpl.createCard(cardRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }
}
