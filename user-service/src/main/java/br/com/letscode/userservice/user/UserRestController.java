package br.com.letscode.userservice.user;

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
@RequestMapping("/users")
@Slf4j
class UserRestController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        log.info("createUser method of UserController ran successfully.");
        return userService.save(userDTO);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO findUserById(@PathVariable Long userId) {
        log.info("findUserById method of UserController ran successfully.");
        return userService.getById(userId);
    }

    @PutMapping("/{userId}")
    public UserDTO update(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return userService.update(userId, userDTO);
    }

    @GetMapping
    public List<UserDTO> listAll(){
        return userService.listAll();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable Long userId) {
        userService.delete(userId);
    }

}
