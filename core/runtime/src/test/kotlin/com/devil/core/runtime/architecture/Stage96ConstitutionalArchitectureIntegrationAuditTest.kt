package com.devil.core.runtime.architecture

import com.devil.core.runtime.DefaultUnifiedDevilRuntime
import com.devil.core.runtime.UnifiedDevilRuntime
import com.devil.core.runtime.authorization.AuthorizationAuthority
import com.devil.core.runtime.capability.CapabilitySelectionAuthority
import com.devil.core.runtime.constitution.ConstitutionValidationAuthority
import com.devil.core.runtime.decision.DecisionAuthority
import com.devil.core.runtime.execution.ExecutionAuthority
import com.devil.core.runtime.executive.ExecutiveReadinessAuthority
import com.devil.core.runtime.identity.IdentityAuthority
import com.devil.core.runtime.learning.EvidenceBasedLearningCoordinator
import com.devil.core.runtime.learning.FailureLearningCoordinator
import com.devil.core.runtime.learning.LearningAuthority
import com.devil.core.runtime.learning.StrategyAdaptationCoordinator
import com.devil.core.runtime.memory.MemoryAuthority
import com.devil.core.runtime.memory.MemoryCommitmentAuthority
import com.devil.core.runtime.memory.MemoryPersistenceAuthority
import com.devil.core.runtime.memory.MemoryProposalAuthority
import com.devil.core.runtime.observation.ObservationAuthority
import com.devil.core.runtime.outcome.OutcomeAuthority
import com.devil.core.runtime.plan.PlanAuthority
import com.devil.core.runtime.task.TaskAuthority
import com.devil.core.runtime.trust.TrustAuthority
import com.devil.core.runtime.understanding.UnderstandingAuthority
import com.devil.core.runtime.verification.VerificationAuthority
import com.devil.core.runtime.worldmodel.WorldModelUpdateAuthority
import com.devil.core.runtime.autonomy.ControlledAutonomyCoordinator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 96 — Constitutional Architecture Integration Audit.
 *
 * This test records architecture truth at the Stage 95 freeze boundary.
 *
 * Stage 96 is an audit. It does not integrate later-stage foundations into
 * DefaultUnifiedDevilRuntime and does not create replacement authorities.
 *
 * In particular:
 *
 * - there remains one UnifiedDevilRuntime contract;
 * - DefaultUnifiedDevilRuntime remains that unified constitutional coordinator;
 * - the protected constitutional authority categories remain distinct;
 * - Memory Authority remains distinct from proposal, commitment, and persistence;
 * - Stage 92–95 coordinators remain bounded preparation foundations;
 * - Stage 92–95 coordinators are not constitutional authorities;
 * - Controlled Autonomy is not Authorization, Decision, Planning, Execution,
 *   Learning, or Memory Authority;
 * - this audit grants no authority and performs no execution.
 *
 * STAGE_96_AUDIT != STAGE_97_RUNTIME_INTEGRATION.
 */
class Stage96ConstitutionalArchitectureIntegrationAuditTest {

    @Test
    fun `default runtime remains the one unified runtime implementation`() {
        assertTrue(
            UnifiedDevilRuntime::class.java
                .isAssignableFrom(DefaultUnifiedDevilRuntime::class.java),
        )
    }

    @Test
    fun `protected constitutional authority contracts remain distinct`() {
        val authorities =
            listOf(
                ConstitutionValidationAuthority::class.java,
                IdentityAuthority::class.java,
                TrustAuthority::class.java,
                AuthorizationAuthority::class.java,
                UnderstandingAuthority::class.java,
                DecisionAuthority::class.java,
                TaskAuthority::class.java,
                PlanAuthority::class.java,
                CapabilitySelectionAuthority::class.java,
                ExecutiveReadinessAuthority::class.java,
                ExecutionAuthority::class.java,
                ObservationAuthority::class.java,
                VerificationAuthority::class.java,
                OutcomeAuthority::class.java,
                LearningAuthority::class.java,
                MemoryProposalAuthority::class.java,
                MemoryAuthority::class.java,
                MemoryCommitmentAuthority::class.java,
                MemoryPersistenceAuthority::class.java,
            )

        assertEquals(
            authorities.size,
            authorities.distinct().size,
        )
    }

    @Test
    fun `memory authority remains distinct from proposal commitment and persistence`() {
        val memoryAuthority = MemoryAuthority::class.java

        assertFalse(
            MemoryProposalAuthority::class.java
                .isAssignableFrom(memoryAuthority),
        )
        assertFalse(
            MemoryCommitmentAuthority::class.java
                .isAssignableFrom(memoryAuthority),
        )
        assertFalse(
            MemoryPersistenceAuthority::class.java
                .isAssignableFrom(memoryAuthority),
        )
    }

    @Test
    fun `stage92 through stage95 coordinators remain bounded non authority foundations`() {
        val coordinators =
            listOf(
                EvidenceBasedLearningCoordinator::class.java,
                FailureLearningCoordinator::class.java,
                StrategyAdaptationCoordinator::class.java,
                ControlledAutonomyCoordinator::class.java,
            )

        val protectedAuthorities =
            listOf(
                AuthorizationAuthority::class.java,
                DecisionAuthority::class.java,
                PlanAuthority::class.java,
                ExecutionAuthority::class.java,
                LearningAuthority::class.java,
                MemoryAuthority::class.java,
            )

        coordinators.forEach { coordinator ->
            protectedAuthorities.forEach { authority ->
                assertFalse(
                    authority.isAssignableFrom(coordinator),
                    "${coordinator.simpleName} must not become ${authority.simpleName}.",
                )
            }
        }
    }

    @Test
    fun `controlled autonomy coordinator is not constitutional authority`() {
        val controlledAutonomy =
            ControlledAutonomyCoordinator::class.java

        val forbiddenAuthorities =
            listOf(
                AuthorizationAuthority::class.java,
                DecisionAuthority::class.java,
                PlanAuthority::class.java,
                ExecutionAuthority::class.java,
                LearningAuthority::class.java,
                MemoryAuthority::class.java,
            )

        forbiddenAuthorities.forEach { authority ->
            assertFalse(
                authority.isAssignableFrom(controlledAutonomy),
                "Controlled Autonomy must not become ${authority.simpleName}.",
            )
        }
    }

    @Test
    fun `world model update authority remains upstream of learning by authority identity`() {
        assertFalse(
            LearningAuthority::class.java
                .isAssignableFrom(WorldModelUpdateAuthority::class.java),
        )

        assertFalse(
            WorldModelUpdateAuthority::class.java
                .isAssignableFrom(LearningAuthority::class.java),
        )
    }
}
