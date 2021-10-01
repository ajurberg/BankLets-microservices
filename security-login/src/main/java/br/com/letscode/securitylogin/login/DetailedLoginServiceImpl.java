package br.com.letscode.securitylogin.login;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
class DetailedLoginServiceImpl implements UserDetailsService {

    private final LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Login> loginOptional = loginRepository.findByLogin(login);
        if (loginOptional.isEmpty()) {
            throw new UsernameNotFoundException("Login: " + login + " not found.");
        }
        return new DetailedLoginData(loginOptional);
    }

}
