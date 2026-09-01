package com.devil.app.execution

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId

/**
 * Stage 314 process-local trace-bound post-action expectation store.
 *
 * This store preserves only a bounded expected Android screen condition after
 * the genuine constitutional execution path has supplied its own trace and
 * capability identity.
 *
 * It does not:
 *
 * - create TraceId;
 * - select a capability;
 * - authenticate a subject;
 * - grant authorization;
 * - approve execution;
 * - perform an Android action;
 * - inspect the Android screen;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - claim execution success;
 * - update World Model state;
 * - perform Learning;
 * - commit Memory;
 * - persist expectations;
 * - or create another runtime.
 *
 * EXPECTATION_STORED != OBSERVED.
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
class Stage314AndroidPostActionExpectationStore {

    private val lock = Any()

    private var expectation:
        Stage314AndroidPostActionExpectation? = null

    fun bind(
        traceId: TraceId,
        capabilityId: CapabilityId,
        expectedVisibleText: String,
    ) {
        val normalizedExpectedVisibleText =
            expectedVisibleText.trim()

        require(normalizedExpectedVisibleText.isNotEmpty()) {
            "Stage 314 post-action expected visible text must not be blank."
        }

        synchronized(lock) {
            expectation =
                Stage314AndroidPostActionExpectation(
                    traceId = traceId,
                    capabilityId = capabilityId,
                    expectedVisibleText =
                        normalizedExpectedVisibleText,
                )
        }
    }

    fun current(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): Stage314AndroidPostActionExpectation? {
        synchronized(lock) {
            val currentExpectation =
                expectation
                    ?: return null

            if (
                currentExpectation.traceId != traceId ||
                currentExpectation.capabilityId != capabilityId
            ) {
                return null
            }

            return currentExpectation
        }
    }

    fun consume(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): Stage314AndroidPostActionExpectation? {
        synchronized(lock) {
            val currentExpectation =
                expectation
                    ?: return null

            if (
                currentExpectation.traceId != traceId ||
                currentExpectation.capabilityId != capabilityId
            ) {
                return null
            }

            expectation = null

            return currentExpectation
        }
    }

    fun clear() {
        synchronized(lock) {
            expectation = null
        }
    }
}

/**
 * One bounded Stage 314 post-action expectation.
 *
 * expectedVisibleText is only an expected observable screen condition.
 * Its presence here is not evidence that the condition actually occurred.
 */
data class Stage314AndroidPostActionExpectation(
    val traceId: TraceId,
    val capabilityId: CapabilityId,
    val expectedVisibleText: String,
)
