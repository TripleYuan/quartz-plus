package org.leekeggs.quartzextendschedulercenter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Collection;
import java.util.List;

/**
 * @author leekeggs
 * @since 2021-05-26
 */
@Configuration
@EnableSwagger2WebMvc
public class Swagger2Config {

    @Bean
    public Docket creatRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.leekeggs.quartzextendschedulercenter.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(true)
                .useDefaultResponseMessages(false)
                .directModelSubstitute(Collection.class, List.class);

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("quartz调度中心API文档")
                .description("quartz调度中心API文档")
                .version("1.0.0")
                .build();
    }
}
