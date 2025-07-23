package com.baem.logisticapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI logisticsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lojistik Firması API")
                        .description("Lojistik firması için geliştirilen REST API servisleri")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Lojistik Firması")
                                .email("info@lojistikfirmasi.com")
                                .url("https://www.lojistikfirmasi.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}