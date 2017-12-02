package br.com.campanha;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.init.AbstractRepositoryPopulatorFactoryBean;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = {"br.com.campanha.repository","br.com.campanha.webhook.repository"})
@EnableFeignClients
@EnableHystrix
public class ApplicationStarter {

    public static void main(String[] args) {
       SpringApplication app = new SpringApplication(ApplicationStarter.class);
       app.setBannerMode(Banner.Mode.OFF);
       app.run(args);
    }

    /**
     * Responsável por ler o arquivo do resources (data.json)
     * e carregar para o MongoDB
     */
    @Bean
    public AbstractRepositoryPopulatorFactoryBean repositoryPopulator() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();
        factoryBean.setResources(new Resource[] { new ClassPathResource("data.json") });
        factoryBean.setMapper(mapper);

        return factoryBean;
    }
}
