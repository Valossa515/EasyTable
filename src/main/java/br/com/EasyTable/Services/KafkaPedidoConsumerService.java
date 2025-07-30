package br.com.EasyTable.Services;

import br.com.EasyTable.Borders.Entities.Pedido;
import br.com.EasyTable.Repositories.PedidoRepository;
import br.com.EasyTable.Shared.Enums.PedidoStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class KafkaPedidoConsumerService {

    private final ObjectMapper objectMapper;
    private final PedidoRepository pedidoRepository;
    private final RedisService redisService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaPedidoConsumerService.class);

    @KafkaListener(topics = "${app.kafka.topic.pedido-criado}", groupId = "cozinha")
    public void consumirPedido(String mensagem) {
        try{
            Pedido pedido = objectMapper.readValue(mensagem, Pedido.class);
            logger.info("Pedido recebido na cozinha: {}", pedido.getId());

            pedido.setStatus(PedidoStatus.EM_PREPARACAO);
            pedidoRepository.save(pedido);

            redisService.salvar("pedido:" + pedido.getId(), pedido, 60);
        }
        catch (Exception e) {
            logger.error("Erro ao processar pedido do Kafka", e);
            throw new RuntimeException("Erro ao processar pedido", e);
        }
    }

}
