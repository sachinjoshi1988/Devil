package com.devil.app.ui.memory

import com.devil.app.ui.accessibility.devilInclusiveHeading
import com.devil.app.ui.accessibility.devilInclusiveInteractiveTarget

import com.devil.app.ui.adaptive.DevilAdaptiveContainer

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
import androidx.compose.material3.MaterialTheme
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
 * Stage 255 Memory Interface.
 *
 * This is a presentation-only surface for already-established bounded
 * memory information supplied by an upstream caller.
 *
 * It may present:
 *
 * - memory class;
 * - sensitivity;
 * - confidence;
 * - retention;
 * - source;
 * - owner-visible reason.
 *
 * It does not independently obtain, infer, create, approve, commit,
 * persist, recall, expose, correct, delete, or verify logical memory.
 *
 * MEMORY_INTERFACE != MEMORY_AUTHORITY.
 * MEMORY_INTERFACE != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_INTERFACE != MEMORY_COMMITMENT.
 * MEMORY_INTERFACE != MEMORY_PERSISTENCE.
 * MEMORY_INTERFACE != STORAGE_SUCCESS.
 * MEMORY_INTERFACE != MEMORY_RECALL.
 * MEMORY_INTERFACE != DISCLOSURE_PERMISSION.
 * MEMORY_INTERFACE != DELETION_EXECUTION.
 * MEMORY_INTERFACE != VERIFICATION.
 *
 * Stage 255 does not implement Stage 256 Task & Automation Interface.
 */
@Composable
fun DevilMemoryInterface(
    memoryClass: String?,
    sensitivity: String?,
    confidence: String?,
    retention: String?,
    source: String?,
    ownerVisibleReason: String?,
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
            DevilMemoryHeader(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
            )

            DevilMemoryVault(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                memoryClass = memoryClass,
                sensitivity = sensitivity,
                confidence = confidence,
                retention = retention,
                source = source,
                ownerVisibleReason = ownerVisibleReason,
            )

            DevilMemoryBoundaryFooter(
                devilRed = devilRed,
                muted = muted,
            )

            androidx.compose.material3.OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
                border =
                    androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = devilRed.copy(alpha = 0.46f),
                    ),
                colors =
                    androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        contentColor = devilRed,
                    ),
                shape = RoundedCornerShape(16.dp),
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
private fun DevilMemoryHeader(
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
                        id =
                            R.drawable.devil_primary_logo,
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
                text =
                    "MEMORY VAULT",
                    modifier = Modifier.devilInclusiveHeading(),
                style =
                    MaterialTheme.typography.titleLarge,
                color =
                    foreground,
                fontWeight =
                    FontWeight.Black,
            )

            Text(
                text =
                    "Governed memory presentation",
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
private fun DevilMemoryVault(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    memoryClass: String?,
    sensitivity: String?,
    confidence: String?,
    retention: String?,
    source: String?,
    ownerVisibleReason: String?,
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
            text =
                "MEMORY STATUS",
            style =
                MaterialTheme.typography.labelLarge,
            color =
                devilRed,
            fontWeight =
                FontWeight.Bold,
        )

        DevilMemoryMetadataRow(
            label = "CLASS",
            value = memoryClass,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilMemoryMetadataRow(
            label = "SENSITIVITY",
            value = sensitivity,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilMemoryMetadataRow(
            label = "CONFIDENCE",
            value = confidence,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilMemoryMetadataRow(
            label = "RETENTION",
            value = retention,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilMemoryMetadataRow(
            label = "SOURCE",
            value = source,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

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
                        color =
                            devilRed.copy(alpha = 0.20f),
                        shape =
                            RoundedCornerShape(16.dp),
                    )
                    .padding(12.dp),
            verticalArrangement =
                Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text =
                    "WHY DEVIL MAY REMEMBER THIS",
                style =
                    MaterialTheme.typography.labelMedium,
                color =
                    devilRed,
                fontWeight =
                    FontWeight.Bold,
            )

            Text(
                text =
                    ownerVisibleReason
                        ?.trim()
                        ?.takeIf(String::isNotEmpty)
                        ?: "No owner-visible reason supplied.",
                style =
                    MaterialTheme.typography.bodyMedium,
                color =
                    if (
                        ownerVisibleReason
                            ?.trim()
                            ?.isNotEmpty() ==
                            true
                    ) {
                        foreground
                    } else {
                        muted
                    },
            )
        }
    }
}

@Composable
private fun DevilMemoryMetadataRow(
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
            text =
                label,
            style =
                MaterialTheme.typography.labelMedium,
            color =
                muted,
        )

        Text(
            text =
                value
                    ?.trim()
                    ?.takeIf(String::isNotEmpty)
                    ?: "Unavailable",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                if (
                    value
                        ?.trim()
                        ?.isNotEmpty() ==
                        true
                ) {
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
private fun DevilMemoryBoundaryFooter(
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
            text =
                "MEMORY PRESENTATION ONLY",
            style =
                MaterialTheme.typography.labelMedium,
            color =
                devilRed,
            fontWeight =
                FontWeight.Bold,
        )

        Text(
            text =
                "Viewing memory information does not itself recall, persist, delete, approve, or verify memory.",
            style =
                MaterialTheme.typography.bodySmall,
            color =
                muted,
        )
    }
}
