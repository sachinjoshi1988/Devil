package com.devil.app.application

/**
 * Stage 182 bounded Navigation Intelligence status.
 *
 * READY means bounded application navigation data has been prepared.
 *
 * DEFERRED means the supplied Stage 177 application inspection does not
 * justify preparing navigation.
 *
 * NAVIGATION_READY != EXECUTION_APPROVAL.
 * NAVIGATION_READY != APPLICATION_LAUNCHED.
 */
enum class AndroidNavigationIntelligenceStatus {
    READY,
    DEFERRED,
}
