package maia.dmt.evaluation.domain.model

enum class Frequency {
    ONCE,
    DAILY,
    WEEKLY;

    companion object {
        fun fromString(value: String): Frequency {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: ONCE
        }
    }
}
