package hardcoder.dev.healtherbackend.extensions

import io.ktor.http.content.*
import java.io.File
import java.util.UUID

fun PartData.FileItem.save(path: String): String {
    val fileBytes = streamProvider().readBytes()
    val fileExtension = originalFileName?.takeLastWhile { it != '.' }
    val fileName = "${UUID.randomUUID()}.$fileExtension"
    val folder = File(path)
    folder.mkdir()
    File("$path/$fileName").writeBytes(fileBytes)
    return fileName
}