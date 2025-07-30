package br.com.EasyTable.Repositories;

import br.com.EasyTable.Borders.Entities.Comanda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComandaRepository extends MongoRepository<Comanda, String>{
    Optional<Comanda> findByCodigoQR(String codigoQR);
    boolean existsByMesaId(String mesaId);
    Optional<Comanda> findByMesaId(String mesaId);
}
