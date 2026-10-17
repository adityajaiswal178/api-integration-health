package com.brightline.health.service;

import com.brightline.health.model.AiIncidentAnalysis;
import com.brightline.health.model.IntegrationHealth;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiIncidentService {

    private final ChatClient chatClient;

    public AiIncidentService(
            ChatClient.Builder builder) {

        this.chatClient = builder.build();
    }

    public AiIncidentAnalysis analyze(
            IntegrationHealth health) {

        String prompt = """
                You are assisting an operations team
                with integration incident analysis.
                
                Integration: %s
                Error Rate: %.2f%%
                Requests: %d
                Failures: %d
                Timeouts: %d
                Reasons: %s
                
                Based ONLY on the supplied information:
                
                1. Summarize the incident.
                2. Identify possible causes.
                3. Provide troubleshooting suggestions.
                4. Create a concise Jira title.
                5. Create a Jira-ready incident description.
                
                Do not claim that a suggested cause is
                confirmed unless the logs prove it.
                """

                .formatted(
                        health.integration(),
                        health.errorRate() * 100,
                        health.requestCount(),
                        health.failureCount(),
                        health.timeoutCount(),
                        health.reasons()
                );


        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .entity(AiIncidentAnalysis.class);
    }
}
