package com.devil.app.application

/**
 * Stage 177 bounded Android application inspection state.
 *
 * FOUND means Android exposed one matching application package.
 *
 * NOT_FOUND means the explicitly supplied package name was not available through
 * the bounded inspection source.
 *
 * Neither state establishes permission, Devil authorization, execution approval,
 * application launch, observation, verification, or Outcome.
 */
enum class AndroidApplicationInspectionStatus {
    FOUND,
    NOT_FOUND,
}
