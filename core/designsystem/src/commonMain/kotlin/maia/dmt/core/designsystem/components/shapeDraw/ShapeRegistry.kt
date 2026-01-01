package maia.dmt.core.designsystem.components.shapeDraw


object ShapeRegistry {
    private val drawers = mutableMapOf<String, ShapeDrawer>()

    init {
        // Register default shapes
        register("square", SquareDrawer())
        register("circle", CircleDrawer())
        register("triangle", TriangleDrawer())
        register("diamond", DiamondDrawer())
        register("star", StarDrawer())
        register("asterisk", AsteriskDrawer())
    }

    fun register(key: String, drawer: ShapeDrawer) {
        drawers[key.lowercase()] = drawer
    }

    fun get(key: String): ShapeDrawer? = drawers[key.lowercase()]
}