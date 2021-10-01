package br.com.letscode.securitylogin.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@AllArgsConstructor
class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final int EXPIRATION_TOKEN = 60000_000;
    public static final String PASSWORD_TOKEN = "75a8030c-83c3-41a4-9bc9-3a9c97a7d8db";

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            Login login = new ObjectMapper().readValue(request.getInputStream(), Login.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getLogin(), login.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException("Failed authentication", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {
        DetailedLoginData detailedLoginData = (DetailedLoginData) authentication.getPrincipal();
        String token = JWT.create()
                .withSubject(detailedLoginData.getUsername())
                .withClaim("id", detailedLoginData.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN))
                .sign(Algorithm.HMAC512(PASSWORD_TOKEN));
        response.getWriter().write(token);
        response.getWriter().flush();
    }

}
