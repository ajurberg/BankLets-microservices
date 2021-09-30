package br.com.letscode.userservice.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class UserDTO implements Serializable {

    private static final long serialVersionUID = 7407475990083206760L;

    private Long userId;
    private String userName;
    private String userPassword;

}
