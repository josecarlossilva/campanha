package br.com.campanha;

import br.com.campanha.repository.CampanhaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationStarterTest {

    @Autowired
    private CampanhaRepository campanhaRepository;

    @Test
    public void quandoAplicacaoInicializarDadosDoArquivoJsonDevemSerCarregadosParaOMongoDB() {

        assertThat(campanhaRepository.findAll())
                .as("Quando a aplicação é inicializada os dados do arquivo data.json devem ser carregados para " +
                        "o MongoDB em memória")
                .isNotNull()
                .isNotEmpty()
                .as("Devem haver dois(2) registros no MongoDB")
                .hasSize(2)
                .as("Todos os campos devem ser preenchidos e os dados devem estar corretos")
                .extracting("nome", "timeCoracaoId", "inicioVigencia", "fimVigencia")
                .contains(tuple("CampanhaMock 1", "TIME-1001", LocalDate.of(2017,10,01), LocalDate.of(2017,10,03) ),
                        tuple("CampanhaMock 2", "TIME-1002", LocalDate.of(2017,10,01), LocalDate.of(2017,10,02)));
    }
}
