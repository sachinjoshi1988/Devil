package com.devil.app.ui.task

import com.devil.app.ui.accessibility.devilInclusiveHeading
import com.devil.app.ui.accessibility.devilInclusiveInteractiveTarget

import com.devil.app.ui.adaptive.DevilAdaptiveContainer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devil.app.R

/**
 * Stage 256 Task & Automation Interface.
 *
 * Presentation-only surface for already-established bounded task and automation
 * information supplied by an upstream caller.
 *
 * It may present:
 *
 * - task identity;
 * - task summary;
 * - task lifecycle state;
 * - scheduled-time or external-event trigger information;
 * - proactive-assistance presentation eligibility;
 * - bounded Controlled Autonomy preparation;
 * - recovery disposition;
 * - recovery-attempt accounting status.
 *
 * This interface does not create, schedule, trigger, authorize, retry, cancel,
 * execute, verify, persist, or autonomously continue work.
 *
 * TASK_INTERFACE != TASK_AUTHORITY.
 * TASK_STATE != EXECUTION.
 * SCHEDULED != DUE.
 * SCHEDULED != TRIGGERED.
 * TRIGGERED != AUTHORIZED.
 * AUTHORIZED != EXECUTED.
 * PROACTIVE_ELIGIBILITY != PRESENTATION_DELIVERY.
 * CONTROLLED_AUTONOMY_PREPARED != AUTONOMY_GRANTED.
 * CONTROLLED_AUTONOMY != AUTHORIZATION.
 * CONTROLLED_AUTONOMY != EXECUTION.
 * RECOVERY_ELIGIBLE != RETRY_AUTHORIZED.
 * RECOVERY_ATTEMPT_RECORDED != RECOVERY_EXECUTED.
 * RECOVERY_ATTEMPT_RECORDED != RECOVERY_SUCCESS.
 *
 * Stage 256 does not implement Stage 257 Education Interface.
 */
@Composable
fun DevilTaskAutomationInterface(
    taskId: String?,
    taskSummary: String?,
    taskState: String?,
    triggerKind: String?,
    triggerCondition: String?,
    proactiveStatus: String?,
    proactiveMessage: String?,
    controlledAutonomyStatus: String?,
    controlledAutonomyScope: String?,
    recoveryDisposition: String?,
    recoveryAttemptStatus: String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val devilRed =
        MaterialTheme.colorScheme.primary

    val surface =
        MaterialTheme.colorScheme.surface

    val elevatedSurface =
        MaterialTheme.colorScheme.surfaceVariant

    val foreground =
        MaterialTheme.colorScheme.onSurface

    val muted =
        MaterialTheme.colorScheme.onSurfaceVariant

    DevilAdaptiveContainer {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.42f),
                    shape = RoundedCornerShape(28.dp),
                ),
        shape =
            RoundedCornerShape(28.dp),
        color =
            surface,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(14.dp),
        ) {
            DevilTaskAutomationHeader(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
            )

            DevilTaskCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                taskId = taskId,
                taskSummary = taskSummary,
                taskState = taskState,
            )

            DevilAutomationCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                triggerKind = triggerKind,
                triggerCondition = triggerCondition,
                proactiveStatus = proactiveStatus,
                proactiveMessage = proactiveMessage,
                controlledAutonomyStatus = controlledAutonomyStatus,
                controlledAutonomyScope = controlledAutonomyScope,
            )

            DevilRecoveryCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                recoveryDisposition = recoveryDisposition,
                recoveryAttemptStatus = recoveryAttemptStatus,
            )

            DevilTaskAutomationBoundaryFooter(
                devilRed = devilRed,
                muted = muted,
            )

            OutlinedButton(
                onClick = onBack,
                modifier =
                    Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
                border =
                    BorderStroke(
                        width = 1.dp,
                        color = devilRed.copy(alpha = 0.46f),
                    ),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = devilRed,
                    ),
                shape =
                    RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "BACK TO CONVERSATION",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
    }
}

@Composable
private fun DevilTaskAutomationHeader(
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    Row(
        modifier =
            Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(58.dp)
                    .clip(
                        RoundedCornerShape(18.dp),
                    )
                    .background(
                        devilRed.copy(alpha = 0.08f),
                    )
                    .border(
                        width = 1.dp,
                        color = devilRed.copy(alpha = 0.46f),
                        shape = RoundedCornerShape(18.dp),
                    ),
            contentAlignment =
                Alignment.Center,
        ) {
            Image(
                painter =
                    painterResource(
                        id = R.drawable.devil_primary_logo,
                    ),
                contentDescription =
                    "Devil",
                modifier =
                    Modifier.size(42.dp),
            )
        }

        Column(
            verticalArrangement =
                Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "TASK CONTROL",
                modifier = Modifier.devilInclusiveHeading(),
                style =
                    MaterialTheme.typography.titleLarge,
                color =
                    foreground,
                fontWeight =
                    FontWeight.Black,
            )

            Text(
                text = "Governed task & automation presentation",
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    muted,
            )
        }
    }

    Spacer(
        modifier =
            Modifier.height(2.dp),
    )

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    devilRed.copy(alpha = 0.32f),
                ),
    )
}

@Composable
private fun DevilTaskCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    taskId: String?,
    taskSummary: String?,
    taskState: String?,
) {
    DevilSectionCard(
        title = "TASK STATUS",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilTaskAutomationRow(
            label = "TASK ID",
            value = taskId,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilTaskAutomationRow(
            label = "STATE",
            value = taskState,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilTaskAutomationTextBlock(
            label = "SUMMARY",
            value = taskSummary,
            emptyText = "No task summary supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilAutomationCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    triggerKind: String?,
    triggerCondition: String?,
    proactiveStatus: String?,
    proactiveMessage: String?,
    controlledAutonomyStatus: String?,
    controlledAutonomyScope: String?,
) {
    DevilSectionCard(
        title = "AUTOMATION STATUS",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilTaskAutomationRow(
            label = "TRIGGER",
            value = triggerKind,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilTaskAutomationTextBlock(
            label = "TRIGGER CONDITION",
            value = triggerCondition,
            emptyText = "No trigger condition supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilTaskAutomationRow(
            label = "PROACTIVE",
            value = proactiveStatus,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilTaskAutomationTextBlock(
            label = "PROACTIVE MESSAGE",
            value = proactiveMessage,
            emptyText = "No proactive presentation supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilTaskAutomationRow(
            label = "CONTROLLED AUTONOMY",
            value = controlledAutonomyStatus,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilTaskAutomationTextBlock(
            label = "BOUNDED SCOPE",
            value = controlledAutonomyScope,
            emptyText = "No Controlled Autonomy scope supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilRecoveryCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    recoveryDisposition: String?,
    recoveryAttemptStatus: String?,
) {
    DevilSectionCard(
        title = "RECOVERY GOVERNANCE",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilTaskAutomationRow(
            label = "DISPOSITION",
            value = recoveryDisposition,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilTaskAutomationRow(
            label = "ATTEMPT STATUS",
            value = recoveryAttemptStatus,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilSectionCard(
    title: String,
    devilRed: Color,
    elevatedSurface: Color,
    content: @Composable () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color =
                        elevatedSurface.copy(alpha = 0.56f),
                    shape =
                        RoundedCornerShape(22.dp),
                )
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.24f),
                    shape = RoundedCornerShape(22.dp),
                )
                .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style =
                MaterialTheme.typography.labelLarge,
            color =
                devilRed,
            fontWeight =
                FontWeight.Bold,
        )

        content()
    }
}

@Composable
private fun DevilTaskAutomationRow(
    label: String,
    value: String?,
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style =
                MaterialTheme.typography.labelMedium,
            color =
                muted,
        )

        Text(
            text =
                normalizedOrUnavailable(value),
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                if (hasValue(value)) {
                    foreground
                } else {
                    devilRed.copy(alpha = 0.72f)
                },
            fontWeight =
                FontWeight.SemiBold,
        )
    }
}

@Composable
private fun DevilTaskAutomationTextBlock(
    label: String,
    value: String?,
    emptyText: String,
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color =
                        devilRed.copy(alpha = 0.045f),
                    shape =
                        RoundedCornerShape(16.dp),
                )
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(12.dp),
        verticalArrangement =
            Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = label,
            style =
                MaterialTheme.typography.labelMedium,
            color =
                devilRed,
            fontWeight =
                FontWeight.Bold,
        )

        Text(
            text =
                value
                    ?.trim()
                    ?.takeIf(String::isNotEmpty)
                    ?: emptyText,
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                if (hasValue(value)) {
                    foreground
                } else {
                    muted
                },
        )
    }
}

@Composable
private fun DevilTaskAutomationBoundaryFooter(
    devilRed: Color,
    muted: Color,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color =
                        Color.Black.copy(alpha = 0.24f),
                    shape =
                        RoundedCornerShape(16.dp),
                )
                .border(
                    width = 1.dp,
                    color =
                        devilRed.copy(alpha = 0.18f),
                    shape =
                        RoundedCornerShape(16.dp),
                )
                .padding(12.dp),
        verticalArrangement =
            Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "PRESENTATION ONLY",
            style =
                MaterialTheme.typography.labelMedium,
            color =
                devilRed,
            fontWeight =
                FontWeight.Bold,
        )

        Text(
            text =
                "Viewing task and automation information does not itself schedule, trigger, authorize, retry, cancel, execute, or verify work.",
            style =
                MaterialTheme.typography.bodySmall,
            color =
                muted,
        )
    }
}

private fun normalizedOrUnavailable(
    value: String?,
): String {
    return value
        ?.trim()
        ?.takeIf(String::isNotEmpty)
        ?: "Unavailable"
}

private fun hasValue(
    value: String?,
): Boolean {
    return value
        ?.trim()
        ?.isNotEmpty() == true
}
