package com.devil.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.devil.app.conversation.ConversationScreen
import com.devil.app.conversation.ConversationUiState

/**
 * Android launcher surface for Devil.
 *
 * Stage 24 hosts the bounded Compose conversation presentation surface and
 * delegates user interaction through the process-scoped conversation
 * coordinators owned by DevilApplication.
 *
 * This Activity does not create constitutional context, choose schema version,
 * assign provenance, trust, or security classification, generate TraceId,
 * invoke UnifiedDevilRuntime directly, execute capabilities, create or persist
 * logical memory, or fabricate runtime outcomes.
 *
 * ConversationUiState remains UI-local presentation state. No conversation
 * persistence is introduced by this Activity.
 */
class DevilActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        val devilApplication =
            application as DevilApplication

        setContent {
            MaterialTheme {
                var conversationState by remember {
                    mutableStateOf(
                        ConversationUiState(),
                    )
                }

                ConversationScreen(
                    state = conversationState,
                    onDraftChange = { updatedDraft ->
                        conversationState =
                            devilApplication
                                .conversationInteractionCoordinator
                                .updateDraft(
                                    state = conversationState,
                                    draft = updatedDraft,
                                )
                    },
                    onSubmit = {
                        conversationState =
                            devilApplication
                                .conversationSubmissionFlowCoordinator
                                .submit(
                                    state = conversationState,
                                )
                    },
                )
            }
        }
    }
}
