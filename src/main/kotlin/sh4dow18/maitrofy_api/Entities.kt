package sh4dow18.maitrofy_api
// Entities Requirements
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
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
    var id: Long,
    var name: String,
    // Many-to-Many Relationship with Theme
    @ManyToMany(mappedBy = "themesList", fetch = FetchType.LAZY, targetEntity = Game::class)
    var gamesList: Set<Game>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Theme
        if (other !is Theme) return false
        // Compare the id of this object with the id of the other object
        return id == other.id
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = id.hashCode()
}
// Genre Entity
@Entity
@Table(name = "genres")
data class Genre(
    @Id
    var id: Long,
    var name: String,
    // Many-to-Many Relationship with Genre
    @ManyToMany(mappedBy = "genresList", fetch = FetchType.LAZY, targetEntity = Game::class)
    var gamesList: Set<Game>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Genre
        if (other !is Genre) return false
        // Compare the id of this object with the id of the other object
        return id == other.id
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = id.hashCode()
}
// Platform Entity
@Entity
@Table(name = "platforms")
data class Platform(
    @Id
    var id: Long,
    var name: String,
    // Many-to-Many Relationship with Platform
    @ManyToMany(mappedBy = "platformsList", fetch = FetchType.LAZY, targetEntity = Game::class)
    var gamesList: Set<Game>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Platform
        if (other !is Platform) return false
        // Compare the id of this object with the id of the other object
        return id == other.id
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = id.hashCode()
}
// Game Entity
@Entity
@Table(name = "games")
data class Game(
    @Id
    var slug: String,
    var name: String,
    @Column(length = 3000)
    var summary: String,
    var cover: String,
    var background: String,
    var rating: Float,
    var classification: String?,
    var year: Int,
    var video: String,
    var collection: String?,
    var developer: String,
    var gameMode: String,
    // Many-to-Many Relationship with Game
    @ManyToMany(targetEntity = Theme::class)
    @JoinTable(
        name = "game_theme",
        joinColumns = [JoinColumn(name = "game_id", referencedColumnName = "slug")],
        inverseJoinColumns = [JoinColumn(name = "theme_id", referencedColumnName = "id")]
    )
    var themesList: Set<Theme>,
    // Many-to-Many Relationship with Game
    @ManyToMany(targetEntity = Genre::class)
    @JoinTable(
        name = "game_genre",
        joinColumns = [JoinColumn(name = "game_id", referencedColumnName = "slug")],
        inverseJoinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")]
    )
    var genresList: Set<Genre>,
    // Many-to-Many Relationship with Game
    @ManyToMany(targetEntity = Platform::class)
    @JoinTable(
        name = "game_platform",
        joinColumns = [JoinColumn(name = "game_id", referencedColumnName = "slug")],
        inverseJoinColumns = [JoinColumn(name = "platform_id", referencedColumnName = "id")]
    )
    var platformsList: Set<Platform>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Game
        if (other !is Game) return false
        // Compare the id of this object with the id of the other object
        return slug == other.slug
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = slug.hashCode()
}