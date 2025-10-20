package maia.dmt.core.data.util

import kotlinx.serialization.json.Json

val defaultJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    isLenient = true
}

inline fun <reified T> encodeValueToJson(value: T, json: Json = defaultJson): String {
    return json.encodeToString(value)
}