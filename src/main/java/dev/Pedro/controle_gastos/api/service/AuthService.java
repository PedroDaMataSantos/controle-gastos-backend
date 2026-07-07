package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.api.dto.LoginRequest;
import dev.Pedro.controle_gastos.api.dto.LoginResponse;
import dev.Pedro.controle_gastos.config.JwtService;
import dev.Pedro.controle_gastos.domain.entity.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest loginRequest) {

        Usuario usuario = usuarioService.buscarPorEmail(loginRequest.email());

        boolean senhaCorreta = passwordEncoder.matches(loginRequest.senha(), usuario.getSenha());

        if (!senhaCorreta) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario.getEmail());

        return new LoginResponse(token);
    }
}