package br.com.EasyTable.Repositories.Database;

import br.com.EasyTable.Borders.Entities.Mesa;
import br.com.EasyTable.Repositories.MesaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PopularDatabase {
    final int numeroMesa = 20;
    @Bean
    CommandLineRunner pupularDatavase(MesaRepository mesaRepository){
        return args ->{
            if(mesaRepository.count() ==0){
                for(int i = 1; i <= numeroMesa; i++) {
                    Mesa mesa = Mesa.builder()
                            .numero(i)
                            .ativa(true)
                            .build();
                    mesaRepository.save(mesa);
                }
                System.out.println("Mesas populadas com sucesso");
            }
        };
    }
}
