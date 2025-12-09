package maia.dmt.cdt.presentation.util

import dmtproms.cogtest.cdt.presentation.generated.resources.Res
import dmtproms.cogtest.cdt.presentation.generated.resources.missions_v_1
import dmtproms.cogtest.cdt.presentation.generated.resources.missions_v_2
import dmtproms.cogtest.cdt.presentation.generated.resources.missions_v_3
import dmtproms.cogtest.cdt.presentation.generated.resources.missions_v_4
import dmtproms.cogtest.cdt.presentation.generated.resources.missions_v_5
import org.jetbrains.compose.resources.getStringArray

class ClockMissionsProvider {

    suspend fun getAllMissions(): Map<Int, List<String>> {
        return mapOf(
            1 to getStringArray(Res.array.missions_v_1),
            2 to getStringArray(Res.array.missions_v_2),
            3 to getStringArray(Res.array.missions_v_3),
            4 to getStringArray(Res.array.missions_v_4),
            5 to getStringArray(Res.array.missions_v_5)
        )
    }
}