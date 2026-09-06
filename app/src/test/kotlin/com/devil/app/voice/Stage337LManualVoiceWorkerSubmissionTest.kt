package com.devil.app.voice

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 337L bounded source-composition proof for recognized manual voice
 * submission execution context.
 *
 * This test does not manufacture physical Android execution evidence.
 *
 * VOICE_SOURCE != SPEAKER_AUTHENTICATED.
 * RECOGNIZED != AUTHORIZED.
 * WORKER_SUBMISSION != EXECUTION_APPROVAL.
 * VOICE_WORKER_HANDOFF != SECOND_RUNTIME.
 * VOICE_WORKER_HANDOFF != VOICE_AUTHENTICATION.
 */
class Stage337LManualVoiceWorkerSubmissionTest {

    @Test
    fun `recognized manual voice reuses existing worker and returns state to main thread`() {
        val source = activitySource()

        val handler =
            source
                .substringAfter(
                    "private fun handleManualVoiceResult(",
                )
                .substringBefore(
                    "private fun toggleHandsFree()",
                )

        assertTrue(
            handler.contains(
                "AndroidVoiceInputStatus.RECOGNIZED",
            ),
        )

        assertTrue(
            handler.contains(
                "stage314RealAndroidSubmissionExecutor.execute {",
            ),
        )

        assertTrue(
            handler.contains(
                ".voiceConversationResultCoordinator",
            ),
        )

        assertTrue(
            handler.contains(
                "stateAtSubmission",
            ),
        )

        assertTrue(
            handler.contains(
                "runOnUiThread {",
            ),
        )

        assertTrue(
            handler.contains(
                "stage337LManualVoiceSubmissionInProgress =",
            ),
        )

        assertFalse(
            handler.contains(
                "stage314OwnerAuthenticationCoordinator",
            ),
        )

        assertFalse(
            handler.contains(
                "stage314OwnerSessionStore",
            ),
        )

        assertFalse(
            handler.contains(
                "Executors.newSingleThreadExecutor()",
            ),
        )
    }

    @Test
    fun `Stage 337L creates no second runtime worker`() {
        val source = activitySource()

        val executorCount =
            source
                .split(
                    "Executors.newSingleThreadExecutor()",
                )
                .size - 1

        assertEquals(
            1,
            executorCount,
        )

        assertTrue(
            source.contains(
                "VOICE_WORKER_HANDOFF != SECOND_RUNTIME.",
            ),
        )

        assertTrue(
            source.contains(
                "VOICE_WORKER_HANDOFF != VOICE_AUTHENTICATION.",
            ),
        )
    }

    @Test
    fun `manual voice input is guarded while its runtime submission is active`() {
        val source = activitySource()

        val requestVoiceInput =
            source
                .substringAfter(
                    "private fun requestVoiceInput(",
                )
                .substringBefore(
                    "private fun startVoiceInput(",
                )

        val startVoiceInput =
            source
                .substringAfter(
                    "private fun startVoiceInput(",
                )
                .substringBefore(
                    "private fun handleHandsFreeVoiceResult(",
                )

        val presentation =
            source
                .substringAfter(
                    "voiceInputEnabled =",
                )
                .take(350)

        assertTrue(
            requestVoiceInput.contains(
                "stage337LManualVoiceSubmissionInProgress",
            ),
        )

        assertTrue(
            startVoiceInput.contains(
                "stage337LManualVoiceSubmissionInProgress",
            ),
        )

        assertTrue(
            presentation.contains(
                "stage337LManualVoiceSubmissionInProgress",
            ),
        )
    }

    private fun activitySource(): String {
        val path =
            "app/src/main/kotlin/com/devil/app/DevilActivity.kt"

        return listOf(
            File(path),
            File("../$path"),
            File("../../$path"),
        )
            .firstOrNull(File::isFile)
            ?.readText()
            ?: error(
                "Unable to locate DevilActivity.kt for Stage 337L worker proof.",
            )
    }
}
