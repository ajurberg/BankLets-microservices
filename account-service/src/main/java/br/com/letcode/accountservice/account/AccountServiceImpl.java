package br.com.letcode.accountservice.account;

import br.com.letcode.accountservice.exception.AccountAlreadyExistsException;
import br.com.letcode.accountservice.exception.CloseAccountBalanceException;
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
        account.setType(AccountTypeEnum.REGULAR);
        log.info("save method of AccountServiceImpl ran successfully.");
        return Account.parseToDtoMono(accountRepository.save(account));
    }

    @Override
    public void delete(Long accountId) {
        Account account = findAccountById(accountId);
        if (account.getAccountBalance().compareTo(BigDecimal.ZERO) == 0) {
            log.info("delete method of AccountServiceImpl ran successfully. Account was removed.");
            accountRepository.deleteById(accountId);
        } else {
            if (account.getAccountBalance().compareTo(BigDecimal.ZERO) > 0) {
                log.info("delete method of AccountServiceImpl ran successfully, but account balance is positive.");
            } else {
                log.info("delete method of AccountServiceImpl ran successfully, but account balance is negative.");
            }
            throw new CloseAccountBalanceException();
        }
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

    public Account findAccountById(Long accountId) {
        log.info("findAccountById method of AccountServiceImpl ran successfully.");
        return accountRepository.findById(accountId).orElse(null);
    }

}
