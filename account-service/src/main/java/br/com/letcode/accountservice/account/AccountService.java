package br.com.letcode.accountservice.account;

import java.util.List;

interface AccountService {

    AccountDTO save(AccountDTO accountDTO);
    void delete(Long accountId);
    AccountDTO update(Long accountId, AccountDTO accountDTO);
    List<AccountDTO> listAll();
    AccountDTO getById(Long accountId);

}
