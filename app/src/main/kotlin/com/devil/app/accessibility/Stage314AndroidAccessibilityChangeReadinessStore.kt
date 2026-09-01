package com.devil.app.accessibility

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import java.util.concurrent.TimeUnit

/**
 * Stage 314 process-local post-execution accessibility snapshot readiness store.
 *
 * One genuine constitutional execution trace may arm one pending readiness
 * record. The matching genuine Android execution attempt must then be marked
 * before accessibility-derived screen snapshots can qualify.
 *
 * After the execution attempt, the first AVAILABLE snapshot becomes only a
 * candidate. A later value-equal AVAILABLE snapshot establishes bounded
 * readiness and is preserved for Observation.
 *
 * This store does not inspect expected destination text and does not establish
 * Observation, Verification, Outcome, authorization, or execution success.
 *
 * SNAPSHOT_CAPTURED != OBSERVED.
 * SNAPSHOT_STABLE != VERIFIED.
 * ATTEMPTED != VERIFIED.
 */
class Stage314AndroidAccessibilityChangeReadinessStore {

    private val monitor =
        Object()

    private var pending: PendingReadiness? =
        null

    fun arm(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ) {
        synchronized(monitor) {
            pending =
                PendingReadiness(
                    traceId = traceId,
                    capabilityId = capabilityId,
                    executionAttempted = false,
                    candidateElements = null,
                    readyElements = null,
                )

            monitor.notifyAll()
        }
    }

    fun markExecutionAttempted(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): Boolean {
        synchronized(monitor) {
            val current =
                pending
                    ?: return false

            if (
                current.traceId != traceId ||
                current.capabilityId != capabilityId
            ) {
                return false
            }

            pending =
                current.copy(
                    executionAttempted = true,
                    candidateElements = null,
                    readyElements = null,
                )

            monitor.notifyAll()

            return true
        }
    }

    fun signalAccessibilitySnapshot(
        elements: List<AndroidScreenElementRecord>,
    ) {
        val preservedElements =
            elements.toList()

        synchronized(monitor) {
            val current =
                pending
                    ?: return

            if (!current.executionAttempted) {
                return
            }

            if (current.readyElements != null) {
                return
            }

            val previousCandidate =
                current.candidateElements

            pending =
                if (
                    previousCandidate != null &&
                    previousCandidate == preservedElements
                ) {
                    current.copy(
                        candidateElements = preservedElements,
                        readyElements = preservedElements,
                    )
                } else {
                    current.copy(
                        candidateElements = preservedElements,
                        readyElements = null,
                    )
                }

            monitor.notifyAll()
        }
    }

    fun awaitStableAccessibilitySnapshot(
        traceId: TraceId,
        capabilityId: CapabilityId,
        timeoutMilliseconds: Long,
    ): List<AndroidScreenElementRecord>? {
        require(timeoutMilliseconds > 0L) {
            "Stage 314 accessibility snapshot wait timeout must be positive."
        }

        val timeoutNanos =
            TimeUnit.MILLISECONDS.toNanos(
                timeoutMilliseconds,
            )

        val deadline =
            System.nanoTime() + timeoutNanos

        synchronized(monitor) {
            while (true) {
                val current =
                    pending
                        ?: return null

                if (
                    current.traceId != traceId ||
                    current.capabilityId != capabilityId
                ) {
                    return null
                }

                current.readyElements?.let {
                    return it.toList()
                }

                val remainingNanos =
                    deadline - System.nanoTime()

                if (remainingNanos <= 0L) {
                    return null
                }

                val remainingMilliseconds =
                    TimeUnit.NANOSECONDS.toMillis(
                        remainingNanos,
                    )

                val remainingNanoseconds =
                    (
                        remainingNanos -
                            TimeUnit.MILLISECONDS.toNanos(
                                remainingMilliseconds,
                            )
                        ).toInt()

                monitor.wait(
                    remainingMilliseconds,
                    remainingNanoseconds,
                )
            }
        }
    }

    fun clear(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ) {
        synchronized(monitor) {
            val current =
                pending
                    ?: return

            if (
                current.traceId == traceId &&
                current.capabilityId == capabilityId
            ) {
                pending = null
                monitor.notifyAll()
            }
        }
    }

    private data class PendingReadiness(
        val traceId: TraceId,
        val capabilityId: CapabilityId,
        val executionAttempted: Boolean,
        val candidateElements: List<AndroidScreenElementRecord>?,
        val readyElements: List<AndroidScreenElementRecord>?,
    )
}
