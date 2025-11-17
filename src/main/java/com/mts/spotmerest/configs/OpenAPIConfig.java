package com.mts.spotmerest.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import java.util.List;


@Configuration
@OpenAPIDefinition(security = {@SecurityRequirement(name = "bearer-key")})
public class OpenAPIConfig {

    @Value("${spotme.env-props.dev-url}")
    private String devUrl;

    @Value("${spotme.env-props.node-url}")
    private String nodeUrl;

    @Value("${spotme.env-props.gateway-url}")
    private String gatewayURL;

    @Value("${spotme.env-props.gateway-path}")
    private String gatewayPath;

    @Value("${spotme.env-props.dev-url}")
    private String localUrl;

    @Value("${spotme.env-props.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        
        Server gatewayServer = new Server();
        String gatewayServerUrl= gatewayURL + gatewayPath.toString();
        gatewayServer.setUrl(gatewayServerUrl);
        gatewayServer.setDescription("Gateway URL");

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL");

        Server nodeServer = new Server();
        nodeServer.setUrl(nodeUrl);
        nodeServer.setDescription("Node URL");

        Server localServer = new Server();
        localServer.setUrl(localUrl);
        localServer.setDescription("Local URL");

        Contact contact = new Contact();
        contact.setEmail("magustechsolutions@gmail.com");
        contact.setName("Magus Tech Solutions");


        Info info = new Info()
                .title("SpotMe Authentication Service API")
                .version("3.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials.").termsOfService("https://www.bezkoder.com/terms");


        return new OpenAPI().components(new Components()
                    .addSecuritySchemes("bearer-key",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT"))).info(info).servers(List.of(gatewayServer,devServer,nodeServer,localServer));
    }
}