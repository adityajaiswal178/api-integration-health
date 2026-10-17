# Brightline API Integration Health

A Java 17 / Spring Boot 3.5.1 MVP that turns integration request logs into explainable health classifications.

## Run

Prerequisites: JDK 17+ and Maven 3.9+.

```bash
mvn clean test
mvn spring-boot:run
```

Open `http://localhost:8080` or call:

```bash
curl http://localhost:8080/api/v1/integrations/health
curl http://localhost:8080/api/v1/integrations/payroll/insight
curl 'http://localhost:8080/api/v1/integrations/health?windowMinutes=30'
```

To use the provided case-study dataset, replace `src/main/resources/integration_logs.json` and retain the header:
a top-level `logs` array containing `integration`, `timestamp`, `status`, nullable `status_code`, and `latency_ms`.

## Demo story

1. Open the dashboard and identify red/amber integrations.
2. Open `/api/v1/integrations/health` and show evidence plus reason codes.
3. Change a threshold in `application.yml`, restart, and explain policy configurability.
4. Open `/api/v1/integrations/payroll/insight`; explain that summary generation is downstream of deterministic scoring.

## Known limitations

CSV-only ingestion, in-memory calculation, one global policy, no authentication, no durable alert state, and no live LLM
call. The insight endpoint intentionally uses a safe deterministic fallback so the core demo has no external dependency.

## Optional Spring AI adapter

Add Spring AI's model starter/BOM compatible with your chosen provider, inject `ChatClient.Builder`, and implement the
same insight contract. Send only the serialized `IntegrationHealth` plus threshold values. Require structured output and
validate every numeric assertion; fall back to the included service on any failure. Store the API key in an environment
variable, never in Git.
