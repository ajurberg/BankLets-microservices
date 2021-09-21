package br.com.letscode.userservice.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String userName;
    private String userPassword;

    static User of(UserDTO userDTO) {
        return new User(userDTO.getUserId(), userDTO.getUserName(), userDTO.getUserPassword());
    }

    static UserDTO parseToDtoMono(User user) {
        return new UserDTO(user.getUserId(), user.getUserName(), user.getUserPassword());
    }

    static List<UserDTO> parseToDtoList(List<User> userList) {
        return userList.stream()
                .map(User::parseToDtoMono)
                .collect(Collectors.toList());
    }
    
}
