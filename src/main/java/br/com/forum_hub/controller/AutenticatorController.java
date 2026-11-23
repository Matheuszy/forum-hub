package br.com.forum_hub.controller;

import br.com.forum_hub.domain.auth.DadosRefresh;
import br.com.forum_hub.domain.auth.TokenDados;
import br.com.forum_hub.domain.auth.DadosLogin;
import br.com.forum_hub.domain.usuario.TokenService;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.domain.usuario.UsuarioRepository;
import br.com.forum_hub.domain.usuario.UsuarioService;
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
    private final UsuarioRepository usuarioRepository;


    public AutenticatorController(AuthenticationManager authenticationManager, TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
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

    @PostMapping("/refresh")
    public ResponseEntity<TokenDados> refresh(@Valid @RequestBody DadosRefresh dados) {
        var newToken = dados.refreshToken();
        Long userId = Long.valueOf(tokenService.verificarToken(newToken));
        var usuario = usuarioRepository.findById(userId).orElseThrow();

        String token = tokenService.generateToken(usuario);
        String refreshToken = tokenService.generateRefreshToken(usuario);
        return ResponseEntity.ok(new TokenDados(token, refreshToken));

    }

}
