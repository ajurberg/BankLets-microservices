package br.com.letscode.eventservice.event;

import br.com.letscode.eventservice.account.AccountDTO;
import br.com.letscode.eventservice.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO implements Serializable {

    private static final long serialVersionUID = -7798816862560204775L;

    private Long id;
    private UserDTO user;
    private List<AccountDTO> accountList;

    static List<Long> convertAccountDTOListToAccountIdList(List<AccountDTO> accountList) {
        return accountList.stream().map(AccountDTO::getAccountId).collect(Collectors.toList());
    }

    static List<AccountDTO> convertAccountIdListToAccountDTOList(List<Long> accountIdList) {
        return accountIdList.stream()
                .map(accountId -> AccountDTO.builder().accountId(accountId).build())
                .collect(Collectors.toList());
    }

}
