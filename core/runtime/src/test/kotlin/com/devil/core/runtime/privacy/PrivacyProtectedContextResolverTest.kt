package com.devil.core.runtime.privacy

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.privacy.PrivacyProtectedContextStatus
import com.devil.core.model.security.SecurityStage
import com.devil.core.model.security.SecurityStateRecord
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class PrivacyProtectedContextResolverTest {

    private val resolver =
        PrivacyProtectedContextResolver()

    @Test
    fun `invalid session cannot establish owner protected context`() {
        val request =
            PrivacyProtectedContextTestFixtures.validityRequest()

        val result =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    SessionValidityResult.create(
                        traceId = request.context.traceId,
                        status = SessionValidityStatus.INVALID,
                        request = request,
                    ),
                securityState =
                    state(
                        SecurityStage.OWNER_MODE,
                    ),
            )

        assertEquals(
            PrivacyProtectedContextStatus.NOT_ESTABLISHED,
            result.status,
        )
    }

    @Test
    fun `valid session at session stage does not establish owner protected context`() {
        val request =
            PrivacyProtectedContextTestFixtures.validityRequest()

        val result =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    SessionValidityResult.create(
                        traceId = request.context.traceId,
                        status = SessionValidityStatus.VALID,
                        request = request,
                    ),
                securityState =
                    state(
                        SecurityStage.SESSION,
                    ),
            )

        assertEquals(
            PrivacyProtectedContextStatus.NOT_ESTABLISHED,
            result.status,
        )
    }

    @Test
    fun `owner mode state record plus valid session does not fabricate owner protection`() {
        val request =
            PrivacyProtectedContextTestFixtures.validityRequest()

        val result =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    SessionValidityResult.create(
                        traceId = request.context.traceId,
                        status = SessionValidityStatus.VALID,
                        request = request,
                    ),
                securityState =
                    state(
                        SecurityStage.OWNER_MODE,
                    ),
            )

        assertEquals(
            PrivacyProtectedContextStatus.UNAVAILABLE,
            result.status,
        )
    }

    @Test
    fun `high security state record does not fabricate owner protection`() {
        val request =
            PrivacyProtectedContextTestFixtures.validityRequest()

        val result =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    SessionValidityResult.create(
                        traceId = request.context.traceId,
                        status = SessionValidityStatus.VALID,
                        request = request,
                    ),
                securityState =
                    state(
                        SecurityStage.HIGH_SECURITY_CONFIRMATION,
                    ),
            )

        assertEquals(
            PrivacyProtectedContextStatus.UNAVAILABLE,
            result.status,
        )
    }

    @Test
    fun `deferred validity remains unavailable`() {
        val result =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    SessionValidityResult.create(
                        traceId =
                            TraceId.from(
                                "stage-46-deferred",
                            ),
                        status =
                            SessionValidityStatus.DEFERRED,
                    ),
                securityState =
                    state(
                        SecurityStage.OWNER_MODE,
                    ),
            )

        assertEquals(
            PrivacyProtectedContextStatus.UNAVAILABLE,
            result.status,
        )
    }

    @Test
    fun `failed validity remains unavailable`() {
        val traceId =
            TraceId.from(
                "stage-46-failed",
            )

        val result =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    SessionValidityResult.create(
                        traceId = traceId,
                        status =
                            SessionValidityStatus.FAILED,
                        error =
                            UniversalErrorRecord.create(
                                errorCode =
                                    ErrorCode.from(
                                        "stage-46-session-validity-failure",
                                    ),
                                traceId = traceId,
                                occurredAt =
                                    com.devil.core.model.common.DevilTimestamp
                                        .fromEpochMilliseconds(
                                            4_000L,
                                        ),
                                summary =
                                    "Session validity evaluation failed.",
                            ),
                    ),
                securityState =
                    state(
                        SecurityStage.OWNER_MODE,
                    ),
            )

        assertEquals(
            PrivacyProtectedContextStatus.UNAVAILABLE,
            result.status,
        )
    }

    private fun state(
        stage: SecurityStage,
    ): SecurityStateRecord {
        return SecurityStateRecord.create(
            stage = stage,
            rationale =
                "Bounded Stage 46 privacy security-state fixture.",
        )
    }
}
