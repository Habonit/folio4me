package com.folio4me.dto;

import com.folio4me.model.ConversationStep;

/**
 * 대화 응답 DTO.
 * POST /sessions/{id}/messages 응답.
 */
public class ConversationResponse {

    /** AI 응답 메시지 */
    private String message;

    /** 현재 대화 단계 */
    private ConversationStep currentStep;

    /** 대화 완료 여부 */
    private boolean completed;

    /** 다음 예상 입력 유형 */
    private String expectedInputType;

    public ConversationResponse() {
    }

    public ConversationResponse(String message, ConversationStep currentStep, boolean completed) {
        this.message = message;
        this.currentStep = currentStep;
        this.completed = completed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConversationStep getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(ConversationStep currentStep) {
        this.currentStep = currentStep;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getExpectedInputType() {
        return expectedInputType;
    }

    public void setExpectedInputType(String expectedInputType) {
        this.expectedInputType = expectedInputType;
    }
}
