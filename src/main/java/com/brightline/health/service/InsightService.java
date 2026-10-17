package com.brightline.health.service;

import com.brightline.health.model.AiInsight;
import com.brightline.health.model.HealthStatus;
import com.brightline.health.model.IntegrationHealth;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class InsightService {

    public AiInsight explain(IntegrationHealth h) {

        String summary = switch (h.status()) {
            case HEALTHY -> h.integration() + " is healthy and remains within configured limits.";
            case DEGRADED -> h.integration() + " needs attention because an early-warning threshold was exceeded.";
            case UNHEALTHY -> h.integration() + " is unhealthy because at least one critical threshold was exceeded.";
            case INSUFFICIENT_DATA ->
                    "There is not enough recent traffic to make a reliable judgment for " + h.integration() + ".";
            case NO_DATA -> "No recent request data is available for " + h.integration() + ".";
        };

        var evidence = List.of("status=" + h.status(), "requestCount=" + h.requestCount(), "errorRate=" + String.format(Locale.ROOT, "%.2f%%", h.errorRate() * 100), "timeoutRate=" + String.format(Locale.ROOT, "%.2f%%", h.timeoutRate() * 100), "statusCodes=" + h.statusCodeCounts(), "p95LatencyMs=" + h.p95LatencyMs(), "reasons=" + h.reasons());
        var checks = h.status() == HealthStatus.UNHEALTHY || h.status() == HealthStatus.DEGRADED ? List.of("Check downstream provider availability and rate limits", "Group failures by outcome/status code and compare with recent deployments", "Review timeout, authentication, and credential-expiry signals") : List.of("Continue monitoring the next evaluation window");
        return new AiInsight(h.integration(), summary, evidence, checks, "Suggested checks are not a confirmed root cause.", "DETERMINISTIC_FALLBACK");
    }
}
