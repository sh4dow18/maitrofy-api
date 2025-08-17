package sh4dow18.maitrofy_api
// Mappers Requirements
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
// Mapper Constants
const val MAP = "stream().map"
const val LIST_TO_STRING = "collect(java.util.stream.Collectors.joining(\", \"))"
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
// Genre Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface GenreMapper {
    fun genreToGenreResponse(
        genre: Genre
    ): GenreResponse
    fun genresListToGenreResponsesList(
        genresList: List<Genre>
    ): List<GenreResponse>
}
// Platform Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PlatformMapper {
    fun platformToPlatformResponse(
        platform: Platform
    ): PlatformResponse
    fun platformsListToPlatformResponsesList(
        platformsList: List<Platform>
    ): List<PlatformResponse>
}
// Game Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface GameMapper {
    // Transform each list in String as element1, element2, element3,...
    @Mapping(target = "themes", expression = "java(game.getThemesList().$MAP(Theme::getName).$LIST_TO_STRING)")
    @Mapping(target = "genres", expression = "java(game.getGenresList().$MAP(Genre::getName).$LIST_TO_STRING)")
    @Mapping(target = "platforms", expression = "java(game.getPlatformsList().$MAP(Platform::getName).$LIST_TO_STRING)")
    fun gameToGameResponse(
        game: Game
    ): GameResponse
    fun gamesListToMinimalGameResponsesList(
        gamesList: List<Game>
    ): List<MinimalGameResponse>
}