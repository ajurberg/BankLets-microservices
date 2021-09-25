package br.com.letcode.accountservice.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Enumerated(EnumType.STRING)
    private AccountTypeEnum type;

    private LocalDate openingDate;
    private LocalDate closingDate;

    private BigDecimal accountBalance;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private AccountStatusEnum status;

    static Account of(AccountDTO accountDTO) {
        var account = new Account(accountDTO.getAccountId(),accountDTO.getType(), accountDTO.getOpeningDate(),
                accountDTO.getClosingDate(), accountDTO.getAccountBalance(), accountDTO.getUserId(),
                accountDTO.getStatus());
        if (account.closingDate != null ) {
            account.setStatus(AccountStatusEnum.ACTIVE);
        }
        return account;
    }

    static List<AccountDTO> parseToDtoList(List<Account> accountList) {
        return accountList.stream().map(Account::parseToDtoMono).collect(Collectors.toList());
    }

    static AccountDTO parseToDtoMono(Account account) {
        return new AccountDTO(account.getAccountId(), account.getType(), account.getAccountBalance(),
                account.getUserId(), account.getOpeningDate(), account.getClosingDate(),
                account.getStatus());
    }

    static List<Account> ofList(List<AccountDTO> accountDTOList) {
        return accountDTOList.stream().map(Account::of).collect(Collectors.toList());
    }

}
