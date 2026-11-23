package br.com.forum_hub.controller;

import br.com.forum_hub.domain.auth.DadosLogin;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutenticatorController {

    private final AuthenticationManager authenticationManager;

    public AutenticatorController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticate(@Valid @RequestBody DadosLogin dados) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                dados.email(),
                dados.senha()
        );
        var authentication = authenticationManager.authenticate(token);
        return ResponseEntity.ok(authentication);

    }
}
