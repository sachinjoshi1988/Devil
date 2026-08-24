package com.devil.app.ui.education

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devil.app.R

/**
 * Stage 258 Language Learning Interface.
 *
 * Presentation-only surface for already-established bounded language-education
 * information supplied by an upstream caller.
 *
 * This interface does not create language sessions, generate lessons,
 * execute curriculum, assess proficiency, start speech recognition,
 * invoke speech output, authenticate, authorize, persist learner state,
 * perform constitutional Learning, or establish verified mastery.
 *
 * LANGUAGE_LEARNING_INTERFACE != LANGUAGE_EDUCATION_AUTHORITY.
 * LANGUAGE_LEARNING_INTERFACE != LANGUAGE_SESSION_CREATION.
 * LANGUAGE_LEARNING_INTERFACE != LESSON_GENERATION.
 * LANGUAGE_LEARNING_UI != VERIFIED_PROFICIENCY.
 * LANGUAGE_PROGRESS_PRESENTATION != VERIFIED_MASTERY.
 * ADAPTIVE_CURRICULUM_PRESENTATION != CURRICULUM_EXECUTION.
 * MULTILINGUAL_UI != LANGUAGE_INFERENCE.
 * SPOKEN_LANGUAGE_MODE_PRESENTATION != SPEECH_EXECUTED.
 * LANGUAGE_LEARNING_UI != MEMORY_COMMITMENT.
 * USER_LANGUAGE_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 *
 * Stage 258 does not implement Stage 259 or later UI work.
 */
@Composable
fun DevilLanguageLearningInterface(
    languageSessionId: String?,
    targetLanguage: String?,
    learningObjective: String?,
    spokenEnglishStatus: String?,
    pronunciationStatus: String?,
    listeningStatus: String?,
    grammarStatus: String?,
    vocabularyStatus: String?,
    writingStatus: String?,
    confidenceStatus: String?,
    academicEnglishStatus: String?,
    professionalEnglishStatus: String?,
    curriculumStatus: String?,
    multilingualTeachingStatus: String?,
    multilingualConversationStatus: String?,
    crossLanguageAssistanceStatus: String?,
    progressStatus: String?,
    assessmentStatus: String?,
    spokenEducationStatus: String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val devilRed = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val elevatedSurface = MaterialTheme.colorScheme.surfaceVariant
    val foreground = MaterialTheme.colorScheme.onSurface
    val muted = MaterialTheme.colorScheme.onSurfaceVariant

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
            DevilLanguageHeader(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
            )

            DevilLanguageSessionCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                languageSessionId = languageSessionId,
                targetLanguage = targetLanguage,
                learningObjective = learningObjective,
            )

            DevilEnglishLearningCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                spokenEnglishStatus = spokenEnglishStatus,
                pronunciationStatus = pronunciationStatus,
                listeningStatus = listeningStatus,
                grammarStatus = grammarStatus,
                vocabularyStatus = vocabularyStatus,
                writingStatus = writingStatus,
            )

            DevilAdvancedEnglishCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                confidenceStatus = confidenceStatus,
                academicEnglishStatus = academicEnglishStatus,
                professionalEnglishStatus = professionalEnglishStatus,
            )

            DevilMultilingualLearningCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                curriculumStatus = curriculumStatus,
                multilingualTeachingStatus = multilingualTeachingStatus,
                multilingualConversationStatus = multilingualConversationStatus,
                crossLanguageAssistanceStatus = crossLanguageAssistanceStatus,
            )

            DevilLanguageProgressCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                progressStatus = progressStatus,
                assessmentStatus = assessmentStatus,
                spokenEducationStatus = spokenEducationStatus,
            )

            DevilLanguageBoundaryFooter(
                devilRed = devilRed,
                muted = muted,
            )

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
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
                    text = "BACK TO EDUCATION",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun DevilLanguageHeader(
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(devilRed.copy(alpha = 0.08f))
                    .border(
                        width = 1.dp,
                        color = devilRed.copy(alpha = 0.46f),
                        shape = RoundedCornerShape(18.dp),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter =
                    painterResource(
                        id = R.drawable.devil_primary_logo,
                    ),
                contentDescription = "Devil",
                modifier = Modifier.size(42.dp),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "LANGUAGE LEARNING",
                style = MaterialTheme.typography.titleLarge,
                color = foreground,
                fontWeight = FontWeight.Black,
            )

            Text(
                text = "Governed language-learning presentation",
                style = MaterialTheme.typography.bodySmall,
                color = muted,
            )
        }
    }
}

@Composable
private fun DevilLanguageSessionCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    languageSessionId: String?,
    targetLanguage: String?,
    learningObjective: String?,
) {
    DevilLanguageSectionCard(
        title = "LANGUAGE SESSION",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilLanguageRow(
            label = "SESSION",
            value = languageSessionId,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilLanguageRow(
            label = "TARGET LANGUAGE",
            value = targetLanguage,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        DevilLanguageTextBlock(
            label = "LEARNING OBJECTIVE",
            value = learningObjective,
            emptyText = "No language-learning objective supplied.",
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilEnglishLearningCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    spokenEnglishStatus: String?,
    pronunciationStatus: String?,
    listeningStatus: String?,
    grammarStatus: String?,
    vocabularyStatus: String?,
    writingStatus: String?,
) {
    DevilLanguageSectionCard(
        title = "ENGLISH LEARNING",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilLanguageRow("SPOKEN ENGLISH", spokenEnglishStatus, devilRed, foreground, muted)
        DevilLanguageRow("PRONUNCIATION", pronunciationStatus, devilRed, foreground, muted)
        DevilLanguageRow("LISTENING", listeningStatus, devilRed, foreground, muted)
        DevilLanguageRow("GRAMMAR", grammarStatus, devilRed, foreground, muted)
        DevilLanguageRow("VOCABULARY", vocabularyStatus, devilRed, foreground, muted)
        DevilLanguageRow("WRITING", writingStatus, devilRed, foreground, muted)
    }
}

@Composable
private fun DevilAdvancedEnglishCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    confidenceStatus: String?,
    academicEnglishStatus: String?,
    professionalEnglishStatus: String?,
) {
    DevilLanguageSectionCard(
        title = "ADVANCED ENGLISH",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilLanguageRow("CONFIDENCE", confidenceStatus, devilRed, foreground, muted)
        DevilLanguageRow("ACADEMIC", academicEnglishStatus, devilRed, foreground, muted)
        DevilLanguageRow("PROFESSIONAL", professionalEnglishStatus, devilRed, foreground, muted)
    }
}

@Composable
private fun DevilMultilingualLearningCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    curriculumStatus: String?,
    multilingualTeachingStatus: String?,
    multilingualConversationStatus: String?,
    crossLanguageAssistanceStatus: String?,
) {
    DevilLanguageSectionCard(
        title = "MULTILINGUAL LEARNING",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilLanguageRow("CURRICULUM", curriculumStatus, devilRed, foreground, muted)
        DevilLanguageRow("MULTILINGUAL TEACHING", multilingualTeachingStatus, devilRed, foreground, muted)
        DevilLanguageRow("CONVERSATION LAB", multilingualConversationStatus, devilRed, foreground, muted)
        DevilLanguageRow("CROSS-LANGUAGE SUPPORT", crossLanguageAssistanceStatus, devilRed, foreground, muted)
    }
}

@Composable
private fun DevilLanguageProgressCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    progressStatus: String?,
    assessmentStatus: String?,
    spokenEducationStatus: String?,
) {
    DevilLanguageSectionCard(
        title = "PROGRESS & MODES",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilLanguageRow("PROGRESS", progressStatus, devilRed, foreground, muted)
        DevilLanguageRow("ASSESSMENT", assessmentStatus, devilRed, foreground, muted)
        DevilLanguageRow("SPOKEN EDUCATION", spokenEducationStatus, devilRed, foreground, muted)
    }
}

@Composable
private fun DevilLanguageSectionCard(
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
                    color = elevatedSurface.copy(alpha = 0.56f),
                    shape = RoundedCornerShape(22.dp),
                )
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.24f),
                    shape = RoundedCornerShape(22.dp),
                )
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = devilRed,
            fontWeight = FontWeight.Bold,
        )

        content()
    }
}

@Composable
private fun DevilLanguageRow(
    label: String,
    value: String?,
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = muted,
        )

        Text(
            text = normalizedOrUnavailable(value),
            style = MaterialTheme.typography.bodyMedium,
            color =
                if (hasValue(value)) {
                    foreground
                } else {
                    devilRed.copy(alpha = 0.72f)
                },
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun DevilLanguageTextBlock(
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
                    color = devilRed.copy(alpha = 0.045f),
                    shape = RoundedCornerShape(16.dp),
                )
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = devilRed,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text =
                value
                    ?.trim()
                    ?.takeIf(String::isNotEmpty)
                    ?: emptyText,
            style = MaterialTheme.typography.bodyMedium,
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
private fun DevilLanguageBoundaryFooter(
    devilRed: Color,
    muted: Color,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Black.copy(alpha = 0.24f),
                    shape = RoundedCornerShape(16.dp),
                )
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "LANGUAGE LEARNING PRESENTATION ONLY",
            style = MaterialTheme.typography.labelMedium,
            color = devilRed,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text =
                "Viewing language-learning information does not itself create lessons, establish proficiency, execute speech, verify mastery, perform constitutional Learning, or persist learner state.",
            style = MaterialTheme.typography.bodySmall,
            color = muted,
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
