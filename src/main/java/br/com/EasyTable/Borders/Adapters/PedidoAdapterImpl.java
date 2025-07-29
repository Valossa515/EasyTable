package br.com.EasyTable.Borders.Adapters;

import br.com.EasyTable.Borders.Adapters.Interfaces.IPedidoAdapter;
import br.com.EasyTable.Borders.Dtos.Requests.CreatePedidoRequest;
import br.com.EasyTable.Borders.Entities.Pedido;
import br.com.EasyTable.Shared.Enums.PedidoStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PedidoAdapterImpl implements IPedidoAdapter {

    @Override
    public Pedido toPedido(CreatePedidoRequest request) {
        return Pedido.builder()
                .mesaId(request.mesaId())
                .itens(request.itens())
                .dataHora(LocalDateTime.now())
                .status(PedidoStatus.PENDENTE)
                .build();
    }
}
