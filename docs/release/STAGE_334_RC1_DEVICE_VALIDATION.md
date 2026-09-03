# Devil Stage 334 — RC1 Device Validation

## Purpose

Stage 334 records bounded physical-device validation of the exact published
Devil V1.1.0 RC1 artifact produced by Stage 333.

Stage 334 does not rebuild RC1, move the RC1 release tag, alter release signing,
change runtime architecture, create execution authority, establish
constitutional acceptance, establish security acceptance, or declare
production readiness.

The physical validation target is the already-published RC1 artifact only.

## Frozen Stage 333 Provenance

Stage 334 began from the frozen Stage 333 completion state:

- Stage 333 completion commit:
  `d3b9a41c80a354808796398cfdb6cf2e1ee59b06`
- Stage 333 RC1 source commit:
  `2784b9ee1dff6db1b1d9452264e1f8e5045296ae`
- RC1 release tag:
  `devil-v1.1.0-rc1`
- GitHub Actions RC1 run:
  `33719347053`

Stage 334 did not rebuild RC1 and did not move or replace the RC1 release tag.

## RC1 Artifact Identity

The physical-device candidate was the exact published RC1 artifact:

- package:
  `com.devil.app`
- versionCode:
  `4`
- versionName:
  `1.1.0-rc1`
- APK SHA-256:
  `44ea0e44b54b179ed2f6e9311a38558ac240ab273979097d8a666895f7fbedd7`
- permanent Devil release certificate SHA-256:
  `96a20adba24a79d102a9c7722a761d290f217270a7e415051849f6a60f73177e`
- certificate identity:
  `CN=Devil V1 Release, OU=Release, O=Devil, C=IN`
- signer key:
  RSA 4096

The verified shared-storage installation copy was:

`~/storage/downloads/Devil-V1.1.0-RC1-VERIFIED.apk`

Its SHA-256 matched the published RC1 APK before installation.

## Existing Installed-Lineage Inspection

Before changing the device installation, Stage 334 inspected the existing
`com.devil.app` package.

The previously installed Devil application reported:

- package:
  `com.devil.app`
- versionCode:
  `3`
- versionName:
  `1.0.0`
- APK SHA-256:
  `3d3fdb311228af944aa04ec45a1a6312e3904f9640615eb79c1a29db935ca0fc`
- signer:
  Android Debug
- signer certificate SHA-256:
  `af6613f2c9c50d532ae6d890b9b8e334785c3229154ed43b77609ded7a0d9f25`

The installed debug signer did not match the permanent Devil release signer.

Therefore versionCode monotonicity alone did not make the existing package
directly upgrade-compatible with RC1. Stage 334 selected a controlled clean
installation path.

## State-Preservation Boundary

Repository inspection before uninstall found:

- `android:allowBackup="false"`;
- the current default Android memory-persistence implementation explicitly
  performs no filesystem, SharedPreferences, DataStore, Room, SQLite, cloud,
  or network persistence write.

No repository evidence established meaningful persisted Devil application
state that had to be preserved before the controlled clean uninstall.

This conclusion was limited to the repository and device evidence inspected
for Stage 334.

## Controlled Clean Installation

An attempted package mutation from an ordinary Termux application UID was
rejected by Android/HyperOS package-manager security boundaries.

The failed Termux uninstall and install attempts were tooling-permission
failures. They were not treated as RC1 artifact or functional failures.

The old debug-signed Devil package was then manually uninstalled through the
Android application-management UI.

Read-only package inspection after that action established that
`com.devil.app` was absent.

The verified RC1 APK was subsequently installed through the normal Android
package-installer UI.

Independent post-install inspection established:

- package:
  `com.devil.app`
- versionCode:
  `4`
- versionName:
  `1.1.0-rc1`
- installed APK SHA-256:
  `44ea0e44b54b179ed2f6e9311a38558ac240ab273979097d8a666895f7fbedd7`
- installed signer certificate SHA-256:
  `96a20adba24a79d102a9c7722a761d290f217270a7e415051849f6a60f73177e`

The APK copied back from the installed package was byte-identical to the
verified shared-storage RC1 APK.

Therefore the physical device contained the exact published Stage 333 RC1
candidate.

## Physical Validation Device

Primary Stage 334 device:

- Redmi Note 12
- Xiaomi model:
  `22111317I`
- Android:
  `14`
- HyperOS environment

Stage 334 validates the bounded observed RC1 paths described below. It does not
claim that every Devil capability was tested.

## Basic RC1 Smoke Observation

The supplied physical-device recording showed:

- successful Devil application launch;
- the expected black/red awakening presentation;
- the central Devil identity mark and `DEVIL INSIDE` presentation;
- successful awakening-to-main-conversation transition;
- main conversation UI availability;
- one typed `Hello Devil` submission;
- one corresponding user conversation entry;
- truthful presentation of:
  `Deferred by the Devil runtime.`;
- visible speaking presentation;
- no observed crash in the recorded smoke path;
- no observed UI freeze in the recorded smoke path;
- no observed duplicate submitted conversation entry;
- no observed broken awakening-to-conversation transition;
- no obvious blocker in the bounded recorded smoke path that invalidated RC1.

The visible `DEFERRED` result remained non-successful.

It was not rewritten as execution success, verified task completion, or
Outcome.

## Voice Observation

A subsequent bounded physical-device validation exercised the existing voice
surface.

The supplied recording showed:

- Devil entering its listening presentation;
- spoken `Hello Devil` being recognized into the conversation path;
- one resulting user submission;
- truthful `Deferred by the Devil runtime.` presentation;
- visible Devil speaking presentation;
- no observed duplicate voice submission;
- no observed crash or stuck-listening blocker in the recorded path.

Voice input provenance did not authenticate the speaker and did not itself
establish Devil authorization or execution approval.

## Accessibility Observation

The clean RC1 installation initially reported Devil accessibility as not
enabled.

The physical-device validation then reached Android/HyperOS Accessibility
settings, displayed the platform accessibility warning, and enabled Devil's
accessibility service through the Android UI.

The recording subsequently showed Devil listed as enabled in the Android
accessibility settings path and a normal return to the Devil conversation UI.

Shell attempts to query secure accessibility settings from the ordinary
Termux UID failed through the Android Binder boundary. Those failed read-only
shell probes were not treated as RC1 failures.

Android accessibility enablement is platform state only. It does not establish
Devil authentication, authorization, execution approval, or verified Outcome.

## Representative Governed Android Request

Stage 334 exercised the already-existing bounded Stage 314 user request:

`Open Settings`

The request was entered once through the normal Devil conversation path.

During this Stage 334 RC1 observation, successful Android Settings execution
was not established. Devil instead presented the truthful non-success result:

`Deferred by the Devil runtime.`

Stage 334 deliberately records that result without upgrading it into a success
claim.

OPEN_SETTINGS_REQUESTED != OPEN_SETTINGS_EXECUTED

DEFERRED != EXECUTION_SUCCESS

DEFERRED != VERIFIED_OUTCOME

This Stage 334 observation does not replace or rewrite the historical Stage 314
real-Android execution evidence.

## Stage 334 Result

The exact published Devil V1.1.0 RC1 artifact completed the bounded Stage 334
device-validation path available for:

- artifact identity;
- checksum identity;
- release-signing identity;
- controlled debug-to-release clean installation;
- exact installed-byte identity;
- physical Redmi Note 12 launch;
- awakening-to-conversation transition;
- typed conversation interaction;
- truthful runtime-result presentation;
- voice listening/recognition presentation;
- voice-output presentation;
- accessibility enablement observation;
- one representative governed Android request;
- absence of an observed blocker in the bounded recorded paths that invalidated
  RC1.

Stage 334 does not establish successful execution for the `Open Settings`
request because the observed runtime result remained `DEFERRED`.

## Constitutional and Release Boundaries

RC1_PUBLISHED != RC1_DEVICE_VALIDATED

RC1_INSTALLED != RC1_FUNCTIONALLY_VALIDATED

RC1_DEVICE_VALIDATED != EVERY_CAPABILITY_VALIDATED

RC1_DEVICE_VALIDATED != CONSTITUTIONAL_ACCEPTANCE

RC1_DEVICE_VALIDATED != SECURITY_ACCEPTANCE

RC1_DEVICE_VALIDATED != PRODUCTION_READINESS

RC1_DEVICE_VALIDATED != PRODUCTION_RELEASE

ANDROID_PERMISSION != DEVIL_AUTHORIZATION

ACCESSIBILITY_ENABLED != DEVIL_AUTHORIZATION

ACCESSIBILITY_CONNECTED != EXECUTION_APPROVAL

VOICE_INPUT != AUTHENTICATION

VOICE_INPUT != AUTHORIZATION

OPEN_SETTINGS_REQUESTED != OPEN_SETTINGS_EXECUTED

DEFERRED != EXECUTION_SUCCESS

DEFERRED != VERIFIED_OUTCOME

INSTALLATION_SUCCESS != EXECUTION_APPROVAL

DEVICE_OBSERVATION != CONSTITUTIONAL_VERIFICATION

STAGE_334 != STAGE_335_RC1_CONSTITUTIONAL_AUDIT

STAGE_334 != STAGE_336_RC1_SECURITY_AUDIT

Stage 335 remains responsible for the RC1 Constitutional Audit.

Stage 336 remains responsible for the RC1 Security Audit.

## Limitations

Stage 334 does not claim:

- every Devil capability was validated;
- every Android action succeeded;
- `Open Settings` executed successfully during this RC1 validation;
- voice input authenticated the speaker;
- accessibility enablement authorized Devil;
- Android permission granted Devil authorization;
- authentication succeeded;
- Owner Mode was established;
- High-Security Confirmation was established;
- execution approval was established;
- a verified runtime Outcome was established;
- constitutional acceptance;
- security acceptance;
- production readiness;
- production release.

The Stage 334 evidence is deliberately bounded to the exact artifact,
installation identity, and physical-device behavior actually observed.
