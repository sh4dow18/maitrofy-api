package sh4dow18.maitrofy_api
// Repositories Requirements
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
// Theme Repository
@Repository
interface ThemeRepository: JpaRepository<Theme, Long> {
    fun findAllByOrderByNameAsc(): List<Theme>
    @Query(value = """
        SELECT t.* FROM themes t
        JOIN game_theme gt ON t.id = gt.theme_id
        JOIN games g ON gt.game_id = g.slug
        JOIN game_logs gl ON g.slug = gl.game_id
        WHERE gl.user_id = :userId
        GROUP BY t.id
        ORDER BY COUNT(t.id) DESC
        LIMIT 1
    """, nativeQuery = true)
    fun findMostFrequentThemeByUserId(userId: Long): Optional<Theme>
}
// Genre Repository
@Repository
interface GenreRepository: JpaRepository<Genre, Long> {
    fun findAllByOrderByNameAsc(): List<Genre>
    @Query(value = """
        SELECT ge.* FROM genres ge
        JOIN game_genre gg ON ge.id = gg.genre_id
        JOIN games g ON gg.game_id = g.slug
        JOIN game_logs gl ON g.slug = gl.game_id
        WHERE gl.user_id = :userId
        GROUP BY ge.id
        ORDER BY COUNT(ge.id) DESC
        LIMIT 1
    """, nativeQuery = true)
    fun findMostFrequentGenreByUserId(userId: Long): Optional<Genre>
}
// Platform Repository
@Repository
interface PlatformRepository: JpaRepository<Platform, Long> {
    fun findAllByOrderByNameAsc(): List<Platform>
    @Query(value = """
        SELECT p.* FROM platforms p
        JOIN game_logs g ON g.platform_id = p.id
        WHERE g.user_id = :userId
        GROUP BY p.id
        ORDER BY COUNT(p.id) DESC
        LIMIT 1
    """, nativeQuery = true)
    fun findMostFrequentPlatformByUserId(userId: Long): Optional<Platform>
}
// Game Repository
@Repository
interface GameRepository: JpaRepository<Game, String> {
    fun findTop100ByOrderByRatingDesc(): List<Game>
    @Query("""
        SELECT DISTINCT g FROM Game g
        LEFT JOIN g.themesList t
        LEFT JOIN g.genresList ge
        LEFT JOIN g.platformsList p
        WHERE LOWER(g.name) LIKE CONCAT('%', LOWER(:name), '%')
          AND (:themeId IS NULL OR t.id = :themeId)
          AND (:genreId IS NULL OR ge.id = :genreId)
          AND (:platformId IS NULL OR p.id = :platformId)
    """)
    fun findByNameAndThemeAndGenreAndPlatform(
        @Param("name") name: String,
        @Param("themeId") themeId: Long?,
        @Param("genreId") genreId: Long?,
        @Param("platformId") platformId: Long?
    ): List<Game>
    @Query("""
        SELECT DISTINCT g FROM Game g
        LEFT JOIN g.themesList t
        LEFT JOIN g.genresList ge
        LEFT JOIN g.platformsList p
        WHERE (:themeId IS NULL OR t.id = :themeId)
          AND (:genreId IS NULL OR ge.id = :genreId)
          AND (:platformId IS NULL OR p.id = :platformId)
    """)
    fun findByThemeAndGenreAndPlatform(
        @Param("themeId") themeId: Long?,
        @Param("genreId") genreId: Long?,
        @Param("platformId") platformId: Long?
    ): List<Game>
    fun findByCollectionIgnoreCaseAndSlugNot(collection: String, slug: String): List<Game>
    @Query("SELECT g FROM Game g JOIN g.themesList t WHERE t.id = :themeId AND g.slug <> :excludeSlug")
    fun findByTheme(@Param("themeId") themeId: Long, @Param("excludeSlug") excludeSlug: String): List<Game>
    @Query("SELECT g FROM Game g JOIN g.genresList ge WHERE ge.id = :genreId AND g.slug <> :excludeSlug")
    fun findByGenre(@Param("genreId") genreId: Long, @Param("excludeSlug") excludeSlug: String): List<Game>

}
// User Repository
@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}
// Privilege Repository
@Repository
interface PrivilegeRepository: JpaRepository<Privilege, String> {
    fun findAllByOrderBySlugAsc(): List<Privilege>
}
// Role Repository
@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findAllByOrderByIdAsc(): List<Role>
    fun findByNameIgnoringCase(name: String): Optional<Role>
}
// Achievement Repository
@Repository
interface AchievementRepository: JpaRepository<Achievement, Long> {
    fun findAllByOrderByIdAsc(): List<Achievement>
    fun findByNameIgnoringCase(name: String): Optional<Achievement>
}
// Game Log Repository
@Repository
interface GameLogRepository: JpaRepository<GameLog, String> {
    fun findByUserIdOrderByDateDesc(userId: Long): List<GameLog>
    fun findByUserIdAndAchievementId(userId: Long, achievementId: Long): List<GameLog>
    fun findTopByUserIdAndRatingIsNotNullOrderByRatingDesc(userId: Long): Optional<GameLog>
    @Query(value = """
        SELECT g.collection
        FROM game_logs gl
        JOIN games g ON gl.game_id = g.slug
        WHERE gl.user_id = :userId
          AND g.collection IS NOT NULL
        GROUP BY g.collection
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """, nativeQuery = true)
    fun findMostFrequentCollectionByUserId(userId: Long): Optional<String>
    @Query(value = """
        SELECT g.developer
        FROM game_logs gl
        JOIN games g ON gl.game_id = g.slug
        WHERE gl.user_id = :userId
        AND g.developer IS NOT NULL
        GROUP BY g.developer
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """, nativeQuery = true)
    fun findMostFrequentDeveloperByUserId(userId: Long): Optional<String>
    @Query(value = """
        SELECT g.game_mode
        FROM game_logs gl
        JOIN games g ON gl.game_id = g.slug
        WHERE gl.user_id = :userId
        AND g.game_mode IS NOT NULL
        GROUP BY g.game_mode
        ORDER BY COUNT(*) DESC
        LIMIT 1
    """, nativeQuery = true)
    fun findMostFrequentGameModeByUserId(userId: Long): Optional<String>
}