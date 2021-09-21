package br.com.letcode.accountservice.account;

import br.com.letcode.accountservice.exception.AccountAlreadyExistsException;
import br.com.letcode.accountservice.exception.CloseAccountBalanceException;
import br.com.letcode.accountservice.exception.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountDTO save(AccountDTO accountDTO) {
        if (accountRepository.findById(Objects.requireNonNull(accountDTO).getAccountId()).isPresent()) {
            throw new AccountAlreadyExistsException();
        }
        Account account = Account.of(accountDTO);
        account.setAccountId(null);
        log.info("save method of AccountServiceImpl ran successfully.");
        return Account.parseToDtoMono(accountRepository.save(account));
    }

    @Override
    public void delete(Long accountId) {
        log.info("delete method of AccountServiceImpl ran successfully.");
        accountRepository.deleteById(accountId);
    }

    @Override
    public AccountDTO update(Long accountId, AccountDTO accountDTO) {
        if (accountRepository.findById(accountId).isPresent()) {
            Account account = Account.of(accountDTO);
            account.setAccountId(accountId);
            log.info("update method of AccountServiceImpl ran successfully.");
            return Account.parseToDtoMono(accountRepository.save(account));
        } else {
            throw new NoSuchElementException("Account ID not found.");   
        }
    }

    @Override
    public List<AccountDTO> listAll() {
        log.info("listAll method of AccountServiceImpl ran successfully.");
        return Account.parseToDtoList(accountRepository.findAll());
    }

    @Override
    public AccountDTO getById(Long accountId) {
        log.info("getById method of AccountServiceImpl ran successfully.");
        return Account.parseToDtoMono(accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account ID not found.")));
    }

    public Account saveAccount(Account account) {
        log.info("saveAccount method of AccountServiceImpl ran successfully.");
        return accountRepository.save(account);
    }

    public Account findAccountById(Long accountId) {
        log.info("findAccountById method of AccountServiceImpl ran successfully.");
        return accountRepository.findById(accountId).orElse(null);
    }

    public BigDecimal viewBalance(Long accountId, AccountDTO accountDTO) {
        Account account = findAccountById(accountId);
        log.info("viewBalance method of AccountService ran successfully.");
        return account.getAccountBalance();
    }

    public void deposit(BigDecimal amount, Long accountId) {
        Account account = findAccountById(accountId);
        BigDecimal balance = account.getAccountBalance().add(amount);
        account.setAccountBalance(balance);
        log.info("deposit method of AccountService ran successfully.");
    }

    public void withdraw(BigDecimal amount, Long accountId) {
        Account account = findAccountById(accountId);
        if (amount.compareTo(account.getAccountBalance()) < 0 ) {
            BigDecimal balance = account.getAccountBalance().subtract(amount);
            account.setAccountBalance(balance);
            log.info("withdraw method of AccountService ran successfully.");
        } else {
            throw new InsufficientBalanceException();
        }
    }

    public void transfer(BigDecimal amount, Long withdrawalAccountId, Long receivingAccountId) {
        withdraw(amount, withdrawalAccountId);
        deposit(amount, receivingAccountId);
        log.info("transfer method of AccountService ran successfully.");
    }

    //invest
    //closeAccount

    public void deleteAccount(Long accountId) {
        Account account = findAccountById(accountId);
        if (account.getAccountBalance().compareTo(BigDecimal.ZERO) == 0) {
            log.info("deleteAccount method of AccountService ran successfully. Account was removed.");
            accountRepository.deleteById(accountId);
        } else {
            if (account.getAccountBalance().compareTo(BigDecimal.ZERO) > 0) {
                log.info("deleteAccount method of AccountService ran successfully, but account balance is positive.");
            } else {
                log.info("deleteAccount method of AccountService ran successfully, but account balance is negative.");
            }
            throw new CloseAccountBalanceException();
        }
    }

}
