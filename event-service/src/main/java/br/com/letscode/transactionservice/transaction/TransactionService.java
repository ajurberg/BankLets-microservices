package br.com.letscode.transactionservice.transaction;

import java.util.List;

interface TransactionService {

    TransactionDTO save(TransactionDTO transactionDTO);
    void delete(Long transactionId);
    TransactionDTO update(Long transactionId, TransactionDTO transactionDTO);
    List<TransactionDTO> listAll();

}
