package com.brightline.health;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

record LogFile(List<RequestLog> logs) {
}

record RequestLog(String integration, LocalDateTime timestamp, String status, Integer statusCode, long latencyMs) {
    boolean failed() {
        return !"success".equalsIgnoreCase(status);
    }

    boolean timeout() {
        return "timeout".equalsIgnoreCase(status);
    }
}

enum HealthStatus {HEALTHY, DEGRADED, UNHEALTHY, INSUFFICIENT_DATA, NO_DATA}

record IntegrationHealth(String integration, HealthStatus status, long requestCount, long failureCount,
                         long timeoutCount, double errorRate, double timeoutRate, long p95LatencyMs,
                         LocalDateTime lastSeen, Map<String, Long> statusCodeCounts, List<String> reasons) {
}

record HealthReport(LocalDateTime generatedAt, int windowMinutes, List<IntegrationHealth> integrations) {
}

record AiInsight(String integration, String summary, List<String> evidence, List<String> suggestedChecks,
                 String disclaimer, String source) {
}
