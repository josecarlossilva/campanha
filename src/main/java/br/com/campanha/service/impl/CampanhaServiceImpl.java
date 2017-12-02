package br.com.campanha.service.impl;

import br.com.campanha.domain.Campanha;
import br.com.campanha.repository.CampanhaRepository;
import br.com.campanha.service.CampanhaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class CampanhaServiceImpl implements CampanhaService {

    private static final Logger logger = LoggerFactory.getLogger(CampanhaServiceImpl.class);

    private static final int NUMERO_DIAS = 1;

    @Autowired
    private CampanhaRepository campanhaRepository;

    @Override
    public List<Campanha> buscarTodasAsCampanhasAtivas(LocalDate hoje) {
        return campanhaRepository.buscarTodasAsCampanhasAtivas(hoje);
    }

    @Override
    public List<Campanha> buscarCampanhasAtivasPorPeriodo(LocalDate inicioVigencia, LocalDate fimVigencia) {
        return campanhaRepository.buscarCampanhasAtivasPorPeriodo(inicioVigencia, fimVigencia);
    }

    @Override
    public Campanha cadastrarCampanha(String nomeDaCampanha, String idDoTimeCoracao, LocalDate inicioVigencia, LocalDate fimVigencia) {
        if(logger.isDebugEnabled()) {
            logger.debug("Cadastrando Campanha com nome {} - Data de Inicio Vigência: {}  - Data de Fim Vigência : {} ",
                    nomeDaCampanha, inicioVigencia, fimVigencia);
        }

        List<Campanha> campanhas = campanhaRepository.buscarCampanhasAtivasPorPeriodo(inicioVigencia, fimVigencia);
        Collections.sort(campanhas, Comparator.comparing(Campanha::getFimVigencia));

        campanhas.forEach(campanhaCadastrada -> {
            campanhaCadastrada.setFimVigencia(campanhaCadastrada.getFimVigencia().plusDays(NUMERO_DIAS));
            adicionaDiaAoFimVigenciaRecursivo(campanhaCadastrada, campanhas);
        } );

        campanhaRepository.save(campanhas);
        return campanhaRepository.save(new Campanha(nomeDaCampanha, idDoTimeCoracao, inicioVigencia, fimVigencia));

    }

    @Override
    public Optional<Campanha> buscarPorId(String id) {
        return Optional.ofNullable(campanhaRepository.findOne(id));
    }

    @Override
    public void deletarPorId(String id) {
        campanhaRepository.delete(id);
    }

    @Override
    public void salvarCampanha(Campanha campanha) {
        campanhaRepository.save(campanha);
    }

    @Override
    public List<Campanha> buscaPorTimeDoCoracao(String timeDoCoracao) {
        return campanhaRepository.
                findByTimeCoracaoIdIgnoreCaseAndInicioVigenciaIsLessThanEqualAndFimVigenciaIsGreaterThanEqual(timeDoCoracao, LocalDate.now(), LocalDate.now());
    }

    private void adicionaDiaAoFimVigenciaRecursivo(Campanha campanha, List<Campanha> campanhasCadastradas){
        if(campanhasCadastradas.stream()
                .filter(campanhaCadastrada -> !campanhaCadastrada.equals(campanha))
                .anyMatch(campanhaCadastrada -> campanhaCadastrada.getFimVigencia().isEqual(campanha.getFimVigencia()))){

            if(logger.isDebugEnabled()) {
                logger.debug("Adcionando fim de vigência na Campanha: {}", campanha);
            }
            campanha.setFimVigencia(campanha.getFimVigencia().plusDays(NUMERO_DIAS));
            adicionaDiaAoFimVigenciaRecursivo(campanha, campanhasCadastradas);
        }
    }
}
