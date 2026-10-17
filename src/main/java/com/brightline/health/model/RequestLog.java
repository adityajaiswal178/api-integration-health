package com.brightline.health.model;

import java.time.LocalDateTime;

public record RequestLog(String integration, LocalDateTime timestamp, String status, Integer statusCode,
                         long latencyMs) {

    public boolean failed() {
        return !"success".equalsIgnoreCase(status);
    }

    public boolean timeout() {
        return "timeout".equalsIgnoreCase(status);
    }
}