package com.eduardonunes.bookstoremanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    private static final String BASE_PACKAGE = "com.eduardonunes.bookstoremanager";
    private static final String API_TITLE = "Book Store Manager course";
    private static final String API_DESCRIPTION = "Book store manager Api";
    private static final String API_VERSION = "1.0.0";
    private static final String CONTACT_NAME = "Eduardo";
    private static final String CONTACT_URL = "https://github.com/EduardoNunes5";
    private static final String API_EMAIL = "eduardo.afonso.silva@ccc.ufcg.edu.br";

    @Bean
    public Docket apiDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(buildApiInfo());
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .contact(new Contact(CONTACT_NAME, CONTACT_URL, API_EMAIL))
                .build();
    }
}
