package dev.Pedro.controle_gastos.domain.repository;

import dev.Pedro.controle_gastos.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.hibernate.boot.model.NamedEntityGraphDefinition.Source.JPA;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByEmail(String email);

    //Saber se o email esta em uso
    boolean existsByEmail(String email);
}
