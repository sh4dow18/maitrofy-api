package sh4dow18.maitrofy_api
// Mappers Requirements
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
// Theme Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ThemeMapper {
    fun themeToThemeResponse(
        theme: Theme
    ): ThemeResponse
    fun themesListToThemeResponsesList(
        themesList: List<Theme>
    ): List<ThemeResponse>
}