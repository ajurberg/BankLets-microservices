package br.com.letscode.eventservice.account;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("account-service")
public interface AccountClientRepository {

    @RequestMapping("/accounts/{accountId}")
    AccountDTO getById(@PathVariable Long accountId);

}
