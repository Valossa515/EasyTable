package br.com.EasyTable.Borders.Adapters.Interfaces;

import br.com.EasyTable.Borders.Dtos.Requests.CreatePedidoRequest;
import br.com.EasyTable.Borders.Entities.Pedido;

public interface IPedidoAdapter {
    Pedido toPedido(CreatePedidoRequest request);
}
