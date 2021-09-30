package br.com.letscode.transactionservice.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private static final long serialVersionUID = -6030269862170249309L;

    private Long accountId;
    private AccountTypeEnum type;
    private BigDecimal accountBalance;
    private LocalDate negativeBalanceDate;
    private LocalDate positiveBalanceDate;
    private Long userId;
    private String status;

}
