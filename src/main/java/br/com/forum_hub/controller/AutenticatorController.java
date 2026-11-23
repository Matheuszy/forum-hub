package br.com.forum_hub.controller;

import br.com.forum_hub.domain.auth.TokenDados;
import br.com.forum_hub.domain.auth.DadosLogin;
import br.com.forum_hub.domain.usuario.TokenService;
import br.com.forum_hub.domain.usuario.Usuario;
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
    private final TokenService tokenService;


    public AutenticatorController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDados> autenticate(@Valid @RequestBody DadosLogin dados) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                dados.email(),
                dados.senha()
        );
        var authentication = authenticationManager.authenticate(authToken);
        String token = tokenService.generateToken((Usuario) authentication.getPrincipal());
        String refreshToken = tokenService.generateRefreshToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenDados(token, refreshToken));

    }
}
