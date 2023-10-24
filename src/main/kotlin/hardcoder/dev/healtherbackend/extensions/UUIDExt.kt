package hardcoder.dev.healtherbackend.extensions

import java.util.UUID

fun randomUUID(): String {
    return UUID.randomUUID().toString()
        .replace("[^a-zA-Z]".toRegex(), "")
}