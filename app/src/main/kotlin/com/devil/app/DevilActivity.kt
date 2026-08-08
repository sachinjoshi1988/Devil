package com.devil.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.devil.app.conversation.ConversationScreen
import com.devil.app.conversation.ConversationUiState

/**
 * Android launcher surface for Devil.
 *
 * Stage 24 hosts the bounded Compose conversation presentation surface.
 *
 * This Activity remains an Android UI boundary. It does not create
 * constitutional context, choose schema version, assign provenance, trust, or
 * security classification, generate trace identity, submit conversation input,
 * invoke the UnifiedDevilRuntime, execute capabilities, create or persist
 * logical memory, or fabricate runtime outcomes.
 *
 * Draft text is currently UI-local and survives ordinary Activity recreation.
 * No draft content enters the constitutional runtime until a later explicitly
 * connected submission boundary is implemented.
 */
class DevilActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var draft by rememberSaveable {
                    mutableStateOf("")
                }

                ConversationScreen(
                    state =
                        ConversationUiState(
                            draft = draft,
                        ),
                    onDraftChange = { updatedDraft ->
                        draft = updatedDraft
                    },
                )
            }
        }
    }
}
