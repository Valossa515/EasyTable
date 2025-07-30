package br.com.EasyTable.Borders.Dtos.Responses;

import br.com.EasyTable.Borders.Entities.ItemCardapio;
import br.com.EasyTable.Shared.Enums.PedidoStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record CreatePedidoResponse(
        String id,
        String mesaId,
        String comandaId,
        List<ItemCardapio> itens,
        Date dataHora,
        PedidoStatus status,
        String qrCodeAcompanhamentoUrl,
        String qrCodeContaUrl
) { }
