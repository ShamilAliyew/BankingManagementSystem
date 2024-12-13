package BankingManagementSystem.BankingManagementSystem.account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    AccountDto createAccount(AccountDto accountDto);
    AccountDto getAccountById(Integer id);
    AccountDto deposit(Integer id, BigDecimal amount);
    AccountDto withdraw(Integer id, BigDecimal amount);
    List<AccountDto> getAllAccounts();
    void  deleteAccount(Integer id);
}
