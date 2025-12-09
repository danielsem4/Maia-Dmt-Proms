package maia.dmt.cdt.presentation.util

import dmtproms.cogtest.cdt.presentation.generated.resources.Res
import dmtproms.cogtest.cdt.presentation.generated.resources.circle_grades
import dmtproms.cogtest.cdt.presentation.generated.resources.hands_grades
import dmtproms.cogtest.cdt.presentation.generated.resources.numbers_grades
import org.jetbrains.compose.resources.getStringArray

class CdtGradeResourceProvider {

    suspend fun getCircleGrades(): List<String> {
        return getStringArray(Res.array.circle_grades)
    }

    suspend fun getNumbersGrades(): List<String> {
        return getStringArray(Res.array.numbers_grades)
    }

    suspend fun getHandsGrades(): List<String> {
        return getStringArray(Res.array.hands_grades)
    }
}