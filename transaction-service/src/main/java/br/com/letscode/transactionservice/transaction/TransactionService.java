package br.com.letscode.transactionservice.transaction;

import java.math.BigDecimal;
import java.util.List;

interface TransactionService {

    TransactionDTO save(TransactionDTO transactionDTO);
    void delete(Long transactionId);
    TransactionDTO update(Long transactionId, TransactionDTO transactionDTO);
    List<TransactionDTO> listAll();

    BigDecimal viewBalance(Long accountId);
    void deposit(Long accountId, BigDecimal amount);
    void withdraw(Long accountId, BigDecimal amount);
    void transfer(Long withdrawalAccountId, Long receivingAccountId, BigDecimal amount);

}
