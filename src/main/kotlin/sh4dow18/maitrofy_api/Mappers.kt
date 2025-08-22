package sh4dow18.maitrofy_api
// Mappers Requirements
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
// Mapper Constants
const val MAP = "stream().map"
const val LIST_TO_STRING = "collect(java.util.stream.Collectors.joining(\", \"))"
const val EMPTY_SET = "java(java.util.Collections.emptySet())"
const val EMPTY_LIST = "java(java.util.Collections.emptyList())"
const val DATE_TO_STRING = "format(java.time.format.DateTimeFormatter.ofPattern(\"dd-MM-yyyy\").withZone(java.time.ZoneId.of(\"America/Costa_Rica\")))"
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
// Privilege Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PrivilegeMapper {
    fun privilegeToPrivilegeResponse(
        privilege: Privilege
    ): PrivilegeResponse
    fun privilegeListToPrivilegeResponsesList(
        privilegesList: List<Privilege>
    ): List<PrivilegeResponse>
    @Mapping(target = "slug", expression = "java(newSlug)")
    @Mapping(target = "rolesList", expression = EMPTY_SET)
    fun privilegeRequestToPrivilege(
        newSlug: String,
        privilegeRequest: PrivilegeRequest
    ): Privilege
}
// Role Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface RoleMapper {
    fun roleToRoleResponse(
        role: Role
    ): RoleResponse
    fun rolesListToRoleResponsesList(
        rolesList: List<Role>
    ): List<RoleResponse>
    @Mapping(target = "privilegesList", expression = EMPTY_SET)
    @Mapping(target = "usersList", expression = EMPTY_LIST)
    fun roleRequestToRole(
        roleRequest: RoleRequest
    ): Role
}
// User Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    @Mapping(target = "createdDate", expression = "java(user.getCreatedDate().$DATE_TO_STRING)")
    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    fun userToMinimalUserResponse(
        user: User
    ): MinimalUserResponse
    fun usersListToMinimalUserResponsesList(
        usersList: List<User>
    ): List<MinimalUserResponse>
    @Mapping(target = "createdDate", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "enabled", expression = "java(true)")
    @Mapping(target = "image", expression = "java(false)")
    @Mapping(target = "role", expression = "java(newRole)")
    @Mapping(target = "gameLogsList", expression = EMPTY_LIST)
    fun userRequestToUser(
        userRequest: UserRequest,
        @Context newRole: Role,
    ): User
}
// Achievement Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AchievementMapper {
    fun achievementToAchievementResponse(
        achievement: Achievement
    ): AchievementResponse
    fun achievementsListToAchievementResponsesList(
        achievementsList: List<Achievement>
    ): List<AchievementResponse>
    @Mapping(target = "logo", expression = "java(newSlug)")
    @Mapping(target = "gameLogsList", expression = EMPTY_LIST)
    fun achievementRequestToAchievement(
        newSlug: String,
        achievementRequest: AchievementRequest
    ): Achievement
}
// Game Log Mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface GameLogMapper {
    @Mapping(target = "date", expression = "java(gameLog.getDate().$DATE_TO_STRING)")
    @Mapping(target = "platform", expression = "java(gameLog.getPlatform() != null ? gameLog.getPlatform().getName() : null)")
    @Mapping(target = "game.themes", expression = "java(game.getThemesList().$MAP(Theme::getName).$LIST_TO_STRING)")
    @Mapping(target = "game.genres", expression = "java(game.getGenresList().$MAP(Genre::getName).$LIST_TO_STRING)")
    @Mapping(target = "game.platforms", expression = "java(game.getPlatformsList().$MAP(Platform::getName).$LIST_TO_STRING)")
    fun gameLogToGameLogResponse(
        gameLog: GameLog
    ): GameLogResponse
    @Mapping(target = "platform", expression = "java(gameLog.getPlatform() != null ? gameLog.getPlatform().getName() : null)")
    @Mapping(target = "date", expression = "java(gameLog.getDate().$DATE_TO_STRING)")
    fun gameLogToMinimalGameLogResponse(
        gameLog: GameLog
    ): MinimalGameLogResponse
    fun gameLogsListToMinimalGameLogResponsesList(
        gameLogsList: List<GameLog>
    ): List<MinimalGameLogResponse> {
        return gameLogsList.map { gameLogToMinimalGameLogResponse(it) }
    }
    @Mapping(target = "slug", expression = "java(newSlug)")
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "date", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "game", expression = "java(existingGame)")
    @Mapping(target = "user", expression = "java(existingUser)")
    fun gameLogRequestToGameLog(
        gameLogRequest: GameLogRequest,
        newSlug: String,
        existingGame: Game,
        existingUser: User
    ): GameLog
}