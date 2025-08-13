package sh4dow18.maitrofy_api
// Repositories Requirements
import org.springframework.data.jpa.repository.JpaRepository
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