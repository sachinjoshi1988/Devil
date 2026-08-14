package com.devil.core.runtime.education

/**
 * Stage 85 bounded education-session preparation status.
 *
 * PREPARED means one structurally valid EducationSessionRecord was prepared
 * from explicitly supplied education-domain inputs.
 *
 * PREPARED does not mean:
 *
 * - the subject is authenticated;
 * - trust was established;
 * - authorization exists;
 * - child policy was evaluated;
 * - guardian approval exists;
 * - a lesson was generated;
 * - instruction was delivered;
 * - mastery was assessed;
 * - a Task or Plan exists;
 * - execution is permitted;
 * - constitutional Learning occurred;
 * - Memory was committed;
 * - or an Outcome occurred.
 *
 * DEFERRED means no truthful bounded education-session record was produced.
 */
enum class EducationSessionPreparationStatus {
    PREPARED,
    DEFERRED,
}
