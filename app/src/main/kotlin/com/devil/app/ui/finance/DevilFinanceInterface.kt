package com.devil.app.ui.finance

import com.devil.app.ui.accessibility.devilInclusiveHeading
import com.devil.app.ui.accessibility.devilInclusiveInteractiveTarget

import com.devil.app.ui.adaptive.DevilAdaptiveContainer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devil.app.R

/**
 * Stage 260 Finance Interface.
 *
 * Presentation-only surface for already-established bounded Financial Domain
 * information supplied by an upstream caller.
 *
 * This interface does not access financial accounts, retrieve balances,
 * transactions, prices, or market data, create financial advice, establish
 * investment suitability, execute accounting, calculate taxes, inspect
 * documents, establish financial safety, authenticate accounts or documents,
 * authorize financial actions, execute transactions, mutate World Model state,
 * perform constitutional Learning, or commit Memory.
 *
 * FINANCE_INTERFACE != FINANCIAL_AUTHORITY.
 * FINANCE_PRESENTATION != ACCOUNT_ACCESS.
 * SUPPLIED_FINANCIAL_FACT != VERIFIED_EXTERNAL_FINANCIAL_STATE.
 * FINANCE_INTERFACE != FINANCIAL_ADVICE.
 * FINANCE_INTERFACE != INVESTMENT_SUITABILITY.
 * FINANCE_INTERFACE != TRANSACTION.
 * FINANCE_INTERFACE != EXECUTION.
 *
 * ACCOUNTING_PRESENTATION != BOOKKEEPING_EXECUTION.
 * ACCOUNTING_PRESENTATION != VERIFIED_ACCOUNTING.
 *
 * TAX_PRESENTATION != TAX_AUTHORITY.
 * TAX_PRESENTATION != VERIFIED_CURRENT_TAX_LAW.
 * TAX_PRESENTATION != TAX_FILING.
 *
 * FINANCIAL_DOCUMENT_PRESENTATION != OCR.
 * FINANCIAL_DOCUMENT_PRESENTATION != DOCUMENT_AUTHENTICITY.
 *
 * FINANCIAL_SAFETY_PRESENTATION != CONSTITUTIONAL_VERIFICATION.
 * FINANCIAL_SAFETY_PRESENTATION != FINANCIAL_GUARANTEE.
 *
 * FINANCE_INTERFACE != WORLD_MODEL_UPDATE.
 * FINANCE_INTERFACE != CONSTITUTIONAL_LEARNING.
 * FINANCE_INTERFACE != MEMORY_COMMITMENT.
 *
 * Stage 260 does not implement Stage 261 or later UI work.
 */
@Composable
fun DevilFinanceInterface(
    financialSubject: String?,
    suppliedFinancialFacts: String?,
    integrationFocus: String?,
    integrationObjective: String?,
    integrationStatus: String?,
    personalFinanceFocus: String?,
    personalFinanceObjective: String?,
    personalFinanceApproach: String?,
    personalFinanceStatus: String?,
    accountingFocus: String?,
    accountingObjective: String?,
    accountingBasis: String?,
    accountingStatus: String?,
    businessAccountingFocus: String?,
    businessAccountingObjective: String?,
    businessAccountingApproach: String?,
    businessAccountingStatus: String?,
    taxFocus: String?,
    taxObjective: String?,
    taxContext: String?,
    taxStatus: String?,
    indianTaxFocus: String?,
    indianTaxObjective: String?,
    indianTaxContext: String?,
    indianTaxStatus: String?,
    documentFocus: String?,
    suppliedDocumentDescription: String?,
    documentInterpretationObjective: String?,
    documentStatus: String?,
    safetyFocus: String?,
    verificationBasisDescription: String?,
    safetyInterpretation: String?,
    safetyStatus: String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val devilRed = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val elevatedSurface = MaterialTheme.colorScheme.surfaceVariant
    val foreground = MaterialTheme.colorScheme.onSurface
    val muted = MaterialTheme.colorScheme.onSurfaceVariant

    DevilAdaptiveContainer {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.42f),
                    shape = RoundedCornerShape(28.dp),
                ),
        shape = RoundedCornerShape(28.dp),
        color = surface,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            DevilFinanceHeader(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
            )

            DevilFinancialContextCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                financialSubject = financialSubject,
                suppliedFinancialFacts = suppliedFinancialFacts,
                integrationFocus = integrationFocus,
                integrationObjective = integrationObjective,
                integrationStatus = integrationStatus,
            )

            DevilPersonalFinanceCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                focus = personalFinanceFocus,
                objective = personalFinanceObjective,
                approach = personalFinanceApproach,
                status = personalFinanceStatus,
            )

            DevilAccountingCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                accountingFocus = accountingFocus,
                accountingObjective = accountingObjective,
                accountingBasis = accountingBasis,
                accountingStatus = accountingStatus,
                businessAccountingFocus = businessAccountingFocus,
                businessAccountingObjective = businessAccountingObjective,
                businessAccountingApproach = businessAccountingApproach,
                businessAccountingStatus = businessAccountingStatus,
            )

            DevilTaxCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                taxFocus = taxFocus,
                taxObjective = taxObjective,
                taxContext = taxContext,
                taxStatus = taxStatus,
                indianTaxFocus = indianTaxFocus,
                indianTaxObjective = indianTaxObjective,
                indianTaxContext = indianTaxContext,
                indianTaxStatus = indianTaxStatus,
            )

            DevilFinancialDocumentCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                documentFocus = documentFocus,
                suppliedDocumentDescription = suppliedDocumentDescription,
                documentInterpretationObjective = documentInterpretationObjective,
                documentStatus = documentStatus,
            )

            DevilFinancialSafetyCard(
                devilRed = devilRed,
                foreground = foreground,
                muted = muted,
                elevatedSurface = elevatedSurface,
                safetyFocus = safetyFocus,
                verificationBasisDescription = verificationBasisDescription,
                safetyInterpretation = safetyInterpretation,
                safetyStatus = safetyStatus,
            )

            DevilFinanceBoundaryFooter(
                devilRed = devilRed,
                muted = muted,
            )

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
                border =
                    BorderStroke(
                        width = 1.dp,
                        color = devilRed.copy(alpha = 0.46f),
                    ),
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = devilRed,
                    ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "BACK TO CONVERSATION",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
    }
}

@Composable
private fun DevilFinanceHeader(
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter =
                painterResource(
                    id = R.drawable.devil_primary_logo,
                ),
            contentDescription = "Devil",
            modifier = Modifier.size(54.dp),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "FINANCE",
                modifier = Modifier.devilInclusiveHeading(),
                style = MaterialTheme.typography.titleLarge,
                color = devilRed,
                fontWeight = FontWeight.Black,
            )

            Text(
                text = "Governed financial intelligence presentation",
                style = MaterialTheme.typography.bodyMedium,
                color = foreground,
            )

            Text(
                text = "Presented financial information is not automatically verified external state.",
                style = MaterialTheme.typography.bodySmall,
                color = muted,
            )
        }
    }
}

@Composable
private fun DevilFinancialContextCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    financialSubject: String?,
    suppliedFinancialFacts: String?,
    integrationFocus: String?,
    integrationObjective: String?,
    integrationStatus: String?,
) {
    DevilFinanceCard(
        title = "FINANCIAL CONTEXT",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilFinanceField(
            label = "SUBJECT",
            value = financialSubject.truthfulFinanceValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilFinanceField(
            label = "SUPPLIED FACTS",
            value = suppliedFinancialFacts.truthfulFinanceValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilFinanceField(
            label = "INTEGRATION FOCUS",
            value = integrationFocus.truthfulFinanceValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilFinanceField(
            label = "INTEGRATION OBJECTIVE",
            value = integrationObjective.truthfulFinanceValue(),
            foreground = foreground,
            muted = muted,
        )

        DevilFinanceField(
            label = "STATUS",
            value = integrationStatus.truthfulFinanceValue(),
            foreground = foreground,
            muted = muted,
        )
    }
}

@Composable
private fun DevilPersonalFinanceCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    focus: String?,
    objective: String?,
    approach: String?,
    status: String?,
) {
    DevilFinanceCard(
        title = "PERSONAL FINANCE",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilFinanceField("FOCUS", focus.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("OBJECTIVE", objective.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("APPROACH", approach.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("STATUS", status.truthfulFinanceValue(), foreground, muted)
    }
}

@Composable
private fun DevilAccountingCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    accountingFocus: String?,
    accountingObjective: String?,
    accountingBasis: String?,
    accountingStatus: String?,
    businessAccountingFocus: String?,
    businessAccountingObjective: String?,
    businessAccountingApproach: String?,
    businessAccountingStatus: String?,
) {
    DevilFinanceCard(
        title = "ACCOUNTING",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilFinanceField("ACCOUNTING FOCUS", accountingFocus.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("ACCOUNTING OBJECTIVE", accountingObjective.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("ACCOUNTING BASIS", accountingBasis.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("ACCOUNTING STATUS", accountingStatus.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("BUSINESS FOCUS", businessAccountingFocus.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("BUSINESS OBJECTIVE", businessAccountingObjective.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("BUSINESS APPROACH", businessAccountingApproach.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("BUSINESS STATUS", businessAccountingStatus.truthfulFinanceValue(), foreground, muted)
    }
}

@Composable
private fun DevilTaxCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    taxFocus: String?,
    taxObjective: String?,
    taxContext: String?,
    taxStatus: String?,
    indianTaxFocus: String?,
    indianTaxObjective: String?,
    indianTaxContext: String?,
    indianTaxStatus: String?,
) {
    DevilFinanceCard(
        title = "TAX INTELLIGENCE",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilFinanceField("TAX FOCUS", taxFocus.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("TAX OBJECTIVE", taxObjective.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("TAX CONTEXT", taxContext.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("TAX STATUS", taxStatus.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("INDIAN TAX FOCUS", indianTaxFocus.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("INDIAN TAX OBJECTIVE", indianTaxObjective.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("INDIA TAX CONTEXT", indianTaxContext.truthfulFinanceValue(), foreground, muted)
        DevilFinanceField("INDIAN TAX STATUS", indianTaxStatus.truthfulFinanceValue(), foreground, muted)
    }
}

@Composable
private fun DevilFinancialDocumentCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    documentFocus: String?,
    suppliedDocumentDescription: String?,
    documentInterpretationObjective: String?,
    documentStatus: String?,
) {
    DevilFinanceCard(
        title = "FINANCIAL DOCUMENTS",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilFinanceField("DOCUMENT FOCUS", documentFocus.truthfulFinanceValue(), foreground, muted)

        DevilFinanceField(
            label = "SUPPLIED DOCUMENT",
            value =
                suppliedDocumentDescription
                    ?.trim()
                    ?.takeIf(String::isNotEmpty)
                    ?: "No financial document description supplied.",
            foreground = foreground,
            muted = muted,
        )

        DevilFinanceField(
            "INTERPRETATION OBJECTIVE",
            documentInterpretationObjective.truthfulFinanceValue(),
            foreground,
            muted,
        )

        DevilFinanceField("STATUS", documentStatus.truthfulFinanceValue(), foreground, muted)
    }
}

@Composable
private fun DevilFinancialSafetyCard(
    devilRed: Color,
    foreground: Color,
    muted: Color,
    elevatedSurface: Color,
    safetyFocus: String?,
    verificationBasisDescription: String?,
    safetyInterpretation: String?,
    safetyStatus: String?,
) {
    DevilFinanceCard(
        title = "SAFETY & VERIFICATION",
        devilRed = devilRed,
        elevatedSurface = elevatedSurface,
    ) {
        DevilFinanceField("SAFETY FOCUS", safetyFocus.truthfulFinanceValue(), foreground, muted)

        DevilFinanceField(
            "SUPPLIED VERIFICATION BASIS",
            verificationBasisDescription.truthfulFinanceValue(),
            foreground,
            muted,
        )

        DevilFinanceField(
            "SAFETY INTERPRETATION",
            safetyInterpretation.truthfulFinanceValue(),
            foreground,
            muted,
        )

        DevilFinanceField("STATUS", safetyStatus.truthfulFinanceValue(), foreground, muted)
    }
}

@Composable
private fun DevilFinanceCard(
    title: String,
    devilRed: Color,
    elevatedSurface: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
        color = elevatedSurface,
        shape = RoundedCornerShape(20.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = devilRed.copy(alpha = 0.26f),
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = title,
                color = devilRed,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )

            content()
        }
    }
}

@Composable
private fun DevilFinanceField(
    label: String,
    value: String,
    foreground: Color,
    muted: Color,
) {
    Column(
        modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            color = muted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = value,
            color = foreground,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun DevilFinanceBoundaryFooter(
    devilRed: Color,
    muted: Color,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().devilInclusiveInteractiveTarget(),
        color = devilRed.copy(alpha = 0.07f),
        shape = RoundedCornerShape(18.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = devilRed.copy(alpha = 0.30f),
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "FINANCIAL BOUNDARY",
                color = devilRed,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text =
                    "Financial context remains bounded presentation. It does not establish " +
                        "account access, current external financial state, financial advice, " +
                        "investment suitability, tax authority, document authenticity, " +
                        "constitutional Verification, authorization, execution, transaction, " +
                        "World Model state, Learning, Memory, or verified Outcome.",
                color = muted,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private fun String?.truthfulFinanceValue(): String =
    this
        ?.trim()
        ?.takeIf(String::isNotEmpty)
        ?: "Unavailable"
