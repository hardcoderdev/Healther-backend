package hardcoder.dev.healtherbackend.data.entities.advices

import kotlinx.serialization.Serializable

@Serializable
data class Page(
    val id: Int,
    val adviceId: Int,
    val title: String?,
    val contentText: String,
    val imagePath: String
)
