package dev.Pedro.controle_gastos.domain.repository;

import dev.Pedro.controle_gastos.domain.model.Registro;
import dev.Pedro.controle_gastos.enums.Categorias;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    List<Registro> findByDataBetween(LocalDate inicio, LocalDate fim);

    List<Registro> findByTipoRegistro(TipoRegistro tipo);

    List<Registro> findByCategoria(Categorias categoria);

}
