package sh4dow18.maitrofy_api
// Entities Requirements
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
// Theme Entity
@Entity
@Table(name = "themes")
data class Theme(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    // Many-to-Many Relationship with Game
    @ManyToMany(targetEntity = Game::class)
    @JoinTable(
        name = "theme_game",
        joinColumns = [JoinColumn(name = "theme_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "game_id", referencedColumnName = "slug")]
    )
    var gamesList: Set<Game>
)
// Genre Entity
@Entity
@Table(name = "genres")
data class Genre(
    @Id
    var id: Long,
    var name: String,
    // Many-to-Many Relationship with Game
    @ManyToMany(targetEntity = Game::class)
    @JoinTable(
        name = "genre_game",
        joinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "game_id", referencedColumnName = "slug")]
    )
    var gamesList: Set<Game>
)
// Platform Entity
@Entity
@Table(name = "platforms")
data class Platform(
    @Id
    var id: Long,
    var name: String,
    // Many-to-Many Relationship with Game
    @ManyToMany(targetEntity = Game::class)
    @JoinTable(
        name = "platform_game",
        joinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "game_id", referencedColumnName = "slug")]
    )
    var gamesList: Set<Game>
)
// Game Entity
@Entity
@Table(name = "games")
data class Game(
    @Id
    var slug: String,
    var name: String,
    var summary: String,
    var cover: String,
    var background: String,
    var rating: Float,
    var classification: String,
    var year: Int,
    var video: String,
    var collection: String,
    var developer: String,
    var gameMode: String,
    // Many-to-Many Relationship with Theme
    @ManyToMany(mappedBy = "gamesList", fetch = FetchType.LAZY, targetEntity = Theme::class)
    var themesList: Set<Theme>,
    // Many-to-Many Relationship with Genre
    @ManyToMany(mappedBy = "gamesList", fetch = FetchType.LAZY, targetEntity = Genre::class)
    var genresList: Set<Genre>,
    // Many-to-Many Relationship with Platform
    @ManyToMany(mappedBy = "gamesList", fetch = FetchType.LAZY, targetEntity = Platform::class)
    var platformsList: Set<Platform>
)