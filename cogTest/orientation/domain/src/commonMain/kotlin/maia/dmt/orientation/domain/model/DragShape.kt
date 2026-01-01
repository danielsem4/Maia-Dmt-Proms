package maia.dmt.orientation.domain.model

enum class DragShape(val key: String) {
    SQUARE("square"),
    CIRCLE("circle"),
    TRIANGLE("triangle"),
    DIAMOND("diamond"),
    STAR("star"),
    ASTERISK("asterisk"),
    NONE("none");

    companion object {
        fun fromKey(key: String): DragShape =
            entries.find { it.key == key } ?: NONE
    }
}