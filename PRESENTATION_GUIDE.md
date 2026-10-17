# 10-minute presentation guide

## Opening (1 minute)

“Brightline's problem is delayed detection. My design produces an explainable health view from logs while keeping
operational judgment configurable.”

## Design (3 minutes)

Walk through ingestion, windowing, metrics, deterministic policy, dashboard/API, then optional AI. Stress why low volume
is not healthy and why worst-signal-wins is easy to explain.

## Demo (3 minutes)

Run the app, show cards, inspect JSON, and show the insight endpoint. Point to `application.yml` and one unit test.

## Trade-offs (2 minutes)

Explain why CSV and in-memory processing fit the assessment, and what changes for production: stream/time-series
storage, per-integration SLOs, consecutive windows, authentication and paging.

## Close (1 minute)

“The core detection path is deterministic, testable and useful without AI. AI improves readability, not truth.”

# Likely panel questions

1. **Why 15 minutes?** A starting balance between sensitivity and stability; validate against historical incidents.
2. **Why p95?** It captures tail pain better than averages while remaining understandable; add p99 when volume supports
   it.
3. **What if traffic is low?** Return insufficient data, not healthy; optionally use a longer window or active probes.
4. **What if one metric is bad?** Worst-signal-wins for v1 because either failures or severe latency can harm customers.
5. **How prevent alert noise?** Require consecutive unhealthy windows, cooldown/deduplication and severity-aware
   routing.
6. **Why not let AI classify health?** Deterministic rules are reproducible, auditable and testable; AI is advisory.
7. **How handle provider differences?** Externalize thresholds per integration and eventually align them with SLOs.
8. **How scale?** Stream to Kafka/Pub/Sub, aggregate into a time-series store, evaluate statelessly and cache snapshots.
9. **How secure HR data?** Never send payloads to the model; use metric-only context, RBAC, tenant isolation, encryption
   and audit.
10. **How know the thresholds work?** Back-test on labeled incidents, measure precision/recall and tune with Ops
    feedback.
11. **What about zero traffic?** Treat it as NO_DATA and distinguish expected quiet periods from broken ingestion using
    schedules/heartbeats.
12. **What would you build next?** Historical trends and alert lifecycle first; AI sophistication only after trustworthy
    metrics.
