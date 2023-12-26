package akletini.life.application;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import java.util.Objects;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Autowired
    private Environment env;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(Objects.requireNonNull(env.getProperty("elastic.url")))
                .build();
    }


    @Override
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(objectMapper);
        RestClientTransport transport = new RestClientTransport(restClient,
                jacksonJsonpMapper);
        return new ElasticsearchClient(transport);
    }

    @Bean
    JacksonJsonpMapper jacksonJsonpMapper(ObjectMapper objectMapper) {
        objectMapper.findAndRegisterModules();
        return new JacksonJsonpMapper(objectMapper);
    }

}
