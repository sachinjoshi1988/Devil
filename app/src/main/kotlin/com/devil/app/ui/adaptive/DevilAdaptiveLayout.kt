package com.devil.app.ui.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Stage 263 Tablet Adaptive UI presentation policy.
 *
 * This contract adapts Compose presentation from available layout width only.
 *
 * The 600dp breakpoint intentionally matches the already-established Stage 82
 * tablet threshold for visual consistency, but Stage 263 does not perform,
 * replace, or reinterpret Stage 82 tablet-form-factor assessment.
 *
 * ADAPTIVE_LAYOUT != TABLET_FORM_FACTOR_ASSESSMENT.
 * ADAPTIVE_LAYOUT != TABLET_EMBODIMENT.
 * AVAILABLE_WIDTH != DEVICE_IDENTITY.
 * AVAILABLE_WIDTH != DEVICE_TRUST.
 * TABLET_PRESENTATION != NEW_DEVIL.
 * TABLET_PRESENTATION != NEW_RUNTIME.
 * TABLET_PRESENTATION != AUTHENTICATION.
 * TABLET_PRESENTATION != AUTHORIZATION.
 * TABLET_PRESENTATION != SESSION_CONTINUITY.
 * TABLET_PRESENTATION != EXECUTION.
 * TABLET_PRESENTATION != MEMORY_SYNC.
 * ADAPTIVE_UI != ACCESSIBILITY_AUTHORITY.
 *
 * Stage 263 does not implement Stage 264 Accessibility & Inclusive Design
 * or Stage 265 UI Production Validation.
 */
object DevilAdaptiveLayoutPolicy {
    val tabletBreakpoint: Dp = 600.dp

    val compactConversationCardMaxWidth: Dp = 310.dp
    val expandedConversationCardMaxWidth: Dp = 520.dp

    val expandedContentMaxWidth: Dp = 1120.dp

    val compactHorizontalPadding: Dp = 0.dp
    val expandedHorizontalPadding: Dp = 24.dp
}

/**
 * Bounded presentation information derived only from the current Compose
 * layout constraints.
 *
 * isExpanded does not claim that the underlying device has been
 * constitutionally classified as a tablet.
 */
data class DevilAdaptivePresentation(
    val isExpanded: Boolean,
    val availableWidth: Dp,
    val conversationCardMaxWidth: Dp,
    val horizontalPadding: Dp,
)

val LocalDevilAdaptivePresentation =
    staticCompositionLocalOf {
        DevilAdaptivePresentation(
            isExpanded = false,
            availableWidth = 0.dp,
            conversationCardMaxWidth =
                DevilAdaptiveLayoutPolicy.compactConversationCardMaxWidth,
            horizontalPadding =
                DevilAdaptiveLayoutPolicy.compactHorizontalPadding,
        )
    }

/**
 * Centers bounded Phase-R content on wider layouts while preserving the
 * existing compact behavior below the Stage 263 presentation breakpoint.
 *
 * No device classification, authorization, execution, persistence, or
 * constitutional state is produced here.
 */
@Composable
fun DevilAdaptiveContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
    ) {
        val expanded =
            maxWidth >= DevilAdaptiveLayoutPolicy.tabletBreakpoint

        val presentation =
            DevilAdaptivePresentation(
                isExpanded = expanded,
                availableWidth = maxWidth,
                conversationCardMaxWidth =
                    if (expanded) {
                        DevilAdaptiveLayoutPolicy.expandedConversationCardMaxWidth
                    } else {
                        DevilAdaptiveLayoutPolicy.compactConversationCardMaxWidth
                    },
                horizontalPadding =
                    if (expanded) {
                        DevilAdaptiveLayoutPolicy.expandedHorizontalPadding
                    } else {
                        DevilAdaptiveLayoutPolicy.compactHorizontalPadding
                    },
            )

        androidx.compose.runtime.CompositionLocalProvider(
            LocalDevilAdaptivePresentation provides presentation,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            PaddingValues(
                                horizontal =
                                    presentation.horizontalPadding,
                            ),
                        ),
                contentAlignment = Alignment.TopCenter,
            ) {
                Box(
                    modifier =
                        if (expanded) {
                            Modifier
                                .fillMaxWidth()
                                .widthIn(
                                    max =
                                        DevilAdaptiveLayoutPolicy
                                            .expandedContentMaxWidth,
                                )
                        } else {
                            Modifier.fillMaxWidth()
                        },
                    contentAlignment = Alignment.TopCenter,
                ) {
                    content()
                }
            }
        }
    }
}
