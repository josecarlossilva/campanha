package br.com.campanha.webhook.service;

import br.com.campanha.domain.Campanha;
import br.com.campanha.webhook.domain.Webhook;

public interface WebhookService {

    Webhook cadastrarWebhook(String url, String chaveAcesso);

    void notificarAtualizacao(Campanha campanha);
}
