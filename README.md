# Devil

Devil is a constitutionally governed, unified AI assistant architecture.

This repository contains the new Devil implementation. It is separate from SJ Lite V1.

## Current milestone

Devil Coding Stage 7 — Complete

Stage 7 completed the Constitutional Decision Foundation by introducing the complete bounded decision-evaluation chain. The runtime now coordinates DecisionEvaluationRequestProvider → DecisionEvaluationResolver → DecisionEvaluationResultMapper inside the Decision Authority while preserving constitutional boundaries, trace continuity, and a single authoritative decision path. No decision policy or reasoning engine has been introduced yet, so the default implementation correctly returns a deferred DecisionRecord without fabricating decisions.

Stage 7 — Constitutional Decision Foundation is complete.

## Official resume point

Tag: `devil-stage-7-complete`


## Core rule

No implementation may bypass:

Constitution → Identity → Trust → Authorization → Understanding → Decision → Task → Capability → Verification → Outcome
