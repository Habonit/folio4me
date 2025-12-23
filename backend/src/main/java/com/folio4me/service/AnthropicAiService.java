package com.folio4me.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.folio4me.config.AiConfig;
import com.folio4me.model.ConversationMessage;
import com.folio4me.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * Anthropic Claude API를 사용하는 AI 서비스 구현.
 */
@Service
public class AnthropicAiService implements AiService {

    private static final Logger log = LoggerFactory.getLogger(AnthropicAiService.class);

    private final AiConfig aiConfig;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public AnthropicAiService(AiConfig aiConfig, ObjectMapper objectMapper) {
        this.aiConfig = aiConfig;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    }

    @Override
    public String generateResponse(String systemPrompt, String userMessage) {
        return generateResponse(systemPrompt, List.of(), userMessage);
    }

    @Override
    public String generateResponse(String systemPrompt, List<Message> conversationHistory, String userMessage) {
        if (!aiConfig.isConfigured()) {
            log.warn("AI API 키가 설정되지 않았습니다. 기본 응답을 반환합니다.");
            return getDefaultResponse(userMessage);
        }

        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", aiConfig.getModel());
            requestBody.put("max_tokens", aiConfig.getMaxTokens());
            requestBody.put("system", systemPrompt);

            ArrayNode messages = requestBody.putArray("messages");

            // 대화 히스토리 추가
            for (Message msg : conversationHistory) {
                ObjectNode msgNode = messages.addObject();
                msgNode.put("role", msg.role());
                msgNode.put("content", msg.content());
            }

            // 현재 사용자 메시지 추가
            ObjectNode userMsg = messages.addObject();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(aiConfig.getBaseUrl() + "/v1/messages"))
                .header("Content-Type", "application/json")
                .header("x-api-key", aiConfig.getApiKey())
                .header("anthropic-version", "2023-06-01")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                .timeout(Duration.ofSeconds(60))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("AI API 오류: {} - {}", response.statusCode(), response.body());
                return getDefaultResponse(userMessage);
            }

            JsonNode responseJson = objectMapper.readTree(response.body());
            return responseJson.path("content").get(0).path("text").asText();

        } catch (Exception e) {
            log.error("AI 응답 생성 중 오류 발생", e);
            return getDefaultResponse(userMessage);
        }
    }

    @Override
    public String generateContextualResponse(Session session, String prompt, String userMessage) {
        List<Message> history = session.getConversationHistory().stream()
            .map(msg -> new Message(msg.getRole(), msg.getContent()))
            .toList();

        String contextPrompt = buildContextPrompt(session, prompt);
        return generateResponse(contextPrompt, history, userMessage);
    }

    private String buildContextPrompt(Session session, String additionalPrompt) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("당신은 포트폴리오 생성을 도와주는 AI 어시스턴트입니다.\n");
        prompt.append("현재 단계: ").append(session.getCurrentStep().getDisplayName()).append("\n");
        prompt.append("사용자가 제공한 정보를 바탕으로 친절하고 전문적인 대화를 진행해주세요.\n\n");

        if (additionalPrompt != null && !additionalPrompt.isEmpty()) {
            prompt.append(additionalPrompt);
        }

        return prompt.toString();
    }

    private String getDefaultResponse(String userMessage) {
        // AI API가 설정되지 않았거나 오류 발생 시 기본 응답
        return "죄송합니다. 현재 AI 서비스를 이용할 수 없습니다. 잠시 후 다시 시도해주세요.";
    }
}
