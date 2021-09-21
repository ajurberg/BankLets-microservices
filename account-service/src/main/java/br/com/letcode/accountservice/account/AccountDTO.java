package br.com.letcode.accountservice.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class AccountDTO implements Serializable {

    private static final long serialVersionUID = -6030269862170249309L;
    private Long accountId;
    private BigDecimal accountBalance;
    private Long userId;
    private Date openingDate;
    private Date closingDate;
    private boolean status;

}
