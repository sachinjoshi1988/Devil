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
