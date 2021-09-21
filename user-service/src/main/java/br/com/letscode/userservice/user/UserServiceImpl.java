package br.com.letscode.userservice.user;

import br.com.letscode.userservice.exception.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO save(UserDTO userDTO) {
        log.info("save method of UserService ran successfully.");
        if (userRepository.findById(Objects.requireNonNull(userDTO).getUserId()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = User.of(userDTO);
        user.setUserId(null);
        return User.parseToDtoMono(userRepository.save(user));
    }

    @Override
    public void delete(Long userId) {
        log.info("delete method of UserService ran successfully.");
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO update(Long userId, UserDTO userDTO) {
        if (userRepository.findById(userId).isPresent()) {
            userDTO.setUserId(userId);
            User user = User.of(userDTO);
            log.info("update method of UserService ran successfully.");
            return User.parseToDtoMono(userRepository.save(user));
        } else {
            throw new NoSuchElementException("User ID not found.");
        }
    }

    @Override
    public List<UserDTO> listAll() {
        log.info("listAll method of UserService ran successfully.");
        return User.parseToDtoList(userRepository.findAll());
    }

    @Override
    public UserDTO getById(Long userId) {
        log.info("getById method of UserService ran successfully.");
        return User.parseToDtoMono(userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID not found."))
        );
    }

}
