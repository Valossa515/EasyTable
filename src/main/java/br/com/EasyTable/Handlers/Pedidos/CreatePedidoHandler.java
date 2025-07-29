package br.com.EasyTable.Handlers.Pedidos;

import br.com.EasyTable.Borders.Adapters.Interfaces.IPedidoAdapter;
import br.com.EasyTable.Borders.Dtos.Requests.CreatePedidoRequest;
import br.com.EasyTable.Borders.Dtos.Responses.CreatePedidoResponse;
import br.com.EasyTable.Borders.Entities.Pedido;
import br.com.EasyTable.Borders.Handlers.ICreatePedidoHandler;
import br.com.EasyTable.Repositories.PedidoRepository;
import br.com.EasyTable.Shared.Handlers.HandlerBase;
import br.com.EasyTable.Shared.Handlers.HandlerResponseWithResult;
import br.com.EasyTable.Shared.Properties.MessageResources;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CreatePedidoHandler extends HandlerBase<CreatePedidoRequest, CreatePedidoResponse>
    implements ICreatePedidoHandler {

    private static final Logger logger = LoggerFactory.getLogger(CreatePedidoHandler.class);
    private final PedidoRepository pedidoRepository;
    private final IPedidoAdapter pedidoAdapter;

    public CreatePedidoHandler(Validator validator, PedidoRepository pedidoRepository, IPedidoAdapter pedidoAdapter) {
        super(logger, validator);
        this.pedidoRepository = pedidoRepository;
        this.pedidoAdapter = pedidoAdapter;
    }

    @Override
    protected CompletableFuture<HandlerResponseWithResult<CreatePedidoResponse>> doExecute(CreatePedidoRequest request) {
        return CompletableFuture.supplyAsync(() -> {

        var novoPedido = new CreatePedidoRequest(
                request.mesaId(),
                request.itens(),
                request.dataHora(),
                request.status()
        );

            try {
                Pedido pedido = pedidoAdapter.toPedido(novoPedido);
                Pedido pedidoSalvo = pedidoRepository.save(pedido);

                if (pedidoSalvo == null) {
                    return badRequest(MessageResources.get("error.create_item_error_code"),
                            MessageResources.get("error.create_item_error"));
                }

                // TODO: salvar no Redis (você pode criar uma camada RedisService para isso)
                // redisService.salvarPedido(pedidoSalvo);

                // TODO: enviar para Kafka futuramente se necessário
                // kafkaService.enviarPedidoCriado(pedidoSalvo);

                var response = new CreatePedidoResponse(
                        pedidoSalvo.getId(),
                        pedidoSalvo.getMesaId(),
                        pedidoSalvo.getItens(),
                        pedidoSalvo.getDataHora(),
                        pedidoSalvo.getStatus()
                );

                logger.info("Pedido feito com sucesso: {}", pedido);

                return created(response);
            } catch (Exception ex) {
                logger.error("Erro ao criar o pedido", ex);
                return badRequest(MessageResources.get("error.create_item_error_code"),
                        MessageResources.get("error.create_item_error"));
            }
        });
    }
}
