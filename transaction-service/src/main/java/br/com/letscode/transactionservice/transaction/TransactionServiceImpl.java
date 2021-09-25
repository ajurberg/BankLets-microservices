package br.com.letscode.transactionservice.transaction;

import br.com.letscode.transactionservice.account.AccountClientRepository;
import br.com.letscode.transactionservice.account.AccountDTO;
import br.com.letscode.transactionservice.exception.FailedDependencyException;
import br.com.letscode.transactionservice.exception.InsufficientBalanceException;
import br.com.letscode.transactionservice.exception.WrongAmountException;
import br.com.letscode.transactionservice.user.UserClientRepository;
import br.com.letscode.transactionservice.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient funds. Operation failed.";

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

    @Override
    public BigDecimal viewBalance(Long accountId) {
        AccountDTO accountDTO = accountClientRepository.getById(accountId);
        return accountDTO.getAccountBalance();
    }

    @Override
    public void deposit(Long accountId, BigDecimal amount) {
        AccountDTO accountDTO = accountClientRepository.getById(accountId);
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            accountDTO.setAccountBalance(accountDTO.getAccountBalance().add(amount));
        } else {
            throw new WrongAmountException(INSUFFICIENT_FUNDS_MESSAGE);
        }
        accountClientRepository.save(accountDTO.getAccountId());
    }

    @Override
    public void withdraw(Long accountId, BigDecimal amount) {
        AccountDTO accountDTO = accountClientRepository.getById(accountId);
        if (amount.compareTo(accountDTO.getAccountBalance()) < 0) {
            accountDTO.setAccountBalance(accountDTO.getAccountBalance().subtract(amount));
        } else {
            throw new InsufficientBalanceException(INSUFFICIENT_FUNDS_MESSAGE);
        }
        accountClientRepository.save(accountDTO.getAccountId());
    }

    @Override
    public void transfer(Long withdrawalAccountId, Long receivingAccountId, BigDecimal amount) {
        AccountDTO withdrawalAccountDTO = accountClientRepository.getById(withdrawalAccountId);
        AccountDTO receivingAccountDTO = accountClientRepository.getById(receivingAccountId);
        if (amount.compareTo(withdrawalAccountDTO.getAccountBalance()) < 0) {
            withdrawalAccountDTO.setAccountBalance(withdrawalAccountDTO.getAccountBalance().subtract(amount));
            receivingAccountDTO.setAccountBalance(receivingAccountDTO.getAccountBalance().add(amount));
        } else {
            throw new InsufficientBalanceException(INSUFFICIENT_FUNDS_MESSAGE);
        }
        accountClientRepository.save(withdrawalAccountDTO.getAccountId());
        accountClientRepository.save(receivingAccountDTO.getAccountId());
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
