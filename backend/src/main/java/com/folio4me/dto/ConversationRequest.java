package com.folio4me.dto;

/**
 * 대화 요청 DTO.
 * POST /sessions/{id}/messages 요청 바디.
 */
public class ConversationRequest {

    /** 사용자 메시지 */
    private String message;

    public ConversationRequest() {
    }

    public ConversationRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
