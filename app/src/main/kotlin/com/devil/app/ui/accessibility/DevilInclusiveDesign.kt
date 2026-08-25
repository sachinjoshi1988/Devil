package com.devil.app.ui.accessibility

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Stage 264 Accessibility & Inclusive Design presentation contract.
 *
 * This contract improves the accessibility and inclusive presentation of the
 * already-established Phase-R Devil UI.
 *
 * It does not connect to Android AccessibilityService, inspect accessibility
 * nodes, understand screen content, perform accessibility actions, request
 * Android permissions, authenticate a subject, establish Owner Mode, grant
 * authorization, approve execution, create an ExecutionRequest, establish
 * constitutional Observation, Verification, or Outcome, mutate World Model
 * state, perform constitutional Learning, or commit Memory.
 *
 * Existing Stage 38 and Stage 178–181 operational Android accessibility
 * architecture remains authoritative and unchanged.
 *
 * Existing Stage 48 conversation semantics remain authoritative and are reused,
 * not replaced.
 *
 * INCLUSIVE_UI != ACCESSIBILITY_SERVICE.
 * UI_SEMANTICS != SCREEN_UNDERSTANDING.
 * UI_SEMANTICS != ACCESSIBILITY_ACTION.
 * ACCESSIBILITY_PRESENTATION != AUTHENTICATION.
 * ACCESSIBILITY_PRESENTATION != OWNER_MODE.
 * ACCESSIBILITY_PRESENTATION != AUTHORIZATION.
 * ACCESSIBILITY_PRESENTATION != EXECUTION_APPROVAL.
 * ACCESSIBILITY_PRESENTATION != CONSTITUTIONAL_OBSERVATION.
 * ACCESSIBILITY_PRESENTATION != CONSTITUTIONAL_VERIFICATION.
 * ACCESSIBILITY_PRESENTATION != VERIFIED_OUTCOME.
 * ACCESSIBILITY_PRESENTATION != WORLD_MODEL_UPDATE.
 * ACCESSIBILITY_PRESENTATION != MEMORY_COMMITMENT.
 * INCLUSIVE_DESIGN != DEVICE_IDENTITY.
 *
 * Stage 264 does not implement Stage 265 UI Production Validation.
 */
object DevilInclusiveDesignPolicy {

    /**
     * Shared minimum interactive target for Phase-R presentation controls.
     *
     * This influences Compose layout only.
     * It does not make an action authorized, executable, or successful.
     */
    val minimumInteractiveTarget: Dp = 48.dp
}

/**
 * Marks an already-visible Phase-R title as a semantic heading.
 *
 * HEADING_SEMANTICS != AUTHORITY.
 */
fun Modifier.devilInclusiveHeading(): Modifier =
    semantics {
        heading()
    }

/**
 * Supplies an explicit accessibility description for meaningful UI imagery.
 *
 * The description is presentation metadata only.
 */
fun Modifier.devilMeaningfulImage(
    description: String,
): Modifier {
    val normalizedDescription = description.trim()

    require(normalizedDescription.isNotEmpty()) {
        "Meaningful Devil UI imagery requires a non-blank accessibility description."
    }

    return semantics {
        contentDescription = normalizedDescription
    }
}

/**
 * Applies the shared bounded Phase-R minimum interactive target.
 *
 * TOUCH_TARGET != EXECUTION_APPROVAL.
 */
fun Modifier.devilInclusiveInteractiveTarget(): Modifier =
    sizeIn(
        minWidth = DevilInclusiveDesignPolicy.minimumInteractiveTarget,
        minHeight = DevilInclusiveDesignPolicy.minimumInteractiveTarget,
    )

/**
 * Announces changing presentation status politely to assistive technology.
 *
 * LIVE_REGION != ANDROID_ACCESSIBILITY_EVENT_AUTHORITY.
 * LIVE_REGION != OBSERVATION.
 * LIVE_REGION != VERIFIED_OUTCOME.
 */
fun Modifier.devilPoliteStatus(): Modifier =
    semantics {
        liveRegion = LiveRegionMode.Polite
    }

/**
 * Stage 264 intentionally preserves Compose/Material text scaling.
 *
 * Phase-R callers should continue using MaterialTheme typography rather than
 * replacing scalable typography with accessibility-specific fixed font sizes.
 */
@Composable
fun DevilInclusiveDesignBoundary() {
    // Deliberately contains no operational behavior.
}
