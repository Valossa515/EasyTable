package br.com.EasyTable.Borders.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
public abstract class DatabaseEntityBase {
    @Id
    private UUID id = UUID.randomUUID();
}
