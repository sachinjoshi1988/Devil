# Devil Stage 53 — RC1

## Status

Stage 53 RC1 release contract and validation record.

Current status:

- Stage 52 Closed Beta APK is formally complete.
- The protected Stage 52 completion point is `devil-stage-52-complete`.
- Stage 53 release-signing governance has been established.
- The permanent Devil V1 release signing identity has been created outside Git.
- GitHub Actions release-signing secrets have been provisioned.
- RC1 release infrastructure is being established.
- No RC1 artifact is considered validated merely because this document or workflow exists.

Stage 53 must not be considered officially complete until the RC1 closure gate passes and the official Stage 53 completion tag is created deliberately.

## Purpose

Stage 53 advances Devil V1 from controlled Closed Beta testing to Release Candidate 1.

RC1 must exercise a release-equivalent Android packaging and signing path.

RC1 is still pre-production.

RC1 does not authorize unrelated feature expansion, deferred UI redesign, constitutional bypasses, or production declaration.

## Stage 52 Baseline

The Stage 53 baseline begins from:

- completion tag:
  `devil-stage-52-complete`
- completion commit:
  `1b543ef57b2f6484de25430106f6727774eeb4d3`

Stage 53 signing-boundary development began at:

`cc10f1d148efb68ce5a2b22fd1ed93d01a2b8f10`

Stage 52 remains the protected rollback baseline entering RC1.

## RC1 Application Identity

The Android package identity remains:

`com.devil.app`

RC1 uses:

- version code:
  `2`
- version name:
  `1.0.0-rc1`

The package identity must not change merely because release maturity changes.

Future release-family artifacts signed by the same Devil release identity must preserve Android signing continuity and use monotonically increasing version codes.

## RC1 Release Identity

The dedicated workflow is:

`.github/workflows/rc1-apk.yml`

The workflow is manually triggered through `workflow_dispatch`.

RC1 uses:

- Git tag:
  `devil-v1.0.0-rc1`
- release name:
  `Devil V1.0.0 RC1`
- APK:
  `devil-v1-rc1.apk`
- checksum:
  `devil-v1-rc1.apk.sha256`
- signer report:
  `devil-v1-rc1.signer.txt`
- GitHub classification:
  prerelease
- Android build type:
  release

RC1 must not be published from `assembleDebug`.

## Release Signing Identity

The Devil V1 private release keystore is maintained outside Git.

Repository code consumes signing material only through environment variables.

GitHub Actions reconstructs a temporary keystore from encrypted repository secrets for the duration of the RC1 job.

The private keystore and passwords must never be committed to Git or published as release assets.

The expected public signing certificate SHA-256 is:

`96a20adba24a79d102a9c7722a761d290f217270a7e415051849f6a60f73177e`

Public signing certificate identity:

`CN=Devil V1 Release, OU=Release, O=Devil, C=IN`

The release key uses a 4096-bit RSA public key.

The local release-signing path was verified before RC1 infrastructure creation through a successful signed `:app:assembleRelease` build and independent `apksigner` verification.

## CI Gate

Every RC1 workflow run must perform:

- repository checkout;
- JDK setup;
- Gradle preparation;
- removal of the Termux-only AAPT2 override in CI;
- release-signing secret presence checks;
- temporary keystore reconstruction;
- core model tests;
- core runtime tests;
- complete app debug unit tests;
- signed release APK assembly;
- APK signer verification;
- expected certificate fingerprint verification;
- SHA-256 generation;
- SHA-256 verification;
- signer-report creation;
- GitHub RC1 prerelease creation.

A failed required gate means RC1 is not validated.

## Source and Artifact Provenance

RC1 provenance must be verified against:

1. workflow source commit;
2. GitHub Release target;
3. actual Git tag target;
4. APK checksum;
5. APK signing certificate.

Matching filenames or release names alone are insufficient evidence.

## Debug-to-Release Installation Boundary

Stage 52 Closed Beta was debug-signed.

Stage 53 RC1 is signed with the permanent Devil V1 release key.

Therefore the existing debug-signed Closed Beta installation must not be treated as directly upgrade-compatible with RC1.

The first RC1 physical-device validation must use a controlled clean installation after preserving any required local state.

This signing discontinuity is intentional between the pre-release debug lineage and the permanent release-key lineage.

After RC1 begins the permanent release-key lineage, later RC and production artifacts must preserve signing continuity.

## RC1 Device Gate

At minimum, RC1 must be validated on:

- Redmi Note 12
- Android 14 / HyperOS

Required physical-device evidence includes:

- exact RC1 provenance;
- checksum verification;
- signer verification;
- controlled clean installation;
- successful application launch;
- accepted Stage 51 awakening remaining intact;
- conversation UI availability;
- typed submission path;
- truthful runtime-result presentation;
- voice-output behavior where available;
- accessibility diagnostic behavior where applicable;
- no observed blocker that invalidates RC1.

A truthful `DEFERRED`, `UNAVAILABLE`, `DEGRADED`, `DENIED`, `FAILED`, or partial result must not be rewritten as success.

## Constitutional Boundaries

RC1 packaging does not change Devil constitutional authority.

The following remain true:

- Brain reasons and decides; Brain does not execute.
- Every action requires a preceding Brain decision.
- No capability invents authority.
- Android permission is not Devil authorization.
- Devil authorization is not Android permission.
- Wake attention is not authentication.
- Observation is not Verification.
- Verification determines actual effect.
- Runtime acceptance is not verified task success.
- No success claim is permitted without verified evidence.
- Memory proposal is not memory commitment.
- Memory commitment is not memory persistence.

Release signing proves artifact identity.

Release signing does not prove authorization, execution success, runtime readiness, verified task completion, or Outcome.

## RC1 Limitations

RC1 is not:

- RC2;
- the final Devil V1 production APK;
- the production completion tag;
- evidence every capability is operational;
- evidence every Android or HyperOS behavior is solved;
- permission to bypass authentication or authorization;
- permission to silently redesign deferred UI;
- evidence of verified task success without Observation and Verification.

## Rollback

The protected development baseline entering Stage 53 is:

`devil-stage-52-complete`

The permanent release keystore must also be preserved securely because release-key loss can break future Android update continuity.

No rollback procedure may replace the permanent release signing identity casually.

## Closure Gate

Stage 53 must not be considered officially complete until all required evidence exists.

The closure gate requires:

- intended Stage 53 repository changes reviewed;
- `git diff --check` passing;
- signing-boundary tests passing;
- debug path remaining healthy;
- release path signed successfully;
- core model tests passing;
- core runtime tests passing;
- complete app unit tests passing;
- intended files committed and pushed;
- local `master` matching `origin/master`;
- RC1 workflow run succeeding;
- exact workflow source commit verified;
- RC1 GitHub prerelease verified;
- release target verified;
- actual Git tag target verified;
- APK asset verified;
- checksum asset verified;
- signer-report asset verified;
- downloaded APK checksum verified;
- downloaded APK signer verified;
- controlled clean installation completed;
- physical-device RC1 test completed;
- actual validation evidence recorded in this document;
- repository clean after validation evidence is committed;
- official `devil-stage-53-complete` tag created deliberately.

Only after this closure gate passes may development advance to:

Stage 54 — RC2, only if required.

If RC2 is not required, the roadmap may advance according to the frozen V1 release plan toward Stage 55 — Devil V1 Production APK.
