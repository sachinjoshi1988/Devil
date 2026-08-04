package com.devil.core.model.trust

/**
 * Describes the bounded trust classification established for one subject.
 *
 * This classification is distinct from ContextTrustLevel, which describes
 * supplied context. It does not authenticate the subject, prove ownership,
 * grant authorization, enter Owner Mode, or permit an action.
 */
enum class SubjectTrustLevel {
    UNESTABLISHED,
    RESTRICTED,
    TRUSTED,
}
