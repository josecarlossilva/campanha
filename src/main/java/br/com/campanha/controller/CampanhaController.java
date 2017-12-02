package br.com.campanha.controller;

import br.com.campanha.domain.Campanha;
import br.com.campanha.domain.CampanhaResource;
import br.com.campanha.error.ErrorInfo;
import br.com.campanha.exception.RecursoNaoEncontradoException;
import br.com.campanha.service.CampanhaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/v1/campanhas")
@Api(value = "Campanha", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
        tags = {"Endpoints da Campanha"}, description = "Lida com todas as requisições para o serviço de campanha",
        basePath = "/api/v1/campanhas")
public class CampanhaController {

    private static final Logger logger = LoggerFactory.getLogger(CampanhaController.class);

    @Autowired
    private CampanhaService campanhaService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Cria uma nova campanha com base nos parametros passados",
            notes = "Cria uma nova campanha e retorna o link do caminho no header",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<?> cadastrarCampanha(@Valid @RequestBody CampanhaResource campanhaResource){

        Campanha campanha = campanhaService.cadastrarCampanha(campanhaResource.getNome(), campanhaResource.getTimeCoracaoId(),
                campanhaResource.getInicioVigencia(), campanhaResource.getFimVigencia());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(campanha.getId()).toUri();

        if(logger.isDebugEnabled()) {
            logger.debug("Campanha : {} criada com sucesso", campanha);
            logger.debug("O link gerado foi : {}", location);
        }

        return created(location).build();
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Busca todos as campanhas ativas",
            notes = "Para retornar as Campanhas ativas é usado a data atual",
            response = CampanhaResource.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<List<CampanhaResource>> buscarTodasCampanhas(){

        List<CampanhaResource> campanhaResources = campanhaService.buscarTodasAsCampanhasAtivas(LocalDate.now()).stream()
                .map(CampanhaResource::new)
                .collect(Collectors.toList());

        campanhaResources.forEach(campanhaResource -> {
            campanhaResource.add(linkTo(methodOn(CampanhaController.class).buscarPorId(campanhaResource.getChave())).withSelfRel());
        });

        if(logger.isDebugEnabled()) {
            logger.debug("Quantidade de campanhas retornadas : {} ", campanhaResources.size());
        }

        return new ResponseEntity<>(campanhaResources, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Busca a campanha por id",
            notes = "Retorna a campanha por ID idependente da data de vigência",
            response = CampanhaResource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<CampanhaResource> buscarPorId(@PathVariable String id){
        Optional<Campanha> campanhaOptional = campanhaService.buscarPorId(id);

        if(campanhaOptional.isPresent()) {
            CampanhaResource campanhaResource = new CampanhaResource(campanhaOptional.get());
            campanhaResource.add(linkTo(methodOn(CampanhaController.class).buscarPorId(campanhaResource.getChave())).withSelfRel());
            return new ResponseEntity<>(campanhaResource, HttpStatus.OK);
        }

        if(logger.isDebugEnabled()) {
            logger.debug("A campanha com ID: {} não foi encontrada", id);
        }

        throw new RecursoNaoEncontradoException();
    }


    @GetMapping(value = "/time-coracao/{timeCoracao}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Busca campanha por time do coração",
            notes = "Retorna a campanha por time do coração somente para campanhas ativas",
            response = CampanhaResource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<List<CampanhaResource>> buscaPorTimeDoCoracao(@PathVariable String timeCoracao){
        List<CampanhaResource> campanhaResources = campanhaService.buscaPorTimeDoCoracao(timeCoracao).stream()
                .map(CampanhaResource::new)
                .collect(Collectors.toList());

        campanhaResources.forEach(campanhaResource -> {
            campanhaResource.add(linkTo(methodOn(CampanhaController.class).buscarPorId(campanhaResource.getChave())).withSelfRel());
        });

        if(logger.isDebugEnabled()) {
            logger.debug("Quantidade de campanhas retornadas : {} ", campanhaResources.size());
        }

        return new ResponseEntity<>(campanhaResources, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Deleta a campanha por id",
            notes = "Deleta a campanha por ID idependente da data de vigência",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<?> deletarPorId(@PathVariable String id){

        if(logger.isDebugEnabled()) {
            logger.debug("Deletando a campanha com ID: {}", id);
        }
        campanhaService.deletarPorId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Atualiza a campanha por id",
            notes = "Atualiza a campanha por ID idependente da data de vigência",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400 , message = "Bad Request"),
            @ApiResponse(code = 500 , message = "Internal Server Error")})
    public ResponseEntity<?> atualizarCampanha(@PathVariable String id , @Valid @RequestBody CampanhaResource campanhaResource){

        Optional<Campanha> campanhaOptional = campanhaService.buscarPorId(id);

        campanhaOptional.ifPresent(campanha -> {
            if(logger.isDebugEnabled()) {
                logger.debug("Atualizando a Campanha : {}", campanha);
            }
            campanha.atualizarDados(campanhaResource);
            campanhaService.salvarCampanha(campanha);
        });

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*
     * Os métodos abaixo capturam a exceção e retornam o Status HTTP e mensagem de erro de acordo com o tipo de Exception
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ErrorInfo handleInternalServerError(Exception ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() , ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody ErrorInfo
    handleHttpMessageNotReadableException( HttpMessageNotReadableException ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() ,ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody ErrorInfo
    handleValidationException( MethodArgumentNotValidException ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() ,ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseBody ErrorInfo
    handleNotFoundException( RecursoNaoEncontradoException ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() ,ex);
    }

}
