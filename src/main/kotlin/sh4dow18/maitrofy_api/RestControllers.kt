package sh4dow18.maitrofy_api
// Rest Controllers Requirements
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
// Theme Rest Controller
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