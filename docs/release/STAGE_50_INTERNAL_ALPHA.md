# Devil Stage 50 — Internal Alpha APK

## Status

Stage 50 Internal Alpha validation record.

This document records the controlled first Devil V1 Android Internal Alpha build,
release, integrity verification, installation, and initial real-device smoke test.

It does not declare Devil V1 production-ready.

## Source and Release Identity

- Internal Alpha release: `devil-internal-alpha-1`
- APK source commit:
  `2aa4749a27f008a846858138291515e724284518`
- APK workflow governance HEAD after prerelease correction:
  `2960457181392cec075ae93285f54cb20e84f723`
- Application ID: `com.devil.app`
- Version code: `1`
- Version name: `0.1.0`
- Build type: debug
- Release classification: GitHub prerelease
- Release artifact:
  `devil-internal-alpha.apk`
- Checksum artifact:
  `devil-internal-alpha.apk.sha256`

The workflow-governance correction at `2960457` does not change the already-built
Alpha 1 APK. Alpha 1 remains tied to its original source commit `2aa4749`.

## CI Build Gate

The first Internal Alpha GitHub Actions run completed successfully.

The workflow successfully completed:

- repository checkout;
- JDK 17 setup;
- removal of the Termux-only AAPT2 override inside CI;
- core model tests;
- core runtime tests;
- complete app debug unit tests;
- debug APK assembly;
- release-file preparation;
- SHA-256 generation;
- release-file verification;
- GitHub Release creation.

The Internal Alpha workflow is manually triggered through `workflow_dispatch`.

Future Internal Alpha releases are explicitly created as GitHub prereleases.

## APK Integrity

Downloaded release files:

- `devil-internal-alpha.apk`
- `devil-internal-alpha.apk.sha256`

Expected SHA-256:

`f0839a24d6974e1558ee9601037451b213c7c1d5808c20301f82b889ce5833be`

Local checksum verification result:

`devil-internal-alpha.apk: OK`

Therefore the installed Internal Alpha APK was verified against the checksum
published with the GitHub Release.

## First Real-Device Smoke Test

Device:

- Redmi Note 12
- Android 14 / HyperOS

Observed successfully:

- APK installation;
- application launch;
- Devil conversation UI rendering;
- empty conversation state rendering;
- text entry;
- Send control behavior;
- submission of `Hello Devil`;
- presentation of the user conversation entry;
- entry into the Unified Devil Runtime;
- truthful runtime response:
  `Deferred by the Devil runtime.`;
- Android voice-output presentation of the runtime response;
- accessibility-service diagnostic presentation while Devil accessibility was
  disabled.

The runtime `DEFERRED` result is not treated as execution success, verified task
completion, or production conversational intelligence.

## Constitutional Boundaries Preserved

The Internal Alpha build does not change these invariants:

- Android permission is not Devil authorization.
- Accessibility enablement is not Devil authorization.
- Wake attention is not authentication.
- Execution approval is not execution evidence.
- Execution attempt is not observation.
- Observation is not verification.
- Verification is not Outcome.
- Memory proposal is not memory commitment.
- Memory commitment is not persistence.
- Runtime acceptance is not verified task success.

No Stage 50 release mechanism creates another Brain, Executive, Planner, Memory
Authority, runtime, or constitutional path.

## Alpha Limitations

This is an internal debug-signed test build.

It is not:

- a production APK;
- a release-candidate APK;
- a final signed Devil V1 APK;
- evidence that every Android capability is operational;
- evidence that every constitutional authority currently produces a completed
  result;
- evidence that all device-specific HyperOS behavior is solved;
- evidence of final visual identity, final theme, final logo, or final voice.

Further owner testing and stabilization remain required by later Devil stages.

## Rollback

The first Internal Alpha APK can be traced exactly to source commit:

`2aa4749a27f008a846858138291515e724284518`

The previous protected V1 development checkpoint is:

`devil-stage-49-complete`

Stage 50 must not be considered officially complete until its final closure gate
passes and the official Stage 50 completion tag is created deliberately.
