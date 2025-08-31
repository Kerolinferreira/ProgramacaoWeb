package com.programacao.web.fatec.api_fatec.domain.cliente;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.programacao.web.fatec.api_fatec.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
