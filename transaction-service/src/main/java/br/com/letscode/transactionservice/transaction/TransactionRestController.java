package br.com.letscode.transactionservice.transaction;

import br.com.letscode.transactionservice.account.AccountClientRepository;
import br.com.letscode.transactionservice.account.AccountDTO;
import br.com.letscode.transactionservice.exception.InsufficientBalanceException;
import br.com.letscode.transactionservice.exception.WrongAmountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountException;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/transactions")
@RestController
class TransactionRestController {

    private final TransactionService transactionService;
    private final AccountClientRepository accountClientRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDTO save(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.save(transactionDTO);
    }

    @PutMapping("/{transactionId}")
    public TransactionDTO update(@PathVariable Long transactionId, @RequestBody TransactionDTO transactionDTO) {
        return transactionService.update(transactionId, transactionDTO);
    }

    @GetMapping
    public List<TransactionDTO> listAll() {
        return transactionService.listAll();
    }

    @DeleteMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long transactionId) {
        transactionService.delete(transactionId);
    }

    @GetMapping("balance/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal viewBalance(@PathVariable Long accountId) {
        AccountDTO accountDTO = accountClientRepository.getById(accountId);
        log.info("viewBalance method of accountRestController ran successfully.");
        return accountDTO.getAccountBalance();
    }

    @PutMapping("deposit/{accountId}/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void deposit(@PathVariable Long accountId,
                        @PathVariable BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            AccountDTO accountDTO = accountClientRepository.getById(accountId);
            accountDTO.setAccountBalance(accountDTO.getAccountBalance().add(amount));
            log.info("deposit method of accountRestController ran successfully.");
            accountClientRepository.save(accountDTO.getAccountId());
        } else {
            throw new WrongAmountException("Insufficient funds. Operation failed.");
        }
    }

    @PutMapping("withdraw/{accountId}/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@PathVariable Long accountId,
                         @PathVariable BigDecimal amount) {
        AccountDTO accountDTO = accountClientRepository.getById(accountId);
        if (amount.compareTo(accountDTO.getAccountBalance()) < 0) {
            accountDTO.setAccountBalance(accountDTO.getAccountBalance().subtract(amount));
            log.info("withdraw method of accountRestController ran successfully.");
        } else {
            throw new InsufficientBalanceException("Insufficient funds. Operation failed.");
        }
        accountClientRepository.save(accountDTO.getAccountId());
    }

    @PutMapping("transfer/{withdrawalAccountId}/{receivingAccountId}/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void transfer(@PathVariable Long withdrawalAccountId,
                         @PathVariable Long receivingAccountId,
                         @PathVariable BigDecimal amount) {
        AccountDTO withdrawalAccountDTO = accountClientRepository.getById(withdrawalAccountId);
        AccountDTO receivingAccountDTO = accountClientRepository.getById(receivingAccountId);
        if (amount.compareTo(withdrawalAccountDTO.getAccountBalance()) < 0) {
            withdrawalAccountDTO.setAccountBalance(withdrawalAccountDTO.getAccountBalance().subtract(amount));
            receivingAccountDTO.setAccountBalance(receivingAccountDTO.getAccountBalance().add(amount));
            log.info("transfer method of accountRestController ran successfully.");
        } else {
            throw new InsufficientBalanceException("Insufficient funds. Operation failed.");
        }
        accountClientRepository.save(withdrawalAccountDTO.getAccountId());
        accountClientRepository.save(receivingAccountDTO.getAccountId());
    }

}
