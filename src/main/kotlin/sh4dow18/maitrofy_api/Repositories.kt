package sh4dow18.maitrofy_api
// Repositories Requirements
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
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
    fun findTop100ByNameContainingIgnoreCase(name: String): List<Game>
    fun findByCollectionIgnoreCaseAndSlugNot(collection: String, slug: String): List<Game>
    @Query("SELECT g FROM Game g JOIN g.themesList t WHERE t.id = :themeId AND g.slug <> :excludeSlug")
    fun findTop15ByTheme(@Param("themeId") themeId: Long, @Param("excludeSlug") excludeSlug: String): List<Game>
    @Query("SELECT g FROM Game g JOIN g.genresList ge WHERE ge.id = :genreId AND g.slug <> :excludeSlug")
    fun findTop15ByGenre(@Param("genreId") genreId: Long, @Param("excludeSlug") excludeSlug: String): List<Game>

}