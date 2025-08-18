package sh4dow18.maitrofy_api

// Requests

data class LoginRequest(
    var email: String,
    var password: String,
)

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

// Minimal Responses

data class MinimalGameResponse(
    var slug: String,
    var name: String,
    var cover: String,
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