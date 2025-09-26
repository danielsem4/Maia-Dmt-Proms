package maia.dmt.proms

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform