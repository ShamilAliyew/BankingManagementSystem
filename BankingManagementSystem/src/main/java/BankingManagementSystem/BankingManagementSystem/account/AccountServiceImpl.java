package BankingManagementSystem.BankingManagementSystem.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public  class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount=accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Integer id) {
         Account account=accountRepository.findById(id).orElseThrow(( )-> new  RuntimeException("Account doesn't exists"));
         return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Integer id, BigDecimal amount) {
        Account account=accountRepository.findById(id).orElseThrow(( )-> new  RuntimeException("Account doesn't exists"));
        account.setBalance(account.getBalance().add(amount));
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }


    @Override
    public AccountDto withdraw(Integer id, BigDecimal amount) {
        Account account=accountRepository.findById(id).orElseThrow(( )-> new  RuntimeException("Account doesn't exists"));
    if(account.getBalance().compareTo(amount)<0) {
        throw new  RuntimeException("Insufficient balance");

    }
    BigDecimal total = account.getBalance().subtract(amount);
    account.setBalance(account.getBalance().subtract(amount));
    Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts=accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::mapToAccountDto)
                .collect(Collectors.toList());

    }

    @Override
    public void deleteAccount(Integer id) {
        Account account=accountRepository.findById(id).orElseThrow(( )-> new  RuntimeException("Account doesn't exists"));
        accountRepository.deleteById(id);
    }

}
