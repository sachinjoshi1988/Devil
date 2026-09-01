package com.devil.app.authentication

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

/**
 * Stage 314 Android owner-alpha authentication boundary.
 *
 * AndroidX BiometricPrompt supplies genuine Android platform authentication.
 *
 * This coordinator does not:
 *
 * - resolve Devil identity;
 * - establish Devil trust;
 * - grant Devil authorization;
 * - create a Devil session;
 * - enter Owner Mode;
 * - grant Android Accessibility permission;
 * - create runtime TraceIds;
 * - execute capabilities;
 * - or report successful task completion.
 *
 * Authentication success is evidence only. Constitutional authorization remains
 * owned by Devil's existing Authorization Authority.
 *
 * ANDROID_AUTHENTICATION_SUCCESS != DEVIL_AUTHORIZATION.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 */
class Stage314AndroidOwnerAuthenticationCoordinator(
    private val activity: FragmentActivity,
) {

    fun authenticate(
        onAuthenticated: () -> Unit,
        onUnavailable: (String) -> Unit,
        onCancelledOrFailed: (String) -> Unit,
    ) {
        val authenticators =
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL

        val biometricManager =
            BiometricManager.from(activity)

        when (
            biometricManager.canAuthenticate(
                authenticators,
            )
        ) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Unit

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onUnavailable(
                    "No supported biometric or device credential is enrolled.",
                )
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onUnavailable(
                    "No supported Android authentication hardware is available.",
                )
                return
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onUnavailable(
                    "Android authentication hardware is currently unavailable.",
                )
                return
            }

            else -> {
                onUnavailable(
                    "Android owner authentication is currently unavailable.",
                )
                return
            }
        }

        val prompt =
            BiometricPrompt(
                activity,
                activity.mainExecutor,
                object :
                    BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationSucceeded(
                        result:
                            BiometricPrompt.AuthenticationResult,
                    ) {
                        super.onAuthenticationSucceeded(
                            result,
                        )

                        onAuthenticated()
                    }

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence,
                    ) {
                        super.onAuthenticationError(
                            errorCode,
                            errString,
                        )

                        onCancelledOrFailed(
                            errString
                                .toString()
                                .trim()
                                .ifEmpty {
                                    "Android owner authentication was cancelled."
                                },
                        )
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()

                        /*
                         * A non-matching biometric attempt is not authentication
                         * success. BiometricPrompt remains active and may allow
                         * another attempt, so no Devil evidence is created here.
                         */
                    }
                },
            )

        val promptInfo =
            BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate Devil owner")
                .setSubtitle(
                    "Confirm your identity to authorize this Devil action.",
                )
                .setAllowedAuthenticators(
                    authenticators,
                )
                .build()

        prompt.authenticate(
            promptInfo,
        )
    }
}
