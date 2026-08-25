package com.devil.app.ui.research

import com.devil.app.ui.accessibility.devilInclusiveHeading
import com.devil.app.ui.accessibility.devilInclusiveInteractiveTarget

import com.devil.app.ui.adaptive.DevilAdaptiveContainer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devil.app.R

/**
 * Stage 259 Research Interface.
 *
 * Presentation-only surface for already-established bounded Research information
 * supplied by an upstream caller.
 *
 * This interface does not retrieve Internet content, create ResearchEvidence,
 * assess sources, calculate corroboration or confidence, resolve conflicts,
 * create synthesis, establish truth or Verification, mutate World Model state,
 * perform Learning, create or commit Memory, authorize, or execute.
 *
 * RESEARCH_INTERFACE != RESEARCH_AUTHORITY.
 * RESEARCH_EVIDENCE_PRESENTATION != TRUE.
 * RESEARCH_EVIDENCE_PRESENTATION != VERIFIED.
 * SOURCE_ASSESSMENT_PRESENTATION != FACT_VERIFICATION.
 * CORROBORATION_PRESENTATION != CONSENSUS.
 * CONFLICT_PRESENTATION != CONFLICT_RESOLUTION.
 * RESEARCH_CONFIDENCE_PRESENTATION != TRUTH.
 * RESEARCH_CONFIDENCE_PRESENTATION != VERIFICATION.
 * RESEARCH_SYNTHESIS_PRESENTATION != TRUTH.
 * RESEARCH_SYNTHESIS_PRESENTATION != WORLD_MODEL.
 * RESEARCH_INTERFACE != LEARNING.
 * RESEARCH_INTERFACE != MEMORY.
 *
 * Stage 259 does not implement Stage 260 or later UI work.
 */
@Composable
fun DevilResearchInterface(
    researchSubject: String?,
    evidenceSourceReference: String?,
    evidenceSourceKind: String?,
    evidenceDescription: String?,
    sourceAuthenticity: String?,
    sourceTrust: String?,
    sourceFreshness: String?,
    corroborationStatus: String?,
    conflictStatus: String?,
    confidenceStatus: String?,
    synthesisStatus: String?,
    synthesisDescription: String?,
    internetAdmissionStatus: String?,
    internetAnalysisStatus: String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val devilRed = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val elevatedSurface = MaterialTheme.colorScheme.surfaceVariant
    val foreground = MaterialTheme.colorScheme.onSurface
    val muted = MaterialTheme.colorScheme.onSurfaceVariant

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
        shape = RoundedCornerShape(28.dp),
        color = surface,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            DevilResearchHeader(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
            )

            DevilResearchSubjectCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                researchSubject = researchSubject,
            )

            DevilResearchEvidenceCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                evidenceSourceReference = evidenceSourceReference,
                evidenceSourceKind = evidenceSourceKind,
                evidenceDescription = evidenceDescription,
            )

            DevilResearchSourceAssessmentCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                sourceAuthenticity = sourceAuthenticity,
                sourceTrust = sourceTrust,
                sourceFreshness = sourceFreshness,
            )

            DevilResearchEvaluationCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                corroborationStatus = corroborationStatus,
                conflictStatus = conflictStatus,
                confidenceStatus = confidenceStatus,
            )

            DevilResearchSynthesisCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                synthesisStatus = synthesisStatus,
                synthesisDescription = synthesisDescription,
            )

            DevilInternetResearchCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                internetAdmissionStatus = internetAdmissionStatus,
                internetAnalysisStatus = internetAnalysisStatus,
            )

            DevilResearchBoundaryFooter(
                devilRed = devilRed,
                muted = muted,
            )

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
                border =
                    BorderStroke(
                        width = 1.dp,
                        color = devilRed.copy(alpha = 0.46f),
                    ),
                colors =
                    ButtonDefaults.outlinedButtonColors(
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
private fun DevilResearchHeader(
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.devil_primary_logo),
            contentDescription = "Devil",
            modifier = Modifier.size(54.dp),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "RESEARCH",
                color = devilRed,
                modifier = Modifier.devilInclusiveHeading(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
            )

            Text(
                text = "Evidence-aware research presentation",
                color = foreground,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = "Presented research is not automatically truth or Verification.",
                color = muted,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun DevilResearchSubjectCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    researchSubject: String?,
) {
    DevilResearchCard(
        title = "RESEARCH SUBJECT",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilResearchField(
            label = "SUBJECT",
            value = researchSubject.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilResearchEvidenceCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    evidenceSourceReference: String?,
    evidenceSourceKind: String?,
    evidenceDescription: String?,
) {
    DevilResearchCard(
        title = "RESEARCH EVIDENCE",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilResearchField(
            label = "SOURCE REFERENCE",
            value = evidenceSourceReference.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilResearchField(
            label = "SOURCE KIND",
            value = evidenceSourceKind.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilResearchField(
            label = "EVIDENCE DESCRIPTION",
            value = evidenceDescription.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilResearchSourceAssessmentCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    sourceAuthenticity: String?,
    sourceTrust: String?,
    sourceFreshness: String?,
) {
    DevilResearchCard(
        title = "SOURCE ASSESSMENT",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilResearchField(
            label = "AUTHENTICITY",
            value = sourceAuthenticity.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilResearchField(
            label = "TRUST",
            value = sourceTrust.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilResearchField(
            label = "FRESHNESS",
            value = sourceFreshness.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilResearchEvaluationCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    corroborationStatus: String?,
    conflictStatus: String?,
    confidenceStatus: String?,
) {
    DevilResearchCard(
        title = "RESEARCH EVALUATION",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilResearchField(
            label = "CORROBORATION",
            value = corroborationStatus.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilResearchField(
            label = "CONFLICT",
            value = conflictStatus.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilResearchField(
            label = "CONFIDENCE",
            value = confidenceStatus.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilResearchSynthesisCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    synthesisStatus: String?,
    synthesisDescription: String?,
) {
    DevilResearchCard(
        title = "RESEARCH SYNTHESIS",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilResearchField(
            label = "SYNTHESIS STATUS",
            value = synthesisStatus.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilResearchField(
            label = "SYNTHESIS",
            value =
                synthesisDescription
                    ?.trim()
                    ?.takeIf(String::isNotEmpty)
                    ?: "No research synthesis supplied.",
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilInternetResearchCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    internetAdmissionStatus: String?,
    internetAnalysisStatus: String?,
) {
    DevilResearchCard(
        title = "INTERNET RESEARCH",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilResearchField(
            label = "ADMISSION",
            value = internetAdmissionStatus.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilResearchField(
            label = "ANALYSIS",
            value = internetAnalysisStatus.truthfulResearchValue(),
            foreground = foreground,
            muted = muted,
        )

        Text(
            text = "External Internet content remains untrusted unless separately governed.",
            color = muted,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun DevilResearchCard(
    title: String,
    devilRed: Color,
    elevatedSurface: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
        color = elevatedSurface,
        shape = RoundedCornerShape(20.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = devilRed.copy(alpha = 0.26f),
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = title,
                color = devilRed,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )

            content()
        }
    }
}

@Composable
private fun DevilResearchField(
    label: String,
    value: String,
    foreground: Color,
    muted: Color,
) {
    Column(
        modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            color = muted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = value,
            color = foreground,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun DevilResearchBoundaryFooter(
    devilRed: Color,
    muted: Color,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
        color = devilRed.copy(alpha = 0.07f),
        shape = RoundedCornerShape(18.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = devilRed.copy(alpha = 0.30f),
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "RESEARCH BOUNDARY",
                color = devilRed,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text =
                    "Evidence, source assessments, corroboration, confidence, and synthesis " +
                        "remain bounded research representations. Presentation does not " +
                        "establish truth, Verification, World Model state, Learning, Memory, " +
                        "authorization, execution, or verified Outcome.",
                color = muted,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private fun String?.truthfulResearchValue(): String =
    this
        ?.trim()
        ?.takeIf(String::isNotEmpty)
        ?: "Unavailable"
