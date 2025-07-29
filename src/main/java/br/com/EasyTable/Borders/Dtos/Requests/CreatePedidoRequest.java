package br.com.EasyTable.Borders.Dtos.Requests;

import br.com.EasyTable.Shared.Enums.PedidoStatus;
import java.util.Date;
import java.util.List;

public record CreatePedidoRequest(
        String mesaId,
        List<String> itensIds,  // Apenas os IDs dos itens
        Date dataHora,
        PedidoStatus status
) { }
