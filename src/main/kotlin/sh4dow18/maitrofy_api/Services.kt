package sh4dow18.maitrofy_api
// Services Requirements
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
// Theme Service Interface where the functions to be used in
// Spring Abstract Theme Service are declared
interface ThemeService {
    fun findAll(): List<ThemeResponse>
}
// Spring Abstract Theme Service
@Service
class AbstractThemeService(
    // Theme Service Props
    @Autowired
    val themeRepository: ThemeRepository,
    @Autowired
    val themeMapper: ThemeMapper
): ThemeService {
    // Finds all Themes and returns them as Themes Replies
    override fun findAll(): List<ThemeResponse> {
        return themeMapper.themesListToThemeResponsesList(themeRepository.findAll())
    }
}