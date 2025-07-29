package br.com.EasyTable.Borders.Entities;

import br.com.EasyTable.Shared.Enums.PedidoStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "pedidos")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Pedido extends DatabaseEntityBase {
    private String mesaId;
    @Builder.Default
    private List<ItemCardapio> itens = new ArrayList<>();
    private LocalDateTime dataHora;
    private PedidoStatus status;
}
