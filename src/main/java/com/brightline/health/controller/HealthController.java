package com.brightline.health.controller;

import com.brightline.health.model.AiInsight;
import com.brightline.health.model.HealthReport;
import com.brightline.health.service.HealthService;
import com.brightline.health.service.InsightService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/integrations")
public class HealthController {

    private final HealthService service;
    private final InsightService insights;

    HealthController(HealthService service, InsightService insights) {
        this.service = service;
        this.insights = insights;
    }

    @GetMapping("/health")
    HealthReport health(@RequestParam(required = false) Integer windowMinutes) {
        return service.evaluate(windowMinutes);
    }

    @GetMapping("/{name}/insight")
    AiInsight insight(@PathVariable String name, @RequestParam(required = false) Integer windowMinutes) {
        var h = service.evaluate(windowMinutes).integrations().stream().filter(x -> x.integration().equalsIgnoreCase(name)).findFirst().orElseThrow();
        return insights.explain(h);
    }
}
