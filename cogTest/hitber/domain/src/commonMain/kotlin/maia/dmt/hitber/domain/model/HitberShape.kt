package maia.dmt.hitber.domain.model

enum class HitberShape(val registryKey: String) {
    PENTAGON("pentagon"),
    HEXAGON("hexagon"),
    X("x"),
    V("v"),
    HASH("hash"),
    STAR("star"),
    TRAPEZOID("trapezoid"),
    RHOMBUS("diamond"),
    ELLIPSE("ellipse"),
    TRIANGLE("triangle");

    companion object {
        val pairs = listOf(
            listOf(PENTAGON, HEXAGON),
            listOf(X, V),
            listOf(HASH, STAR),
            listOf(TRAPEZOID, RHOMBUS),
            listOf(ELLIPSE, TRIANGLE)
        )
    }
}