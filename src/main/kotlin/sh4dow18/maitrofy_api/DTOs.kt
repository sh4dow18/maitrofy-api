package sh4dow18.maitrofy_api
// DTOs Requirements
import com.fasterxml.jackson.annotation.JsonCreator

// Requests

data class PrivilegeRequest(
    var name: String,
    var description: String,
)
data class RoleRequest(
    var name: String,
    var description: String,
    var privilegesList: List<String>
)
data class UserRequest(
    var email: String,
    var name: String,
    var password: String,
)
data class AchievementRequest(
    var name: String,
    var points: Int,
)
data class GameLogRequest(
    var game: String
)

// Update Requests

data class GameLogUpdateRequest(
    var game: String,
    var rating: Int?,
    var date: Long?,
    var review: String?,
    var hoursSpend: Int?,
    var platform: Long?,
    var achievement: Long?
)

// Special Request

data class LoginRequest(
    var email: String,
    var password: String,
){
    @JsonCreator
    @Suppress("unused")
    constructor() : this("", "")
}

// Responses

data class ThemeResponse(
    var id: Long,
    var name: String
)
data class GenreResponse(
    var id: Long,
    var name: String
)
data class PlatformResponse(
    var id: Long,
    var name: String
)
data class GameResponse(
    var slug: String,
    var name: String,
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
    var themes: String,
    var genres: String,
    var platforms: String
)
data class PrivilegeResponse(
    var slug: String,
    var name: String,
    var description: String,
)
data class RoleResponse(
    var id: Long,
    var name: String,
    var description: String,
    var privilegesList: List<PrivilegeResponse>
)
data class AchievementResponse(
    var id: Long,
    var name: String,
    var points: Int,
    var logo: String,
)
data class GameLogResponse(
    var slug: String,
    var rating: Int?,
    var date: String,
    var review: String?,
    var hoursSpend: Int?,
    var game: GameResponse,
    var platform: String?,
    var achievement: AchievementResponse?
)
data class UserResponse(
    val account: ProfileAccountResponse,
    val statistics: ProfileStatisticsResponse,
    val preferences: ProfilePreferencesResponse
)

// Minimal Responses

data class MinimalGameResponse(
    var slug: String,
    var name: String,
    var cover: String,
)
data class MinimalGameLogResponse(
    var slug: String,
    var rating: Int?,
    var date: String,
    var game: MinimalGameResponse,
    var platform: String?,
    var achievement: AchievementResponse?
)
data class MinimalUserResponse(
    var id: Long,
    var email: String?,
    var name: String?,
    var createdDate: String,
    var enabled: Boolean,
    var image: Boolean,
    var role: String,
)

// Special Responses

// Profile Responses

data class ProfileAccountResponse(
    var id: Long,
    var name: String?,
    var date: String,
)
data class ProfileAchievementsResponse(
    var achievement: AchievementResponse,
    var amount: Int
)
data class ProfileStatisticsResponse(
    var gameLogsCount: Int,
    var achievementsList: List<ProfileAchievementsResponse>,
    var points: Int
)
data class ProfilePreferencesResponse(
    var game: ProfileFavoriteGameResponse?,
    var theme: String,
    var genre: String,
    var platform: String,
    var collection: String,
    var developer: String,
    var gameMode: String
)
data class ProfileFavoriteGameResponse(
    var name: String,
    var cover: String,
    var background: String
)

// IGDB API Responses

data class IgdbGameResponse(
    val id: Long,
    val name: String,
    val slug: String,
    val summary: String?,
    val coverUrl: String?,
    val artworks: List<Map<String, Any?>>,
    val screenshots: List<Map<String, Any?>>,
    val rating: Float?,
    val ageRatings: List<Map<String, Any?>>,
    val firstReleaseDate: Int?,
    val videos: List<Map<String, Any?>>,
    val collections: List<Map<String, Any?>>,
    val companies: List<Map<String, Any?>>,
    val gameModes: List<Map<String, Any?>>,
    val genres: List<Int>,
    val platforms: List<Int>,
    val themes: List<Int>
)