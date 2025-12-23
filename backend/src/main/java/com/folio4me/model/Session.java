package com.folio4me.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 세션 모델.
 * UUID 기반 세션 관리 및 대화 상태 추적.
 */
public class Session {

    /** 세션 UUID */
    private String id;

    /** 현재 대화 단계 */
    private ConversationStep currentStep;

    /** 세션 상태 */
    private SessionStatus status;

    /** 대화 히스토리 */
    private List<ConversationMessage> conversationHistory;

    /** 포트폴리오 데이터 */
    private Portfolio portfolio;

    /** 세션 생성 시각 */
    private LocalDateTime createdAt;

    /** 마지막 활동 시각 */
    private LocalDateTime lastActivityAt;

    public Session() {
        this.currentStep = ConversationStep.STEP_0_GATE;
        this.status = SessionStatus.ACTIVE;
        this.conversationHistory = new ArrayList<>();
        this.portfolio = new Portfolio();
        this.createdAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }

    public Session(String id) {
        this();
        this.id = id;
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

    public List<ConversationMessage> getConversationHistory() {
        return conversationHistory;
    }

    public void setConversationHistory(List<ConversationMessage> conversationHistory) {
        this.conversationHistory = conversationHistory;
    }

    public void addMessage(ConversationMessage message) {
        this.conversationHistory.add(message);
        this.lastActivityAt = LocalDateTime.now();
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
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

    /**
     * 다음 단계로 이동.
     */
    public void advanceToNextStep() {
        if (this.currentStep != null && !this.currentStep.isLast()) {
            this.currentStep = this.currentStep.next();
        }
        this.lastActivityAt = LocalDateTime.now();
    }

    /**
     * 세션 완료 처리.
     */
    public void complete() {
        this.status = SessionStatus.COMPLETED;
        this.portfolio.getMeta().setStatus("complete");
        this.lastActivityAt = LocalDateTime.now();
    }
}
