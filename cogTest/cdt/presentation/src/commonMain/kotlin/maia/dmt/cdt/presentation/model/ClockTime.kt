package maia.dmt.cdt.presentation.model

data class ClockTime(
    val hour: Int,
    val minute: Int
) {
    fun toFormattedString(): String {
        return String.format("%02d:%02d", hour, minute)
    }

    companion object {
        val DEFAULT = ClockTime(12, 0)
    }
}

fun String.Companion.format(string: String, hour: Int, minute: Int): String {
    return string.replace("%s", ClockTime(hour, minute).toFormattedString())
}
