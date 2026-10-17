package com.brightline.health;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "health.policy")
public record HealthPolicyProperties(int windowMinutes, int noDataMinutes, int minimumRequests,
                                     double degradedErrorRate, double unhealthyErrorRate, double degradedTimeoutRate,
                                     double unhealthyTimeoutRate, long degradedP95Ms, long unhealthyP95Ms) {
}
