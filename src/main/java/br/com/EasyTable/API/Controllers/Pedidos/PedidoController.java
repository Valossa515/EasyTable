package br.com.EasyTable.API.Controllers.Pedidos;

import br.com.EasyTable.Borders.Dtos.Requests.CreatePedidoRequest;
import br.com.EasyTable.Borders.Handlers.ICreatePedidoHandler;
import br.com.EasyTable.Shared.Models.ResponseEntityConverterImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/pedidos/v1")
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos.")
public class PedidoController {
    private final ICreatePedidoHandler createPedidoHandler;
    private final ResponseEntityConverterImpl responseEntityConverter;

    public PedidoController(ICreatePedidoHandler createPedidoHandler, ResponseEntityConverterImpl responseEntityConverter) {
        this.createPedidoHandler = createPedidoHandler;
        this.responseEntityConverter = responseEntityConverter;
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<?>> createPedido(
            @org.springframework.web.bind.annotation.RequestBody CreatePedidoRequest request) {
        return createPedidoHandler.execute(request)
                .thenApplyAsync(response -> responseEntityConverter.convert(response, true));
    }
}
