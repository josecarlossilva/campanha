package br.com.campanha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@ApiModel(value="CampanhaResource", description="Representa os dados da campanha que devem ser recebidos e retornados pela API Rest de campanha")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampanhaResource extends ResourceSupport {

    @Size(min=5, max=100, message="Nome tem capacidade de 5 a 100 caracteres.")
    @NotNull(message="Nome da campanha é obrigatório!")
    @ApiModelProperty(value = "Nome da campanha", dataType = "string", required = true)
    private String nome;

    @NotNull(message="A identificação do time é obrigatório!")
    @ApiModelProperty(value = "Id do time do coração", dataType = "string", required = true)
    private String timeCoracaoId;

    @NotNull(message="O inicio da vigência é obrigatório!")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "Data de inicio de vigência", dataType = "date", required = true)
    private LocalDate inicioVigencia;

    @NotNull(message="O fim vigência é obrigatório!")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "Data de fim de vigência", dataType = "date", required = true)
    private LocalDate fimVigencia;

    @JsonIgnore
    private String chave;

    public CampanhaResource() {
    }

    public CampanhaResource(Campanha campanha) {
        this.nome = campanha.getNome();
        this.timeCoracaoId = campanha.getTimeCoracaoId();
        this.inicioVigencia = campanha.getInicioVigencia();
        this.fimVigencia = campanha.getFimVigencia();
        this.chave = campanha.getId();
    }

    public CampanhaResource(String nome, String timeCoracaoId, LocalDate inicioVigencia, LocalDate fimVigencia) {
        this.nome = nome;
        this.timeCoracaoId = timeCoracaoId;
        this.inicioVigencia = inicioVigencia;
        this.fimVigencia = fimVigencia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTimeCoracaoId() {
        return timeCoracaoId;
    }

    public void setTimeCoracaoId(String timeCoracaoId) {
        this.timeCoracaoId = timeCoracaoId;
    }

    public LocalDate getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(LocalDate inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public LocalDate getFimVigencia() {
        return fimVigencia;
    }

    public void setFimVigencia(LocalDate fimVigencia) {
        this.fimVigencia = fimVigencia;
    }

    public String getChave() {
        return chave;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("nome", nome)
                .add("timeCoracaoId", timeCoracaoId)
                .add("inicioVigencia", inicioVigencia)
                .add("fimVigencia", fimVigencia)
                .toString();
    }
}
