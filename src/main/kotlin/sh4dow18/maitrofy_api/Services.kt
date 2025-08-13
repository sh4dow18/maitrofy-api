package sh4dow18.maitrofy_api
// Services Requirements
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
// Theme Service Interface where the functions to be used in
// Spring Abstract Theme Service are declared
interface ThemeService {
    fun findAll(): List<ThemeResponse>
    fun insertAllFromIGDB(): List<ThemeResponse>
}
// Spring Abstract Theme Service
@Suppress("unused")
@Service
class AbstractThemeService(
    // Theme Service Props
    @Autowired
    val themeRepository: ThemeRepository,
    @Autowired
    val themeMapper: ThemeMapper,
    @Autowired
    val igdbService: IGDBService,
    @Autowired
    val translateService: TranslateService
): ThemeService {
    // Find all Themes and returns them as Themes Responses
    override fun findAll(): List<ThemeResponse> {
        return themeMapper.themesListToThemeResponsesList(themeRepository.findAllByOrderByNameAsc())
    }
    // Insert all themes from IGDB APi into database
    override fun insertAllFromIGDB(): List<ThemeResponse> {
        // Set the theme query to IGDB API
        val query = """
            fields id, name;
            limit 500;
        """.trimIndent()
        // Find all themes from IGDB APi
        val themesFromIGDBList = igdbService.findAllObjects(query, "themes")
        // Creates a new themes list
        val newThemesList = themesFromIGDBList.map {
            // Get values from props
            val id = (it["id"] as Number).toLong()
            val originalName = it["name"] as? String ?: ""
            // If the original name is "", throw a new Error
            if (originalName == "") {
                throw Exception("Error al obtener el Nombre del Tema con el id '${id}'")
            }
            // Translate the original name into Spanish and capitalize the first character.
            val translatedName = translateService.translateText(originalName)?.lowercase()?.replaceFirstChar {
                char -> char.uppercase()
            }
            // Create a new Theme with the information sent
            val theme = Theme(
                id = id,
                name = translatedName ?: originalName,
                gamesList = emptySet()
            )
            // Save the new theme
            themeRepository.save(theme)
        }
        // Return themes list as themes responses list
        return themeMapper.themesListToThemeResponsesList(newThemesList)
    }
}
// Genre Service Interface where the functions to be used in
// Spring Abstract Genre Service are declared
interface GenreService {
    fun findAll(): List<GenreResponse>
    fun insertAllFromIGDB(): List<GenreResponse>
}
// Spring Abstract Genre Service
@Suppress("unused")
@Service
class AbstractGenreService(
    // Genre Service Props
    @Autowired
    val genreRepository: GenreRepository,
    @Autowired
    val genreMapper: GenreMapper,
    @Autowired
    val igdbService: IGDBService,
    @Autowired
    val translateService: TranslateService
): GenreService {
    // Find all Genres and returns them as Genre Responses
    override fun findAll(): List<GenreResponse> {
        return genreMapper.genresListToGenreResponsesList(genreRepository.findAllByOrderByNameAsc())
    }
    // Insert all Genres from IGDB APi into database
    override fun insertAllFromIGDB(): List<GenreResponse> {
        // Set the genre query to IGDB API
        val query = """
            fields id, name;
            limit 500;
        """.trimIndent()
        // Find all Genres from IGDB APi
        val genresFromIGDBList = igdbService.findAllObjects(query, "genres")
        // Creates a new Genres list
        val newGenresList = genresFromIGDBList.map {
            // Get values from props
            val id = (it["id"] as Number).toLong()
            val originalName = it["name"] as? String ?: ""
            // If the original name is "", throw a new Error
            if (originalName == "") {
                throw Exception("Error al obtener el Nombre del Tema con el id '${id}'")
            }
            // Translate the original name into Spanish and capitalize the first character.
            val translatedName = translateService.translateText(originalName)?.lowercase()?.replaceFirstChar {
                    char -> char.uppercase()
            }
            // Create a new Genre with the information sent
            val genre = Genre(
                id = id,
                name = translatedName ?: originalName,
                gamesList = emptySet()
            )
            // Save the new Genre
            genreRepository.save(genre)
        }
        // Return Genres list as Genres responses list
        return genreMapper.genresListToGenreResponsesList(newGenresList)
    }
}

// Utils Services

// Translate Service Interface where the functions to be used in
// Spring Abstract Translate Service are declared
interface TranslateService {
    fun translateText(text: String): String?
}
// Spring Abstract Translate Service
@Suppress("unused")
@Service
class AbstractTranslateService(
    @Autowired
    private val webClient: WebClient = WebClient.create(),
    @Autowired
    private val mapper: ObjectMapper = jacksonObjectMapper()
): TranslateService {
    override fun translateText(text: String): String? {
        val response = webClient.get()
            // Creates a new get request to Free Tranlation API with all info
            .uri { uriBuilder ->
                uriBuilder
                    .scheme("https")
                    .host("ftapi.pythonanywhere.com")
                    .path("/translate")
                    .queryParam("sl", "en")
                    .queryParam("dl", "es")
                    .queryParam("text", text)
                    .build()
            }
            // Add referer header
            .header("Referer", "https://ftapi.pythonanywhere.com/")
            // Send the request
            .retrieve()
            // Sets the response to be a string
            .bodyToMono(String::class.java)
            // Lock thread until information is obtained
            .block()
        // Return "destination-text" value, if it is null, return null
        return response?.let { mapper.readTree(it)["destination-text"]?.asText() }
    }
}
// IGDB API Service Interface where the functions to be used in
// Spring Abstract IGDB API Service are declared
interface IGDBService {
    fun findAllObjects(query: String, uri: String): List<Map<String, Any>>
}
// Spring Abstract IGDB API Service
@Suppress("unused")
@Service
class AbstractIGDBService(
    @Value("\${igdb.clientId}")
    private val clientId: String?,
    @Value("\${igdb.accessToken}")
    private val accessToken: String?,
): IGDBService {

    // Set a general web client to all functions of IGDB API
    private val webClient = WebClient.builder()
        .baseUrl("https://api.igdb.com/v4/")
        .defaultHeader("Client-ID", clientId)
        .defaultHeader("Authorization", "Bearer $accessToken")
        .build()
    // Find All Themes from IGDB API
    override fun findAllObjects(query: String, uri: String): List<Map<String, Any>> {
        // Return IGDB API Post Response
        return webClient.post()
            // Set the endpoint destination
            .uri(uri)
            // Set thw query as the request body
            .bodyValue(query)
            // Send the request
            .retrieve()
            // Sets the response to be a generic object
            .bodyToMono(object : ParameterizedTypeReference<List<Map<String, Any>>>() {})
            // Lock thread until information is obtained
            // If error or null, set as Empty list
            .block() ?: emptyList()
    }
}