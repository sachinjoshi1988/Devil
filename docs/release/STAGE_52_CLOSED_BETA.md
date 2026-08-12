# Devil Stage 52 — Closed Beta APK

## Status

Stage 52 Closed Beta release contract and validation record.

Current status:

- Stage 51 Owner Alpha Stabilization is formally complete.
- The protected Stage 51 completion point is `devil-stage-51-complete`.
- The accepted Stage 51 implementation is
  `d75797aba9fd535d1e57f131f9e3ad88d0b930ef`.
- Closed Beta release infrastructure is established and the first Closed Beta artifact has completed its recorded validation path.
- Closed Beta 1 validation evidence is recorded below; Stage 52 remains pending final repository closure and the official completion tag.

Stage 52 must not be considered officially complete until the Closed Beta closure gate passes and the official Stage 52 completion tag is created deliberately.

## Purpose

Stage 52 advances Devil V1 from owner-alpha stabilization into controlled Closed Beta distribution.

This is a release-maturity stage.

It does not create a new Brain, Executive, Planner, Memory Authority, Security Architecture, runtime, logical memory domain, or constitutional execution path.

It does not authorize unrelated feature expansion or deferred visual redesign.

The Closed Beta release must preserve the existing Unified Devil Runtime and all frozen constitutional boundaries.

## Stage 51 Baseline

The Stage 52 baseline begins from the formally frozen Stage 51 checkpoint:

- Completion tag: `devil-stage-51-complete`
- Baseline commit:
  `d75797aba9fd535d1e57f131f9e3ad88d0b930ef`
- Accepted Owner Alpha tag:
  `devil-internal-alpha-6`

Stage 52 changes must remain traceable from this baseline.

## Closed Beta Release Identity

The dedicated Closed Beta workflow is:

`.github/workflows/closed-beta-apk.yml`

The workflow is manually triggered through `workflow_dispatch`.

Closed Beta releases use:

- release tag family:
  `devil-closed-beta-*`
- APK artifact:
  `devil-closed-beta.apk`
- checksum artifact:
  `devil-closed-beta.apk.sha256`
- GitHub release classification:
  prerelease
- current Android build type:
  debug

The Closed Beta artifact remains a controlled test artifact.

Closed Beta does not mean release candidate or production.

## CI Build Gate

Every Closed Beta workflow run must perform:

- repository checkout;
- JDK setup;
- Gradle wrapper preparation;
- removal of the Termux-only AAPT2 override inside CI;
- Gradle environment verification;
- core model tests;
- core runtime tests;
- complete app debug unit tests;
- debug APK assembly;
- Closed Beta release-file preparation;
- SHA-256 generation;
- checksum verification;
- GitHub prerelease creation.

A failed required gate means the Closed Beta release is not validated.

## Source Provenance

Every Closed Beta GitHub Release must identify its exact source commit.

The workflow targets the GitHub Release at:

`GITHUB_SHA`

Therefore release provenance must be verified against:

1. the workflow source commit;
2. the GitHub release target;
3. the actual Git tag target;
4. the downloaded APK checksum.

Matching names alone are insufficient evidence.

## APK Integrity

The workflow publishes:

- `devil-closed-beta.apk`
- `devil-closed-beta.apk.sha256`

Before installation, the downloaded APK must be independently verified against the published SHA-256 checksum.

The exact verified APK must be the artifact installed for device testing.

An APK with ambiguous download provenance must not be treated as validated.

## Closed Beta Device Gate

At minimum, the Stage 52 Closed Beta artifact must be tested on the primary Devil physical test device:

- Redmi Note 12
- Android 14 / HyperOS

The validation record must truthfully record the actual observed results.

Required Closed Beta verification includes:

- exact APK provenance;
- checksum verification;
- successful installation;
- successful application launch;
- accepted Stage 51 awakening presentation remaining intact;
- conversation UI availability;
- typed submission path;
- Unified Devil Runtime entry where constitutionally available;
- truthful runtime-result presentation;
- voice-output behavior where available;
- accessibility diagnostic behavior where applicable;
- no observed regression that invalidates the Closed Beta baseline.

A capability that truthfully reports unavailable, deferred, degraded, denied, failed, or partial status must not be rewritten as success.


## Closed Beta 1 Validation Evidence

### Workflow and Source

The first Stage 52 Closed Beta artifact was produced by:

- GitHub Actions workflow:
  `Devil Closed Beta APK`
- Workflow run:
  `31553641457`
- Workflow result:
  `success`
- Workflow source commit:
  `9ec1ae699a074fcf2a2d19b179b1d5fcab5bc93f`

The workflow source commit exactly matched the Stage 52 Closed Beta infrastructure commit.

The required workflow gates completed successfully, including:

- core model tests;
- core runtime tests;
- complete app debug unit tests;
- debug APK assembly;
- Closed Beta release-file preparation;
- SHA-256 generation;
- SHA-256 verification;
- GitHub Closed Beta prerelease creation.

### Release Identity and Provenance

The resulting GitHub Closed Beta release is:

- release tag:
  `devil-closed-beta-1`
- release name:
  `Devil Closed Beta 1`
- release classification:
  prerelease
- release target:
  `9ec1ae699a074fcf2a2d19b179b1d5fcab5bc93f`

The actual Git tag target was independently fetched and resolved to:

`9ec1ae699a074fcf2a2d19b179b1d5fcab5bc93f`

Therefore:

workflow source commit
=
GitHub Release target
=
actual Git tag target.

### Published Artifacts and Integrity

The Closed Beta release published:

- `devil-closed-beta.apk`
- `devil-closed-beta.apk.sha256`

Published APK SHA-256:

`18a410910ff8db441d401f2d08607b58889217f3532c7845547427f6079267bc`

The release assets were downloaded through `gh` into an isolated verification directory.

The independently calculated downloaded APK SHA-256 was:

`18a410910ff8db441d401f2d08607b58889217f3532c7845547427f6079267bc`

`sha256sum --check` result:

`devil-closed-beta.apk: OK`

A clearly identified shared-storage install copy was then created:

`~/storage/downloads/Devil-Closed-Beta-1-VERIFIED.apk`

Its independently calculated SHA-256 was also:

`18a410910ff8db441d401f2d08607b58889217f3532c7845547427f6079267bc`

Therefore the prepared physical-device test artifact remained byte-exact with the published Closed Beta APK.

### Physical-Device Closed Beta Validation

Primary validation device:

- Redmi Note 12
- Android 14 / HyperOS

The verified Closed Beta install copy was used for the Stage 52 physical-device validation path.

The supplied physical-device screen recording showed:

- Devil launching successfully;
- the accepted Stage 51 `DEVIL INSIDE` awakening remaining intact;
- the awakening completing and transitioning into the conversation UI;
- the conversation UI remaining available;
- typed input of `hello devil`;
- successful submission presentation;
- the user conversation entry appearing correctly;
- runtime-result presentation:
  `Deferred by the Devil runtime.`;
- the runtime result remaining truthfully non-successful rather than being rewritten as completed execution;
- voice-output presentation being exercised through the visible speaking state;
- accessibility diagnostic presentation truthfully reporting that Devil accessibility was not enabled;
- no observed crash during the recorded test path;
- no observed freeze during the recorded test path;
- no observed broken awakening-to-conversation transition;
- no observed duplicate runtime entry;
- no obvious Stage 52 regression in the recorded path that invalidated the Closed Beta baseline.

The physical-device evidence does not claim that every Devil capability is available or operational.

The visible `DEFERRED` runtime result is not treated as verified task success, execution success, or Outcome.

The screen recording demonstrates the observed presentation and interaction behavior only; it does not independently prove every internal constitutional transition.

### Closed Beta 1 Validation Conclusion

Closed Beta 1 passed the bounded Stage 52 artifact-provenance, integrity, CI, and recorded physical-device smoke-test path.

This validation does not make the artifact:

- RC1;
- RC2;
- production-ready;
- final production-signed;
- evidence that every Devil capability works;
- evidence of verified task completion where the runtime returned `DEFERRED`.

Stage 52 remains open until this validation record is committed and pushed, final repository closure evidence is verified, and the official `devil-stage-52-complete` tag is created deliberately.

## Constitutional Boundaries

The Closed Beta release does not change these invariants:

- Brain reasons and decides; Brain does not execute.
- Every action requires a preceding Brain decision.
- No capability invents authority.
- Android permission is not Devil authorization.
- Devil authorization is not Android permission.
- Wake attention is not authentication.
- Age classification is not authentication.
- Execution approval is not execution evidence.
- Execution attempt is not observation.
- Observation is not verification.
- Verification determines actual effect.
- No success claim is permitted without verified evidence.
- Runtime acceptance is not verified task success.
- Memory proposal is not memory commitment.
- Memory commitment is not memory persistence.

No Stage 52 release mechanism creates another intelligence or bypasses the constitutional execution chain.

## Closed Beta Limitations

A Stage 52 Closed Beta APK is not:

- an RC1 APK;
- an RC2 APK;
- a Devil V1 production APK;
- a final production-signed release merely by virtue of being Beta;
- evidence that every capability is available;
- evidence that every Android or HyperOS behavior is solved;
- permission to bypass authentication or authorization;
- evidence of verified task success without Observation and Verification;
- permission to introduce deferred UI redesign silently.

The Closed Beta build remains controlled pre-production software.

## Rollback

The protected rollback baseline entering Stage 52 is:

`devil-stage-51-complete`

resolved to:

`d75797aba9fd535d1e57f131f9e3ad88d0b930ef`

The exact source commit for each generated Closed Beta artifact must also be recorded after the workflow run is validated.

If Stage 52 introduces a regression, the repository and release evidence must make the last accepted Stage 51 baseline recoverable without ambiguity.

## Closure Gate

Stage 52 must not be considered officially complete until all required evidence exists.

The closure gate requires:

- intended Stage 52 repository changes reviewed;
- `git diff --check` passing;
- relevant tests passing;
- complete app unit tests passing;
- intended files committed and pushed;
- local `master` matching `origin/master`;
- Closed Beta workflow run succeeding;
- workflow source commit verified;
- Closed Beta GitHub prerelease verified;
- release target verified;
- actual Git tag target verified;
- APK asset verified;
- SHA-256 asset verified;
- directly downloaded APK checksum verified;
- exact verified APK installed;
- physical-device Closed Beta test completed;
- actual validation evidence recorded in this document;
- repository clean after the validation record is committed;
- official `devil-stage-52-complete` tag created deliberately.

Only after that closure gate passes may development advance to:

Stage 53 — RC1.
