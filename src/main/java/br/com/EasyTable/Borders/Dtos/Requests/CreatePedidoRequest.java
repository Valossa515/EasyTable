package br.com.EasyTable.Borders.Dtos.Requests;

import br.com.EasyTable.Borders.Entities.ItemCardapio;
import br.com.EasyTable.Shared.Enums.PedidoStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreatePedidoRequest(
        String mesaId,
        List<ItemCardapio> itens,
        LocalDateTime dataHora,
        PedidoStatus status
) { }
