package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.api.dto.UsuarioRequest;
import dev.Pedro.controle_gastos.api.dto.UsuarioResponse;
import dev.Pedro.controle_gastos.domain.entity.Usuario;
import dev.Pedro.controle_gastos.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UsuarioResponse create(UsuarioRequest usuarioRequest) {
        if(usuarioRepository.existsByEmail(usuarioRequest.email())){
            throw new RuntimeException("Email ja existente");
        }

        Usuario usuario = toEntity(usuarioRequest);

        return toResponse(usuarioRepository.save(usuario));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }


    private Usuario toEntity(UsuarioRequest usuarioRequest){

        return new Usuario(
                usuarioRequest.nome(),
                usuarioRequest.email(),
                bCryptPasswordEncoder.encode(usuarioRequest.senha())

        );

    }


    private UsuarioResponse toResponse(Usuario usuario){

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
