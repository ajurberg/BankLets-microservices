package br.com.letscode.securitylogin.login;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
class DetailedLoginData implements UserDetails {

    private static final long serialVersionUID = 9210806931739752588L;
    private final Optional<Login> loginOptional;

    public Long getId() {
        return loginOptional.orElse(new Login()).getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return loginOptional.orElse(new Login()).getPassword();
    }

    @Override
    public String getUsername() {
        return loginOptional.orElse(new Login()).getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
