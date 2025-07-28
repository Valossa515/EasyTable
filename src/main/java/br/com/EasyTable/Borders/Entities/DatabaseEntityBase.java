package br.com.EasyTable.Borders.Entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public abstract class DatabaseEntityBase {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
}
