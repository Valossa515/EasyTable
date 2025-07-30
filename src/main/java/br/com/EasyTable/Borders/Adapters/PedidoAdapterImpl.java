package br.com.EasyTable.Borders.Adapters;

import br.com.EasyTable.Borders.Adapters.Interfaces.IPedidoAdapter;
import br.com.EasyTable.Borders.Dtos.Requests.CreatePedidoRequest;
import br.com.EasyTable.Borders.Entities.ItemCardapio;
import br.com.EasyTable.Borders.Entities.Pedido;
import br.com.EasyTable.Repositories.ItemCardapioRepository;
import br.com.EasyTable.Shared.Enums.PedidoStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
public class PedidoAdapterImpl implements IPedidoAdapter {

    private final ItemCardapioRepository itemCardapioRepository;

    public PedidoAdapterImpl(ItemCardapioRepository itemCardapioRepository) {
        this.itemCardapioRepository = itemCardapioRepository;
    }

    @Override
    public Pedido toPedido(CreatePedidoRequest request) {
        List<ItemCardapio> itens = itemCardapioRepository.findAllById(request.itensIds());
        if(itens.size() != request.itensIds().size()) {
            throw new IllegalArgumentException("Alguns itens não foram encontrados no cardápio");
        }

        return Pedido.builder()
                .mesaId(request.mesaId())
                .comandaId(request.comandaId())
                .itens(itens)
                .dataHora(new Date())
                .status(PedidoStatus.PENDENTE)
                .build();
    }
}
