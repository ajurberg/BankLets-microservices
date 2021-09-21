package br.com.letscode.transactionservice.transaction;

import br.com.letscode.transactionservice.user.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

class ConverterTransactionDTO {

    private ConverterTransactionDTO() {
        super();
    }

    static Transaction parseToTransactionMono(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .id(transactionDTO.getId())
                .userId(transactionDTO.getUser().getUserId())
                .accountIdList(TransactionDTO.convertAccountDTOListToAccountIdList(transactionDTO.getAccountList()))
                .build();
    }

    static List<Transaction> parseToTransactionList(List<TransactionDTO> transactionDTOListMock) {
        return transactionDTOListMock.stream()
                .map(ConverterTransactionDTO::parseToTransactionMono)
                .collect(Collectors.toList());
    }

    static TransactionDTO of(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .user(UserDTO.builder().userId(transaction.getId()).build())
                .accountList(TransactionDTO.convertAccountIdListToAccountDTOList(transaction.getAccountIdList()))
                .build();
    }

}
