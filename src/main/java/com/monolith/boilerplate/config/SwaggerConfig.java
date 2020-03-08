package com.monolith.boilerplate.config;

import com.google.common.collect.Lists;
import com.monolith.boilerplate.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.singletonList;

@Configuration
@EnableSwagger2
@Profile(value = {"development"})
public class SwaggerConfig {
    @Bean
    public Docket api() {
        final String swaggerToken = "default_token";
        return  new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.monolith.boilerplate.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .ignoredParameterTypes(UserPrincipal.class)
                .useDefaultResponseMessages(true)
                .globalOperationParameters(singletonList(
                        new ParameterBuilder()
                                .name("Authorization")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)
                                .hidden(true)
                                .defaultValue("Bearer " + swaggerToken)
                                .build()
                        )
                );
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Monolith API", "", "1.0", "", new Contact("", "", ""), "", "", Lists.newArrayList());
    }

}

