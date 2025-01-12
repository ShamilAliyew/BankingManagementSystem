package BankingManagementSystem.BankingManagementSystem.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")

public class AccountController {
    private AccountServiceImp accountServiceImp;

    public AccountController(AccountServiceImp accountServiceImp) {
        this.accountServiceImp = accountServiceImp;
    }

    @PostMapping("/add_account")
    public ResponseEntity<AccountDto> addAccount(@RequestBody CreateAccountDto createAccountDto) {
        try {
            AccountDto createdAccount = accountServiceImp.createAccount(createAccountDto);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
}

@PostMapping("/login")
public ResponseEntity<AccountDto>login(@RequestBody AccountLoginDTO accountLoginDTO) {
        AccountDto loggedInAccount = accountServiceImp.login(accountLoginDTO);
    return new ResponseEntity<>(loggedInAccount, HttpStatus.OK);
}


    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountDetails(@PathVariable Long id) {
        AccountDto accountDto = accountServiceImp.getAccountDetails(id);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/make-payment")
    ResponseEntity<String> makePayment(@RequestBody PaymentRequestAccount paymentRequestAccount) {
    try{
    accountServiceImp.makePayment(paymentRequestAccount.getAccountNumber(), paymentRequestAccount.getAmount());
        BigDecimal cashback = paymentRequestAccount.getAmount().multiply(BigDecimal.valueOf(0.03));

        return ResponseEntity.ok("Payment Successful\nCashback: "+ cashback);
    }catch (IllegalArgumentException e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    }
    @PostMapping("/deposit")
    ResponseEntity<String> deposit(@RequestBody PaymentRequestAccount paymentRequestAccount) {
        try{
            accountServiceImp.deposit(paymentRequestAccount.getAccountNumber(), paymentRequestAccount.getAmount());
            return ResponseEntity.ok("Amount added to your account successfully");
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/withdraw")
    ResponseEntity<String> withdraw(@RequestBody PaymentRequestAccount paymentRequestAccount) {
        try{
            accountServiceImp.withdraw(paymentRequestAccount.getAccountNumber(), paymentRequestAccount.getAmount());
            return ResponseEntity.ok("Take your money");
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/transfer")
    ResponseEntity<String>transfer(@RequestBody TransferFromAccountRequest transferFromAccountRequest) {
        try{
            accountServiceImp.transfer(transferFromAccountRequest.getFromAccountNumber(),
                    transferFromAccountRequest.getToAccountNumber(),
                    transferFromAccountRequest.getAmount());
            return ResponseEntity.ok("Transfer Successful");
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try{
            accountServiceImp.deleteAccount(id);
            return ResponseEntity.ok("Account Deleted");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
