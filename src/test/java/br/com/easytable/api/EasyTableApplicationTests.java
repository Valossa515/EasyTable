package br.com.easytable.api;

import br.com.EasyTable.Repositories.ComandaRepository;
import br.com.EasyTable.Repositories.ItemCardapioRepository;
import br.com.EasyTable.Repositories.MesaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import br.com.EasyTable.Repositories.PedidoRepository;

@SpringBootTest
@ActiveProfiles("test")
public class EasyTableApplicationTests {

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private ItemCardapioRepository itemCardapioRepository;

    @MockBean
    private MesaRepository mesaRepository;

    @MockBean
    private ComandaRepository comandaRepository;

    @Test
    void contextLoads() {
        // Este teste verifica se o contexto da aplicação Spring Boot é carregado corretamente.
        // Se houver algum problema com a configuração do contexto, este teste falhará.
        System.out.println("Contexto da aplicação carregado com sucesso.");
    }
}
