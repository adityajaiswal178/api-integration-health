package com.brightline.health.model;

import java.util.List;

public record AiIncidentAnalysis(
        String summary,
        String probableCause,
        List<String> suggestedActions,
        String jiraTitle,
        String jiraDescription
) {
}