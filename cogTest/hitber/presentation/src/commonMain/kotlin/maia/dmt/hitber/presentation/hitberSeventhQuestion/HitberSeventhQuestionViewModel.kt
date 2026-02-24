package maia.dmt.hitber.presentation.hitberSeventhQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.hitber.presentation.session.HitberSessionManager

class HitberSeventhQuestionViewModel(
    private val sessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberSeventhQuestionState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HitberSeventhQuestionState(),
    )

    private val eventChannel = Channel<HitberSeventhQuestionEvent>()
    val events = eventChannel.receiveAsFlow()

    private var itemsInitialized = false

    init {
        setupForVersion(sessionManager.sessionData.value.testVersion)
    }

    private fun setupForVersion(version: Int) {
        val targetItem: FridgeItemType
        val targetNapkin: NapkinColor
        val instructionUrl: String

        when (version) {
            1 -> {
                targetItem = FridgeItemType.GRAPE
                targetNapkin = NapkinColor.BLUE
                instructionUrl = URL_GRAPES_TO_BLUE
            }
            2 -> {
                targetItem = FridgeItemType.CHICKEN
                targetNapkin = NapkinColor.GREEN
                instructionUrl = URL_CHICKEN_TO_GREEN
            }
            3 -> {
                targetItem = FridgeItemType.CAN
                targetNapkin = NapkinColor.YELLOW
                instructionUrl = URL_CAN_TO_YELLOW
            }
            else -> {
                targetItem = FridgeItemType.MILK
                targetNapkin = NapkinColor.RED
                instructionUrl = URL_MILK_TO_RED
            }
        }

        val items = FridgeItemType.entries.mapIndexed { index, type ->
            FridgeItem(id = index, type = type, currentOffset = Offset.Zero, isInFridge = true)
        }

        _state.update {
            it.copy(
                targetItem = targetItem,
                targetNapkin = targetNapkin,
                instructionUrl = instructionUrl,
                items = items,
            )
        }
    }

    fun onAction(action: HitberSeventhQuestionAction) {
        when (action) {
            is HitberSeventhQuestionAction.OnFridgeClick -> onFridgeClick()
            is HitberSeventhQuestionAction.OnNapkinPositioned -> onNapkinPositioned(action.color, action.rect)
            is HitberSeventhQuestionAction.OnContainerPositioned -> onContainerPositioned(action.offset)
            is HitberSeventhQuestionAction.OnLayoutReady -> onLayoutReady(action.fridgeCenterX, action.screenHeightPx)
            is HitberSeventhQuestionAction.OnItemDrag -> onItemDrag(action.id, action.dragAmount)
            is HitberSeventhQuestionAction.OnItemDrop -> onItemDrop(action.id)
            is HitberSeventhQuestionAction.OnNextClick -> navigateNext()
        }
    }

    private fun onFridgeClick() {
        _state.update { it.copy(isFridgeOpen = !it.isFridgeOpen) }
    }

    private fun onNapkinPositioned(color: NapkinColor, rect: Rect) {
        _state.update { current ->
            current.copy(napkinBounds = current.napkinBounds + (color to rect))
        }
    }

    private fun onContainerPositioned(offset: Offset) {
        _state.update { it.copy(containerRootOffset = offset) }
    }

    private fun onLayoutReady(fridgeCenterX: Float, screenHeightPx: Float) {
        if (itemsInitialized) return
        itemsInitialized = true

        val spacingX = fridgeCenterX / 3.5f
        val spacingY = screenHeightPx * 0.22f
        val startX = fridgeCenterX * 0.05f
        val startY = screenHeightPx * 0.08f

        val items = FridgeItemType.entries.mapIndexed { index, type ->
            val col = index % 3
            val row = index / 3
            FridgeItem(
                id = index,
                type = type,
                currentOffset = Offset(
                    x = startX + col * spacingX,
                    y = startY + row * spacingY,
                ),
                isInFridge = true,
            )
        }
        _state.update { it.copy(items = items) }
    }

    private fun onItemDrag(id: Int, dragAmount: Offset) {
        _state.update { current ->
            val updatedItems = current.items.map { item ->
                if (item.id == id) {
                    item.copy(
                        currentOffset = item.currentOffset + dragAmount,
                        isInFridge = false,
                    )
                } else item
            }
            current.copy(items = updatedItems)
        }
    }

    private fun onItemDrop(id: Int) {
        val currentState = _state.value
        val item = currentState.items.find { it.id == id } ?: return
        if (item.type != currentState.targetItem) return
        val targetBounds = currentState.napkinBounds[currentState.targetNapkin] ?: return

        // Check if item top-left corner (in root coords) lands within the napkin bounds (with tolerance)
        val itemPositionInRoot = currentState.containerRootOffset + item.currentOffset
        if (targetBounds.inflate(DROP_TOLERANCE).contains(itemPositionInRoot)) {
            _state.update { it.copy(isCompleted = true) }
        }
    }

    private fun navigateNext() {
        viewModelScope.launch {
            eventChannel.send(HitberSeventhQuestionEvent.NavigateToNextScreen)
        }
    }

    companion object {
        private const val DROP_TOLERANCE = 60f

        private const val URL_MILK_TO_RED =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/Three%20Phase%20Instruction%20Versions%2Fcomprehension_milk_to_red_napkin.mp3?alt=media&token=feef793a-a83e-4b84-b9dc-96cfe9420a2d"
        private const val URL_GRAPES_TO_BLUE =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/Three%20Phase%20Instruction%20Versions%2Fcomprehension_grapes_to_blue_napkin.mp3?alt=media&token=fcd736eb-23be-4222-b4a0-888b4daeba34"
        private const val URL_CAN_TO_YELLOW =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/Three%20Phase%20Instruction%20Versions%2Fcomprehension_can_to_yellow_napkin.mp3?alt=media&token=f8b3502f-9a3c-4b19-abc1-e9c85db54124"
        private const val URL_CHICKEN_TO_GREEN = "" // Audio not yet available
    }
}
