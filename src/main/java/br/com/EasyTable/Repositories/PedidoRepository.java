package br.com.EasyTable.Repositories;

import br.com.EasyTable.Borders.Entities.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {
}
