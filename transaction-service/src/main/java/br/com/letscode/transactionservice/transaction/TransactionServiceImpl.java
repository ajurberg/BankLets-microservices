package br.com.letscode.transactionservice.transaction;

import br.com.letscode.transactionservice.account.AccountClientRepository;
import br.com.letscode.transactionservice.account.AccountDTO;
import br.com.letscode.transactionservice.account.AccountTypeEnum;
import br.com.letscode.transactionservice.exception.FailedDependencyException;
import br.com.letscode.transactionservice.exception.IllegalTransactionException;
import br.com.letscode.transactionservice.exception.InsufficientBalanceException;
import br.com.letscode.transactionservice.exception.WrongAmountException;
import br.com.letscode.transactionservice.user.UserClientRepository;
import br.com.letscode.transactionservice.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient funds. Operation failed.";
    private static final float FEE_CHARGE = 0.01F;

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
        TransactionDTO transactionDTO = new TransactionDTO();
        AccountDTO accountDTO = accountClientRepository.getById(accountId);
        switch (transactionDTO.getType()) {
            case REGULAR:
                log.info("Deposit completed upon regular transaction.");
                validadeDeposit(accountId, amount, accountDTO);
                accountClientRepository.save(accountDTO.getAccountId());
                break;
            case PIX:
                log.info("Deposit completed upon PIX transaction.");
                validadeDeposit(accountId, amount, accountDTO);
                accountClientRepository.save(accountDTO.getAccountId());
                break;
            case TED_DOC:
                log.info("Deposit completed upon TED/DOC transaction.");
                validadeDeposit(accountId, amount, accountDTO);
                accountClientRepository.save(accountDTO.getAccountId());
                break;
            case CELL_PHONE_RECHARGE:
                log.info("Transaction of cell phone recharge.");
        }
    }

    @Override
    public void withdraw(Long accountId, BigDecimal amount) {
        TransactionDTO transactionDTO = new TransactionDTO();
        AccountDTO accountDTO = accountClientRepository.getById(accountId);
        switch (transactionDTO.getType()) {
            case REGULAR:
                log.info("Withdraw completed upon regular transaction.");
                validateWithdraw(amount, accountDTO);
                accountClientRepository.save(accountDTO.getAccountId());
                break;
            case PIX:
                log.info("Withdraw completed upon PIX transaction.");
                accountClientRepository.save(accountDTO.getAccountId());
                break;
            case TED_DOC:
                log.info("Withdraw completed upon TED/DOC transaction.");
                accountClientRepository.save(accountDTO.getAccountId());
                break;
            case CELL_PHONE_RECHARGE:
                throw new IllegalTransactionException("Illegal operation.");
        }
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

    private BigDecimal computeInterest(Long accountId) {
        AccountDTO accountDTO = accountClientRepository.getById(accountId);
        if (accountDTO.getAccountBalance().compareTo(BigDecimal.ZERO) > 0) {
            accountDTO.setPositiveBalanceDate(LocalDate.now());
            accountDTO.setType(AccountTypeEnum.REGULAR);
            long days = accountDTO.getNegativeBalanceDate().until(accountDTO.getPositiveBalanceDate(), ChronoUnit.DAYS);
            BigDecimal interest = BigDecimal.valueOf(Math.pow(1 + FEE_CHARGE, days));
            log.info("You paid your debt of R${}. You were overdrawn for {} days.", interest, days);
            return interest;
        } else {
            throw new NoSuchElementException();
        }
    }

    private void validadeDeposit(Long accountId, BigDecimal amount, AccountDTO accountDTO) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            accountDTO.setAccountBalance(accountDTO.getAccountBalance().add(amount));
            if (accountDTO.getType() == AccountTypeEnum.OVERDRAFT) {
                BigDecimal currentBalance = accountDTO.getAccountBalance();
                if (amount.compareTo(currentBalance) > 0) {
                    BigDecimal interest = computeInterest(accountId);
                    accountDTO.setAccountBalance(accountDTO.getAccountBalance().add(amount.subtract(interest)));
                }
            } else {
                accountDTO.setAccountBalance(accountDTO.getAccountBalance().add(amount));
            }
        } else {
            throw new WrongAmountException(INSUFFICIENT_FUNDS_MESSAGE);
        }
    }

    private void validateWithdraw(BigDecimal amount, AccountDTO accountDTO) {
        if (amount.compareTo(accountDTO.getAccountBalance()) >= 0) {
            accountDTO.setNegativeBalanceDate(LocalDate.now());
            accountDTO.setType(AccountTypeEnum.OVERDRAFT);
            log.info("You went into your bank overdraft. The daily interest is 1%.");
        }
        accountDTO.setAccountBalance(accountDTO.getAccountBalance().subtract(amount));
    }

    /**
     * Builds the TransactionDTO, which validates both userId and accountIdList
     * whether they exist on their respective microservices.
     *
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
