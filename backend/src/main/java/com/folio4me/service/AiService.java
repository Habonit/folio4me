package com.folio4me.service;

import com.folio4me.model.Session;

import java.util.List;

/**
 * AI 서비스 인터페이스.
 * 대화형 AI 응답 생성을 위한 추상화.
 */
public interface AiService {

    /**
     * 시스템 프롬프트와 사용자 메시지를 기반으로 AI 응답 생성.
     *
     * @param systemPrompt 시스템 프롬프트
     * @param userMessage 사용자 메시지
     * @return AI 응답 문자열
     */
    String generateResponse(String systemPrompt, String userMessage);

    /**
     * 대화 히스토리를 포함한 AI 응답 생성.
     *
     * @param systemPrompt 시스템 프롬프트
     * @param conversationHistory 이전 대화 내역
     * @param userMessage 현재 사용자 메시지
     * @return AI 응답 문자열
     */
    String generateResponse(String systemPrompt, List<Message> conversationHistory, String userMessage);

    /**
     * 세션 컨텍스트를 포함한 AI 응답 생성.
     *
     * @param session 현재 세션
     * @param prompt 추가 프롬프트
     * @param userMessage 사용자 메시지
     * @return AI 응답 문자열
     */
    String generateContextualResponse(Session session, String prompt, String userMessage);

    /**
     * 대화 메시지 구조.
     */
    record Message(String role, String content) {}
}
