package com.devil.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Android launcher surface for Devil.
 *
 * Stage 24 introduces the first bounded Compose conversation shell.
 *
 * This Activity remains only an Android UI boundary. It does not create
 * constitutional context, choose schema version, assign provenance, trust, or
 * security classification, generate trace identity, submit conversation input,
 * invoke the UnifiedDevilRuntime, execute capabilities, create or persist
 * logical memory, or fabricate runtime outcomes.
 */
class DevilActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        setContent {
            DevilConversationRoot()
        }
    }
}

/**
 * Minimal Stage 24 conversation root.
 *
 * This surface intentionally contains no interactive submission path yet.
 */
@Composable
private fun DevilConversationRoot() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Devil",
                    style = MaterialTheme.typography.headlineMedium,
                )

                Text(
                    text = "Conversation",
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = "Conversation input is not connected yet.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
