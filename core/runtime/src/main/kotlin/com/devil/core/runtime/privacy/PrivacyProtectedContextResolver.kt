package com.devil.core.runtime.privacy

import com.devil.core.model.privacy.PrivacyProtectedContextEvidence
import com.devil.core.model.privacy.PrivacyProtectedContextStatus
import com.devil.core.model.security.SecurityStage
import com.devil.core.model.security.SecurityStateRecord
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus

/**
 * Stage 46 bounded bridge from already established constitutional security
 * evidence into privacy protected-context evidence.
 *
 * This resolver consumes security results. It is not a Security Authority.
 *
 * In particular:
 *
 * - SessionValidityStatus.VALID proves session validity only;
 * - SecurityStage.OWNER_MODE in SecurityStateRecord represents security position
 *   only;
 * - neither fact proves owner identity;
 * - combining those two facts must not fabricate owner authentication or
 *   protected owner-presentation context.
 *
 * The current constitutional architecture exposes no independent operational
 * Owner-Mode establishment result that this resolver can safely consume.
 *
 * Therefore this resolver deliberately fails closed for owner-protected privacy
 * context until such genuine evidence exists.
 *
 * It does not authenticate, create sessions, advance SecurityStage, enter Owner
 * Mode, grant authorization, grant Android permission, invoke
 * UnifiedDevilRuntime, disclose information, or execute anything.
 */
class PrivacyProtectedContextResolver {

    fun resolveOwnerProtectedContext(
        sessionValidityResult: SessionValidityResult,
        securityState: SecurityStateRecord,
    ): PrivacyProtectedContextEvidence {
        return when (sessionValidityResult.status) {
            SessionValidityStatus.INVALID ->
                PrivacyProtectedContextEvidence.create(
                    status =
                        PrivacyProtectedContextStatus.NOT_ESTABLISHED,
                    rationale =
                        "Owner-protected privacy context is not established because constitutional session validity is INVALID.",
                )

            SessionValidityStatus.DEFERRED ->
                PrivacyProtectedContextEvidence.create(
                    status =
                        PrivacyProtectedContextStatus.UNAVAILABLE,
                    rationale =
                        "Owner-protected privacy context is unavailable because constitutional session validity is DEFERRED.",
                )

            SessionValidityStatus.FAILED ->
                PrivacyProtectedContextEvidence.create(
                    status =
                        PrivacyProtectedContextStatus.UNAVAILABLE,
                    rationale =
                        "Owner-protected privacy context is unavailable because constitutional session-validity evaluation FAILED.",
                )

            SessionValidityStatus.VALID ->
                resolveValidSession(
                    securityState = securityState,
                )
        }
    }

    private fun resolveValidSession(
        securityState: SecurityStateRecord,
    ): PrivacyProtectedContextEvidence {
        return when (securityState.stage) {
            SecurityStage.LOCKED,
            SecurityStage.WAKE,
            SecurityStage.AUTHENTICATION,
            SecurityStage.SESSION,
            ->
                PrivacyProtectedContextEvidence.create(
                    status =
                        PrivacyProtectedContextStatus.NOT_ESTABLISHED,
                    rationale =
                        "A valid session does not establish the protected owner context required for owner presentation.",
                )

            SecurityStage.OWNER_MODE,
            SecurityStage.HIGH_SECURITY_CONFIRMATION,
            ->
                PrivacyProtectedContextEvidence.create(
                    status =
                        PrivacyProtectedContextStatus.UNAVAILABLE,
                    rationale =
                        "The represented security stage is insufficient by itself because the current architecture exposes no independent operational Owner-Mode establishment result for privacy to consume.",
                )
        }
    }
}
