package sh4dow18.maitrofy_api
// Inline to copy this function to every place where it is used
// Reified allows to know the generic type in execution time
// getTyped is an extension of Map that will know that "T" is "String" and will do "as? String" internally
// Returns the value safely cast to T or null
inline fun <reified T> Map<String, Any?>.getTyped(key: String): T? = this[key] as? T
// Inline to copy this function to every place where it is used
// Reified allows to know the generic type in execution time
// getTyped is an extension of Map that will know that "T" is "String" and will do "as? String" internally
// Returns a list that attempts to cast "List<*>" and then with "filterIsInstance<T>()" obtain only the elements that are of type "T".
// If can not cast, returns an empty list
inline fun <reified T> Map<String, Any?>.getList(key: String): List<T> = (this[key] as? List<*>)?.filterIsInstance<T>() ?: emptyList()
// Strong conversion from IGDG Response to IgdbGameResponse DTO
// If a key field is missing, throw an error so that an incomplete game is not created
fun Map<String, Any?>.toIgdbGame() = IgdbGameResponse(
    id = (getTyped<Number>("id") ?: error("Sin ID")).toLong(),
    name = getTyped<String>("name") ?: error("Juego sin nombre"),
    slug = getTyped<String>("slug") ?: error("Juego sin slug"),
    summary = getTyped("summary"),
    coverUrl = getTyped<Map<String, Any?>>("cover")?.getTyped("url"),
    artworks = getList("artworks"),
    screenshots = getList("screenshots"),
    rating = getTyped<Number>("rating")?.toFloat(),
    ageRatings = getList("age_ratings"),
    firstReleaseDate = getTyped("first_release_date"),
    videos = getList("videos"),
    collections = getList("collections"),
    companies = getList("involved_companies"),
    gameModes = getList("game_modes"),
    genres = getList("genres"),
    platforms = getList("platforms"),
    themes = getList("themes")
)