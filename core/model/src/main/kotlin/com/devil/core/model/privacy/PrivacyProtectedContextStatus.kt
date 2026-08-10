package com.devil.core.model.privacy

/**
 * Stage 46 bounded status describing whether protected privacy context has been
 * established by an approved upstream constitutional mechanism.
 *
 * ESTABLISHED means approved evidence has established the bounded privacy
 * protection required by the requesting privacy policy.
 *
 * NOT_ESTABLISHED means available evidence affirmatively does not establish that
 * protection.
 *
 * UNAVAILABLE means no justified determination is currently available.
 *
 * This status does not itself:
 *
 * - authenticate a subject;
 * - prove owner identity;
 * - establish trust;
 * - create or validate a session;
 * - enter Owner Mode;
 * - grant authorization;
 * - grant Android permission;
 * - permit disclosure;
 * - persist memory;
 * - or permit execution.
 *
 * ESTABLISHED
 * != authentication
 * != Owner Mode
 * != authorization
 * != disclosure permission.
 */
enum class PrivacyProtectedContextStatus {
    ESTABLISHED,
    NOT_ESTABLISHED,
    UNAVAILABLE,
}
