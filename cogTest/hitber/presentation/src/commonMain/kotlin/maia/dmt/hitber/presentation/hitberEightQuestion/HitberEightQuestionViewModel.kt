package maia.dmt.hitber.presentation.hitberEightQuestion

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
import maia.dmt.hitber.presentation.session.HitberQ8Result
import maia.dmt.hitber.presentation.session.HitberSessionManager

class HitberEightQuestionViewModel(
    private val sessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberEightQuestionState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HitberEightQuestionState(),
    )

    private val eventChannel = Channel<HitberEightQuestionEvent>()
    val events = eventChannel.receiveAsFlow()

    private var ballsInitialized = false

    init {
        val target = targetBallForVersion(sessionManager.sessionData.value.testVersion)
        _state.update { it.copy(targetBallColor = target) }
    }

    private fun targetBallForVersion(version: Int): BallColor = when (version) {
        1 -> BallColor.BLACK
        2 -> BallColor.YELLOW
        3 -> BallColor.GREEN
        else -> BallColor.RED
    }

    fun onAction(action: HitberEightQuestionAction) {
        when (action) {
            is HitberEightQuestionAction.OnBallDrag -> onBallDrag(action.id, action.dragAmount)
            is HitberEightQuestionAction.OnBallDrop -> onBallDrop(action.id)
            is HitberEightQuestionAction.OnDropZonePositioned -> onDropZonePositioned(action.rect)
            is HitberEightQuestionAction.OnContainerPositioned -> onContainerPositioned(action.offset)
            is HitberEightQuestionAction.OnLayoutReady -> onLayoutReady(
                action.containerWidth,
                action.containerHeight,
                action.ballSizePx,
            )
            is HitberEightQuestionAction.OnNextClick -> navigateNext()
        }
    }

    private fun onBallDrag(id: Int, dragAmount: Offset) {
        _state.update { current ->
            current.copy(
                balls = current.balls.map { ball ->
                    if (ball.id == id) ball.copy(currentOffset = ball.currentOffset + dragAmount)
                    else ball
                },
            )
        }
    }

    private fun onBallDrop(id: Int) {
        val current = _state.value
        if (current.isCompleted) return
        val ball = current.balls.find { it.id == id } ?: return
        val dropBounds = current.dropZoneBounds
        if (dropBounds == Rect.Zero) return

        val ballPositionInRoot = current.containerRootOffset + ball.currentOffset
        if (dropBounds.inflate(DROP_TOLERANCE).contains(ballPositionInRoot)) {
            _state.update {
                it.copy(
                    droppedBallColor = ball.color,
                    isCompleted = true,
                )
            }
        }
    }

    private fun onDropZonePositioned(rect: Rect) {
        _state.update { it.copy(dropZoneBounds = rect) }
    }

    private fun onContainerPositioned(offset: Offset) {
        _state.update { it.copy(containerRootOffset = offset) }
    }

    private fun onLayoutReady(width: Float, height: Float, ballSizePx: Float) {
        if (ballsInitialized) return
        ballsInitialized = true

        val halfBall = ballSizePx / 2f
        val leftCenterX = width * 0.125f - halfBall
        val rightCenterX = width * 0.875f - halfBall
        val topY = height * 0.25f - halfBall
        val bottomY = height * 0.62f - halfBall

        val balls = listOf(
            ColorBall(id = 0, color = BallColor.RED, currentOffset = Offset(leftCenterX, topY)),
            ColorBall(id = 1, color = BallColor.BLACK, currentOffset = Offset(leftCenterX, bottomY)),
            ColorBall(id = 2, color = BallColor.GREEN, currentOffset = Offset(rightCenterX, topY)),
            ColorBall(id = 3, color = BallColor.YELLOW, currentOffset = Offset(rightCenterX, bottomY)),
        )
        _state.update { it.copy(balls = balls) }
    }

    private fun navigateNext() {
        val current = _state.value
        val dropped = current.droppedBallColor ?: return
        sessionManager.recordQ8Result(
            HitberQ8Result(
                targetBallColor = current.targetBallColor.name,
                droppedBallColor = dropped.name,
                isCorrect = dropped == current.targetBallColor,
            )
        )
        viewModelScope.launch {
            eventChannel.send(HitberEightQuestionEvent.NavigateToNextScreen)
        }
    }

    companion object {
        private const val DROP_TOLERANCE = 60f
    }
}
