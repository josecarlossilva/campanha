package br.com.campanha.mock;

import br.com.campanha.domain.Campanha;
import br.com.campanha.domain.CampanhaResource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampanhaMock {

    public static List<Campanha> getCampanhas(){
        List<Campanha> campanhas = new ArrayList();
        campanhas.add(new Campanha("Campanha 1",  "TIME-1001",
                LocalDate.of(2017,10,01),LocalDate.of(2017,10,03)));

        campanhas.add(new Campanha("Campanha 2",  "TIME-1002",
                LocalDate.of(2017,10,01),LocalDate.of(2017,10,02)));
        return campanhas;
    }

    public static List<Campanha> getCampanhasAtivas(){
        List<Campanha> campanhas = new ArrayList();
        campanhas.add(new Campanha("Campanha 10",  "TIME-1001",
                LocalDate.now(), LocalDate.now().plusDays(3)));

        campanhas.add(new Campanha("Campanha 20",  "TIME-1001",
                LocalDate.now(), LocalDate.now().plusDays(5)));
        return campanhas;
    }

    public static CampanhaResource criarCampanhaVigencia10Resource(){
        return new CampanhaResource("Campanha 4",  "TIME-1004",
                LocalDate.of(2017,10,10),LocalDate.of(2017,10,20));

    }

    public static CampanhaResource criarCampanhaComFalhasResource(){
        return new CampanhaResource(null,  "TIME-1004",
                LocalDate.of(2017,10,10),LocalDate.of(2017,10,20));

    }
}
