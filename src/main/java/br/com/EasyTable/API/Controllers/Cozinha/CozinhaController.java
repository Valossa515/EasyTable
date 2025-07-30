package br.com.EasyTable.API.Controllers.Cozinha;

import br.com.EasyTable.Borders.Entities.Pedido;
import br.com.EasyTable.Repositories.PedidoRepository;
import br.com.EasyTable.Services.RedisService;
import br.com.EasyTable.Shared.Enums.PedidoStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cozinha", description = "Operações da cozinha sobre os pedidos.")
public class CozinhaController {
    private final PedidoRepository pedidoRepository;
    private final RedisService redisService;

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatusPedido(
            @PathVariable String id,
            @RequestParam PedidoStatus status) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(status);
        pedidoRepository.save(pedido);
        redisService.salvar("pedido:" + id, pedido, 60);
        return ResponseEntity.ok("Status do pedido atualizado para: " + status);
    }
}
