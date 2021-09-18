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
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO update(Long userId, UserDTO userDTO) {
        if (userRepository.findById(userId).isPresent()) {
            userDTO.setUserId(userId);
            User user = User.of(userDTO);
            return User.parseToDtoMono(userRepository.save(user));
        } else {
            throw new NoSuchElementException("User ID not found.");
        }
    }

    @Override
    public List<UserDTO> listAll() {
        return User.parseToDtoList(userRepository.findAll());
    }

    @Override
    public UserDTO getById(Long userId) {
        return User.parseToDtoMono(userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID not found."))
        );
    }

}
