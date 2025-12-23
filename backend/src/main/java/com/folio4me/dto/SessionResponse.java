package com.folio4me.dto;

import com.folio4me.model.ConversationStep;
import com.folio4me.model.SessionStatus;

import java.time.LocalDateTime;

/**
 * 세션 응답 DTO.
 * GET /sessions/{id} 응답.
 */
public class SessionResponse {

    /** 세션 UUID */
    private String id;

    /** 현재 대화 단계 */
    private ConversationStep currentStep;

    /** 세션 상태 */
    private SessionStatus status;

    /** 세션 생성 시각 */
    private LocalDateTime createdAt;

    /** 마지막 활동 시각 */
    private LocalDateTime lastActivityAt;

    /** 대화 진행률 (0-100) */
    private int progressPercentage;

    public SessionResponse() {
    }

    public SessionResponse(String id, ConversationStep currentStep, SessionStatus status) {
        this.id = id;
        this.currentStep = currentStep;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConversationStep getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(ConversationStep currentStep) {
        this.currentStep = currentStep;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
