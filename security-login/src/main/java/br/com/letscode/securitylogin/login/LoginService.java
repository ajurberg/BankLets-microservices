package br.com.letscode.securitylogin.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
class LoginService {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> createLogin(Login login) {
        if (loginRepository.findByLogin(login.getLogin()).isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login is already registered.");
        } else {
            login.setLogin(passwordEncoder.encode(login.getLogin()));
            login.setPassword(passwordEncoder.encode(login.getPassword()));
            log.info("Login and password were registered with success.");
            return ResponseEntity.status(HttpStatus.CREATED).body(loginRepository.save(login));
        }
    }

    public ResponseEntity<?> changePassword(String password, String token) throws JSONException, IOException {
        JSONObject tokenJson = new JSONObject(jwtFilter(token));
        if (!isValidBearerToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Expired or invalid token.");
        }
        if (list((Long) tokenJson.get("id")).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login was not found.");
        }
        Optional<Login> loginOptional = list((Long) tokenJson.get("id"));
        Login login = new Login();
        if (loginOptional.isPresent()) {
            login.setId(loginOptional.get().getId());
            login.setLogin(loginOptional.get().getLogin());
            login.setActive(loginOptional.get().isActive());
            login.setPassword(passwordEncoder.encode(password));
        }
        return ResponseEntity.status(HttpStatus.OK).body(loginRepository.save(login));
    }

    public ResponseEntity<?> remove(String token) throws JSONException, IOException {
        JSONObject tokenJson = new JSONObject(jwtFilter(token));
        if (!isValidBearerToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Expired or invalid token.");
        }
        if (list((Long) tokenJson.get("id")).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Expired or invalid login.");
        }
        Login login = list((Long) tokenJson.get("id")).get();
        login.setActive(false);
        loginRepository.save(login);
        return ResponseEntity.status(HttpStatus.OK).body("Login is blocked.");
    }

    private String jwtFilter(String token) {
        Base64.Decoder decoder = Base64.getDecoder();
        token.replace("Bearer", " ");
        String[] chunks = token.split("\\.");
        return new String(decoder.decode(chunks[1]));
    }

    private boolean isValidBearerToken(String accessToken) throws IOException {
        boolean isValid = false;
        var token = accessToken.replace("Bearer ", "");
        try {
            long jwtExpireAt = JWT.decode(token).getExpiresAt().getTime() / 1000;
            long difference = jwtExpireAt - (System.currentTimeMillis() / 1000);
            if (difference >= 30) {
                isValid = true;
            }
        } catch (JWTDecodeException exception) {
            throw new IOException(exception.getMessage());
        }
        return isValid;
    }

    private Optional<Login> list(Long id) {
        return loginRepository.findById(id);
    }

}
