package com.brightline.health;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class JsonLogRepository {
    private final ObjectMapper mapper;

    JsonLogRepository(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    List<RequestLog> findAll() {
        try (var in = new ClassPathResource("integration_logs.json").getInputStream()) {
            return mapper.readValue(in, LogFile.class).logs();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot parse integration_logs.json", e);
        }
    }
}
