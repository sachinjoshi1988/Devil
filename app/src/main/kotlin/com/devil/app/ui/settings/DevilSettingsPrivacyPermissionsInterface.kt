package com.devil.app.ui.settings

import com.devil.app.ui.accessibility.devilInclusiveHeading
import com.devil.app.ui.accessibility.devilInclusiveInteractiveTarget

import com.devil.app.ui.adaptive.DevilAdaptiveContainer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.devil.app.R

/**
 * Stage 262 Settings / Privacy / Permissions Interface.
 *
 * Stage 262 remains a presentation surface for already-established bounded settings-control,
 * Android-permission, and privacy-governance information supplied by an upstream
 * caller. Stage 337B additionally provides an interactive presentation boundary for
 * entering a new conversational-model credential or requesting local credential
 * removal. Those callbacks request upstream handling only; this composable does not
 * authenticate an owner or mutate credential storage itself.

 *
 * This interface does not open Android settings, mutate Android settings, request
 * or grant Android permission, grant Devil authorization, evaluate privacy
 * exposure, derive disclosure treatment, perform disclosure, expose protected raw
 * representation content, execute capabilities, establish constitutional
 * Verification or Outcome, mutate World Model state, perform constitutional
 * Learning, or commit Memory.
 *
 * SETTINGS_INTERFACE != SETTINGS_AUTHORITY.
 * SETTINGS_PRESENTATION != SETTINGS_CHANGED.
 * SETTINGS_READY != SETTINGS_CHANGED.
 *
 * PERMISSION_PRESENTATION != ANDROID_PERMISSION_REQUEST.
 * PERMISSION_PRESENTATION != ANDROID_PERMISSION_GRANT.
 * ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.
 * PERMISSION_ASSESSED != EXECUTION_APPROVAL.
 *
 * PRIVACY_PRESENTATION != PRIVACY_AUTHORIZATION.
 * PRIVACY_EXPOSURE_ALLOWED != DEVIL_AUTHORIZATION.
 * DISCLOSURE_AVAILABLE != PERMISSION_TO_TRANSMIT.
 * DISCLOSURE_PRESENTATION != DISCLOSURE_PERFORMED.
 * PRIVACY_REPRESENTATION_STATUS != VERIFIED_PRIVACY.
 *
 * SETTINGS_PRIVACY_PERMISSIONS_INTERFACE != EXECUTION.
 * SETTINGS_PRIVACY_PERMISSIONS_INTERFACE != CONSTITUTIONAL_VERIFICATION.
 * SETTINGS_PRIVACY_PERMISSIONS_INTERFACE != WORLD_MODEL_UPDATE.
 * SETTINGS_PRIVACY_PERMISSIONS_INTERFACE != MEMORY_COMMITMENT.
 *
 * Stage 262 does not implement Stage 263 or later UI work.
 */
@Composable
fun DevilSettingsPrivacyPermissionsInterface(
    settingsCommand: String?,
    settingsControlStatus: String?,
    permissionCapability: String?,
    permissionAssessmentStatus: String?,
    requiredPermissions: String?,
    privacyExposureStatus: String?,
    privacyExposureRationale: String?,
    privacyDisclosureStatus: String?,
    privacyDisclosureTreatment: String?,
    privacyDisclosureRationale: String?,
    privacyRepresentationStatus: String?,
    privacyDataClassification: String?,
    onBack: () -> Unit,
    modelCredentialStatus: String? = null,
    modelCredentialAuthenticationInProgress: Boolean = false,
    onModelCredentialProvisionRequested: (String) -> Unit = {},
    onModelCredentialRemovalRequested: () -> Unit = {},
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
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            DevilSettingsPrivacyPermissionsHeader(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
            )

            DevilSettingsCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                settingsCommand = settingsCommand,
                settingsControlStatus = settingsControlStatus,
            )

            DevilPermissionsCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                permissionCapability = permissionCapability,
                permissionAssessmentStatus = permissionAssessmentStatus,
                requiredPermissions = requiredPermissions,
            )

            DevilPrivacyExposureCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                privacyExposureStatus = privacyExposureStatus,
                privacyExposureRationale = privacyExposureRationale,
            )

            DevilPrivacyDisclosureCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                privacyDisclosureStatus = privacyDisclosureStatus,
                privacyDisclosureTreatment = privacyDisclosureTreatment,
                privacyDisclosureRationale = privacyDisclosureRationale,
            )

            DevilPrivacyRepresentationCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                privacyRepresentationStatus = privacyRepresentationStatus,
                privacyDataClassification = privacyDataClassification,
            )

            DevilModelCredentialManagementCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                credentialStatus = modelCredentialStatus,
                authenticationInProgress =
                    modelCredentialAuthenticationInProgress,
                onProvisionRequested =
                    onModelCredentialProvisionRequested,
                onRemovalRequested =
                    onModelCredentialRemovalRequested,
            )

            DevilSettingsPrivacyPermissionsBoundaryFooter(
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
private fun DevilSettingsPrivacyPermissionsHeader(
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
                text = "SETTINGS",
                modifier = Modifier.devilInclusiveHeading(),
                style = MaterialTheme.typography.titleLarge,
                color = devilRed,
                fontWeight = FontWeight.Black,
            )

            Text(
                text = "Settings, privacy, and permissions presentation",
                style = MaterialTheme.typography.bodyMedium,
                color = foreground,
            )

            Text(
                text = "Presented state does not itself change settings, grant permissions, authorize, or disclose data.",
                style = MaterialTheme.typography.bodySmall,
                color = muted,
            )
        }
    }
}

@Composable
private fun DevilSettingsCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    settingsCommand: String?,
    settingsControlStatus: String?,
) {
    DevilStage262Card(
        title = "DEVICE SETTINGS",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilStage262Field(
            label = "COMMAND",
            value = settingsCommand.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )

        DevilStage262Field(
            label = "STATUS",
            value = settingsControlStatus.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilPermissionsCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    permissionCapability: String?,
    permissionAssessmentStatus: String?,
    requiredPermissions: String?,
) {
    DevilStage262Card(
        title = "ANDROID PERMISSIONS",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilStage262Field(
            label = "CAPABILITY",
            value = permissionCapability.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )

        DevilStage262Field(
            label = "ASSESSMENT STATUS",
            value = permissionAssessmentStatus.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )

        DevilStage262Field(
            label = "REQUIRED PERMISSIONS",
            value = requiredPermissions.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilPrivacyExposureCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    privacyExposureStatus: String?,
    privacyExposureRationale: String?,
) {
    DevilStage262Card(
        title = "PRIVACY EXPOSURE",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilStage262Field(
            label = "STATUS",
            value = privacyExposureStatus.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )

        DevilStage262Field(
            label = "RATIONALE",
            value = privacyExposureRationale.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilPrivacyDisclosureCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    privacyDisclosureStatus: String?,
    privacyDisclosureTreatment: String?,
    privacyDisclosureRationale: String?,
) {
    DevilStage262Card(
        title = "PRIVACY DISCLOSURE",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilStage262Field(
            label = "STATUS",
            value = privacyDisclosureStatus.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )

        DevilStage262Field(
            label = "TREATMENT",
            value = privacyDisclosureTreatment.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )

        DevilStage262Field(
            label = "RATIONALE",
            value = privacyDisclosureRationale.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilPrivacyRepresentationCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    privacyRepresentationStatus: String?,
    privacyDataClassification: String?,
) {
    DevilStage262Card(
        title = "PRIVACY REPRESENTATION",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilStage262Field(
            label = "STATUS",
            value = privacyRepresentationStatus.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )

        DevilStage262Field(
            label = "DATA CLASSIFICATION",
            value = privacyDataClassification.truthfulStage262Value(),
            foreground = foreground,
            muted = muted,
        )

        Text(
            text = "Protected representation content is intentionally not displayed by this interface.",
            color = muted,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun DevilStage262Card(
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
private fun DevilStage262Field(
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
private fun DevilSettingsPrivacyPermissionsBoundaryFooter(
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
                text = "SETTINGS / PRIVACY / PERMISSIONS BOUNDARY",
                color = devilRed,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text =
                    "This screen presents bounded settings, privacy, and Android permission metadata only. " +
                        "It does not change settings, request or grant permissions, authorize Devil, disclose " +
                        "protected data, execute capabilities, establish constitutional Verification, update " +
                        "World Model state, perform Learning, commit Memory, or establish verified Outcome.",
                color = muted,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private fun String?.truthfulStage262Value(): String =
    this
        ?.trim()
        ?.takeIf(String::isNotEmpty)
        ?: "Unavailable"

/**
 * Stage 337B presentation-only conversational-model credential management.
 *
 * The credential draft exists only as ephemeral Compose state. It is masked,
 * is never populated from persistent storage, and is cleared from visible UI
 * before the provisioning callback is emitted.
 *
 * This composable does not authenticate an owner, access Android Keystore,
 * mutate credential storage, validate a provider credential, grant Devil
 * authorization, invoke a model, execute a capability, or establish verified
 * truth.
 *
 * CREDENTIAL_ENTRY != CREDENTIAL_STORED.
 * ANDROID_AUTHENTICATION_SUCCESS != DEVIL_AUTHORIZATION.
 * CREDENTIAL_PROVISIONED != CREDENTIAL_VALID.
 * CREDENTIAL_REMOVED != PROVIDER_REVOKED.
 */
@Composable
private fun DevilModelCredentialManagementCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    credentialStatus: String?,
    authenticationInProgress: Boolean,
    onProvisionRequested: (String) -> Unit,
    onRemovalRequested: () -> Unit,
) {
    var credentialDraft by remember {
        mutableStateOf("")
    }

    val canProvision =
        !authenticationInProgress &&
            credentialDraft.isNotBlank()

    val canRemove =
        !authenticationInProgress

    DevilStage262Card(
        title = "MODEL ACCESS",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        Text(
            text =
                "Provision or remove the local conversational-model credential.",
            style = MaterialTheme.typography.bodyMedium,
            color = foreground,
        )

        Text(
            text =
                "The stored credential is never displayed. Android owner authentication is required before local credential storage is changed.",
            style = MaterialTheme.typography.bodySmall,
            color = muted,
        )

        OutlinedTextField(
            value = credentialDraft,
            onValueChange = { value ->
                credentialDraft = value
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .devilInclusiveInteractiveTarget(),
            enabled = !authenticationInProgress,
            singleLine = true,
            label = {
                Text(
                    text = "MODEL CREDENTIAL",
                )
            },
            placeholder = {
                Text(
                    text = "Enter new credential",
                )
            },
            visualTransformation =
                PasswordVisualTransformation(),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = devilRed,
                    unfocusedBorderColor =
                        devilRed.copy(alpha = 0.36f),
                    focusedTextColor = foreground,
                    unfocusedTextColor = foreground,
                    cursorColor = devilRed,
                ),
            shape = RoundedCornerShape(16.dp),
        )

        OutlinedButton(
            onClick = {
                /*
                 * Clear visible secret state before authentication begins.
                 *
                 * The immutable String passed to the callback may still exist
                 * transiently in JVM memory and cannot be reliably zeroized.
                 */
                val credentialToProvision =
                    credentialDraft

                credentialDraft = ""

                onProvisionRequested(
                    credentialToProvision,
                )
            },
            enabled = canProvision,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .devilInclusiveInteractiveTarget(),
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
                text =
                    if (authenticationInProgress) {
                        "AUTHENTICATION IN PROGRESS"
                    } else {
                        "SAVE CREDENTIAL"
                    },
                fontWeight = FontWeight.Bold,
            )
        }

        OutlinedButton(
            onClick = onRemovalRequested,
            enabled = canRemove,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .devilInclusiveInteractiveTarget(),
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
                text = "REMOVE LOCAL CREDENTIAL",
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text =
                credentialStatus
                    ?: "No credential-management operation has been performed in this screen session.",
            style = MaterialTheme.typography.bodySmall,
            color = muted,
        )

        Text(
            text =
                "Local removal does not revoke a credential at the remote provider.",
            style = MaterialTheme.typography.bodySmall,
            color = muted,
        )
    }
}
