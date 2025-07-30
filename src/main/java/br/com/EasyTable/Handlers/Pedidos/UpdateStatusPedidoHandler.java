package br.com.EasyTable.Handlers.Pedidos;

import br.com.EasyTable.Borders.Adapters.PedidoAdapterImpl;
import br.com.EasyTable.Borders.Dtos.Requests.UpdateStatusPedidoRequest;
import br.com.EasyTable.Borders.Dtos.Responses.UpdateStatusPedidoResponse;
import br.com.EasyTable.Borders.Entities.Pedido;
import br.com.EasyTable.Borders.Handlers.IUpdateStatusPedidoHandler;
import br.com.EasyTable.Repositories.PedidoRepository;
import br.com.EasyTable.Services.RedisService;
import br.com.EasyTable.Shared.Enums.PedidoStatus;
import br.com.EasyTable.Shared.Handlers.HandlerBase;
import br.com.EasyTable.Shared.Handlers.HandlerResponseWithResult;
import br.com.EasyTable.Shared.Properties.MessageResources;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UpdateStatusPedidoHandler
        extends HandlerBase<UpdateStatusPedidoRequest, UpdateStatusPedidoResponse> implements IUpdateStatusPedidoHandler {

    private static final Logger logger = LoggerFactory.getLogger(UpdateStatusPedidoHandler.class);

    private final PedidoRepository pedidoRepository;
    private final RedisService redisService;
    private final PedidoAdapterImpl pedidoAdapter;

    public UpdateStatusPedidoHandler
            (Validator validator,
             PedidoRepository pedidoRepository,
             RedisService redisService,
             PedidoAdapterImpl pedidoAdapter) {
        super(logger, validator);
        this.pedidoRepository = pedidoRepository;
        this.redisService = redisService;
        this.pedidoAdapter = pedidoAdapter;
    }

    @Override
    protected CompletableFuture<HandlerResponseWithResult<UpdateStatusPedidoResponse>>
        doExecute(UpdateStatusPedidoRequest request) {

        try{
            var pedidoOptional = pedidoRepository.findById(request.pedidoId());

            if (pedidoOptional.isEmpty()) {
                logger.warn("Pedido with ID {} not found", request.pedidoId());
                return CompletableFuture.completedFuture(
                  notFound(MessageResources.get("error.pedido.not_found")));
            }

            Pedido pedido = pedidoOptional.get();

            pedidoAdapter.updatePedido(pedido, request);

            pedidoRepository.save(pedido);

            redisService.salvar("pedido:" + pedido.getId(), pedido, 60);

            logger.info("Status do pedido atualizado para {}", pedido.getStatus());

            return CompletableFuture.completedFuture(
                success(new UpdateStatusPedidoResponse(
                    MessageResources.get("success.pedido.status_updated"), pedido.getStatus().name())));
        }
        catch (Exception e) {
            logger.error("Erro ao atualizar status do pedido: {}", e.getMessage(), e);
            return CompletableFuture.completedFuture(
                badRequest(MessageResources.get("error.pedido.status_update_failed"), e.getMessage()));
        }
    }
}
