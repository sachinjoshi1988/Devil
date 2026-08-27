package com.devil.app.ui.education

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
 * Stage 257 Education Interface.
 *
 * Presentation-only surface for already-established bounded Education Domain
 * information supplied by an upstream caller.
 *
 * This interface does not create, infer, authenticate, authorize, schedule,
 * teach, execute, verify, persist, or autonomously continue educational work.
 *
 * EDUCATION_INTERFACE != EDUCATION_AUTHORITY.
 * EDUCATION_SESSION != SECURITY_SESSION.
 * USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 * EDUCATION_OBJECTIVE != DECISION.
 * EDUCATION_OBJECTIVE != TASK.
 *
 * LANGUAGE_EDUCATION_SESSION != AUTHORIZATION.
 * LANGUAGE_EDUCATION_SESSION != VERIFIED_LEARNING_PROGRESS.
 *
 * STUDY_COMPANION != STUDY_SCHEDULER.
 * STUDY_COMPANION != TASK_CREATION.
 * STUDY_COMPANION != VERIFIED_MASTERY.
 *
 * HOMEWORK_ASSISTANCE != HOMEWORK_COMPLETION.
 * HOMEWORK_ASSISTANCE != ASSIGNMENT_SUBMISSION.
 * HOMEWORK_ASSISTANCE != VERIFIED_CORRECTNESS.
 *
 * LEARNING_PROGRESS != VERIFIED_MASTERY.
 * LEARNING_PROGRESS != CONSTITUTIONAL_VERIFICATION.
 * PROGRESS_INTERPRETATION != VERIFIED_OUTCOME.
 *
 * CHILD_EDUCATION_CONTEXT != CHILD_CLASSIFICATION.
 * CHILD_CLASSIFICATION != AUTHENTICATION.
 * CHILD_EDUCATION_INTEGRATION != GUARDIAN_AUTHORITY.
 * CHILD_EDUCATION_INTEGRATION != GUARDIAN_APPROVAL.
 *
 * AGE_APPROPRIATE_TEACHING != AGE_INFERENCE.
 * AGE_APPROPRIATE_TEACHING != CHILD_CLASSIFICATION.
 *
 * GUARDIAN_POLICY_FOUNDATION != GUARDIAN_AUTHORITY.
 * GUARDIAN_POLICY_FOUNDATION != GUARDIAN_APPROVAL.
 *
 * CHILD_PRIVACY_BOUNDARY != PRIVACY_AUTHORIZATION.
 * PRIVACY_BOUNDARY != DISCLOSURE_OCCURRED.
 *
 * SPOKEN_EDUCATION_MODE != SPEECH_EXECUTED.
 * EDUCATIONAL_VISION != VERIFIED_CORRECTNESS.
 * TABLET_CONTEXT != AUTHORIZATION.
 *
 * Stage 257 does not implement Stage 258 or later UI work.
 */
@Composable
fun DevilEducationInterface(
    educationSessionId: String?,
    subject: String?,
    educationObjective: String?,
    targetLanguage: String?,
    studyFocus: String?,
    studyApproach: String?,
    learnerSupportObjective: String?,
    progressFocus: String?,
    learnerEvidence: String?,
    progressInterpretation: String?,
    childEducationStatus: String?,
    teachingLevel: String?,
    teachingApproach: String?,
    guardianPolicyStatus: String?,
    privacyBoundaryStatus: String?,
    spokenEducationStatus: String?,
    educationalVisionStatus: String?,
    tabletEducationStatus: String?,
    onLanguageLearningOpen: () -> Unit,
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
            DevilEducationHeader(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
            )

            DevilEducationSessionCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                educationSessionId = educationSessionId,
                subject = subject,
                educationObjective = educationObjective,
                targetLanguage = targetLanguage,
            )

            DevilStudySupportCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                studyFocus = studyFocus,
                studyApproach = studyApproach,
                learnerSupportObjective = learnerSupportObjective,
            )

            DevilProgressCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                progressFocus = progressFocus,
                learnerEvidence = learnerEvidence,
                progressInterpretation = progressInterpretation,
            )

            DevilChildGuardianCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                childEducationStatus = childEducationStatus,
                teachingLevel = teachingLevel,
                teachingApproach = teachingApproach,
                guardianPolicyStatus = guardianPolicyStatus,
                privacyBoundaryStatus = privacyBoundaryStatus,
            )

            DevilEducationModesCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                spokenEducationStatus = spokenEducationStatus,
                educationalVisionStatus = educationalVisionStatus,
                tabletEducationStatus = tabletEducationStatus,
            )

            OutlinedButton(
                onClick = onLanguageLearningOpen,
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
                    text = "LANGUAGE LEARNING",
                    fontWeight = FontWeight.Bold,
                )
            }

            DevilEducationBoundaryFooter(
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
private fun DevilEducationHeader(
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
                text = "EDUCATION",
                modifier = Modifier.devilInclusiveHeading(),
                style =
                    MaterialTheme.typography.titleLarge,
                color =
                    foreground,
                fontWeight =
                    FontWeight.Black,
            )

            Text(
                text = "Governed learning presentation",
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
private fun DevilEducationSessionCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    educationSessionId: String?,
    subject: String?,
    educationObjective: String?,
    targetLanguage: String?,
) {
    DevilEducationSectionCard(
        title = "EDUCATION SESSION",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilEducationRow(
            label = "SESSION",
            value = educationSessionId,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationRow(
            label = "SUBJECT",
            value = subject,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationTextBlock(
            label = "LEARNING OBJECTIVE",
            value = educationObjective,
            emptyText = "No education objective supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationRow(
            label = "TARGET LANGUAGE",
            value = targetLanguage,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilStudySupportCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    studyFocus: String?,
    studyApproach: String?,
    learnerSupportObjective: String?,
) {
    DevilEducationSectionCard(
        title = "STUDY SUPPORT",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilEducationTextBlock(
            label = "STUDY FOCUS",
            value = studyFocus,
            emptyText = "No study focus supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationTextBlock(
            label = "STUDY APPROACH",
            value = studyApproach,
            emptyText = "No study approach supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationTextBlock(
            label = "LEARNER SUPPORT",
            value = learnerSupportObjective,
            emptyText = "No learner-support objective supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilProgressCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    progressFocus: String?,
    learnerEvidence: String?,
    progressInterpretation: String?,
) {
    DevilEducationSectionCard(
        title = "LEARNING PROGRESS",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilEducationTextBlock(
            label = "PROGRESS FOCUS",
            value = progressFocus,
            emptyText = "No progress focus supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationTextBlock(
            label = "LEARNER EVIDENCE",
            value = learnerEvidence,
            emptyText = "No learner evidence supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationTextBlock(
            label = "PROGRESS INTERPRETATION",
            value = progressInterpretation,
            emptyText = "No progress interpretation supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilChildGuardianCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    childEducationStatus: String?,
    teachingLevel: String?,
    teachingApproach: String?,
    guardianPolicyStatus: String?,
    privacyBoundaryStatus: String?,
) {
    DevilEducationSectionCard(
        title = "CHILD & GUARDIAN",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilEducationRow(
            label = "CHILD EDUCATION",
            value = childEducationStatus,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationRow(
            label = "TEACHING LEVEL",
            value = teachingLevel,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationTextBlock(
            label = "TEACHING APPROACH",
            value = teachingApproach,
            emptyText = "No teaching approach supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationRow(
            label = "GUARDIAN POLICY",
            value = guardianPolicyStatus,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationRow(
            label = "PRIVACY BOUNDARY",
            value = privacyBoundaryStatus,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilEducationModesCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    spokenEducationStatus: String?,
    educationalVisionStatus: String?,
    tabletEducationStatus: String?,
) {
    DevilEducationSectionCard(
        title = "EDUCATION MODES",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilEducationRow(
            label = "SPOKEN EDUCATION",
            value = spokenEducationStatus,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationRow(
            label = "EDUCATIONAL VISION",
            value = educationalVisionStatus,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilEducationRow(
            label = "TABLET EDUCATION",
            value = tabletEducationStatus,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilEducationSectionCard(
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
private fun DevilEducationRow(
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
private fun DevilEducationTextBlock(
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
private fun DevilEducationBoundaryFooter(
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
            text = "EDUCATION PRESENTATION ONLY",
            style =
                MaterialTheme.typography.labelMedium,
            color =
                devilRed,
            fontWeight =
                FontWeight.Bold,
        )

        Text(
            text =
                "Viewing education information does not itself teach, authenticate, authorize, verify mastery, perform constitutional Learning, or persist learner progress.",
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
