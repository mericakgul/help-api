package com.mericakgul.helpapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mericakgul.helpapi"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(this.apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("Help API")
                .description("Brings customers and service providers together.")
                .contact(new Contact("Meriç Akgül", "https://mericakgul.com", "meric.ext@gmail.com"))
                .version("0.0.1")
                .build();
    }

    @Bean
    UiConfiguration uiConfig(){
        return UiConfigurationBuilder.builder().docExpansion(DocExpansion.LIST).build();
    }
}
