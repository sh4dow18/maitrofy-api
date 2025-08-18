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
}
// Genre Repository
@Repository
interface GenreRepository: JpaRepository<Genre, Long> {
    fun findAllByOrderByNameAsc(): List<Genre>
}
// Platform Repository
@Repository
interface PlatformRepository: JpaRepository<Platform, Long> {
    fun findAllByOrderByNameAsc(): List<Platform>
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