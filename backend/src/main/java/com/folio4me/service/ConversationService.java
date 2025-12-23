package com.folio4me.service;

import com.folio4me.dto.ConversationRequest;
import com.folio4me.dto.ConversationResponse;
import com.folio4me.model.ConversationMessage;
import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.model.SessionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 대화 처리 서비스.
 * 사용자 메시지를 받아 적절한 핸들러로 전달하고 응답 반환.
 */
@Service
public class ConversationService {

    private static final Logger log = LoggerFactory.getLogger(ConversationService.class);

    private final SessionService sessionService;
    private final StepHandlerRegistry stepHandlerRegistry;
    private final StorageService storageService;

    public ConversationService(
            SessionService sessionService,
            StepHandlerRegistry stepHandlerRegistry,
            StorageService storageService) {
        this.sessionService = sessionService;
        this.stepHandlerRegistry = stepHandlerRegistry;
        this.storageService = storageService;
    }

    /**
     * 사용자 메시지 처리.
     *
     * @param sessionId 세션 ID
     * @param request 대화 요청
     * @return 대화 응답
     */
    public ConversationResponse processMessage(String sessionId, ConversationRequest request) {
        Session session = sessionService.getSession(sessionId);
        String userMessage = request.getMessage();

        log.info("Processing message for session {}: step={}, message={}",
            sessionId, session.getCurrentStep(), truncate(userMessage, 50));

        // 사용자 메시지 히스토리에 추가
        session.addMessage(ConversationMessage.userMessage(userMessage, session.getCurrentStep()));

        // 현재 단계 핸들러로 처리
        ConversationStep currentStep = session.getCurrentStep();
        StepHandler handler = stepHandlerRegistry.getHandler(currentStep);

        String responseMessage = handler.handle(session, userMessage);

        // 다음 단계로 진행 여부 확인
        boolean shouldAdvance = handler.shouldAdvance(session, userMessage);
        boolean completed = false;

        if (shouldAdvance) {
            if (currentStep.isLast()) {
                // 마지막 단계 완료
                session.complete();
                completed = true;
                log.info("Session {} completed successfully", sessionId);
            } else {
                // 다음 단계로 이동
                session.advanceToNextStep();
                log.info("Session {} advanced to step {}", sessionId, session.getCurrentStep());

                // 다음 단계 초기 프롬프트 추가
                StepHandler nextHandler = stepHandlerRegistry.getHandler(session.getCurrentStep());
                String initialPrompt = nextHandler.getInitialPrompt(session);
                if (initialPrompt != null && !initialPrompt.isEmpty()) {
                    responseMessage = responseMessage + "\n\n" + initialPrompt;
                }
            }
        }

        // 세션 종료 상태 확인 (비기술 직군 등)
        if (session.getStatus() == SessionStatus.TERMINATED) {
            completed = true;
        }

        // AI 응답 히스토리에 추가
        session.addMessage(ConversationMessage.assistantMessage(responseMessage, session.getCurrentStep()));

        // 포트폴리오 저장
        storageService.savePortfolio(sessionId, session.getPortfolio());

        // 응답 생성
        ConversationResponse response = new ConversationResponse();
        response.setMessage(responseMessage);
        response.setCurrentStep(session.getCurrentStep());
        response.setCompleted(completed);
        response.setExpectedInputType(getExpectedInputType(session.getCurrentStep()));

        return response;
    }

    /**
     * 세션의 현재 대화 단계 조회.
     *
     * @param sessionId 세션 ID
     * @return 현재 대화 단계
     */
    public ConversationStep getCurrentStep(String sessionId) {
        Session session = sessionService.getSession(sessionId);
        return session.getCurrentStep();
    }

    /**
     * 세션의 현재 단계 초기 메시지 조회.
     * 세션 재개 시 사용.
     *
     * @param sessionId 세션 ID
     * @return 초기 프롬프트 메시지
     */
    public String getInitialPrompt(String sessionId) {
        Session session = sessionService.getSession(sessionId);
        StepHandler handler = stepHandlerRegistry.getHandler(session.getCurrentStep());
        return handler.getInitialPrompt(session);
    }

    private String getExpectedInputType(ConversationStep step) {
        return switch (step) {
            case STEP_0_GATE -> "role";
            case STEP_1_PERSONAL_INFO -> "personal_info";
            case STEP_2_WORK_EXPERIENCE -> "work_experience";
            case STEP_3_EDUCATION -> "education";
            case STEP_4_REPRESENTATIVE_PROJECTS, STEP_5_PROJECTS -> "project";
            case STEP_6_AWARDS -> "award";
            case STEP_7_MAJOR_ACTIVITIES, STEP_8_OTHER_ACTIVITIES -> "activity";
            case STEP_9_CERTIFICATIONS -> "certification";
            case STEP_10_TECHNICAL_SKILLS -> "skills";
            case STEP_11_ABOUT -> "confirmation";
        };
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }
}
