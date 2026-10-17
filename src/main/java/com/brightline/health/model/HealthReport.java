package com.brightline.health.model;

import java.time.LocalDateTime;
import java.util.List;

public record HealthReport(LocalDateTime generatedAt, int windowMinutes, List<IntegrationHealth> integrations) {
}

