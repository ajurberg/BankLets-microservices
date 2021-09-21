package br.com.letscode.transactionservice.transaction;

import br.com.letscode.transactionservice.account.AccountClientRepository;
import br.com.letscode.transactionservice.account.AccountDTO;
import br.com.letscode.transactionservice.exception.FailedDependencyException;
import br.com.letscode.transactionservice.user.UserClientRepository;
import br.com.letscode.transactionservice.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClientRepository accountClientRepository;
    private final UserClientRepository userClientRepository;

    @Override
    public TransactionDTO save(TransactionDTO transactionDTO) {
        Transaction transaction = ConverterTransactionDTO.parseToTransactionMono(transactionDTO);
        transaction.setId(null);
        buildDTO(transaction);
        return ConverterTransactionDTO.of(transactionRepository.save(transaction));
    }

    @Override
    public void delete(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    @Override
    public TransactionDTO update(Long transactionId, TransactionDTO transactionDTO) {
        if (transactionRepository.findById(transactionId).isPresent()) {
            Transaction transaction = ConverterTransactionDTO.parseToTransactionMono(transactionDTO);
            transaction.setId(transactionId);
            buildDTO(transaction);
            return ConverterTransactionDTO.of(transactionRepository.save(transaction));
        } else {
            throw new NoSuchElementException("Transaction ID not found.");
        }
    }

    @Override
    public List<TransactionDTO> listAll() {
        List<Transaction> transactionList = transactionRepository.findAll();
        return transactionList.stream()
                .map(this::buildDTO)
                .collect(Collectors.toList());
    }

    /**
     * Builds the TransactionDTO, which validates both userId and accountIdList
     * whether they exist on their respective microservices.
     * @param transaction the database entity
     * @return the TransactionDTO in accordance to the API contract,
     * containing the full userDTO and accountDTO list     *
     */
    private TransactionDTO buildDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .user(getUserById(transaction.getUserId()))
                .accountList(getAccountDTOList(transaction.getAccountIdList()))
                .build();
    }

    private UserDTO getUserById(Long userId) {
        return Optional.ofNullable(userClientRepository.getById(userId))
                .orElseThrow(() -> new FailedDependencyException("User ID not found."));
    }

    private List<AccountDTO> getAccountDTOList(List<Long> accountIdList) {
        return accountIdList.stream().map(this::validateAccountId)
                .collect(Collectors.toList());
    }

    private AccountDTO validateAccountId(Long accountId) {
        return Optional.ofNullable(accountClientRepository.getById(accountId))
                .orElseThrow(() -> new FailedDependencyException("Account ID not found."));
    }

}
