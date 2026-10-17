package com.brightline.health.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record IntegrationHealth(String integration, HealthStatus status, long requestCount, long failureCount,
                                long timeoutCount, double errorRate, double timeoutRate, long p95LatencyMs,
                                LocalDateTime lastSeen, Map<String, Long> statusCodeCounts, List<String> reasons) {
}
