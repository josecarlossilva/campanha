package br.com.campanha.webhook.repository;

import br.com.campanha.domain.Campanha;
import br.com.campanha.webhook.domain.Webhook;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebhookRepository extends MongoRepository<Webhook, String> {
}
