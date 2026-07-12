package br.com.pitstop.spring_boot_clean_car.service;

import br.com.pitstop.spring_boot_clean_car.dto.request.ClienteRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.ClienteResponse;
import br.com.pitstop.spring_boot_clean_car.entity.Cliente;
import br.com.pitstop.spring_boot_clean_car.exception.ResourceNotFoundException;
import br.com.pitstop.spring_boot_clean_car.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ClienteResponse buscarPorId(UUID id) {
        return toResponse(buscarEntidadePorId(id));
    }

    public ClienteResponse criar(ClienteRequest request) {
        Cliente cliente = Cliente.builder()
                .nome(request.nome())
                .telefone(request.telefone())
                .endereco(request.endereco())
                .build();

        return toResponse(clienteRepository.save(cliente));
    }

    public ClienteResponse atualizar(UUID id, ClienteRequest request) {
        Cliente cliente = buscarEntidadePorId(id);

        cliente.setNome(request.nome());
        cliente.setTelefone(request.telefone());
        cliente.setEndereco(request.endereco());

        return toResponse(clienteRepository.save(cliente));
    }

    public void deletar(UUID id) {
        Cliente cliente = buscarEntidadePorId(id);
        clienteRepository.delete(cliente);
    }

    public Cliente buscarEntidadePorId(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente nao encontrado"));
    }

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getTelefone(), cliente.getEndereco());
    }
}
