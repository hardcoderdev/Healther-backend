package hardcoder.dev.healtherbackend.data.entities.advices

import kotlinx.serialization.Serializable

@Serializable
data class Advice(
    val id: Int,
    val title: String?,
    val contentText: String,
    val contentColorHex: String,
    val contentBackgroundColorHex: String,
    val imagePath: String
)
