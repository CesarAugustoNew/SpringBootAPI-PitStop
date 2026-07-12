package br.com.pitstop.spring_boot_clean_car.service;

import br.com.pitstop.spring_boot_clean_car.dto.request.OrdemServicoRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.OrdemServicoResponse;
import br.com.pitstop.spring_boot_clean_car.entity.Cliente;
import br.com.pitstop.spring_boot_clean_car.entity.OrdemServico;
import br.com.pitstop.spring_boot_clean_car.entity.Usuario;
import br.com.pitstop.spring_boot_clean_car.entity.Veiculo;
import br.com.pitstop.spring_boot_clean_car.enums.StatusOrdem;
import br.com.pitstop.spring_boot_clean_car.exception.ResourceNotFoundException;
import br.com.pitstop.spring_boot_clean_car.repository.IOrdemServicoRepository;
import br.com.pitstop.spring_boot_clean_car.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/*
  Ordens de servico: o dia a dia do lava-rapido. Cada ordem registra
  qual funcionario atendeu (extraido do usuario logado, nao enviado
  pelo cliente da API, para manter a integridade do registro) e
  segue o fluxo de status RECEBIDO -> EM_LAVAGEM -> FINALIZADO -> ENTREGUE.
*/
@Service
public class OrdemServicoService {

    @Autowired
    private IOrdemServicoRepository ordemServicoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VeiculoService veiculoService;

    public List<OrdemServicoResponse> listar() {
        return ordemServicoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<OrdemServicoResponse> listarPorStatus(StatusOrdem status) {
        return ordemServicoRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public OrdemServicoResponse buscarPorId(UUID id) {
        return toResponse(buscarEntidadePorId(id));
    }

    public OrdemServicoResponse criar(OrdemServicoRequest request, String emailFuncionarioLogado) {
        Cliente cliente = clienteService.buscarEntidadePorId(request.clienteId());
        Veiculo veiculo = veiculoService.buscarEntidadePorId(request.veiculoId());

        if (!veiculo.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Esse veiculo nao pertence ao cliente informado");
        }

        Usuario funcionario = usuarioRepository.findByEmail(emailFuncionarioLogado)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario logado nao encontrado"));

        OrdemServico ordem = OrdemServico.builder()
                .cliente(cliente)
                .veiculo(veiculo)
                .funcionario(funcionario)
                .tipoServico(request.tipoServico())
                .status(StatusOrdem.RECEBIDO)
                .objetosValor(request.objetosValor())
                .valor(request.valor())
                .dataEntrada(LocalDateTime.now())
                .build();

        return toResponse(ordemServicoRepository.save(ordem));
    }

    public OrdemServicoResponse atualizarStatus(UUID id, StatusOrdem novoStatus) {
        OrdemServico ordem = buscarEntidadePorId(id);
        ordem.setStatus(novoStatus);
        return toResponse(ordemServicoRepository.save(ordem));
    }

    public void deletar(UUID id) {
        OrdemServico ordem = buscarEntidadePorId(id);
        ordemServicoRepository.delete(ordem);
    }

    public OrdemServico buscarEntidadePorId(UUID id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de servico nao encontrada"));
    }

    public OrdemServicoResponse toResponse(OrdemServico ordem) {
        return new OrdemServicoResponse(
                ordem.getId(),
                clienteService.toResponse(ordem.getCliente()),
                veiculoService.toResponse(ordem.getVeiculo()),
                ordem.getFuncionario().getNome(),
                ordem.getTipoServico(),
                ordem.getStatus(),
                ordem.getObjetosValor(),
                ordem.getValor(),
                ordem.getDataEntrada()
        );
    }
}
