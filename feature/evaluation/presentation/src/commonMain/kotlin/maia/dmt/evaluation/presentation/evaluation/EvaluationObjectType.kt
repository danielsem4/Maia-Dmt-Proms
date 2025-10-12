package maia.dmt.evaluation.presentation.evaluation

enum class EvaluationObjectType(val type: Int) {

    INPUT(1),
    RADIO(2),
    DROPDOWN(3),
    CHECKBOX(4),
    TOGGLE(5),
    BUTTON(6),
    FREE_TEXT(7),
    NUMBER(8),
    DEDUCTION(9),
    SENTENCE(10),
    INSTRUCTION(11),
    WORD(12),
    MATH(13),
    PHOTOGRAPHY(14),
    CAMERA(15),
    GPS(16),
    ACCELEROMETER(17),
    GAMIFICATION_DATA(18),
    GAMIFICATION_IMAGE(19),
    GAMIFICATION_VIDEO(20),
    DRAW(21),
    SHAPE(22),
    VOICE(23),
    VOICE2TEXT(24),
    VIDEO(25),
    AUDIO(26),
    IMAGE(27),
    LIST(28),
    LABEL(29),
    DATA_FILE(30),
    SCALE(34),
    BODY(35);

    companion object {
        fun fromInt(value: Int): EvaluationObjectType? = entries.find { it.type == value }
    }
}