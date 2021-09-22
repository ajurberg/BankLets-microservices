package br.com.letcode.accountservice.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private BigDecimal accountBalance;
    private Long userId;
    private Date openingDate;
    private Date closingDate;
    private boolean status;

    // TODO Evaluate if setStatus makes sense
    static Account of(AccountDTO accountDTO) {
        var account = new Account(accountDTO.getAccountId(), accountDTO.getAccountBalance(),
                accountDTO.getUserId(), accountDTO.getOpeningDate(), accountDTO.getClosingDate(),
                accountDTO.isStatus());
        if (account.closingDate != null ) {
            account.setStatus(false);
        }
        return account;
    }

    static List<AccountDTO> parseToDtoList(List<Account> accountList) {
        return accountList.stream().map(Account::parseToDtoMono).collect(Collectors.toList());
    }

    static AccountDTO parseToDtoMono(Account account) {
        return new AccountDTO(account.getAccountId(), account.getAccountBalance(),
                account.getUserId(), account.getOpeningDate(), account.getClosingDate(),
                account.isStatus());
    }

    static List<Account> ofList(List<AccountDTO> accountDTOList) {
        return accountDTOList.stream().map(Account::of).collect(Collectors.toList());
    }

}
