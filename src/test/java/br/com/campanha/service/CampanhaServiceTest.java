package br.com.campanha.service;

import br.com.campanha.domain.Campanha;
import br.com.campanha.mock.CampanhaMock;
import br.com.campanha.repository.CampanhaRepository;
import org.junit.Before;
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
public class CampanhaServiceTest {

    @Autowired
    private CampanhaService campanhaService;

    @Autowired
    private CampanhaRepository campanhaRepository;

    @Before
    public void setUp() throws Exception {
        campanhaRepository.deleteAll();
        campanhaRepository.save(CampanhaMock.getCampanhas());
    }

    /**
     * Testa o requisito 2 -
     * caso exista uma campanha ou N campanhas associadas naquele período, o sistema deverá somar um dia no término da vigência de cada campanha já existente.*
     * Caso a data final da vigência seja igual a outra campanha, deverá ser acrescido um dia a mais de forma que as campanhas não tenham a mesma data de término de vigência.
     * @throws Exception
     */
    @Test
    public void campanhaDeveSerCadastradasComDadosCorretos(){
        Campanha campanha = campanhaService.cadastrarCampanha("Campanha 3", "TIME-1003",
                LocalDate.of(2017, 10, 01), LocalDate.of(2017, 10, 03));

        assertThat(campanha)
                .as("A camapanha 3 deve ser cadastrada na base de dados e ter os dados corretos conforme os parametros")
                .isNotNull()
                .extracting("nome", "timeCoracaoId", "inicioVigencia", "fimVigencia")
                .contains("Campanha 3", "TIME-1003", LocalDate.of(2017,10,01), LocalDate.of(2017,10,03));


        assertThat(campanhaRepository.findAll())
                .as("As camapanhas 1 e 2 devem ter seus dados atualizados conforme regras de data de fim vigência")
                .extracting("nome", "timeCoracaoId", "inicioVigencia", "fimVigencia")
                .contains(tuple("Campanha 1", "TIME-1001", LocalDate.of(2017,10,01), LocalDate.of(2017,10,05)),
                        tuple("Campanha 2", "TIME-1002", LocalDate.of(2017,10,01), LocalDate.of(2017,10,04)),
                        tuple("Campanha 3", "TIME-1003", LocalDate.of(2017,10,01), LocalDate.of(2017,10,03)));

    }

    @Test
    public void buscarCampanhasAtivasPorPeriodo(){

        assertThat(campanhaService.buscarCampanhasAtivasPorPeriodo(
                LocalDate.of(2017, 10, 01), LocalDate.of(2017, 10, 02)))
                .as("Deve trazersomente uma Campanha (Campanha 2) dado que somente ela esta dentro da vigência deste período")
                .hasSize(1);

        assertThat(campanhaService.buscarCampanhasAtivasPorPeriodo(
                LocalDate.of(2017, 10, 01), LocalDate.of(2017, 10, 03)))
                .as("Deve trazer as duas Campanhas ativas dado que todas estão com a vigência dentro deste período")
                .hasSize(2);

        assertThat(campanhaService.buscarCampanhasAtivasPorPeriodo(
                LocalDate.of(2017, 10, 01), LocalDate.of(2017, 10, 04)))
                .as("Deve trazer as duas Campanhas ativas dado que todas estão com a vigência dentro deste período")
                .hasSize(2);
    }

}