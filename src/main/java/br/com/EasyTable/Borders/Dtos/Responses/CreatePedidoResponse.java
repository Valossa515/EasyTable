package br.com.EasyTable.Borders.Dtos.Responses;

import br.com.EasyTable.Borders.Entities.ItemCardapio;
import br.com.EasyTable.Shared.Enums.PedidoStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CreatePedidoResponse(
        String id,
        String mesaId,
        List<ItemCardapio> itens,
        LocalDateTime dataHora,
        PedidoStatus status
) { }
