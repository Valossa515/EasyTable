package br.com.EasyTable.Borders.Dtos.Requests;

import br.com.EasyTable.Shared.Enums.PedidoStatus;

public record UpdateStatusPedidoRequest(String pedidoId, PedidoStatus status) {
}
