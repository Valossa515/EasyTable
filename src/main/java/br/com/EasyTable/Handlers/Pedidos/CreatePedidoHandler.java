package br.com.EasyTable.Handlers.Pedidos;

import br.com.EasyTable.Borders.Adapters.Interfaces.IPedidoAdapter;
import br.com.EasyTable.Borders.Dtos.Requests.CreatePedidoRequest;
import br.com.EasyTable.Borders.Dtos.Responses.CreatePedidoResponse;
import br.com.EasyTable.Borders.Entities.Pedido;
import br.com.EasyTable.Borders.Handlers.ICreatePedidoHandler;
import br.com.EasyTable.Repositories.PedidoRepository;
import br.com.EasyTable.Services.KafkaPedidoProducerService;
import br.com.EasyTable.Services.RedisService;
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
    private final KafkaPedidoProducerService kafkaService;
    private final RedisService redisService;

    public CreatePedidoHandler
            (Validator validator,
             PedidoRepository pedidoRepository,
             IPedidoAdapter pedidoAdapter,
             KafkaPedidoProducerService kafkaService,
             RedisService redisService) {
        super(logger, validator);
        this.pedidoRepository = pedidoRepository;
        this.pedidoAdapter = pedidoAdapter;
        this.kafkaService = kafkaService;
        this.redisService = redisService;
    }

    @Override
    protected CompletableFuture<HandlerResponseWithResult<CreatePedidoResponse>> doExecute(CreatePedidoRequest request) {
        return CompletableFuture.supplyAsync(() -> {

            try {
                Pedido pedido = pedidoAdapter.toPedido(request);
                Pedido pedidoSalvo = pedidoRepository.save(pedido);

                if (pedidoSalvo == null) {
                    return badRequest(MessageResources.get("error.create_item_error_code"),
                            MessageResources.get("error.create_item_error"));
                }

                redisService.salvarPedido("pedido:" + pedidoSalvo.getId(), pedidoSalvo, 60);
                kafkaService.enviarPedidoCriado(pedidoSalvo);

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
