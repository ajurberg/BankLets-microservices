package br.com.letscode.eventservice.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private static final long serialVersionUID = -6030269862170249309L;
    private Long accountId;
    private BigDecimal accountBalance;
    private boolean status;
    private Long userId;

}
