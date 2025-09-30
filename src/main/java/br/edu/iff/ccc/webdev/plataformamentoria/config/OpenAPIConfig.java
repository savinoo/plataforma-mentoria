package br.edu.iff.ccc.webdev.plataformamentoria.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API da Plataforma de Mentoria",
        version = "v1",
        description = "API RESTful para gerenciar a Plataforma de Mentoria para Estudantes.",
        license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
    )
)
public class OpenAPIConfig {
}