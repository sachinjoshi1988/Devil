# Devil

Devil is a constitutionally governed, unified AI assistant architecture.

This repository contains the new Devil implementation. It is separate from SJ Lite V1.

## Current milestone

Devil Coding Stage 23 — Complete

Stage 23 established the Constitutional Security and Session Foundation.

The repository now contains explicit constitutional security-stage, security-state,
security-transition, session-identity, session-lifecycle, session-record, and
session-validity contracts.

Security transition handling is separated into bounded request, evaluation,
result-mapping, and authority layers. The default transition evaluator safely
defers when genuine transition policy or required constitutional evidence is not
available.

Session validity handling is likewise separated into bounded request, evaluation,
result-mapping, and authority layers.

The default session-validity evaluator determines validity only from the explicit
session lifecycle state, approved validity window, and authoritative observation
time supplied by the request. An ACTIVE session is valid only at or after its
establishment time and strictly before expiration. EXPIRED and REVOKED sessions
are invalid.

Stage 23 does not authenticate a subject, prove owner identity, establish trust,
grant authorization, create, extend, renew, or revoke sessions, advance
SecurityStage, enter Owner Mode, approve high-security confirmation, grant Android
permission, invoke Android credentials, or permit capability execution.

Security remains a system authority rather than a feature. No evaluator, mapper,
coordinator, session record, request, or result may grant itself authority.

Stage 23 — Constitutional Security and Session Foundation is complete.

## Official resume point

Tag: `devil-stage-23-complete`

## Core rule

No implementation may bypass:

Constitution → Identity → Trust → Authorization → Understanding → Decision → Task → Plan → Capability → Execution → Observation → Verification → Outcome → World Model Update → Learning → Memory Proposal → Memory Authority → Memory Commitment → Memory Persistence
