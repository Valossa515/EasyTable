package br.com.EasyTable.Repositories;

import br.com.EasyTable.Borders.Entities.Pedido;
import br.com.EasyTable.Shared.Enums.PedidoStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {
    List<Pedido> findByComandaId(String comandaId);

    // Opcional: adicione para buscar pedidos por comandaId e status
    List<Pedido> findByComandaIdAndStatus(String comandaId, PedidoStatus status);
}
