package com.danielcontador.onebox_technical_test.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Daniel Contador Fraile",
                        url = "www.linkedin.com/in/daniel-contador-fraile-a721a5a6",
                        email = "danielcontaf1996@gmail.com"
                ),
                description = "OpenApi documentation for Onebox Technical Test",
                title = "OpenAPI documentation",
                version = "1.0.0"
        )
)

public class OpenApiConfig {
}
