package com.programacao.web.fatec.api_fatec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.programacao.web.fatec.api_fatec.domain.cliente.ClienteRepository;
import com.programacao.web.fatec.api_fatec.entities.Cliente;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
// Eu removi as listas em memória, mantendo apenas banco de dados 
    @Autowired
    private ClienteRepository clienteRepository;

    // dados iniciais 
    @PostConstruct
    public void seed() {
        if (clienteRepository.count() == 0) {
            clienteRepository.save(new Cliente(null, "Danilo", "Rua XXX"));
            clienteRepository.save(new Cliente(null, "João", "Rua YYY"));
            clienteRepository.save(new Cliente(null, "Maria", "Rua ZZZ"));
        }
    }

    // GET /api/clientes  -> lista todos
    @GetMapping
    public List<Cliente> listar(@RequestParam(name = "nome", required = false) String nome) {
        if (nome == null || nome.isBlank()) {
            return clienteRepository.findAll();
        }
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    // GET /api/clientes/{id} -> busca por id
    // Retorna o objeto quando encontra, ou a String "NÃO ENCONTRADO" quando não encontra
    @GetMapping("/{id}")
    public Object buscarPorId(@PathVariable Long id) {
        return clienteRepository.findById(id).<Object>map(c -> c)
                .orElse("NÃO ENCONTRADO");
    }

    // POST /api/clientes -> cria e retorna o registro salvo (id gerado pelo banco)
    @PostMapping
    public Cliente criar(@RequestBody Cliente cliente) {
        cliente.setId(null); // garante geração automática do id
        return clienteRepository.save(cliente);
    }

    // PUT /api/clientes/{id} -> atualiza nome e endereço
    // Retorna "OK" ou "NÃO ENCONTRADO"
    @PutMapping("/{id}")
    public String atualizar(@PathVariable Long id, @RequestBody Cliente dados) {
        return clienteRepository.findById(id)
                .map(existente -> {
                    existente.setNome(dados.getNome());
                    existente.setEndereco(dados.getEndereco());
                    clienteRepository.save(existente);
                    return "OK";
                })
                .orElse("NÃO ENCONTRADO");
    }

    // DELETE /api/clientes/{id} -> remove pelo id
    // Retorna "OK" ou "NÃO ENCONTRADO"
    @DeleteMapping("/{id}")
    public String excluir(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) return "NÃO ENCONTRADO";
        clienteRepository.deleteById(id);
        return "OK";
    }
}
