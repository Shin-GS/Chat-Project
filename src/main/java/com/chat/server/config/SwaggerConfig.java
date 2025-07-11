package com.chat.server.config;

import com.chat.server.common.constant.Constants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Chat API", version = "v1"),
        security = {
                @SecurityRequirement(name = Constants.SWAGGER_ACCESS_TOKEN),
                @SecurityRequirement(name = Constants.SWAGGER_REFRESH_TOKEN)
        }
)
@SecurityScheme(
        name = Constants.SWAGGER_ACCESS_TOKEN,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Access Token"
)
@SecurityScheme(
        name = Constants.SWAGGER_REFRESH_TOKEN,
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = Constants.HEADER_AUTHORIZATION_REFRESH,
        description = "Refresh Token"
)
@Configuration
public class SwaggerConfig {
}
