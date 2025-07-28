package br.com.EasyTable.Borders.Entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "mesas")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mesa extends DatabaseEntityBase {
    private int numero;
    private boolean ativa;
}
