# Devil Stage 55 — Devil V1 Production APK

## Status

Stage 55 production release contract and validation record.

Current status:

- Stage 53 RC1 is formally complete.
- The protected Stage 53 completion point is `devil-stage-53-complete`.
- Stage 54 RC2 was inspected and determined not to be required because no unresolved RC1 production blocker was recorded.
- The permanent Devil V1 release signing identity is already established.
- Production release infrastructure is being established.
- No production artifact is considered validated merely because this document or workflow exists.

Stage 55 must not be considered officially complete until the production closure gate passes and the official Stage 55 completion tag is created deliberately.

## Purpose

Stage 55 produces and validates the first Devil V1 production APK.

This stage advances the validated RC1 release lineage to the first normal production release.

It does not authorize unrelated feature expansion, architectural redesign, deferred capability expansion, or constitutional bypasses.

Production packaging must preserve the existing Unified Devil Runtime and all frozen constitutional boundaries.

## Stage 53 Baseline

The protected development baseline entering Stage 55 is:

- completion tag:
  `devil-stage-53-complete`
- completion commit:
  `95bd728f742933c9a0cdd96aba3b7d18b8c02cec`

The validated RC1 artifact lineage is rooted at:

- RC1 release tag:
  `devil-v1.0.0-rc1`
- RC1 artifact source:
  `039e8a29db4912110fe406b462878e51ee7ca7a9`

Stage 54 RC2 is intentionally skipped because the post-RC1 necessity inspection found no recorded unresolved production blocker requiring a second release candidate.

## Production Application Identity

The Android package identity remains:

`com.devil.app`

Devil V1 production uses:

- version code:
  `3`
- version name:
  `1.0.0`

The production version code is greater than the RC1 version code `2`.

The package identity must not change merely because release maturity changes.

## Production Release Identity

The dedicated production workflow is:

`.github/workflows/production-apk.yml`

The workflow is manually triggered through `workflow_dispatch`.

Production uses:

- Git tag:
  `devil-v1.0.0`
- GitHub Release name:
  `Devil V1.0.0`
- APK:
  `devil-v1.0.0.apk`
- checksum:
  `devil-v1.0.0.apk.sha256`
- signer report:
  `devil-v1.0.0.signer.txt`
- GitHub classification:
  normal release
- Android build type:
  release

The production workflow must not publish from `assembleDebug`.

The production GitHub Release must not be marked as a prerelease.

## Permanent Release Signing Identity

Production must continue the permanent Devil V1 release-signing lineage established before RC1.

The expected public signing certificate SHA-256 is:

`96a20adba24a79d102a9c7722a761d290f217270a7e415051849f6a60f73177e`

Public certificate identity:

`CN=Devil V1 Release, OU=Release, O=Devil, C=IN`

The public key uses RSA with a 4096-bit key.

The private release keystore remains outside Git.

Repository code consumes signing material only through environment variables.

GitHub Actions reconstructs a temporary release keystore from encrypted repository secrets only for the duration of the production workflow.

The private keystore and passwords must never be committed to Git or published as release assets.

A production APK signed by a different certificate must fail validation.

## RC1-to-Production Signing Continuity

RC1 began the permanent release-key lineage.

Production therefore must preserve the same signing certificate.

Unlike the Stage 52 debug-to-RC1 transition, RC1 and production are intended to be Android signing-compatible.

Signing continuity alone does not prove application correctness or successful upgrade behavior.

The production validation path must independently verify the exact artifact, signer, packaged version, installation behavior, and observed device behavior.

## CI Gate

Every Stage 55 production workflow run must perform:

- repository checkout;
- JDK setup;
- Gradle preparation;
- removal of the Termux-only AAPT2 override inside CI;
- release-signing secret presence checks;
- temporary release-keystore reconstruction;
- core model tests;
- core runtime tests;
- complete app debug unit tests;
- signed release APK assembly;
- APK signer verification;
- expected signing-certificate fingerprint verification;
- SHA-256 generation;
- SHA-256 verification;
- signer-report generation;
- normal GitHub production Release creation.

A failed required gate means the production release is not validated.

## Source and Artifact Provenance

Production provenance must be verified against:

1. workflow source commit;
2. GitHub Release target;
3. actual Git tag target;
4. downloaded APK checksum;
5. downloaded APK signing certificate;
6. packaged version identity.

Matching filenames, release names, or visual appearance alone are insufficient evidence.

## Production Device Gate

At minimum, the Stage 55 production artifact must be validated on:

- Redmi Note 12
- Android 14 / HyperOS

Required physical-device evidence includes:

- exact production artifact provenance;
- checksum verification;
- signer verification;
- packaged version verification;
- installation or verified RC1-to-production update behavior;
- successful application launch;
- accepted Stage 51 awakening remaining intact;
- conversation UI availability;
- typed submission path;
- truthful runtime-result presentation;
- voice-output behavior where available;
- accessibility diagnostic behavior where applicable;
- no observed blocker invalidating production release.

A truthful `DEFERRED`, `UNAVAILABLE`, `DEGRADED`, `DENIED`, `FAILED`, or partial runtime result must not be rewritten as verified success.

Production release status does not convert a truthful non-success runtime outcome into execution success or Outcome.

## Constitutional Boundaries

Stage 55 packaging does not change Devil constitutional authority.

The following remain true:

- Brain reasons and decides; Brain does not execute.
- Exactly one primary Brain decision occurs per reasoning cycle.
- Every action requires a preceding Brain decision.
- No capability invents authority.
- Android permission is not Devil authorization.
- Devil authorization is not Android permission.
- Wake attention is not authentication.
- Age classification is not authentication.
- Execution approval is not execution evidence.
- Execution attempt is not observation.
- Observation is not Verification.
- Verification determines actual effect.
- Runtime acceptance is not verified task success.
- No success claim is permitted without verified evidence.
- Memory proposal is not memory commitment.
- Memory commitment is not memory persistence.

Production signing proves artifact identity and signing continuity.

Production signing does not prove authorization, execution success, runtime readiness, verified task completion, or Outcome.

No Stage 55 release mechanism creates another Brain, Executive, Planner, Memory Authority, Security Architecture, runtime, logical Memory Domain, or constitutional execution path.

## Production Limitations

A production Devil V1 APK does not imply:

- every future Devil capability already exists;
- every planned intelligence domain is complete;
- every Android or HyperOS behavior is solved;
- every request can produce verified task completion;
- future capability development is unnecessary;
- authentication or authorization may be bypassed;
- Observation or Verification may be skipped;
- release signing itself proves runtime success.

Devil V1 production is the completion of the frozen V1 release baseline, not the completion of the long-term Devil roadmap.

## Rollback and Release-Key Continuity

The protected rollback baseline entering Stage 55 is:

`devil-stage-53-complete`

The permanent release keystore must be preserved securely.

Loss of the permanent release signing identity can break future Android update continuity.

No rollback procedure may casually replace the permanent Devil V1 release identity.

The exact Stage 55 source commit and production artifact provenance must be recorded after the production workflow succeeds.

## Closure Gate

Stage 55 must not be considered officially complete until all required evidence exists.

The closure gate requires:

- intended Stage 55 repository changes reviewed;
- `git diff --check` passing;
- debug path remaining healthy;
- unsigned release path continuing to fail closed;
- local signed production release build succeeding;
- core model tests passing;
- core runtime tests passing;
- complete app unit tests passing;
- packaged production version verified;
- local production signer verified;
- intended files committed and pushed;
- local `master` matching `origin/master`;
- production workflow run succeeding;
- exact workflow source commit verified;
- normal GitHub production Release verified;
- production Release confirmed not to be a prerelease;
- release target verified;
- actual Git tag target verified;
- APK asset verified;
- checksum asset verified;
- signer-report asset verified;
- downloaded APK checksum verified;
- downloaded APK signer verified;
- downloaded APK packaged version verified;
- exact verified production APK installed or valid RC1-to-production upgrade behavior verified;
- physical-device production smoke test completed;
- actual validation evidence recorded in this document;
- repository clean after validation evidence is committed;
- official `devil-stage-55-complete` tag created deliberately.

Only after this closure gate passes may Devil V1 be declared formally complete at the frozen production baseline.
