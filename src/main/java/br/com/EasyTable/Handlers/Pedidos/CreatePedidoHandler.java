package br.com.EasyTable.Handlers.Pedidos;

import br.com.EasyTable.Borders.Adapters.Interfaces.IPedidoAdapter;
import br.com.EasyTable.Borders.Dtos.Requests.CreatePedidoRequest;
import br.com.EasyTable.Borders.Dtos.Responses.CreatePedidoResponse;
import br.com.EasyTable.Borders.Entities.Comanda;
import br.com.EasyTable.Borders.Entities.ItemCardapio;
import br.com.EasyTable.Borders.Entities.Pedido;
import br.com.EasyTable.Borders.Handlers.ICreatePedidoHandler;
import br.com.EasyTable.Configs.QrCodeProperties;
import br.com.EasyTable.Repositories.ItemCardapioRepository;
import br.com.EasyTable.Repositories.PedidoRepository;
import br.com.EasyTable.Services.ComandaService;
import br.com.EasyTable.Services.KafkaPedidoProducerService;
import br.com.EasyTable.Services.QRCodeService;
import br.com.EasyTable.Services.RedisService;
import br.com.EasyTable.Shared.Enums.PedidoStatus;
import br.com.EasyTable.Shared.Handlers.HandlerBase;
import br.com.EasyTable.Shared.Handlers.HandlerResponseWithResult;
import br.com.EasyTable.Shared.Properties.MessageResources;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CreatePedidoHandler extends HandlerBase<CreatePedidoRequest, CreatePedidoResponse>
    implements ICreatePedidoHandler {

    private static final Logger logger = LoggerFactory.getLogger(CreatePedidoHandler.class);
    private final PedidoRepository pedidoRepository;
    private final IPedidoAdapter pedidoAdapter;
    private final KafkaPedidoProducerService kafkaService;
    private final RedisService redisService;
    private final QRCodeService qrCodeService;
    private final QrCodeProperties qrCodeProperties;
    private final ComandaService comandaService;
    private final ItemCardapioRepository itemCardapioRepository;



    public CreatePedidoHandler
            (Validator validator,
             PedidoRepository pedidoRepository,
             IPedidoAdapter pedidoAdapter,
             KafkaPedidoProducerService kafkaService,
             RedisService redisService,
             QRCodeService qrCodeService,
             QrCodeProperties qrCodeProperties,
             ComandaService comandaService,
             ItemCardapioRepository itemCardapioRepository) {
        super(logger, validator);
        this.pedidoRepository = pedidoRepository;
        this.pedidoAdapter = pedidoAdapter;
        this.kafkaService = kafkaService;
        this.redisService = redisService;
        this.qrCodeService = qrCodeService;
        this.qrCodeProperties = qrCodeProperties;
        this.comandaService = comandaService;
        this.itemCardapioRepository = itemCardapioRepository;
    }

    @Override
    protected CompletableFuture<HandlerResponseWithResult<CreatePedidoResponse>> doExecute(CreatePedidoRequest request) {
        return CompletableFuture.supplyAsync(() -> {

            try {
                Comanda comanda = comandaService.validarComanda(request.comandaId());

                Pedido pedido = pedidoAdapter.toPedido(request);
                pedido.setComandaId(comanda.getId());
                pedido.setDataHora(new Date());
                pedido.setStatus(PedidoStatus.PENDENTE);

                List<ItemCardapio> itensCompletos = itemCardapioRepository.findAllById(request.itensIds());
                if (itensCompletos.size() != request.itensIds().size()) {
                    // Tratar erro: alguns itens não foram encontrados
                    return badRequest(MessageResources.get("error.invalid_items_code"),
                            MessageResources.get("error.invalid_items_message"));
                }
                pedido.setItens(itensCompletos); // Define os itens completos no pedido

                Pedido pedidoSalvo = pedidoRepository.save(pedido);

                if (pedidoSalvo == null) {
                    return badRequest(MessageResources.get("error.create_item_error_code"),
                            MessageResources.get("error.create_item_error"));
                }

                redisService.salvar("pedido:" + pedidoSalvo.getId(), pedidoSalvo, 60);
                kafkaService.enviarPedidoCriado(pedidoSalvo);

                String qrCodeAcompanhamentoUrl = qrCodeProperties.getBaseUrl() +
                        qrCodeProperties.getStatusPath().replace("{id}", pedidoSalvo.getId());
                String qrCodeContaUrl = qrCodeProperties.getBaseUrl() +
                        qrCodeProperties.getContaPath().replace("{id}", pedidoSalvo.getId());

                // O campo 'codigoQR' da comanda já existe na entidade Comanda
                String comandaQrCodeData = comanda.getCodigoQR();

                var response = new CreatePedidoResponse(
                        pedidoSalvo.getId(),
                        pedidoSalvo.getMesaId(),
                        pedidoSalvo.getComandaId(),
                        pedidoSalvo.getItens(),
                        pedidoSalvo.getDataHora(),
                        pedidoSalvo.getStatus(),
                        qrCodeAcompanhamentoUrl,
                        qrCodeContaUrl
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
