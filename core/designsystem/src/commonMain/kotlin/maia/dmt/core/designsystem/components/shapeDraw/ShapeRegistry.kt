package maia.dmt.core.designsystem.components.shapeDraw


object ShapeRegistry {
    private val drawers = mutableMapOf<String, ShapeDrawer>()

    init {
        register("square", SquareDrawer())
        register("rectangle", RectangleDrawer())
        register("circle", CircleDrawer())
        register("filled_circle", FilledCircleDrawer())
        register("ellipse", EllipseDrawer())
        register("triangle", TriangleDrawer())

        register("pentagon", PentagonDrawer())
        register("hexagon", HexagonDrawer())
        register("octagon", OctagonDrawer())
        register("trapezoid", TrapezoidDrawer())
        register("diamond", DiamondDrawer())
        register("rhombus", DiamondDrawer())

        register("star", StarDrawer())
        register("asterisk", AsteriskDrawer())
        register("x", XDrawer())
        register("v", VDrawer())
        register("hash", HashDrawer())
        register("hashtag", HashDrawer())

        register("vertical_line", VerticalLineDrawer())
        register("horizontal_line", HorizontalLineDrawer())


        register("cone", ConeDrawer())
    }

    fun register(key: String, drawer: ShapeDrawer) {
        drawers[key.lowercase()] = drawer
    }

    fun get(key: String): ShapeDrawer? = drawers[key.lowercase()]
}