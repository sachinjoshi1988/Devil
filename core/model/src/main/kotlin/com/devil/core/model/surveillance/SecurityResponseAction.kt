package com.devil.core.model.surveillance

/**
 * Immutable Stage 91 representation of one explicitly supplied bounded
 * Security Response action description.
 *
 * The value describes a response intention only.
 *
 * Examples may include supplied descriptions such as:
 *
 * - notify owner;
 * - raise local security alert;
 * - request protected device lock;
 * - request emergency escalation;
 * - request additional observation;
 * - or another future governed security response.
 *
 * Stage 91 intentionally keeps this value extensible rather than binding
 * Security Response to Android, CCTV, notification, alarm, lock, network,
 * emergency-service, or other platform-specific APIs.
 *
 * Creating this value does not:
 *
 * - establish that a threat exists;
 * - establish verified identity;
 * - establish criminal status;
 * - authenticate anyone;
 * - establish trust;
 * - grant authorization;
 * - create a constitutional Decision;
 * - create a Task or Plan;
 * - register or select a capability;
 * - establish capability availability or readiness;
 * - create an ExecutionRequest;
 * - execute an action;
 * - send a notification;
 * - trigger an alarm;
 * - lock or unlock a device;
 * - contact emergency services;
 * - communicate with a platform or network;
 * - establish constitutional Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or commit Memory;
 * - or persist Security Response state.
 *
 * SECURITY_RESPONSE_ACTION != CAPABILITY.
 * SECURITY_RESPONSE_ACTION != AUTHORIZATION.
 * SECURITY_RESPONSE_ACTION != EXECUTION_REQUEST.
 * SECURITY_RESPONSE_ACTION != EXECUTION.
 */
@ConsistentCopyVisibility
data class SecurityResponseAction private constructor(
    val value: String,
) {
    companion object {

        fun from(
            rawValue: String,
        ): SecurityResponseAction {
            val normalizedValue =
                rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Security Response action must not be blank."
            }

            return SecurityResponseAction(
                value = normalizedValue,
            )
        }
    }
}
