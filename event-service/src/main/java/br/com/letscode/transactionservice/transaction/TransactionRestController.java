package br.com.letscode.transactionservice.transaction;

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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/transactions")
@RestController
class TransactionRestController {

    private final TransactionService transactionService;

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

//    // TODO Move to transactions service
//    @GetMapping("balance/{accountId}")
//    @ResponseStatus(HttpStatus.OK)
//    public BigDecimal viewBalance(@PathVariable Long accountId, @RequestBody AccountDTO accountDTO) {
//        log.info("viewBalance method of accountRestController ran successfully.");
//        return accountService.viewBalance(accountId, accountDTO);
//    }
//
//    // FIXME Add @RequestBody!!
//    @PutMapping("deposit/{accountId}")
//    @ResponseStatus(HttpStatus.OK)
//    public void deposit(@PathVariable Long accountId, @RequestBody AccountDTO accountDTO) {
//        log.info("deposit method of accountRestController ran successfully.");
//        accountService.deposit(amount, accountId);
//    }
//
//    @PutMapping("withdraw/{accountId}/{amount}")
//    @ResponseStatus(HttpStatus.OK)
//    public void withdraw(@PathVariable Long accountId,
//                         @PathVariable BigDecimal amount) {
//        log.info("withdraw method of accountRestController ran successfully.");
//        accountService.withdraw(amount, accountId);
//    }
//
//    @PutMapping("transfer/{withdrawalAccountId}/{receivingAccountId}/{amount}")
//    @ResponseStatus(HttpStatus.OK)
//    public void transfer(@PathVariable Long withdrawalAccountId,
//                         @PathVariable Long receivingAccountId,
//                         @PathVariable BigDecimal amount) {
//        log.info("transfer method of accountRestController ran successfully.");
//        accountService.transfer(amount, withdrawalAccountId, receivingAccountId);
//    }

}
