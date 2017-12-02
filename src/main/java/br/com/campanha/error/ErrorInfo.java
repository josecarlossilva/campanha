package br.com.campanha.error;

import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="ErrorInfo", description="Representa informações de erros que serão retornadas pela API")
public class ErrorInfo {
    @ApiModelProperty(value = "URL que foi chamada durante o erro",dataType = "string", required = true)
    public final String url;

    @ApiModelProperty(value = "Mensagem de exceção",dataType = "string", required = true)
    public final String ex;

    public ErrorInfo(String url, Exception ex) {
        this.url = url;
        this.ex = ex.getLocalizedMessage();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("ex", ex)
                .toString();
    }
}
