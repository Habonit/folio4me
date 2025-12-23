package com.folio4me.model;

import java.time.LocalDateTime;

/**
 * 대화 메시지 모델.
 * 사용자/AI 메시지를 포함한 대화 히스토리 추적.
 */
public class ConversationMessage {

    /** 메시지 역할 (user/assistant) */
    private String role;

    /** 메시지 내용 */
    private String content;

    /** 메시지 생성 시각 */
    private LocalDateTime timestamp;

    /** 해당 대화 단계 */
    private ConversationStep step;

    public ConversationMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public ConversationMessage(String role, String content, ConversationStep step) {
        this();
        this.role = role;
        this.content = content;
        this.step = step;
    }

    public static ConversationMessage userMessage(String content, ConversationStep step) {
        return new ConversationMessage("user", content, step);
    }

    public static ConversationMessage assistantMessage(String content, ConversationStep step) {
        return new ConversationMessage("assistant", content, step);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ConversationStep getStep() {
        return step;
    }

    public void setStep(ConversationStep step) {
        this.step = step;
    }
}
