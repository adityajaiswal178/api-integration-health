package com.brightline.health.model;

import java.util.List;

public record AiInsight(String integration, String summary, List<String> evidence, List<String> suggestedChecks,
                        String disclaimer, String source) {
}
