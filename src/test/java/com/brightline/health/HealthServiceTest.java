package com.brightline.health;

import com.brightline.health.service.HealthService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HealthServiceTest {
    @Test
    void nearestRankP95() {
        assertThat(HealthService.percentile95(List.of(10L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L))).isEqualTo(100L);
    }
}
