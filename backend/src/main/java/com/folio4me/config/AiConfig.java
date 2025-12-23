package com.folio4me.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * AI API 설정.
 * Anthropic Claude API 연결 설정.
 */
@Configuration
public class AiConfig {

    @Value("${folio.ai.api-key:}")
    private String apiKey;

    @Value("${folio.ai.model:claude-sonnet-4-20250514}")
    private String model;

    @Value("${folio.ai.max-tokens:4096}")
    private int maxTokens;

    @Value("${folio.ai.temperature:0.7}")
    private double temperature;

    @Value("${folio.ai.base-url:https://api.anthropic.com}")
    private String baseUrl;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * API 키가 설정되어 있는지 확인.
     */
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty();
    }
}
