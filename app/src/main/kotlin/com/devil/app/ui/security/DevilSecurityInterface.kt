package com.devil.app.ui.security

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
 * Stage 261 Security Interface.
 *
 * Presentation-only surface for already-established bounded Security and
 * Surveillance information supplied by an upstream caller.
 *
 * This interface does not activate surveillance sources, connect cameras,
 * authenticate a subject, establish Owner Mode, grant authorization, determine
 * threat or emergency state, send alerts, execute Security Responses, contact
 * emergency services, persist security evidence, perform privacy disclosure,
 * mutate World Model state, perform constitutional Learning, or commit Memory.
 *
 * SECURITY_INTERFACE != SECURITY_AUTHORITY.
 * SECURITY_PRESENTATION != AUTHENTICATION.
 * SECURITY_PRESENTATION != OWNER_MODE.
 * SECURITY_PRESENTATION != AUTHORIZATION.
 * SECURITY_EVENT_PRESENTATION != VERIFIED_REALITY.
 * SECURITY_EVENT_PRESENTATION != THREAT_DETERMINATION.
 * SECURITY_ALERT_PRESENTATION != OWNER_NOTIFIED.
 * SECURITY_RESPONSE_PRESENTATION != EXECUTION_APPROVED.
 * EMERGENCY_PRESENTATION != EMERGENCY_CONFIRMED.
 * EMERGENCY_PRESENTATION != EMERGENCY_SERVICE_CONTACTED.
 * EVIDENCE_RETENTION_PRESENTATION != PERSISTENCE.
 * PRIVACY_CONTROL_PRESENTATION != PRIVACY_DISCLOSURE_PERFORMED.
 * SECURITY_VALIDATION_PRESENTATION != CONSTITUTIONAL_VERIFICATION.
 * SECURITY_INTERFACE != EXECUTION.
 * SECURITY_INTERFACE != WORLD_MODEL_UPDATE.
 * SECURITY_INTERFACE != MEMORY_COMMITMENT.
 *
 * Stage 261 does not implement Stage 262 or later UI work.
 */
@Composable
fun DevilSecurityInterface(
    securityStage: String?,
    securityState: String?,
    surveillanceIntegrationStatus: String?,
    cameraAdapterStatus: String?,
    eventUnderstandingStatus: String?,
    eventUnderstandingDescription: String?,
    alertingStatus: String?,
    alertDescription: String?,
    responseGovernanceStatus: String?,
    emergencyEscalationStatus: String?,
    escalationDescription: String?,
    ownerDashboardStatus: String?,
    dashboardSummary: String?,
    evidenceRetentionStatus: String?,
    retentionDescription: String?,
    privacyControlsStatus: String?,
    privacyControlsDescription: String?,
    productionValidationStatus: String?,
    validationFocus: String?,
    validationEvidenceDescription: String?,
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
            DevilSecurityHeader(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
            )

            DevilSecurityStateCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                securityStage = securityStage,
                securityState = securityState,
            )

            DevilSurveillanceCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                surveillanceIntegrationStatus = surveillanceIntegrationStatus,
                cameraAdapterStatus = cameraAdapterStatus,
            )

            DevilSecurityEventCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                eventUnderstandingStatus = eventUnderstandingStatus,
                eventUnderstandingDescription = eventUnderstandingDescription,
            )

            DevilSecurityAlertCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                alertingStatus = alertingStatus,
                alertDescription = alertDescription,
            )

            DevilSecurityResponseCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                responseGovernanceStatus = responseGovernanceStatus,
                emergencyEscalationStatus = emergencyEscalationStatus,
                escalationDescription = escalationDescription,
            )

            DevilOwnerSecurityCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                ownerDashboardStatus = ownerDashboardStatus,
                dashboardSummary = dashboardSummary,
            )

            DevilSecurityEvidenceCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                evidenceRetentionStatus = evidenceRetentionStatus,
                retentionDescription = retentionDescription,
            )

            DevilSecurityPrivacyCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                privacyControlsStatus = privacyControlsStatus,
                privacyControlsDescription = privacyControlsDescription,
            )

            DevilSecurityValidationCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                productionValidationStatus = productionValidationStatus,
                validationFocus = validationFocus,
                validationEvidenceDescription = validationEvidenceDescription,
            )

            DevilSecurityBoundaryFooter(
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
                    text = "BACK TO CONVERSATION",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun DevilSecurityHeader(
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter =
                painterResource(
                    id = R.drawable.devil_primary_logo,
                ),
            contentDescription = "Devil",
            modifier = Modifier.size(54.dp),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "SECURITY",
                style = MaterialTheme.typography.titleLarge,
                color = devilRed,
                fontWeight = FontWeight.Black,
            )

            Text(
                text = "Governed security and surveillance presentation",
                style = MaterialTheme.typography.bodyMedium,
                color = foreground,
            )

            Text(
                text = "Presented security state is not automatically verified reality or authority.",
                style = MaterialTheme.typography.bodySmall,
                color = muted,
            )
        }
    }
}

@Composable
private fun DevilSecurityStateCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    securityStage: String?,
    securityState: String?,
) {
    DevilSecurityCard(
        title = "SECURITY STATE",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilSecurityField(
            label = "SECURITY STAGE",
            value = securityStage.truthfulSecurityValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilSecurityField(
            label = "SECURITY STATE",
            value = securityState.truthfulSecurityValue(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilSurveillanceCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    surveillanceIntegrationStatus: String?,
    cameraAdapterStatus: String?,
) {
    DevilSecurityCard(
        title = "SURVEILLANCE",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilSecurityField(
            "INTEGRATION STATUS",
            surveillanceIntegrationStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "CAMERA ADAPTER STATUS",
            cameraAdapterStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )
    }
}

@Composable
private fun DevilSecurityEventCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    eventUnderstandingStatus: String?,
    eventUnderstandingDescription: String?,
) {
    DevilSecurityCard(
        title = "EVENT UNDERSTANDING",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilSecurityField(
            "STATUS",
            eventUnderstandingStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "UNDERSTANDING",
            eventUnderstandingDescription.truthfulSecurityValue(),
            foreground,
            muted,
        )
    }
}

@Composable
private fun DevilSecurityAlertCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    alertingStatus: String?,
    alertDescription: String?,
) {
    DevilSecurityCard(
        title = "SECURITY ALERTING",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilSecurityField(
            "STATUS",
            alertingStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "ALERT",
            alertDescription.truthfulSecurityValue(),
            foreground,
            muted,
        )
    }
}

@Composable
private fun DevilSecurityResponseCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    responseGovernanceStatus: String?,
    emergencyEscalationStatus: String?,
    escalationDescription: String?,
) {
    DevilSecurityCard(
        title = "RESPONSE & EMERGENCY",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilSecurityField(
            "RESPONSE GOVERNANCE",
            responseGovernanceStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "EMERGENCY ESCALATION",
            emergencyEscalationStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "ESCALATION DESCRIPTION",
            escalationDescription.truthfulSecurityValue(),
            foreground,
            muted,
        )
    }
}

@Composable
private fun DevilOwnerSecurityCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    ownerDashboardStatus: String?,
    dashboardSummary: String?,
) {
    DevilSecurityCard(
        title = "OWNER SECURITY DASHBOARD",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilSecurityField(
            "STATUS",
            ownerDashboardStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "SUMMARY",
            dashboardSummary.truthfulSecurityValue(),
            foreground,
            muted,
        )
    }
}

@Composable
private fun DevilSecurityEvidenceCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    evidenceRetentionStatus: String?,
    retentionDescription: String?,
) {
    DevilSecurityCard(
        title = "EVIDENCE RETENTION",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilSecurityField(
            "STATUS",
            evidenceRetentionStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "RETENTION CONTEXT",
            retentionDescription.truthfulSecurityValue(),
            foreground,
            muted,
        )
    }
}

@Composable
private fun DevilSecurityPrivacyCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    privacyControlsStatus: String?,
    privacyControlsDescription: String?,
) {
    DevilSecurityCard(
        title = "SURVEILLANCE PRIVACY",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilSecurityField(
            "STATUS",
            privacyControlsStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "PRIVACY CONTROL CONTEXT",
            privacyControlsDescription.truthfulSecurityValue(),
            foreground,
            muted,
        )
    }
}

@Composable
private fun DevilSecurityValidationCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    productionValidationStatus: String?,
    validationFocus: String?,
    validationEvidenceDescription: String?,
) {
    DevilSecurityCard(
        title = "SECURITY VALIDATION",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilSecurityField(
            "STATUS",
            productionValidationStatus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "VALIDATION FOCUS",
            validationFocus.truthfulSecurityValue(),
            foreground,
            muted,
        )

        DevilSecurityField(
            "VALIDATION EVIDENCE",
            validationEvidenceDescription.truthfulSecurityValue(),
            foreground,
            muted,
        )
    }
}

@Composable
private fun DevilSecurityCard(
    title: String,
    devilRed: Color,
    elevatedSurface: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
private fun DevilSecurityField(
    label: String,
    value: String,
    foreground: Color,
    muted: Color,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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
private fun DevilSecurityBoundaryFooter(
    devilRed: Color,
    muted: Color,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
                text = "SECURITY BOUNDARY",
                color = devilRed,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text =
                    "Security and surveillance context remains bounded presentation. It does not " +
                        "establish authentication, Owner Mode, authorization, verified threat or " +
                        "emergency state, execution approval, evidence persistence, privacy disclosure, " +
                        "constitutional Verification, World Model state, Learning, Memory, or verified Outcome.",
                color = muted,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private fun String?.truthfulSecurityValue(): String =
    this
        ?.trim()
        ?.takeIf(String::isNotEmpty)
        ?: "Unavailable"
