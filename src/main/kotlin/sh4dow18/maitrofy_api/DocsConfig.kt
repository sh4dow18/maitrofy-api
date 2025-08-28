package sh4dow18.maitrofy_api
// Docs Config Requirements
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Configuration
// Set Open API Definition
@OpenAPIDefinition(
    info = Info(title = "Maitrofy API", version = "0.10.0"),
    tags = [
        Tag(name = "Achievements"),
        Tag(name = "Game Logs"),
        Tag(name = "Games"),
        Tag(name = "Genres"),
        Tag(name = "Platforms"),
        Tag(name = "Privileges"),
        Tag(name = "Roles"),
        Tag(name = "Themes"),
        Tag(name = "Users"),
        Tag(name = "Utils"),
    ]
)
@Suppress("unused")
// Set Swagger Config Class
@Configuration
class SwaggerConfig
