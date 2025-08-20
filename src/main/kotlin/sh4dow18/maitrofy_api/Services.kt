package sh4dow18.maitrofy_api
// Services Requirements
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.text.Normalizer
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt
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
                throw Exception("Error al obtener el Nombre del Género con el id '${id}'")
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
// Platform Service Interface where the functions to be used in
// Spring Abstract Platform Service are declared
interface PlatformService {
    fun findAll(): List<PlatformResponse>
    fun insertAllFromIGDB(): List<PlatformResponse>
}
// Spring Abstract Platform Service
@Suppress("unused")
@Service
class AbstractPlatformService(
    // Platform Service Props
    @Autowired
    val platformRepository: PlatformRepository,
    @Autowired
    val platformMapper: PlatformMapper,
    @Autowired
    val igdbService: IGDBService,
    @Autowired
    val translateService: TranslateService
): PlatformService {
    // Find all Platform and returns them as Platform Responses
    override fun findAll(): List<PlatformResponse> {
        return platformMapper.platformsListToPlatformResponsesList(platformRepository.findAllByOrderByNameAsc())
    }
    // Insert all Platform from IGDB APi into database
    override fun insertAllFromIGDB(): List<PlatformResponse> {
        // Set the Platform query to IGDB API
        val query = """
            fields id, name;
            limit 500;
        """.trimIndent()
        // Find all Platforms from IGDB APi
        val platformFromIGDBList = igdbService.findAllObjects(query, "platforms")
        // Creates a new Platforms list
        val newPlatformsList = platformFromIGDBList.map {
            // Get values from props
            val id = (it["id"] as Number).toLong()
            val name = it["name"] as? String ?: ""
            // If the original name is "", throw a new Error
            if (name == "") {
                throw Exception("Error al obtener el Nombre de la Plataforma con el id '${id}'")
            }
            // Create a new Platform with the information sent
            val platform = Platform(
                id = id,
                name = name,
                gamesList = emptySet(),
                gameLogsList = emptyList()
            )
            // Save the new Platform
            platformRepository.save(platform)
        }
        // Return Platforms list as Platform responses list
        return platformMapper.platformsListToPlatformResponsesList(newPlatformsList)
    }
}
// Game Service Interface where the functions to be used in
// Spring Abstract Game Service are declared
interface GameService {
    fun findTop100(): List<MinimalGameResponse>
    fun findBySearch(name: String?, themeId: Long?, genreId: Long?, platformId: Long?): List<MinimalGameResponse>
    fun findRecommendationsById(id: String): List<MinimalGameResponse>
    fun findById(id: String): GameResponse
    fun insertTop5000ByRatingFromIGDB(): String
    fun insert(id: String): GameResponse
}
// Spring Abstract Game Service
@Suppress("unused")
@Service
class AbstractGameService(
    // Game Service Props
    @Autowired
    val gameRepository: GameRepository,
    @Autowired
    val gameMapper: GameMapper,
    @Autowired
    val themeRepository: ThemeRepository,
    @Autowired
    val genreRepository: GenreRepository,
    @Autowired
    val platformRepository: PlatformRepository,
    @Autowired
    val igdbService: IGDBService,
    @Autowired
    val translateService: TranslateService
): GameService {
    // Set util maps
    private val pegiMap = mapOf(1 to "3+", 2 to "7+", 3 to "12+", 4 to "16+", 5 to "18+")
    private val gameModesMap = mapOf("Single player" to "1 Jugador", "Multiplayer" to "2+ Jugadores")
    // Find top 100 Games and returns them as Game Responses
    override fun findTop100(): List<MinimalGameResponse> {
        return gameMapper.gamesListToMinimalGameResponsesList(gameRepository.findTop100ByOrderByRatingDesc())
    }
    // Find Top 100 Games that Contains the information name sent
    override fun findBySearch(name: String?, themeId: Long?, genreId: Long?, platformId: Long?): List<MinimalGameResponse> {
        // Get Games list from database
        val gamesList = if (name == null) {
            // If name was not sent, find by theme, genre and platform ids
            gameRepository.findByThemeAndGenreAndPlatform(themeId, genreId, platformId)
        } else {
            // If name was sent, find by name and theme, genre and platform ids
            gameRepository.findByNameAndThemeAndGenreAndPlatform(name, themeId, genreId, platformId)
        }
        // Return games list as Minimal Games Responses
        return gameMapper.gamesListToMinimalGameResponsesList(gamesList.take(100))
    }
    override fun findRecommendationsById(id: String): List<MinimalGameResponse> {
        // Find Game in Database, if not exists, throw an error
        val game = gameRepository.findById(id).orElseThrow {
            NoSuchElementExists(id, "Juego")
        }
        // Create Recommendations List
        val recommendationsList: MutableList<Game> = mutableListOf()
        // If the game has a collection, add the games with the same collection
        if (game.collection != null) {
            recommendationsList.addAll(gameRepository.findByCollectionIgnoreCaseAndSlugNot(game.collection!!, game.slug))
        }
        // If the game has themes, add the games with the same first theme
        if (game.genresList.isNotEmpty()) {
            recommendationsList.addAll(gameRepository.findByTheme(game.genresList.toList()[0].id, game.slug).take(15))
        }
        // If the game has genres, add the games with the same first genre
        if (game.themesList.isNotEmpty()) {
            recommendationsList.addAll(gameRepository.findByGenre(game.themesList.toList()[0].id, game.slug).take(15))
        }
        // Remove duplicate games in recommendations list
        val distinctRecommendations = recommendationsList.distinctBy { it.slug }
        // Get Top 15 games
        val top15Recommendations = distinctRecommendations.take(15)
        // Return Top 15 Game Recommendations as Game Responses
        return gameMapper.gamesListToMinimalGameResponsesList(top15Recommendations)
    }
    override fun findById(id: String): GameResponse {
        // Find Game in Database, if not exists, throw an error
        val game = gameRepository.findById(id).orElseThrow {
            NoSuchElementExists(id, "Juego")
        }
        // Return the game as Game Response
        return gameMapper.gameToGameResponse(game)
    }
    // Insert Top 5000 Games by Rating from IGDB APi into database
    override fun insertTop5000ByRatingFromIGDB(): String {
        // Set for total param and limit per request
        val limit = 50
        val total = 5000
        // Set real game counter
        var counter = 0
        // For to get the total games set
        for (offset in 0 until total step limit) {
            // Set the Game query to IGDB API
            val query = """
                fields name,summary,cover.url,
                       artworks.url,artworks.width,artworks.height,artworks.alpha_channel,
                       screenshots.url,screenshots.width,screenshots.height,
                       rating,age_ratings.rating,age_ratings.category,
                       first_release_date,slug,videos.video_id,
                       genres,platforms,themes,collections.name,
                       involved_companies.company.name,
                       involved_companies.developer,
                       game_modes.name;
                where rating != null & rating_count >= 100;
                sort rating desc;
                limit $limit;
                offset $offset;
            """.trimIndent()
            // Find Query Games from IGDB APi
            val gamesList = igdbService.findAllObjects(query, "games")
            // Insert all games in query
            gamesList.map { rawGame ->
                // Get IGDB Game from Raw JSON Game
                val igdbGame = rawGame.toIgdbGame()
                // Transform IGDB Game to Game Entity
                val newGame = igdbGameToGame(igdbGame)
                // Save new game
                gameRepository.save(newGame)
                counter++
            }
        }
        return "Se han guardado un total de $counter juegos"
    }
    override fun insert(id: String): GameResponse {
        // Get IGDB Game by Slug
        val igdbGame = igdbService.findGameById(id)
        // Transform IGDB Game to Game Entity
        val newGame = igdbGameToGame(igdbGame)
        // Save new game
        return gameMapper.gameToGameResponse(gameRepository.save(newGame))
    }
    // Transform a IGDB Game to Game Entity
    private fun igdbGameToGame(game: IgdbGameResponse): Game {
        // Replace all line breaks to spaces to get only 1 paragraph, also, translate it to spanish
        val translatedSummary = game.summary?.replace(Regex("\\n+"), " ")?.let {
            translateService.translateText(if(it.length > 1997) it.substring(0, 1997) + "..." else it)
        }
        // Get PEGI Classification from game (has id "2")
        val classification = game.ageRatings.find {
            (it["category"] as? Number)?.toInt() == 2
        }?.get("rating") as? Number
        // Get PEGI Classification as String
        val classificationStr = classification?.let { pegiMap[it.toInt()] }
        // Get background from artworks, but if is null, set the best screenshot
        val background = bestArtwork(game.artworks) ?: bestArtwork(game.screenshots)
        // Create a new Game
        return Game(
            slug = game.slug,
            name = game.name,
            summary = translatedSummary ?: "",
            cover = game.coverUrl?.let(::extractFilename) ?: "",
            background = background ?: "",
            rating = (((game.rating ?: 0f) / 10f) * 10).roundToInt() / 10f,
            classification = classificationStr,
            year = timestampToYear(game.firstReleaseDate) ?: 0,
            video = game.videos.firstOrNull()?.getTyped("video_id") ?: "",
            collection = game.collections.firstOrNull()?.getTyped("name"),
            developer = game.companies
                .firstOrNull { it["developer"] as? Boolean == true }
                ?.getTyped<Map<String, Any?>>("company")
                ?.getTyped("name")
                ?: "",
            gameMode = game.gameModes.firstOrNull()
                ?.getTyped<String>("name")
                ?.let { gameModesMap[it] } ?: "",
            themesList = themeRepository.findAllById(game.themes.map { it.toLong() }).toSet(),
            genresList = genreRepository.findAllById(game.genres.map { it.toLong() }).toSet(),
            platformsList = platformRepository.findAllById(game.platforms.map { it.toLong() }).toSet(),
            gameLogsList = emptyList()
        )
    }
    // Extract File name from URL
    private fun extractFilename(url: String): String {
        val parts = url.split("/")
        return parts.last()
    }
    // Get the best artwork to use it as background image
    private fun bestArtwork(list: List<Map<String, Any?>>?): String? {
        // If the list is null or empty
        if (list.isNullOrEmpty()) {
            return null
        }
        // Get the horizontal images to be sure that the background image is large
        val horizontalImages = list.filter { image ->
            // Remove transparent images
            val alphaChannel = image["alpha_channel"] as? Boolean ?: false
            // Get width and height
            val width = image["width"] as? Int ?: 0
            val height = image["height"] as? Int ?: 0
            // Get the images without transparency and that are horizontal rectangles
            !alphaChannel && width > height
        }
        // From horizontal images, get the largest image using max by or null
        val largestImage = horizontalImages.maxByOrNull { image ->
            // Get width and height
            val width = image["width"] as? Int ?: 0
            val height = image["height"] as? Int ?: 0
            // Set the multiplication between the height and the width
            width * height
        }
        // Get the url from the largest image
        val url = largestImage?.get("url") as? String ?: return null
        // Returns only the file name from the URL
        return extractFilename(url)
    }
    // Timestamp date to Int function
    private fun timestampToYear(timestamp: Number?): Int? {
        // If timestamp is null, return null
        if (timestamp == null) {
            return null
        }
        // Get the timestamp as milliseconds, because kotlin manage timestamps in milliseconds
        val milliseconds = timestamp.toLong() * 1000
        // Get timestamp as date
        val date = Date(milliseconds)
        // Creates a calendar to get the year from date
        val calendar = Calendar.getInstance()
        calendar.time = date
        // Return Year
        return calendar.get(Calendar.YEAR)
    }
}
// Privilege Service Interface where the functions to be used in
// Spring Abstract Privilege Service are declared
interface PrivilegeService {
    fun findAll(): List<PrivilegeResponse>
    fun insertAllNeeded(): List<PrivilegeResponse>
}
// Spring Abstract Privilege Service
@Suppress("unused")
@Service
class AbstractPrivilegeService(
    // Privilege Service Props
    @Autowired
    val privilegeRepository: PrivilegeRepository,
    @Autowired
    val privilegeMapper: PrivilegeMapper
): PrivilegeService {
    override fun findAll(): List<PrivilegeResponse> {
        // Return all privileges as Privileges Response ordered by slug
        return privilegeMapper.privilegeListToPrivilegeResponsesList(privilegeRepository.findAllByOrderBySlugAsc())
    }
    override fun insertAllNeeded(): List<PrivilegeResponse> {
        // Needed Privileges Request List
        val privilegeRequestList = listOf(
            PrivilegeRequest("Insertar Temas", "Permite insertar temas en el sistema"),
            PrivilegeRequest("Insertar Géneros", "Permite insertar géneros en el sistema"),
            PrivilegeRequest("Insertar Plataformas", "Permite insertar plataformas en el sistema"),
            PrivilegeRequest("Insertar Top Juegos", "Permite insertar top de juegos por Rating"),
            PrivilegeRequest("Insertar Juego", "Permite insertar un juego"),
            PrivilegeRequest("Ver Privilegios", "Permite obtener los privilegios del sistema"),
            PrivilegeRequest("Insertar Privilegios", "Permite insertar privilegios en el sistema"),
            PrivilegeRequest("Ver Roles", "Permite obtener los roles del sistema"),
            PrivilegeRequest("Insertar Roles", "Permite insertar roles en el sistema"),
            PrivilegeRequest("Insertar Administrador Principal", "Permite insertar el usuario administrador principal en el sistema"),
        )
        // Create a new privileges list with privileges request
        val privilegesList = privilegeRequestList.map {
            // Create a Slug from name
            val slug = toSlug(it.name)
            // Create a new Privilege
            val newPrivilege = privilegeMapper.privilegeRequestToPrivilege(slug, it)
            // Save it into database
            privilegeRepository.save(newPrivilege)
        }
        // Return the privileges list as privilege responses list
        return privilegeMapper.privilegeListToPrivilegeResponsesList(privilegesList)
    }
    // Function that allow to transform a text into slug
    private fun toSlug(text: String): String {
        // Return String as Slug Form
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            // Remove Accents
            .replace("\\p{M}+".toRegex(), "")
            // Put in Lowercase
            .lowercase()
            // Replace all chars that are not letters or numbers
            .replace("\\W+".toRegex(), "-")
            // Remove "-" from start and end
            .trim('-')
    }
}
// Role Service Interface where the functions to be used in
// Spring Abstract Role Service are declared
interface RoleService {
    fun findAll(): List<RoleResponse>
    fun insertAllNeeded(): List<RoleResponse>
}
// Spring Abstract Role Service
@Suppress("unused")
@Service
class AbstractRoleService(
    // Role Service Props
    @Autowired
    val roleRepository: RoleRepository,
    @Autowired
    val roleMapper: RoleMapper,
    @Autowired
    val privilegeRepository: PrivilegeRepository
): RoleService {
    override fun findAll(): List<RoleResponse> {
        // Return all Roles as Role Responses ordered by slug
        return roleMapper.rolesListToRoleResponsesList(roleRepository.findAllByOrderByIdAsc())
    }
    override fun insertAllNeeded(): List<RoleResponse> {
        // Needed Roles Request List
        val roleRequestsList = listOf(
            RoleRequest("Administrador Principal", "Rol que pertenece a los administradores del sistema", listOf("insertar-generos", "insertar-juego", "insertar-plataformas", "insertar-privilegios", "insertar-temas", "insertar-top-juegos", "ver-privilegios", "ver-roles", "insertar-roles", "insertar-administrador-principal")),
            RoleRequest("Administrador", "Rol que pertenece a los administradores del sistema", listOf("insertar-generos", "insertar-juego", "insertar-plataformas", "insertar-privilegios", "insertar-temas", "insertar-top-juegos", "ver-privilegios", "ver-roles", "insertar-roles", "insertar-administrador-principal")),
            RoleRequest("Jugador", "Rol que pertenece a los jugadores que usan el sistema para tener su registro de juego", listOf("insertar-juego"))
        )
        // Create a new Roles list with Role requests
        val rolesList = roleRequestsList.map {
            // Find the necessary privileges, if someone are not found, throw a new exception
            val privilegesList = it.privilegesList.map { privilegeSlug ->
                val privilege = privilegeRepository.findById(privilegeSlug).orElseThrow {
                    NoSuchElementExists(privilegeSlug, "Privilegio")
                }
                privilege
            }.toSet()
            // Check if the roles already exists
            val role = roleRepository.findByNameIgnoringCase(it.name).orElse(null)
            // If already exists, throw a new error
            if (role == null) {
                // Create a new Roles
                val newRole = roleMapper.roleRequestToRole(it)
                newRole.privilegesList.addAll(privilegesList)
                // Save it into database
                roleRepository.save(newRole)
            }
            else {
                // Update existing role
                role.name = it.name
                role.description = it.description
                role.privilegesList.clear()
                role.privilegesList.addAll(privilegesList)
                roleRepository.save(role)
            }
        }
        // Return the Roless list as Roles responses list
        return roleMapper.rolesListToRoleResponsesList(rolesList)
    }
}
// User Service Interface where the functions to be used in
// Spring Abstract User Service are declared
interface UserService {
    fun findById(id: Long): UserResponse
    fun insertMainAdmin(): UserResponse
    fun insert(userRequest: UserRequest): UserResponse
}
// Spring Abstract User Service
@Suppress("unused")
@Service
class AbstractUserService(
    // User Service Props
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val userMapper: UserMapper,
    @Autowired
    val roleRepository: RoleRepository,
    @Value("\${main.user.email}")
    private val mainUserEmail: String,
    @Value("\${main.user.password}")
    private val mainUserPassword: String,
): UserService {
    override fun findById(id: Long): UserResponse {
        // Check if the user already exists, if not, throw a new error
        val user = userRepository.findById(id).orElseThrow {
            NoSuchElementExists("$id", "Usuario")
        }
        // Return user as User Response
        return userMapper.userToUserResponse(user)
    }
    override fun insertMainAdmin(): UserResponse {
        // Main User Request
        val mainUser = UserRequest(mainUserEmail,"Administrador Principal", passwordEncoder(mainUserPassword))
        // Check if already exists the main user
        val user = userRepository.findByEmail(mainUserEmail).orElse(null)
        if (user != null) {
            throw ElementAlreadyExists(mainUserEmail, "Administrador Principal")
        }
        // Find the necessary Role, if not found, throw a new exception
        val role = roleRepository.findByNameIgnoringCase("Administrador Principal").orElseThrow {
            NoSuchElementExists("Administrador Principal", "Rol")
        }
        // Create a new User
        val newUser = userMapper.userRequestToUser(mainUser, role)
        // Return the User as User response
        return userMapper.userToUserResponse(userRepository.save(newUser))
    }
    override fun insert(userRequest: UserRequest): UserResponse {
        // Find the necessary Role, if not found, throw a new exception
        val role = roleRepository.findByNameIgnoringCase("Jugador").orElseThrow {
            NoSuchElementExists("Jugador", "Rol")
        }
        // Check if the user already exist
        val user = userRepository.findByEmail(userRequest.email).orElse(null)
        if (user != null) {
            throw ElementAlreadyExists(userRequest.email, "Usuario")
        }
        // Encrypt Password
        userRequest.password = passwordEncoder(userRequest.password)
        // Create a new User
        val newUser = userMapper.userRequestToUser(userRequest, role)
        // Return the User as User response
        return userMapper.userToUserResponse(userRepository.save(newUser))
    }
    // Encode Passwords
    private fun passwordEncoder(password: String): String {
        return BCryptPasswordEncoder().encode(password)
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
    fun findGameById(slug: String): IgdbGameResponse
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
    // Find Game by id from IGDB API
    override fun findGameById(slug: String): IgdbGameResponse {
        // Set the Game query to IGDB API
        val query = """
                fields name,summary,cover.url,
                       artworks.url,artworks.width,artworks.height,artworks.alpha_channel,
                       screenshots.url,screenshots.width,screenshots.height,
                       rating,age_ratings.rating,age_ratings.category,
                       first_release_date,slug,videos.video_id,
                       genres,platforms,themes,collections.name,
                       involved_companies.company.name,
                       involved_companies.developer,
                       game_modes.name;
                where slug = "$slug";
            """.trimIndent()
        // Get Game Response from IGDB API
        val response = webClient.post()
            // Set the endpoint destination
            .uri("games")
            // Set thw query as the request body
            .bodyValue(query)
            // Send the request
            .retrieve()
            // Sets the response to be a generic object
            .bodyToMono(object : ParameterizedTypeReference<List<Map<String, Any>>>() {})
            // Lock thread until information is obtained
            // If error or null, set as Empty list
            .block() ?: emptyList()
        // Return Response as IGDB Game
        return response[0].toIgdbGame()
    }
}
@Suppress("unused")
// AppUserDetailsService Service Class
// Spring Abstract App User Details Service
@Service
// Tag that establishes that is a Transactional Service. This one makes a transaction when
// this service is in operation.
@Transactional
class AppUserDetailsService(
    // App User Detail sService Props
    @Autowired
    val userRepository: UserRepository,
) : UserDetailsService {
    // Tag that allows to throw a Username Not Found Exception
    @Throws(UsernameNotFoundException::class)
    // Function that is used to the user details during the authentication
    override fun loadUserByUsername(email: String): UserDetails {
        val user: User = userRepository.findByEmail(email).orElse(null)
            ?: return org.springframework.security.core.userdetails.User(
                "Error", "Error", false, false, false,
                false, emptyList()
            )
        // Returns a Spring Security "User" with the "User" information found
        return org.springframework.security.core.userdetails.User(
            user.id.toString(), user.password, user.enabled, true, true,
            true, getAuthorities(user.role)
        )
    }
    // Function that get the privileges of the user as authorities
    private fun getAuthorities(role: Role): Collection<GrantedAuthority> {
        return role.privilegesList.map { privilege -> SimpleGrantedAuthority(privilege.slug) }
    }
}