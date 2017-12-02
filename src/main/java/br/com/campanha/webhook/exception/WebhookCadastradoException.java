package br.com.campanha.webhook.exception;

public class WebhookCadastradoException extends RuntimeException {

    public WebhookCadastradoException() {
        super("Webhook(URL) já cadastrado");
    }
}
