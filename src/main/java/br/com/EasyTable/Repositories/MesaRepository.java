package br.com.EasyTable.Repositories;

import br.com.EasyTable.Borders.Entities.Mesa;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MesaRepository extends MongoRepository<Mesa, String> {

}
