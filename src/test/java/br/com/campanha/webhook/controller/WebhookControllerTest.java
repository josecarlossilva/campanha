package br.com.campanha.webhook.controller;

import br.com.campanha.webhook.domain.Webhook;
import br.com.campanha.webhook.domain.WebhookResource;
import br.com.campanha.webhook.exception.WebhookCadastradoException;
import br.com.campanha.webhook.repository.WebhookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebhookControllerTest {

    @Autowired
    private WebhookController webhookController;

    @Autowired
    private WebhookRepository webhookRepository;

    @Before
    public void setUp() throws Exception {
        webhookRepository.deleteAll();
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @Test
    public void cadastrarWebhookTest() throws Exception {
        final ResponseEntity<?> responseEntity =
                webhookController.cadastrarWebhook(new WebhookResource("http://localhost:8080/api/v1/hook", "@#$GDFG%$GH"));
        assertThat(responseEntity).as("O webhook deve ser criado com sucesso").isNotNull();
        assertThat(responseEntity.getStatusCode()).as("O Status code deve ser created").isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().get("location")).as("Deve retornar a URL do webhook criado").isNotNull();
    }

    @Test
    public void quandoWebhookJaCadastradoRetornarErro() throws Exception {
        webhookRepository.save(new Webhook("http://localhost:8080/api/v1/hook", "@#$GDFG%$GH"));

        assertThatExceptionOfType(WebhookCadastradoException.class)
                .isThrownBy(() ->  webhookController.cadastrarWebhook(new WebhookResource("http://localhost:8080/api/v1/hook", "@#$GDFG%$GH")))
                .withMessageContaining("Webhook(URL) já cadastrado");
    }

}
