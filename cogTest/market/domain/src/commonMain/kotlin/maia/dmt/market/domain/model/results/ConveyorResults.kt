package maia.dmt.market.domain.model.results

data class ConveyorResults(
    val selectedGroceries: Set<String> = emptySet(),
    val correctClicks: Int = 0,
    val wrongClicks: Int = 0,
    val timeElapsed: Int = 0,
    val completedAllGroceries: Boolean = false
)
