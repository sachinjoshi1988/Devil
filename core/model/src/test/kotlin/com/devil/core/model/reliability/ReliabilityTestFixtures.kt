package com.devil.core.model.reliability

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord

internal object ReliabilityTestFixtures {

    fun error(): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "stage-45-test-failure",
                ),
            traceId =
                TraceId.from(
                    "stage-45-test-trace",
                ),
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1L,
                ),
            summary =
                "Bounded Stage 45 test failure.",
        )
    }

    fun recoveryAttemptRecord(): RecoveryAttemptRecord {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition =
                        ReliabilityCondition.DEGRADED,
                    recoveryPathKnown = true,
                ),
            )

        val request =
            RecoveryRequest.create(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                    ),
            )

        return RecoveryAttemptRecord.create(
            request = request,
        )
    }
}
