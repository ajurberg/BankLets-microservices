package br.com.letscode.securitylogin.login;

import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RequestMapping("/bank/login")
@RestController
class LoginController {

    private final LoginService loginService;

    @PostMapping("/create")
    public ResponseEntity<?> createLogin(@RequestBody Login login) {
        return loginService.createLogin(login);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePassword(@RequestBody String password,
                                            @RequestHeader(value = "Authorization", required = true) String token) throws JSONException, IOException {
        return loginService.changePassword(password, token);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeLogin(@RequestHeader(value = "Authorization", required = true) String token) throws JSONException, IOException {
        return loginService.remove(token);
    }

    @GetMapping
    public String login() {
        return "login";
    }

}
