package hardcoder.dev.healtherbackend.routing.advices.requests

data class AdviceMultipartRequest(
    val title: String,
    val contentText: String,
    val contentColorHex: String,
    val contentBackgroundColorHex: String,
    val imageFileName: String?
)
