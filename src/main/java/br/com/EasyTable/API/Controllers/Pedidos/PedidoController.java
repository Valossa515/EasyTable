package br.com.EasyTable.API.Controllers.Pedidos;

import br.com.EasyTable.Borders.Dtos.Requests.CreatePedidoRequest;
import br.com.EasyTable.Borders.Handlers.ICreatePedidoHandler;
import br.com.EasyTable.Services.PedidoService;
import br.com.EasyTable.Shared.Models.ResponseEntityConverterImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/pedidos/v1")
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos.")
public class PedidoController {
    private final ICreatePedidoHandler createPedidoHandler;
    private final ResponseEntityConverterImpl responseEntityConverter;
    private final PedidoService pedidoService;

    public PedidoController
            (ICreatePedidoHandler createPedidoHandler,
             ResponseEntityConverterImpl responseEntityConverter,
             PedidoService pedidoService) {
        this.createPedidoHandler = createPedidoHandler;
        this.responseEntityConverter = responseEntityConverter;
        this.pedidoService = pedidoService;
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<?>> createPedido(
            @RequestBody CreatePedidoRequest request) {
        return createPedidoHandler.execute(request)
                .thenApplyAsync(response -> responseEntityConverter.convert(response, true));
    }

    @GetMapping("/{id}/status")
    public CompletableFuture<ResponseEntity<?>> getPedidoStatus(@PathVariable String id) {
        return pedidoService.getStatus(id)
                .thenApply(status -> ResponseEntity.ok().body(status));
    }

    @GetMapping("/{id}/conta")
    public CompletableFuture<ResponseEntity<?>> getValorConta(@PathVariable String id) {
        return pedidoService.calcularValorTotal(id)
                .thenApply(valor -> ResponseEntity.ok().body(valor));
    }

    // Buscar pedidos pela comanda (código QR)
    @GetMapping("/comanda/{codigoQR}/pedidos")
    public CompletableFuture<ResponseEntity<?>> getPedidosPorComanda(@PathVariable String codigoQR) {
        return pedidoService.getPedidosPorComanda(codigoQR)
                .thenApply(pedidos -> {
                    if (pedidos == null || pedidos.isEmpty()) {
                        return ResponseEntity.noContent().build();
                    }
                    return ResponseEntity.ok(pedidos);
                });
    }

    // Obter status agregado dos pedidos da comanda (quantos pendentes, finalizados, etc)
    @GetMapping("/comanda/{codigoQR}/status")
    public CompletableFuture<ResponseEntity<?>> getStatusPedidosPorComanda(@PathVariable String codigoQR) {
        return pedidoService.getStatusPedidosPorComanda(codigoQR)
                .thenApply(statusMap -> ResponseEntity.ok(statusMap));
    }

    // Obter total agregado dos pedidos da comanda
    @GetMapping("/comanda/{codigoQR}/total")
    public CompletableFuture<ResponseEntity<?>> getTotalPorComanda(@PathVariable String codigoQR) {
        return pedidoService.getTotalPorComanda(codigoQR)
                .thenApply(total -> ResponseEntity.ok(total));
    }

    // Exemplo de endpoint para fechar conta (pode ser POST, com a lógica para finalizar)
    @PostMapping("/comanda/{codigoQR}/fechar")
    public CompletableFuture<ResponseEntity<?>> fecharConta(@PathVariable String codigoQR) {

        return pedidoService.fecharContaPorComanda(codigoQR)
                .thenApply(resposta -> ResponseEntity.ok(resposta));
    }

    @GetMapping("/comanda/{codigoQR}")
    public CompletableFuture<ResponseEntity<?>> getComandaInfo(@PathVariable String codigoQR) {
        return pedidoService.getComandaInfo(codigoQR)
                .thenApply(comandaInfo -> ResponseEntity.ok(comandaInfo));
    }
}
