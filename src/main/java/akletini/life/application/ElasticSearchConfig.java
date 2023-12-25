package akletini.life.application;

import org.springframework.beans.factory.annotation.Autowired;
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
}
