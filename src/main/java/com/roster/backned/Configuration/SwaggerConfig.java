package com.roster.backned.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Roster Management System Backend API",
                version = "1.0",
                description = "API documentation for the Roster Management System Backend",
                contact = @Contact(
                        name = "Buddhika Senanayake",
                        email = "bnsbuddhika@gmail.com"
                ),
                license = @License(
                        name = "GPL-3.0 license",
                        url = "https://www.gnu.org/licenses/"
                ),
                termsOfService = "Term of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER,
        description = "JWT auth description"
)
public class SwaggerConfig {
}
