package br.com.campanha.webhook.domain;

import com.google.common.base.MoreObjects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Document
public class Webhook {

    @Id
    private String id;

    @Indexed(unique = true)
    @Size(min=10, max=200, message="Url tem capacidade de 5 a 100 caracteres.")
    @NotNull(message="Url é obrigatório!")
    @Field(value = "url")
    private String url;

    @Indexed
    @Field(value = "chaveAcesso")
    private String chaveAcesso;

    public Webhook() {
    }

    public Webhook(String url, String chaveAcesso) {
        this.url = url;
        this.chaveAcesso = chaveAcesso;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("url", url)
                .add("chaveAcesso", chaveAcesso)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Webhook webhook = (Webhook) o;
        return Objects.equals(id, webhook.id) &&
                Objects.equals(getUrl(), webhook.getUrl()) &&
                Objects.equals(getChaveAcesso(), webhook.getChaveAcesso());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getUrl(), getChaveAcesso());
    }
}
