package BankingManagementSystem.BankingManagementSystem.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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


    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountDetails(@PathVariable Long id) {
        AccountDto accountDto = accountServiceImp.getAccountDetails(id);
        return  ResponseEntity.ok(accountDto);
    }
//
//
//    @PutMapping("/{id}/deposit")
//    public ResponseEntity<AccountDto>deposit(@PathVariable Long id,@RequestBody Map<String, BigDecimal> request){
//        BigDecimal amount = request.get("amount");
//        AccountDto accountDto = accountService.deposit(id, amount);
//         return ResponseEntity.ok(accountDto);
//    }
//
//    @PutMapping("{id}/withdraw")
//    public ResponseEntity<AccountDto> withdraw(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request){
//        BigDecimal amount = request.get("amount");
//        AccountDto accountDto = accountService.withdraw(id, amount);
//
//        return ResponseEntity.ok(accountDto);
//    }
//
//
//
//    @DeleteMapping("{id}")
//    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
//        accountService.deleteAccount(id);
//        return ResponseEntity.ok("Account deleted");
//    }

}
