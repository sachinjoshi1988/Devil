package com.devil.core.model.task

import com.devil.core.model.authorization.AuthorizationEvaluationState
import com.devil.core.model.decision.DecisionState

/**
 * Immutable Stage 116 request for bounded compound-work Task re-entry.
 *
 * The request preserves:
 *
 * - one exact Stage 115 CompoundWorkReconsiderationRecord;
 * - the exact fresh selected Decision already preserved by that record;
 * - the exact Stage 114 continuation record transitively;
 * - the exact Stage 77 CompoundWorkRequest transitively;
 * - the exact eligible Stage 77 CompoundWorkStep transitively;
 * - and one model-safe representation that current constitutional authorization
 *   was explicitly established before this request was prepared.
 *
 * Stage 116 does not reconstruct, reinterpret, copy, replace, or infer any of
 * those constitutional records.
 *
 * The runtime Authorization Authority result remains runtime-owned. The model
 * request preserves only the already-established model authorization state
 * supplied after the runtime boundary has been checked.
 *
 * Creating this request does not:
 *
 * - create a TaskRecord;
 * - create or generate a TaskId;
 * - invoke TaskAuthority;
 * - resolve Identity;
 * - establish Trust;
 * - grant Authorization;
 * - replace Understanding;
 * - replace the fresh Decision;
 * - create a Plan;
 * - select or activate a capability;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - operate Memory;
 * - automatically continue compound work;
 * - grant Controlled Autonomy;
 * - invoke a platform;
 * - or invoke a network.
 *
 * RECONSIDERATION != AUTHORIZATION.
 * AUTHORIZATION != TASK_CREATED.
 * RECONSIDERATION != TASK_CREATED.
 * TASK_REENTRY_REQUEST != TASK_CREATED.
 * TASK_REENTRY_REQUEST != TASK_AUTHORITY_RESULT.
 * AUTHORIZATION_STATE != AUTHORIZATION_AUTHORITY.
 * TASK_REENTRY != AUTOMATIC_CONTINUATION.
 * TASK_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkTaskReentryRequest private constructor(
    val reconsideration: CompoundWorkReconsiderationRecord,
    val authorizationState: AuthorizationEvaluationState,
) {
    companion object {

        fun create(
            reconsideration: CompoundWorkReconsiderationRecord,
            authorizationState: AuthorizationEvaluationState,
        ): CompoundWorkTaskReentryRequest {
            require(
                reconsideration.request.freshDecision.state ==
                    DecisionState.SELECTED,
            ) {
                "Compound-work Task re-entry requires the preserved fresh Decision to remain selected."
            }

            require(
                authorizationState ==
                    AuthorizationEvaluationState.AUTHORIZED,
            ) {
                "Compound-work Task re-entry requires explicitly established current constitutional authorization."
            }

            return CompoundWorkTaskReentryRequest(
                reconsideration = reconsideration,
                authorizationState = authorizationState,
            )
        }
    }
}
