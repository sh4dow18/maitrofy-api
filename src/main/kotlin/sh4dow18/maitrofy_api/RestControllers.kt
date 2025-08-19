package sh4dow18.maitrofy_api
// Rest Controllers Requirements
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
/*
* Important Tags
* - PreAuthorize tag is used to check if the request has a JWT
* */
// Theme Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.themes}")
class ThemeRestController(private val themeService: ThemeService) {
    @GetMapping
    @ResponseBody
    fun findAll() = themeService.findAll()
    @PreAuthorize("isAuthenticated()")
    @PostMapping("all")
    @ResponseBody
    fun insertAllFromIGDB() = themeService.insertAllFromIGDB()
}
// Genre Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.genres}")
class GenreRestController(private val genreService: GenreService) {
    @GetMapping
    @ResponseBody
    fun findAll() = genreService.findAll()
    @PreAuthorize("isAuthenticated()")
    @PostMapping("all")
    @ResponseBody
    fun insertAllFromIGDB() = genreService.insertAllFromIGDB()
}
// Platform Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.platforms}")
class PlatformRestController(private val platformService: PlatformService) {
    @GetMapping
    @ResponseBody
    fun findAll() = platformService.findAll()
    @PreAuthorize("isAuthenticated()")
    @PostMapping("all")
    @ResponseBody
    fun insertAllFromIGDB() = platformService.insertAllFromIGDB()
}
// Game Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.games}")
class GameRestController(private val gameService: GameService) {
    @GetMapping
    @ResponseBody
    fun findTop100() = gameService.findTop100()
    @GetMapping("search")
    @ResponseBody
    fun findBySearch(
        @RequestParam("name") name: String?, @RequestParam("themeId") themeId: Long?,
        @RequestParam("genreId") genreId: Long?, @RequestParam("platformId") platformId: Long?,
    ) = gameService.findBySearch(name, themeId, genreId, platformId)
    @GetMapping("recommendations/{id}")
    @ResponseBody
    fun findRecommendationsById(@PathVariable("id") id: String) = gameService.findRecommendationsById(id)
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable("id") id: String) = gameService.findById(id)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("{id}")
    @ResponseBody
    fun insert(@PathVariable("id") id: String) = gameService.insert(id)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("all")
    @ResponseBody
    fun insertTop5000() = gameService.insertTop5000ByRatingFromIGDB()
}
// Privilege Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.privileges}")
class PrivilegeRestController(private val privilegeService: PrivilegeService) {
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseBody
    fun findAll() = privilegeService.findAll()
    @PreAuthorize("isAuthenticated()")
    @PostMapping("all")
    @ResponseBody
    fun insertAllNeeded() = privilegeService.insertAllNeeded()
}
// Role Rest Controller
@Suppress("unused")
@RestController
@RequestMapping("\${endpoint.roles}")
class RoleRestController(private val roleService: RoleService) {
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseBody
    fun findAll() = roleService.findAll()
    @PreAuthorize("isAuthenticated()")
    @PostMapping("all")
    @ResponseBody
    fun insertAllNeeded() = roleService.insertAllNeeded()
}