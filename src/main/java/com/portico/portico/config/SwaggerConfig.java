package com.portico.portico.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Portico",
                description = "Portico is a convenient software designed to make shipping and transportation in the freight industry easier",
                summary = "Portico operations",
                termsOfService = "T&C",
                contact = @Contact(
                        name = "Portico Team"
                ),
                version = "v1"
        ),
        servers = {
                @Server(
                        description = "dev",
                        url = "http://localhost:8080"
                )
        }

)
public class SwaggerConfig {
}
