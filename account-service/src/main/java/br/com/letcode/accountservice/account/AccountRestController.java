package br.com.letcode.accountservice.account;

import lombok.AllArgsConstructor;
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

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
@Slf4j
public class AccountRestController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createAccount(@RequestBody AccountDTO accountDTO) {
        log.info("createAccount method of accountRestController ran successfully.");
        return accountService.save(accountDTO);
    }

    @PutMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDTO update(@PathVariable Long accountId, @RequestBody AccountDTO accountDTO) {
        return accountService.update(accountId, accountDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDTO> listAll() {
        return accountService.listAll();
    }

    @GetMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDTO findAccountById(@PathVariable Long accountId) {
        log.info("findAccountById method of accountRestController ran successfully.");
        return accountService.getById(accountId);
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long accountId) {
        log.info("delete method of accountRestController ran successfully.");
        accountService.delete(accountId);
    }

}