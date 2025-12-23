package com.folio4me.service;

import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;

/**
 * 대화 단계 핸들러 인터페이스.
 * 각 단계별 사용자 입력 처리 및 응답 생성.
 */
public interface StepHandler {

    /**
     * 이 핸들러가 처리하는 단계.
     *
     * @return ConversationStep 열거형 값
     */
    ConversationStep getStep();

    /**
     * 사용자 입력 처리 및 응답 생성.
     *
     * @param session 현재 세션
     * @param userMessage 사용자 메시지
     * @return AI/시스템 응답 메시지
     */
    String handle(Session session, String userMessage);

    /**
     * 다음 단계로 진행할 수 있는지 확인.
     *
     * @param session 현재 세션
     * @param userMessage 사용자 메시지
     * @return 다음 단계 진행 가능 여부
     */
    boolean shouldAdvance(Session session, String userMessage);

    /**
     * 이 단계의 초기 질문 메시지.
     *
     * @param session 현재 세션
     * @return 단계 시작 시 보여줄 메시지
     */
    String getInitialPrompt(Session session);

    /**
     * "없습니다", "패스" 등 스킵 입력인지 확인.
     *
     * @param userMessage 사용자 메시지
     * @return 스킵 입력 여부
     */
    default boolean isSkipInput(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return false;
        }
        String normalized = userMessage.trim().toLowerCase();
        return normalized.equals("없습니다") ||
               normalized.equals("없음") ||
               normalized.equals("패스") ||
               normalized.equals("pass") ||
               normalized.equals("스킵") ||
               normalized.equals("skip") ||
               normalized.equals("없어요") ||
               normalized.equals("해당없음");
    }

    /**
     * 완료 입력인지 확인 (더 이상 추가할 항목이 없음).
     *
     * @param userMessage 사용자 메시지
     * @return 완료 입력 여부
     */
    default boolean isCompleteInput(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return false;
        }
        String normalized = userMessage.trim().toLowerCase();
        return normalized.equals("완료") ||
               normalized.equals("끝") ||
               normalized.equals("더 없습니다") ||
               normalized.equals("더없음") ||
               normalized.equals("done") ||
               normalized.equals("다 입력했습니다") ||
               normalized.equals("이상입니다");
    }
}
