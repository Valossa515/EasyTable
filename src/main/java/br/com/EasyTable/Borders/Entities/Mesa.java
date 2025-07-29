package br.com.EasyTable.Borders.Entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mesas")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Mesa extends DatabaseEntityBase {
    private int numero;
    private boolean ativa;
}
