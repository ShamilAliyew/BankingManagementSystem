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
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto) {

        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Integer id) {
        AccountDto accountDto = accountService.getAccountById(id);
        return  ResponseEntity.ok(accountDto);
    }


    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDto>deposit(@PathVariable Integer id,@RequestBody Map<String, BigDecimal> request){
        BigDecimal amount = request.get("amount");
        AccountDto accountDto = accountService.deposit(id, amount);
         return ResponseEntity.ok(accountDto);
    }

    @PutMapping("{id}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Integer id, @RequestBody Map<String, BigDecimal> request){
        BigDecimal amount = request.get("amount");
        AccountDto accountDto = accountService.withdraw(id, amount);

        return ResponseEntity.ok(accountDto);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Integer id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted");
    }

}
