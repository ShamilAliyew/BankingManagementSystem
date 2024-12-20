package BankingManagementSystem.BankingManagementSystem.account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountMapper {


        public static AccountDto mapToAccountDto(Account account) {
            AccountDto accountDto = new AccountDto();

                    //accountDto.setCustomerId(account.getCustomerId());
                    accountDto.setAccountNumber(account.getAccountNumber());
                    accountDto.setAccountType(account.getAccountType().name()); // Enum -> String
                    accountDto.setBalance(account.getBalance());
                    accountDto.setCreatedDate(account.getCreatedDate());
                    accountDto.setDeleted(account.isDeleted());

            return accountDto;
        }


        public static Account mapToAccount(AccountDto accountDto) {
            Account account = new Account();
            //account.setId(accountDto.getId());
            account.setAccountNumber(accountDto.getAccountNumber());
            account.setAccountType(AccountType.valueOf(accountDto.getAccountType())); // String -> Enum
            account.setBalance(accountDto.getBalance());
            account.setCreatedDate(LocalDateTime.now());
            account.setDeleted(false);
            return account;
        }
    }


