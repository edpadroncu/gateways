package com.edpadron.gateways.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>();
            SwaggerResource wsResource = new SwaggerResource();
            wsResource.setName("Gateways Manager");
            wsResource.setSwaggerVersion("2.0");
            wsResource.setLocation("/swagger.yaml");
            resources.add(wsResource);
            return resources;
        };
    }
}