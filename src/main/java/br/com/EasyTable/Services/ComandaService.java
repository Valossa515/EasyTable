package br.com.EasyTable.Services;

import br.com.EasyTable.Borders.Entities.Comanda;
import br.com.EasyTable.Repositories.ComandaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ComandaService {

    private final ComandaRepository comandaRepository;

    public ComandaService(ComandaRepository comandaRepository) {
        this.comandaRepository = comandaRepository;
    }

    public Comanda criarNovaComanda(String mesaId) {
        Comanda comanda = new Comanda();
        comanda.setCodigoQR(UUID.randomUUID().toString());
        comanda.setMesaId(mesaId);
        comanda.setAtiva(true);
        comanda.setDataCriacao(new Date());
        return comandaRepository.save(comanda);
    }

    public Comanda validarComanda(String comandaId) { // Renomeado o parâmetro para clareza
        Comanda comanda = comandaRepository.findById(comandaId) // Use findById para buscar pelo _id
                .orElseThrow(() -> new RuntimeException("Comanda não encontrada para validação: " + comandaId));

        if (!comanda.isAtiva()) {
            throw new RuntimeException("Comanda inativa ou já fechada.");
        }
        return comanda;
    }
}
