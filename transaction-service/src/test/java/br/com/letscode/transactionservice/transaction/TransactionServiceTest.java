package br.com.letscode.transactionservice.transaction;

import br.com.letscode.transactionservice.account.AccountClientRepository;
import br.com.letscode.transactionservice.account.AccountDTO;
import br.com.letscode.transactionservice.user.UserClientRepository;
import br.com.letscode.transactionservice.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountClientRepository accountClientRepository;
    @Mock
    private UserClientRepository userClientRepository;

    private TransactionService transactionService;
    private List<TransactionDTO> transactionDTOListMock;

//    @BeforeEach
//    void setUp() {
//        transactionService = new TransactionServiceImpl(transactionRepository, accountClientRepository, userClientRepository);
//        transactionDTOListMock = Arrays.asList(
//                new TransactionDTO(1,100, new UserDTO(1, "usário1", "123"),
//                        singletonList(new AccountDTO(1, "account1", )))
//        )
//
//    }

}
