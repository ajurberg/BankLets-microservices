package br.com.letcode.accountservice.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private BigDecimal accountBalance;
    private boolean status;
    private Long userId;

    static Account of(AccountDTO accountDTO) {
        return new Account(accountDTO.getAccountId(),
                accountDTO.getAccountBalance(), accountDTO.isStatus(), accountDTO.getUserId());
    }

    static List<AccountDTO> parseToDtoList(List<Account> accountList) {
        return accountList.stream().map(Account::parseToDtoMono).collect(Collectors.toList());
    }

    static AccountDTO parseToDtoMono(Account account) {
        return new AccountDTO(account.getAccountId(), account.getAccountBalance(), account.isStatus(), account.getUserId());
    }

    static List<Account> ofList(List<AccountDTO> accountDTOList) {
        return accountDTOList.stream().map(Account::of).collect(Collectors.toList());
    }

}
