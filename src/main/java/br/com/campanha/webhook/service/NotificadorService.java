package br.com.campanha.webhook.service;

import br.com.campanha.domain.CampanhaResource;
import feign.Headers;
import feign.RequestLine;

public interface NotificadorService {

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    void notifica(CampanhaResource campanhaResource);
}
