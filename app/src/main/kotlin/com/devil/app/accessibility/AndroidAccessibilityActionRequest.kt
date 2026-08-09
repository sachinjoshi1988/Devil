package com.devil.app.accessibility

/**
 * Stage 38 bounded request for one Android accessibility platform action.
 *
 * This record preserves an already-established action and target only.
 *
 * Creating this request does not:
 *
 * - resolve conversational intent;
 * - authenticate a user;
 * - grant Devil authorization;
 * - establish Executive readiness;
 * - grant Android permission;
 * - approve constitutional execution;
 * - perform an accessibility action;
 * - observe an effect;
 * - verify an outcome;
 * - or establish task success.
 *
 * The constitutional execution boundary must approve any production use before
 * this request reaches the Android accessibility source.
 */
data class AndroidAccessibilityActionRequest(
    val actionType: AndroidAccessibilityActionType,
    val target: AndroidAccessibilityTarget,
)
