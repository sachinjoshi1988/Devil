# Devil

Devil is a constitutionally governed, unified AI assistant architecture.

This repository contains the new Devil implementation. It is separate from SJ Lite V1.

## Current milestone

Devil Coding Stage 22 — Complete

Stage 22 established the Android Application and Unified Runtime Boundary Foundation.

The repository now contains a real Android application module with one Android
Application bootstrap, one minimal launcher Activity, and one bounded Android
runtime-entry chain into the existing single UnifiedDevilRuntime.

The Android boundary provides bounded trace-identity generation, observation-time
capture, ContextEnvelope composition, ConversationInput adaptation, runtime
submission, and Android input coordination.

Exactly one DefaultUnifiedDevilRuntime is constructed by DevilApplication.
Android runtime submission reaches that same process-scoped runtime only through
DefaultAndroidRuntimeGateway.

Stage 22 does not introduce a second brain, planner, memory authority, security
authority, runtime, or execution path.

The launcher Activity performs no runtime submission.

Production Android code does not yet choose schema version, provenance, trust
classification, or security classification. Those values remain explicit
constitutional inputs that must be established by their proper authorities before
real Android user input may enter the runtime.

Stage 22 introduces no Android device capability execution, accessibility actions,
speech recognition, text-to-speech, networking, filesystem persistence, database
persistence, cloud persistence, or logical-memory storage.

Stage 22 — Android Application & Unified Runtime Boundary Foundation is complete.

## Official resume point

Tag: `devil-stage-22-complete`

## Core rule

No implementation may bypass:

Constitution → Identity → Trust → Authorization → Understanding → Decision → Task → Plan → Capability → Execution → Observation → Verification → Outcome → World Model Update → Learning → Memory Proposal → Memory Authority → Memory Commitment → Memory Persistence
