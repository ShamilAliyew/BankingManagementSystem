package BankingManagementSystem.BankingManagementSystem.Card;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/cards")
public class CardController {

    private final CardRepository cardRepository;
    private  CardServiceImpl cardServiceImpl;

    public CardController(CardServiceImpl cardServiceImpl, CardRepository cardRepository) {
        this.cardServiceImpl = cardServiceImpl;
        this.cardRepository = cardRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<CardDTO> createCard(@RequestBody CardRequestDTO cardRequestDTO){
        System.out.println("Received CardRequestDTO: " + cardRequestDTO.toString());
        CardDTO createdCard  = cardServiceImpl.createCard(cardRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CardDTO>getCardById(@PathVariable Long id) {
        try{
        CardDTO cardDetails = cardServiceImpl.getCardById(id);
        return ResponseEntity.ok(cardDetails);
    }catch(IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    }

    @PutMapping("/{cardNumber}/limit")
    public ResponseEntity<String >updateCardLimit(@PathVariable String cardNumber, @RequestParam  BigDecimal newLimit) {
        cardServiceImpl.updateCardLimit(cardNumber, newLimit);
        return ResponseEntity.ok("Card limit updated succesfully");
    }

    @PutMapping("/{cardNumber}/payment")
    public ResponseEntity<String> makePayment(@PathVariable String cardNumber,@RequestParam  BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero");
        }
        try{
            cardServiceImpl.makePayment(cardNumber, amount);
            return ResponseEntity.ok("Payment made succesfully");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{customerId}/all")
    public ResponseEntity<List<CardDTO>>getAllCardsByCustomerId(Long customerId){
        try{
            List<CardDTO> cardList = cardServiceImpl.getAllCardsByCustomerId(customerId);
            return ResponseEntity.ok(cardList);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PutMapping("/{cardNumber}/withdraw")
    public ResponseEntity<String> withdrawFromCard(@PathVariable String cardNumber, @RequestParam BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO)<0){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero");
        }
        try{
            cardServiceImpl.withdrawFromCard(cardNumber,amount);
            return ResponseEntity.ok(amount+" has been charged from your card.");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
 }
    @PutMapping("/{cardNumber}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String cardNumber,@RequestParam BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO)<0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero");
        }
        try{
            cardServiceImpl.deposit(cardNumber,amount);
            return ResponseEntity.ok(amount+ " added to your balance");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/{id}/update-pin")
    public ResponseEntity<String>updatePin(@PathVariable Long id,@RequestParam String oldPin, @RequestParam String newPin){
        try{
            cardServiceImpl.updateCardPin(id,oldPin,newPin);
            return ResponseEntity.ok("Card Pin updated succesfully");
        }catch (IllegalArgumentException e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
