package sh4dow18.maitrofy_api
// Entities Requirements
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.ZonedDateTime
// Theme Entity
@Entity
@Table(name = "themes")
data class Theme(
    @Id
    var id: Long,
    var name: String,
    // Many-to-Many Relationship with Game
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
    // Many-to-Many Relationship with Game
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
    // Many-to-Many Relationship with Game
    @ManyToMany(mappedBy = "platformsList", fetch = FetchType.LAZY, targetEntity = Game::class)
    var gamesList: Set<Game>,
    // One-to-Many Relationship with Game Log
    @OneToMany(mappedBy = "platform", targetEntity = GameLog::class)
    var gameLogsList: List<GameLog>,
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
    // Many-to-Many Relationship with Theme
    @ManyToMany(targetEntity = Theme::class)
    @JoinTable(
        name = "game_theme",
        joinColumns = [JoinColumn(name = "game_id", referencedColumnName = "slug")],
        inverseJoinColumns = [JoinColumn(name = "theme_id", referencedColumnName = "id")]
    )
    var themesList: Set<Theme>,
    // Many-to-Many Relationship with Genre
    @ManyToMany(targetEntity = Genre::class)
    @JoinTable(
        name = "game_genre",
        joinColumns = [JoinColumn(name = "game_id", referencedColumnName = "slug")],
        inverseJoinColumns = [JoinColumn(name = "genre_id", referencedColumnName = "id")]
    )
    var genresList: Set<Genre>,
    // Many-to-Many Relationship with Platform
    @ManyToMany(targetEntity = Platform::class)
    @JoinTable(
        name = "game_platform",
        joinColumns = [JoinColumn(name = "game_id", referencedColumnName = "slug")],
        inverseJoinColumns = [JoinColumn(name = "platform_id", referencedColumnName = "id")]
    )
    var platformsList: Set<Platform>,
    // One-to-Many Relationship with Game Log
    @OneToMany(mappedBy = "game", targetEntity = GameLog::class)
    var gameLogsList: List<GameLog>,
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
// User Entity
@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var email: String?,
    var name: String?,
    var password: String?,
    var createdDate: ZonedDateTime,
    var enabled: Boolean,
    var image: Boolean,
    // Many-to-One Relationship with Role
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    var role: Role,
    // One-to-Many Relationship with Game Log
    @OneToMany(mappedBy = "user", targetEntity = GameLog::class)
    var gameLogsList: List<GameLog>,
)
// Role Entity
@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    var description: String,
    // One-to-Many Relationship with Users
    @OneToMany(mappedBy = "role", targetEntity = User::class)
    var usersList: List<User>,
    // Many-to-Many Relationship with Privileges
    @ManyToMany(targetEntity = Privilege::class)
    @JoinTable(
        name = "role_privilege",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "slug")]
    )
    var privilegesList: MutableSet<Privilege>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Role
        if (other !is Role) return false
        // Compare the id of this object with the id of the other object
        return id == other.id
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = id.hashCode()
}
// Privileges Entity
@Entity
@Table(name = "privileges")
data class Privilege(
    @Id
    var slug: String,
    var name: String,
    var description: String,
    // Many-to-Many Relationship with Role
    @ManyToMany(mappedBy = "privilegesList", fetch = FetchType.LAZY, targetEntity = Role::class)
    var rolesList: MutableSet<Role>
) {
    override fun equals(other: Any?): Boolean {
        // Check if the current object is the same instance as other
        if (this === other) return true
        // Check if other is a Privilege
        if (other !is Privilege) return false
        // Compare the id of this object with the id of the other object
        return slug == other.slug
    }
    // Use the hashCode of the "id" field as the hash code for the entire object
    override fun hashCode(): Int = slug.hashCode()
}
// Game Log Entity
@Entity
@Table(name = "gameLogs")
data class GameLog(
    // Game Log Properties
    @Id
    var slug: String,
    var rating: Int?,
    var date: ZonedDateTime,
    @Column(length = 1000)
    var review: String?,
    var hoursSpend: Int?,
    // Many-to-One Relationship with Game
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false, referencedColumnName = "slug")
    var game: Game,
    // Many-to-One Relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    var user: User,
    // Many-to-One Relationship with Platform
    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = true, referencedColumnName = "id")
    var platform: Platform?,
    // Many-to-One Relationship with Achievement
    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = true, referencedColumnName = "id")
    var achievement: Achievement?
)
// Achievement Entity
@Entity
@Table(name = "achievements")
data class Achievement(
    // Achievement Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    var points: Int,
    var logo: String,
    // One-to-Many Relationship with Game Log
    @OneToMany(mappedBy = "achievement", targetEntity = GameLog::class)
    var gameLogsList: List<GameLog>,
)