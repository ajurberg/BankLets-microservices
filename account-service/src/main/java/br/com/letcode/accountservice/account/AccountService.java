package br.com.letcode.accountservice.account;

import java.math.BigDecimal;
import java.util.List;

interface AccountService {

    AccountDTO save(AccountDTO accountDTO);
    void delete(Long accountId);
    AccountDTO update(Long accountId, AccountDTO accountDTO);
    List<AccountDTO> listAll();
    AccountDTO getById(Long accountId);

    BigDecimal viewBalance(Long accountId);
    void deposit(BigDecimal amount, Long accountId);
    void withdraw(BigDecimal amount, Long accountId);
    void transfer(BigDecimal amount, Long withdrawalAccountId, Long receivingAccountId);

}
