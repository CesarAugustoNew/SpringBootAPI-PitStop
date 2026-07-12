package br.com.pitstop.spring_boot_clean_car.service;

import br.com.pitstop.spring_boot_clean_car.dto.request.VeiculoRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.VeiculoResponse;
import br.com.pitstop.spring_boot_clean_car.entity.Cliente;
import br.com.pitstop.spring_boot_clean_car.entity.Veiculo;
import br.com.pitstop.spring_boot_clean_car.exception.ResourceNotFoundException;
import br.com.pitstop.spring_boot_clean_car.repository.IVeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/*
  Cadastro de veiculos do lava-rapido (o pedido original do projeto).
  Cada veiculo pertence a um cliente ja cadastrado.
*/
@Service
public class VeiculoService {

    @Autowired
    private IVeiculoRepository veiculoRepository;

    @Autowired
    private ClienteService clienteService;

    public List<VeiculoResponse> listar() {
        return veiculoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<VeiculoResponse> listarPorCliente(UUID clienteId) {
        return veiculoRepository.findByClienteId(clienteId).stream().map(this::toResponse).toList();
    }

    public VeiculoResponse buscarPorId(UUID id) {
        return toResponse(buscarEntidadePorId(id));
    }

    public VeiculoResponse criar(VeiculoRequest request) {
        if (veiculoRepository.existsByPlaca(request.placa())) {
            throw new IllegalStateException("Ja existe um veiculo cadastrado com essa placa");
        }

        Cliente cliente = clienteService.buscarEntidadePorId(request.clienteId());

        Veiculo veiculo = Veiculo.builder()
                .placa(request.placa().toUpperCase())
                .modelo(request.modelo())
                .marca(request.marca())
                .cliente(cliente)
                .build();

        return toResponse(veiculoRepository.save(veiculo));
    }

    public VeiculoResponse atualizar(UUID id, VeiculoRequest request) {
        Veiculo veiculo = buscarEntidadePorId(id);

        if (!veiculo.getPlaca().equalsIgnoreCase(request.placa())
                && veiculoRepository.existsByPlaca(request.placa())) {
            throw new IllegalStateException("Ja existe um veiculo cadastrado com essa placa");
        }

        Cliente cliente = clienteService.buscarEntidadePorId(request.clienteId());

        veiculo.setPlaca(request.placa().toUpperCase());
        veiculo.setModelo(request.modelo());
        veiculo.setMarca(request.marca());
        veiculo.setCliente(cliente);

        return toResponse(veiculoRepository.save(veiculo));
    }

    public void deletar(UUID id) {
        Veiculo veiculo = buscarEntidadePorId(id);
        veiculoRepository.delete(veiculo);
    }

    public Veiculo buscarEntidadePorId(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veiculo nao encontrado"));
    }

    public VeiculoResponse toResponse(Veiculo veiculo) {
        return new VeiculoResponse(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getMarca(),
                clienteService.toResponse(veiculo.getCliente())
        );
    }
}
