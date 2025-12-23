package com.folio4me.controller;

import com.folio4me.dto.ConversationRequest;
import com.folio4me.dto.ConversationResponse;
import com.folio4me.exception.InvalidInputException;
import com.folio4me.model.Portfolio;
import com.folio4me.model.Session;
import com.folio4me.service.ConversationService;
import com.folio4me.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 대화 관련 REST API 컨트롤러.
 */
@RestController
@RequestMapping("/api/v1/sessions/{sessionId}")
public class ConversationController {

    private static final Logger log = LoggerFactory.getLogger(ConversationController.class);

    private final ConversationService conversationService;
    private final SessionService sessionService;

    public ConversationController(
            ConversationService conversationService,
            SessionService sessionService) {
        this.conversationService = conversationService;
        this.sessionService = sessionService;
    }

    /**
     * 대화 메시지 전송.
     * POST /api/v1/sessions/{sessionId}/messages
     *
     * @param sessionId 세션 ID
     * @param request 대화 요청 (message 필드 포함)
     * @return 대화 응답
     */
    @PostMapping("/messages")
    public ResponseEntity<ConversationResponse> sendMessage(
            @PathVariable String sessionId,
            @RequestBody ConversationRequest request) {

        log.info("POST /sessions/{}/messages", sessionId);

        // 메시지 검증
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new InvalidInputException("message", "", "메시지를 입력해주세요.");
        }

        ConversationResponse response = conversationService.processMessage(sessionId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 세션의 포트폴리오 조회.
     * GET /api/v1/sessions/{sessionId}/portfolio
     *
     * @param sessionId 세션 ID
     * @return 포트폴리오 데이터
     */
    @GetMapping("/portfolio")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable String sessionId) {
        log.info("GET /sessions/{}/portfolio", sessionId);

        Session session = sessionService.getSession(sessionId);
        return ResponseEntity.ok(session.getPortfolio());
    }

    /**
     * 대화 재개 시 현재 단계 초기 프롬프트 조회.
     * GET /api/v1/sessions/{sessionId}/prompt
     *
     * @param sessionId 세션 ID
     * @return 현재 단계 초기 프롬프트
     */
    @GetMapping("/prompt")
    public ResponseEntity<ConversationResponse> getCurrentPrompt(@PathVariable String sessionId) {
        log.info("GET /sessions/{}/prompt", sessionId);

        Session session = sessionService.getSession(sessionId);
        String prompt = conversationService.getInitialPrompt(sessionId);

        ConversationResponse response = new ConversationResponse();
        response.setMessage(prompt);
        response.setCurrentStep(session.getCurrentStep());
        response.setCompleted(false);

        return ResponseEntity.ok(response);
    }
}
