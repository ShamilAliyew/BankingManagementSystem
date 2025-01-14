package BankingManagementSystem.BankingManagementSystem.Card;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/cards")
public class CardController {

    private final CardRepository cardRepository;
    private final CardService cardService;

    public CardController(CardService cardService, CardRepository cardRepository) {
        this.cardService = cardService;
        this.cardRepository = cardRepository;
    }
    @PostMapping("/order-debit")
    public ResponseEntity<CardDTO> orderDebitCard(@RequestBody DebitCardRequest debitCardRequest){
        try{
            CardDTO createdCard = cardService.orderDebitCard(debitCardRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/order-credit")
    public ResponseEntity<CardDTO> orderCreditCard(@RequestBody CreditCardRequest creditCardRequest) {
        try {
            CardDTO createdCard = cardService.orderCreditCard(creditCardRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/card-transfer")
    public  ResponseEntity<String> transfer(@RequestBody TransferFromCardRequest transferFromCardRequest) {
        try{
            cardService.transfer(transferFromCardRequest.getFromCardNumber(),
                    transferFromCardRequest.getToCardNumber(),
                    transferFromCardRequest.getAmount());
            return ResponseEntity.ok("Transfer Successful");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/card-payment")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequestCard paymentRequestCard) {
        if(paymentRequestCard.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero");
        }
        try{
            cardService.makePayment(paymentRequestCard.getCardNumber(),paymentRequestCard.getCvv() ,paymentRequestCard.getAmount());
            BigDecimal cashback = paymentRequestCard.getAmount().multiply(BigDecimal.valueOf(0.05));
            return ResponseEntity.ok("Payment made successfully\nCashback: "+cashback);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/card-withdraw")
    public ResponseEntity<String> withdrawFromCard(@RequestBody CardWithdrawRequest cardWithdraw){
        if(cardWithdraw.getAmount().compareTo(BigDecimal.ONE)<0){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero");
        }
        try{
            cardService.withdrawFromCard(cardWithdraw.getCardNumber(),cardWithdraw.getAmount(),cardWithdraw.getPin());
            return ResponseEntity.ok(cardWithdraw.getAmount()+" has been charged from your card.");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/card-deposit")
    public ResponseEntity<String> deposit(@RequestBody CardDepositRequest cardDeposit){
        if(cardDeposit.getAmount().compareTo(BigDecimal.ZERO)<0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero");
        }
        try{
            cardService.deposit(cardDeposit.getCardNumber(),cardDeposit.getAmount());
            return ResponseEntity.ok(cardDeposit.getAmount()+ " added to your balance");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/update-pin")
    public ResponseEntity<String>updatePin(@PathVariable Long id,@RequestBody UpdatePinRequest updatePinRequest){
        try{
            cardService.updateCardPin(id,updatePinRequest.getOldPin(),updatePinRequest.getNewPin());
            return ResponseEntity.ok("Card Pin updated successfully");
        }catch (IllegalArgumentException e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{cardNumber}/update-limit")
    public ResponseEntity<String>updateCardLimit(@PathVariable String cardNumber,@RequestParam BigDecimal newLimit) {
        cardService.updateCardLimit(cardNumber, newLimit);
        return ResponseEntity.ok("Card limit updated successfully");
    }

    @GetMapping("/{customerId}/all")
    public ResponseEntity<List<CardDTO>>getAllCardsByCustomerId(@PathVariable Long customerId){
        try{
            List<CardDTO> cardList = cardService.getAllCardsByCustomerId(customerId);
            return ResponseEntity.ok(cardList);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO>getCardById(@PathVariable Long id){
        try{
            CardDTO cardDetails = cardService.getCardById(id);
            return ResponseEntity.ok(cardDetails);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
