package br.com.EasyTable.Borders.Entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "pedidos")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido extends DatabaseEntityBase {
    private UUID mesaId;
    private List<ItemCardapio> itens;
    private LocalDateTime dataHora;
    private String status;
}
