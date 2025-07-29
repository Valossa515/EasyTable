package br.com.EasyTable.Services;

import br.com.EasyTable.Borders.Entities.Pedido;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPedidoProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaPedidoProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${app.kafka.topic.pedido-criado}")
    private String topic;

    public void enviarPedidoCriado(Pedido pedido) {
        try {
            String pedidoJson = objectMapper.writeValueAsString(pedido);
            kafkaTemplate.send(topic, pedido.getId(), pedidoJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar pedido para Kafka", e);
        }
    }

}
