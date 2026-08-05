# Unified Devil Runtime Pipeline

## Purpose

The Unified Devil Runtime is the single constitutional entry path for work entering Devil.

It coordinates bounded authorities but must not absorb their responsibilities or become a second Brain, Planner, Security Authority, Memory Authority, Executive, Capability implementation, Observation authority, or Verification authority.

## Constitutional Order

No runtime path may bypass:

Constitution → Identity → Trust → Authorization → Understanding → Decision → Task → Capability → Verification → Outcome

## Operational Pipeline

1. Context Acceptance
2. Constitutional Validation
3. Identity and Trace Continuity
4. Trust Evaluation
5. Authorization Evaluation
6. Understanding
7. Decision
8. Task Creation
9. Planning
10. Capability Selection
11. Executive Readiness
12. Execution
13. Observation
14. Verification
15. Outcome
16. Communication

## Runtime Responsibilities

The Unified Devil Runtime may:

- accept a validated ContextEnvelope;
- preserve trace continuity;
- coordinate the constitutional sequence;
- return structured runtime results;
- stop, reject, or defer work when required;
- route failures through UniversalErrorRecord;
- prevent stages from being skipped;
- prevent capabilities from inventing authority;
- prevent unverified success claims.

## Runtime Prohibitions

The Unified Devil Runtime must not:

- grant authority;
- replace Security;
- perform Brain reasoning;
- create or change goals;
- create plans;
- implement capabilities;
- execute Android or platform actions directly;
- commit logical memory;
- invent observations;
- claim an outcome without verification;
- bypass the constitutional order.

## Stage 1 Runtime Meaning

During Stage 1, DefaultUnifiedDevilRuntime is deliberately non-executing.

RuntimeStatus.ACCEPTED means only:

- the ContextEnvelope was accepted into the constitutional runtime boundary;
- trace identity was preserved;
- a structured RuntimeResult was returned.

It does not mean:

- understanding completed;
- authorization was granted;
- a decision was selected;
- a task was created;
- planning occurred;
- a capability was selected;
- execution began;
- execution succeeded;
- an outcome was verified.

## Future Growth Rule

Future runtime stages must be introduced through bounded interfaces and independently testable authorities.

DefaultUnifiedDevilRuntime must remain a coordinator and must not become a giant implementation containing the full Devil system.

## Stage 2 Runtime Meaning

During Stage 2, DefaultUnifiedDevilRuntime coordinates the bounded
constitutional path through Executive Readiness:

Constitutional Validation → Identity → Trust → Authorization →
Understanding → Decision → Task → Planning → Capability Selection →
Executive Readiness

The default Stage 2 authorities remain deliberately conservative. Where the
required intelligence or operating subsystem does not yet exist, they return a
structured deferred result rather than inventing identity, authority,
understanding, decisions, tasks, plans, capabilities, or readiness.

RuntimeStatus.DEFERRED means:

- the ContextEnvelope entered the unified constitutional runtime;
- constitutional ordering and trace continuity were preserved;
- the completed Stage 2 authorities were coordinated;
- work stopped honestly before the execution boundary.

It does not mean:

- execution began;
- a capability was available or healthy;
- operating-system permission existed;
- an action succeeded;
- an observation occurred;
- verification occurred;
- an outcome was established.

Execution, Observation, Verification, Outcome production, and Communication
remain later-stage responsibilities and are not implemented by Stage 2.

## Stage 3 Identity Runtime Meaning

During Stage 3, the Identity authority coordinates the bounded internal path:

ContextEnvelope → Identity Resolution Request Provider → Identity Resolution
Resolver → Identity Resolution Result Mapper → IdentityResult

Stage 3 establishes:

- a stable identity identifier;
- bounded owner and subject context;
- identity-evidence provenance;
- coherent identity-evidence sets;
- structured resolution requests;
- unique candidate collections;
- bounded identity confidence;
- explicit resolved, unresolved, and ambiguous resolution states;
- structured resolution selections and records;
- a conservative runtime mapping to the stable IdentityResult contract;
- an internal request-provider, resolver, and mapper chain;
- trace continuity across the identity runtime boundary.

The default Stage 3 provider returns UNAVAILABLE because ContextEnvelope does not
contain genuine subject identity evidence. The default Identity authority
therefore returns IdentityStatus.UNRESOLVED rather than fabricating an identity,
evidence, confidence, ownership claim, or successful resolution.

Identity resolution does not mean:

- the subject was authenticated;
- ownership was proven;
- a relationship was established;
- trust was granted;
- authorization was granted;
- Owner Mode was entered;
- execution was permitted;
- an action or outcome was verified.

OwnerContext records the bounded owner and current-subject identities only. It
does not itself prove ownership, authentication, trust, relationship, authority,
or permission to act.

Future identity growth must enter through genuine evidence providers and bounded
resolution policy. DefaultIdentityAuthority must remain the Identity coordinator
and must not absorb Trust, Authorization, Security, Brain, Planning, Execution,
Observation, Verification, or Memory responsibilities.

## Stage 4 Trust and Authorization Runtime Meaning

During Stage 4, the Trust authority coordinates the bounded internal path:

ContextEnvelope + IdentityResult → Trust Evaluation Request Provider →
Trust Evaluation Resolver → Trust Evaluation Result Mapper → TrustResult

The Authorization authority coordinates the bounded internal path:

ContextEnvelope + IdentityResult + TrustResult →
Authorization Evaluation Request Provider →
Authorization Evaluation Resolver →
Authorization Evaluation Result Mapper → AuthorizationResult

Stage 4 establishes:

- a subject-specific trust classification separate from context provenance;
- structured trust assessments and evaluation requests;
- bounded trust request-provider, resolver, and mapper contracts;
- structured constitutional authorization assessments and requests;
- explicit authorized, denied, and deferred continuation states;
- bounded authorization request-provider, resolver, and mapper contracts;
- trace continuity across trust and authorization runtime boundaries;
- conservative default behavior when genuine policy inputs are unavailable.

ContextTrustLevel describes supplied-context provenance. SubjectTrustLevel describes
a bounded subject trust assessment. Neither classification automatically grants
authorization.

The default trust resolver returns SubjectTrustLevel.UNESTABLISHED because no
subject-trust policy is available. The stable TrustResult contract currently
exposes ContextTrustLevel rather than TrustAssessment, so the default trust
result mapper returns TrustStatus.DEFERRED rather than fabricating runtime trust.

The default authorization request provider returns UNAVAILABLE because the
stable TrustResult contract does not expose the genuine TrustAssessment required
by AuthorizationEvaluationRequest. DefaultAuthorizationAuthority therefore
returns AuthorizationStatus.DEFERRED rather than reconstructing subject trust or
inventing authority.

Trust evaluation does not mean:

- the subject was authenticated;
- ownership was proven;
- authorization was granted;
- Owner Mode was entered;
- a capability was authorized;
- operating-system permission existed;
- execution was permitted;
- an outcome was verified.

Constitutional authorization applies only to continuation beyond the
authorization boundary. It does not authorize an individual capability, grant
operating-system permission, enter Owner Mode, perform execution, observe a
result, verify completion, or establish an outcome.

Future trust and authorization growth must enter through genuine bounded policy
and evidence providers. DefaultTrustAuthority and DefaultAuthorizationAuthority
must remain coordinators and must not absorb Identity, Security, Brain, Planning,
Capability, Execution, Observation, Verification, Outcome, or Memory
responsibilities.

## Stage 5 Conversation and Context Intake Runtime Meaning

During Stage 5, the unified runtime entry contract changed from ContextEnvelope to ConversationInput.

ConversationInput owns the authoritative ContextEnvelope and carries normalized, non-blank textual content without interpreting language or inferring intent.

The unified runtime now preserves the ordered path:

ConversationInput → Constitutional Validation → Identity → Trust → Authorization → Conversation Intake → Understanding → Decision → Task → Plan → Capability Selection → Executive Readiness

Stage 5 establishes:

- one bounded ConversationInput model;
- explicit accepted, deferred, and rejected intake states;
- structured conversation-intake records and results;
- a bounded Conversation Intake Authority;
- produced, deferred, and failed operational authority states;
- trace continuity across conversation intake;
- one unified runtime entry path;
- explicit placement of Conversation Intake after Authorization and before Understanding;
- preservation of the original conversation input without semantic interpretation.

Conversation Intake does not mean:

- language was understood;
- intent was inferred;
- identity was resolved;
- trust was established;
- authorization was granted;
- memory was created;
- a decision was selected;
- a task or plan was created;
- a capability was executed;
- an outcome was observed or verified.

DefaultConversationIntakeAuthority maps established constitutional authorization state into bounded intake state:

- AUTHORIZED → ACCEPTED;
- DENIED → REJECTED;
- DEFERRED → DEFERRED;
- FAILED → matching propagated failure.

The default authority does not inspect or interpret textual content.

UnderstandingAuthority now requires the completed ConversationIntakeAuthorityResult and verifies trace continuity before understanding may proceed. DefaultUnderstandingAuthority still returns DEFERRED because no language-understanding policy is available yet.

Future voice, keyboard, vision, notification, automation, and other modalities must normalize into the same unified conversation pipeline. They must not create separate runtime paths or separate Devil intelligences.

Conversation Intake must remain separate from Understanding, Memory, Identity, Trust, Authorization, Decision, Planning, Capability, Execution, Observation, Verification, and Outcome responsibilities.

## Stage 6 Structured Understanding Runtime Meaning

During Stage 6, the Understanding Authority changed from an always-deferred placeholder into a bounded coordinator.

The structured internal path is:

ConversationIntakeAuthorityResult → Understanding Evaluation Request Provider → UnderstandingEvaluationRequest → Understanding Evaluation Resolver → UnderstandingRecord → Understanding Evaluation Result Mapper → UnderstandingAuthorityResult

Stage 6 establishes:

- one structured UnderstandingEvaluationRequest model;
- explicit available, unavailable, and failed request-construction states;
- a bounded Understanding Evaluation Request Provider;
- a bounded Understanding Evaluation Resolver;
- a bounded Understanding Evaluation Result Mapper;
- trace continuity across all Understanding handoffs;
- operational separation between produced understanding and understanding quality;
- conservative default behavior when no structured language-understanding policy exists.

UnderstandingEvaluationRequest preserves the completed ConversationIntakeResult. It does not duplicate the authoritative ContextEnvelope or textual content.

DefaultUnderstandingEvaluationRequestProvider creates a request only when Conversation Intake produced an ACCEPTED intake record.

Its behavior is:

- produced ACCEPTED intake → AVAILABLE;
- produced DEFERRED intake → UNAVAILABLE;
- produced REJECTED intake → UNAVAILABLE;
- deferred Conversation Intake Authority result → UNAVAILABLE;
- failed Conversation Intake Authority result → matching propagated failure.

DefaultUnderstandingEvaluationResolver does not infer intent from textual content. Because no structured language-understanding policy is available, it preserves the authoritative context and produces:

- UnderstandingState.UNSUPPORTED;
- summary: “No structured language-understanding policy is available.”

This is a genuinely produced UnderstandingRecord. UNSUPPORTED describes understanding quality and does not mean the Understanding Authority failed operationally.

DefaultUnderstandingEvaluationResultMapper maps every valid UnderstandingRecord to UnderstandingAuthorityStatus.PRODUCED while preserving the record’s COMPLETE, AMBIGUOUS, INCOMPLETE, or UNSUPPORTED state unchanged.

An unavailable evaluation request produces UnderstandingAuthorityStatus.DEFERRED. A failed request-construction result produces UnderstandingAuthorityStatus.FAILED with the matching error.

Structured Understanding does not mean:

- intent was inferred;
- a decision was selected;
- memory was created;
- a task or plan was created;
- a capability was authorized;
- execution occurred;
- an outcome was observed or verified.

DefaultUnderstandingAuthority must remain a coordinator and must not absorb Conversation Intake, Identity, Trust, Authorization, Memory, Brain, Decision, Task, Planning, Capability, Execution, Observation, Verification, or Outcome responsibilities.

Future language understanding must enter through bounded policies or resolver implementations. It must not bypass Conversation Intake, fabricate meaning, or create a parallel intelligence path.

## Stage 7 Constitutional Decision Runtime Meaning


Stage 7 establishes the bounded Constitutional Decision Foundation without
introducing a constitutional reasoning engine or decision policy.

The runtime now coordinates the following bounded chain:

UnderstandingAuthorityResult
        ↓
DecisionEvaluationRequestProvider
        ↓
DecisionEvaluationRequestResult
        ↓
DecisionEvaluationResolver
        ↓
DecisionRecord
        ↓
DecisionEvaluationResultMapper
        ↓
DecisionAuthorityResult

DecisionEvaluationRequest preserves one produced UnderstandingRecord as the
authoritative input to constitutional decision evaluation. It does not
reinterpret understanding, create memory, create tasks, create plans,
authorize capabilities, execute actions, observe outcomes, or verify results.

DefaultDecisionEvaluationRequestProvider creates an evaluation request only
when Understanding Authority produced one bounded UnderstandingRecord.
Deferred understanding remains unavailable while failed understanding
propagates its matching error.

DefaultDecisionEvaluationResolver currently preserves the supplied
UnderstandingRecord and returns DecisionState.DEFERRED because no
constitutional decision policy has been implemented yet. It never fabricates
a selected decision, rejection, or clarification requirement.

DefaultDecisionEvaluationResultMapper converts every valid DecisionRecord into
a produced DecisionAuthorityResult while preserving the DecisionState inside
the DecisionRecord.

DefaultDecisionAuthority now acts only as the bounded coordinator of the
decision-evaluation chain. It validates trace continuity, requests evaluation,
delegates resolution, maps the DecisionRecord, and preserves propagated
failures without performing identity resolution, trust evaluation,
authorization, understanding, task creation, planning, capability selection,
execution, observation, or verification.

The unified runtime therefore continues as:

Understanding
    ↓
Decision
    ↓
Task

Stage 7 completes the Constitutional Decision Foundation while preserving one
authoritative constitutional decision path and preventing fabricated
DecisionRecords in the absence of justified policy evidence.

## Stage 8 Constitutional Task Runtime Meaning


Stage 8 establishes the bounded Constitutional Task Foundation without
introducing task-identity fabrication, planning behavior, capability binding,
execution, observation, verification, or outcome responsibilities.

The runtime now coordinates the following bounded chain:

DecisionAuthorityResult
        ↓
TaskCreationRequestProvider
        ↓
TaskCreationRequestResult
        ↓
TaskIdentityProvider
        ↓
TaskIdentityProvisionResult
        ↓
TaskCreationResolver
        ↓
TaskRecord
        ↓
TaskCreationResultMapper
        ↓
TaskAuthorityResult

A task-creation request is available only when the Decision Authority has
produced one DecisionRecord whose DecisionState is SELECTED.

Decision records in DEFERRED, REQUIRES_CLARIFICATION, or REJECTED state do not
create task requests. A deferred Decision Authority result also remains
unavailable, while decision failure propagates its matching error.

Task identity is a separate bounded dependency. TaskId validates and represents
an existing identity, but it does not generate one. No approved constitutional
task-identity policy exists yet, so DefaultTaskIdentityProvider returns
UNAVAILABLE rather than fabricating identity from trace data or hard-coded
values.

When one genuine TaskId is available, DefaultTaskCreationResolver preserves the
selected DecisionRecord, applies the supplied TaskId, and creates exactly one
TaskRecord in TaskState.CREATED. The task summary is preserved from the
DecisionRecord and is not reinterpreted.

DefaultTaskCreationResultMapper maps every valid TaskRecord as operationally
TaskAuthorityStatus.CREATED. The task lifecycle state remains inside TaskState
and is not converted into operational deferral or failure.

DefaultTaskAuthority is the coordinator for this chain. It validates trace
continuity across context, identity, trust, authorization, understanding,
decision, request preparation, task identity provision, and mapped task result.

The default runtime therefore safely defers task creation because no genuine
task identity is available. This is intentional constitutional behavior and
must not be bypassed by inventing task identities.

Constitutional Task does not mean:

- planning work,
- selecting or binding capabilities,
- authorizing execution,
- executing platform actions,
- observing execution,
- verifying outcomes,
- or changing final outcome state.

Those responsibilities remain with their own later authorities.

Future task-identity policy must enter through a bounded TaskIdentityProvider
implementation. It must not be hidden inside TaskCreationResolver,
DefaultTaskAuthority, or the unified runtime coordinator.

## Stage 9 Constitutional Planning Runtime Meaning


Stage 9 establishes the bounded Constitutional Planning Foundation without introducing planning intelligence, capability binding, execution, observation, verification, or outcome responsibilities.

The runtime now coordinates the following bounded chain:

TaskAuthorityResult
        ↓
PlanCreationRequestProvider
        ↓
PlanCreationRequestResult
        ↓
PlanningStrategyProvider
        ↓
PlanningStrategyProvisionResult
        ↓
PlanIdentityProvider
        ↓
PlanIdentityProvisionResult
        ↓
PlanCreationResolver
        ↓
PlanRecord
        ↓
PlanCreationResultMapper
        ↓
PlanAuthorityResult

A plan-creation request is available only when the Task Authority has produced one TaskRecord.

Planning strategy and plan identity remain separate bounded dependencies. Neither may be fabricated by the runtime or the Plan Authority.

DefaultPlanAuthority coordinates request preparation, planning strategy, plan identity, plan creation, and result mapping while preserving trace continuity across every handoff.

The default runtime safely defers planning whenever no genuine planning strategy or plan identity is available. This is intentional constitutional behavior and must never be bypassed.

Constitutional Planning does not mean:

- selecting capabilities,
- authorizing execution,
- executing platform actions,
- observing execution,
- verifying outcomes,
- or changing final outcome state.

Those responsibilities remain with their own later authorities.

Future planning policy must enter through bounded PlanningStrategyProvider and PlanIdentityProvider implementations. It must not be hidden inside PlanCreationResolver, DefaultPlanAuthority, or the unified runtime coordinator.

## Stage 10 Constitutional Capability Selection Runtime Meaning


Stage 10 establishes the bounded Constitutional Capability Selection Foundation without introducing capability-selection policy, capability authorization, execution, observation, verification, or outcome responsibilities.

The runtime now coordinates the following bounded chain:

PlanAuthorityResult
        ↓
CapabilitySelectionRequestProvider
        ↓
CapabilitySelectionRequestResult
        ↓
CapabilityRegistry
        ↓
CapabilityRegistryResult
        ↓
CapabilitySelectionResolver
        ↓
CapabilitySelectionResolutionResult
        ↓
CapabilitySelectionResultMapper
        ↓
CapabilitySelectionResult

A capability-selection request is available only when the Plan Authority has produced one PlanRecord.

The Capability Registry exposes existing registered capabilities only. It does not fabricate registrations, select capabilities, authorize capabilities, activate capabilities, execute actions, observe results, verify outcomes, or report final outcomes.

DefaultCapabilitySelectionResolver resolves at most one registered capability from the bounded request and registry result. No approved constitutional capability-selection policy exists yet, so the default resolver intentionally returns UNAVAILABLE rather than inventing selection policy or choosing a capability without justified constitutional evidence.

DefaultCapabilitySelectionResultMapper converts bounded capability-selection resolution into the stable operational CapabilitySelectionResult contract. A resolved capability becomes SELECTED, resolution unavailability becomes DEFERRED, and resolution failure preserves its matching error.

DefaultCapabilitySelectionAuthority coordinates request preparation, capability registry access, capability resolution, and result mapping while preserving trace continuity across every constitutional handoff.

The default runtime therefore safely defers capability selection whenever no genuine constitutional capability-selection policy can justify selecting one registered capability. This intentional behavior must never be bypassed by fabricated policy or arbitrary capability selection.

Constitutional Capability Selection does not mean:

- granting authorization,
- evaluating operating-system permissions,
- checking capability health or readiness,
- executing platform actions,
- observing execution,
- verifying outcomes,
- or changing final outcome state.

Those responsibilities remain with their own later authorities.

Future constitutional capability-selection policy must enter through bounded CapabilitySelectionResolver implementations. It must not be hidden inside DefaultCapabilitySelectionAuthority, DefaultCapabilitySelectionResultMapper, the Capability Registry, or the unified runtime coordinator.
