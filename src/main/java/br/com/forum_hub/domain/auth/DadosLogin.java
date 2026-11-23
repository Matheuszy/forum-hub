package br.com.forum_hub.domain.auth;

import jakarta.validation.constraints.NotBlank;

public record DadosLogin(
        @NotBlank
        String email,
        @NotBlank
        String senha
) {
}
