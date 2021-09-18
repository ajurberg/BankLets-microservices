package br.com.letscode.userservice.user;

import java.util.List;

interface UserService {

    UserDTO save(UserDTO userDTO);
    void delete(Long userId);
    UserDTO update(Long userId, UserDTO userDTO);
    List<UserDTO> listAll();
    UserDTO getById(Long userId);

}
