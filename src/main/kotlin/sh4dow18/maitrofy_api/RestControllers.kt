package sh4dow18.maitrofy_api
// Rest Controllers Requirements
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
// Theme Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.themes}")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:3001", "\${ip.domain}"])
class ThemeRestController(private val themeService: ThemeService) {
    @GetMapping
    @ResponseBody
    fun findAll() = themeService.findAll()
    @PostMapping("all")
    @ResponseBody
    fun insertAllFromIGDB() = themeService.insertAllFromIGDB()
}
// Genre Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.genres}")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:3001", "\${ip.domain}"])
class GenreRestController(private val genreService: GenreService) {
    @GetMapping
    @ResponseBody
    fun findAll() = genreService.findAll()
    @PostMapping("all")
    @ResponseBody
    fun insertAllFromIGDB() = genreService.insertAllFromIGDB()
}
// Platform Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.platforms}")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:3001", "\${ip.domain}"])
class PlatformRestController(private val platformService: PlatformService) {
    @GetMapping
    @ResponseBody
    fun findAll() = platformService.findAll()
    @PostMapping("all")
    @ResponseBody
    fun insertAllFromIGDB() = platformService.insertAllFromIGDB()
}
// Game Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.games}")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:3001", "\${ip.domain}"])
class GameRestController(private val gameService: GameService) {
    @GetMapping
    @ResponseBody
    fun findTop100() = gameService.findTop100()
    @PostMapping("all")
    @ResponseBody
    fun insertTop5000() = gameService.insertTop5000ByRatingFromIGDB()
}