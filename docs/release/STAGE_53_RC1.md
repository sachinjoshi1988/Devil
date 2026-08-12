# Devil Stage 53 — RC1

## Status

Stage 53 RC1 release contract and validation record.

Current status:

- Stage 52 Closed Beta APK is formally complete.
- The protected Stage 52 completion point is `devil-stage-52-complete`.
- Stage 53 release-signing governance has been established.
- The permanent Devil V1 release signing identity has been created outside Git.
- GitHub Actions release-signing secrets have been provisioned.
- RC1 release infrastructure is established and the signed RC1 artifact has completed its recorded validation path.
- RC1 validation evidence is recorded below; Stage 53 remains pending final repository closure and the official completion tag.

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

## RC1 Validation Evidence

### Workflow History and Source

The first Stage 53 RC1 workflow attempt was:

- workflow:
  `Devil V1 RC1 APK`
- run:
  `31557758907`
- source commit:
  `2d48d8e9b318423853414fa3b73d0c42d24c09c7`
- result:
  `failure`

That run successfully reached the signed RC1 release APK and then failed closed at the APK signer-verification gate.

The failure was caused by a version-specific parser expecting:

`Signer #1 certificate SHA-256 digest:`

while the GitHub runner's `apksigner` reported the signer using the form:

`V2 Signer: certificate SHA-256 digest:`

The failed run did not proceed to checksum publication or GitHub RC1 Release creation.

The signer parser was corrected without weakening signer verification.

The correction was committed as:

`039e8a29db4912110fe406b462878e51ee7ca7a9`

with subject:

`Stage 53: Fix RC1 signer verification parser`

The authoritative successful RC1 workflow run was:

- workflow:
  `Devil V1 RC1 APK`
- run:
  `31558219895`
- result:
  `success`
- workflow source commit:
  `039e8a29db4912110fe406b462878e51ee7ca7a9`

The workflow source commit matched the corrected Stage 53 RC1 repository HEAD exactly.

The successful workflow completed all required RC1 gates, including:

- repository checkout;
- JDK setup;
- Gradle preparation;
- release-signing secret presence verification;
- temporary release-keystore reconstruction;
- core model tests;
- core runtime tests;
- complete app debug unit tests;
- signed release APK assembly;
- RC1 APK signer verification;
- expected signing-certificate fingerprint verification;
- SHA-256 generation;
- SHA-256 verification;
- signer-report generation;
- GitHub RC1 prerelease creation.

### RC1 Release Identity and Provenance

The validated GitHub Release is:

- release tag:
  `devil-v1.0.0-rc1`
- release name:
  `Devil V1.0.0 RC1`
- classification:
  prerelease
- release target:
  `039e8a29db4912110fe406b462878e51ee7ca7a9`

The actual Git tag was fetched independently and resolved to:

`039e8a29db4912110fe406b462878e51ee7ca7a9`

Therefore the validated RC1 provenance chain is:

workflow source commit
=
GitHub Release target
=
actual Git tag target.

### Published RC1 Artifacts

The RC1 GitHub Release published:

- `devil-v1-rc1.apk`
- `devil-v1-rc1.apk.sha256`
- `devil-v1-rc1.signer.txt`

Published and independently verified APK SHA-256:

`54a9917245dc00425f220a74a4115674fbb6d5dfe6fa87a25e5169f9774afd3d`

The published checksum verified successfully with:

`devil-v1-rc1.apk: OK`

The exact release assets were downloaded through `gh` into an isolated verification directory.

The independently calculated downloaded APK SHA-256 was also:

`54a9917245dc00425f220a74a4115674fbb6d5dfe6fa87a25e5169f9774afd3d`

A clearly identified shared-storage physical-device install copy was then created:

`~/storage/downloads/Devil-V1.0.0-RC1-VERIFIED.apk`

Its independently calculated SHA-256 was also:

`54a9917245dc00425f220a74a4115674fbb6d5dfe6fa87a25e5169f9774afd3d`

Therefore the prepared RC1 install copy remained byte-exact with the published RC1 APK.

### RC1 Signing Identity

Independent `apksigner` verification of the downloaded RC1 APK reported:

- certificate DN:
  `CN=Devil V1 Release, OU=Release, O=Devil, C=IN`
- certificate SHA-256:
  `96a20adba24a79d102a9c7722a761d290f217270a7e415051849f6a60f73177e`
- key algorithm:
  RSA
- key size:
  4096 bits
- signer count:
  `1`
- APK Signature Scheme v2:
  verified

The independently observed signing-certificate SHA-256 exactly matched the expected permanent Devil V1 release certificate.

The published signer report also recorded the same Devil V1 release-signing identity.

Release signing proves artifact identity and signing continuity.

It does not prove Devil authorization, runtime readiness, execution success, verified task completion, or Outcome.

### Packaged Application Identity

Independent `apkanalyzer` inspection of the downloaded RC1 APK reported:

- application release version code:
  `2`
- application release version name:
  `1.0.0-rc1`

These values matched the Stage 53 RC1 contract exactly.

The Android package identity remains:

`com.devil.app`

### Debug-to-Release Installation Transition

The Stage 52 Closed Beta lineage was debug-signed.

Stage 53 RC1 begins the permanent Devil V1 release-signing lineage.

The RC1 physical-device test therefore used the controlled clean-install path rather than treating the debug-signed Closed Beta as directly upgrade-compatible with RC1.

The verified install artifact used for this path was:

`~/storage/downloads/Devil-V1.0.0-RC1-VERIFIED.apk`

with SHA-256:

`54a9917245dc00425f220a74a4115674fbb6d5dfe6fa87a25e5169f9774afd3d`

The supplied screen recording demonstrates the observed post-install RC1 behavior.

The recording itself is not treated as independent visual proof of every uninstall or installation action that occurred before recording began.

### Physical-Device RC1 Validation

Primary validation device:

- Redmi Note 12
- Android 14 / HyperOS

The supplied physical-device screen recording showed:

- Devil launching successfully;
- the accepted Stage 51 awakening presentation remaining intact;
- the dark full-screen presentation;
- red environmental code animation;
- the central Devil D identity;
- the `DEVIL INSIDE` wordmark;
- successful awakening-to-conversation transition;
- conversation UI availability;
- typed input of `Hello Devil`;
- the user conversation entry appearing once;
- runtime-result presentation:
  `Deferred by the Devil runtime.`;
- the `DEFERRED` result remaining truthfully non-successful rather than being rewritten as completed execution;
- voice-output presentation being exercised through the visible speaking state;
- accessibility diagnostic presentation truthfully reporting that Devil accessibility was not enabled;
- no observed application crash during the recorded path;
- no observed UI freeze during the recorded path;
- no observed broken awakening-to-conversation transition;
- no observed duplicate submitted conversation entry;
- no obvious blocker in the recorded smoke-test path that invalidated RC1.

The physical-device evidence does not claim that every Devil capability is available or operational.

The visible `DEFERRED` runtime result is not treated as execution success, verified task success, or Outcome.

The recording demonstrates observed presentation and interaction behavior only; it does not independently prove every internal constitutional transition.

### RC1 Validation Conclusion

Devil V1 RC1 passed the bounded Stage 53 evidence available for:

- release-variant packaging;
- permanent release signing;
- signing-certificate identity;
- CI testing;
- source provenance;
- Git tag provenance;
- release provenance;
- artifact integrity;
- packaged version identity;
- controlled physical-device RC1 smoke testing;
- truthful non-success runtime-result presentation.

This validation does not make RC1:

- RC2;
- the final Devil V1 production APK;
- production-ready merely because RC1 passed;
- evidence that every Devil capability works;
- evidence that every Android or HyperOS behavior is solved;
- evidence of verified task completion where the runtime returned `DEFERRED`.

Stage 53 remains open until this validation record is reviewed, committed and pushed, final repository closure evidence is verified, and the official `devil-stage-53-complete` tag is created deliberately.


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
