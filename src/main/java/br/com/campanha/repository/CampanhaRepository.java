package br.com.campanha.repository;

import br.com.campanha.domain.Campanha;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CampanhaRepository extends MongoRepository<Campanha, String> {

    @Query("{ 'inicioVigencia' : { $lte: ?0 }, 'fimVigencia' : { $gte: ?0 } }, $orderby: { fimVigencia : 1 } ")
    List<Campanha> buscarTodasAsCampanhasAtivas(LocalDate hoje);

    @Query("{ 'inicioVigencia' : { $gte: ?0, $lte: ?1}, 'fimVigencia' : { $gte: ?0, $lte: ?1 } } , $orderby: { fimVigencia : 1 }")
    List<Campanha> buscarCampanhasAtivasPorPeriodo(LocalDate inicioVigencia, LocalDate fimVigencia);

    List<Campanha> findByTimeCoracaoIdIgnoreCaseAndInicioVigenciaIsLessThanEqualAndFimVigenciaIsGreaterThanEqual
            (String timeCoracaoId, LocalDate inicioVigencia, LocalDate fimVigencia);

}
