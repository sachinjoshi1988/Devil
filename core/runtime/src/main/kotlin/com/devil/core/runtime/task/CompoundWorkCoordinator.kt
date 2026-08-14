package com.devil.core.runtime.task

import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.task.CompoundWorkRequest
import com.devil.core.model.task.CompoundWorkStep

/**
 * Stage 77 bounded compound-work coordinator.
 *
 * The coordinator accepts:
 *
 * - one already-existing constitutional DecisionRecord; and
 * - one explicitly supplied ordered list of bounded step intentions.
 *
 * It does not split raw user text, reinterpret the selected goal, invent steps,
 * create additional Brain decisions, create TaskRecord values, create plans,
 * select capabilities, authorize actions, execute, observe, verify, establish
 * Outcomes, update the World Model, perform Learning, or create Memory.
 *
 * A selected Decision with fewer than two supplied step intentions remains
 * outside compound-work handling and is DEFERRED.
 *
 * Compound preparation != compound execution.
 */
class CompoundWorkCoordinator {

    fun prepare(
        decision: DecisionRecord,
        stepSummaries: List<String>,
    ): CompoundWorkPreparationResult {
        val traceId =
            decision.understanding.context.traceId

        if (decision.state != DecisionState.SELECTED) {
            return CompoundWorkPreparationResult.create(
                traceId = traceId,
                status = CompoundWorkPreparationStatus.DEFERRED,
            )
        }

        if (stepSummaries.size < MINIMUM_COMPOUND_STEP_COUNT) {
            return CompoundWorkPreparationResult.create(
                traceId = traceId,
                status = CompoundWorkPreparationStatus.DEFERRED,
            )
        }

        val steps =
            stepSummaries.mapIndexed { index, summary ->
                CompoundWorkStep.create(
                    position = index + 1,
                    summary = summary,
                )
            }

        val request =
            CompoundWorkRequest.create(
                decision = decision,
                steps = steps,
            )

        return CompoundWorkPreparationResult.create(
            traceId = traceId,
            status = CompoundWorkPreparationStatus.PREPARED,
            request = request,
        )
    }

    private companion object {
        const val MINIMUM_COMPOUND_STEP_COUNT: Int = 2
    }
}
