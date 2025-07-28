package br.com.EasyTable.Borders.Entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
@EqualsAndHashCode(callSuper = true)
@Document(collection = "cardapio")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCardapio extends DatabaseEntityBase {
    private String nome;
    private String descricao;
    private Double preco;
    private String imagemUrl;
}