package hardcoder.dev.healtherbackend.repository.advices

import hardcoder.dev.healtherbackend.data.entities.advices.Advice

interface AdvicesRepository {
    suspend fun getAllAdvices(): List<Advice>
    suspend fun getAdviceById(id: Int): Advice?
    suspend fun createAdvice(
        title: String,
        contentText: String,
        contentColorHex: String,
        contentBackgroundColorHex: String,
        imagePath: String
    ): Advice?
    suspend fun updateAdvice(
        id: Int,
        title: String,
        contentText: String,
        contentColorHex: String,
        contentBackgroundColorHex: String,
        imagePath: String?
    ): Boolean
    suspend fun deleteAdviceById(id: Int): Boolean
}