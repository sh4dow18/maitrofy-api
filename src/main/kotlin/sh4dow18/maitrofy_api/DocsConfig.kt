package sh4dow18.maitrofy_api
// Docs Config Requirements
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.TreeMap

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
class SwaggerConfig {
    @Bean
    // Function that allows to Set new Props Values in Open API
    fun customOpenAPI(): OpenApiCustomizer {
        // Returns OpenAPI Customizer with new Props
        return OpenApiCustomizer { openApi: OpenAPI ->
            // Get a Schemas Map from OpenAPI
            val schemas: Map<String, Schema<*>>? = openApi.components?.schemas
            // If any Schema exist, pass
            if (schemas != null) {
                // Order Schemas by Name using TreeMap to keep the keys in natural mode, in this case alphabetically
                openApi.components.schemas = TreeMap(schemas)
            }
        }
    }
}