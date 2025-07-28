package br.com.EasyTable.Borders.Entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
@EqualsAndHashCode(callSuper = true)
@Document(collection = "cardapio")
@Data
@SuperBuilder
public class ItemCardapio extends DatabaseEntityBase {
    private String nome;
    private String descricao;
    private Double preco;
    private String imagemUrl;
}