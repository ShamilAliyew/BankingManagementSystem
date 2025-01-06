package BankingManagementSystem.BankingManagementSystem.account;

import java.math.BigDecimal;

public interface AccountService {
    AccountDto createAccount(AccountDto accountDto);
    public AccountDto login(AccountLoginDTO accountLoginDTO);
    AccountDto getAccountById(Account id);
    AccountDto deposit(Account id, BigDecimal amount);
    AccountDto withdraw(Account id, BigDecimal amount);
}
