package com.folio4me.controller;

import com.folio4me.dto.SessionResponse;
import com.folio4me.model.Session;
import com.folio4me.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 세션 관리 REST API 컨트롤러.
 */
@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    private static final Logger log = LoggerFactory.getLogger(SessionController.class);

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * 새 세션 생성.
     * POST /api/v1/sessions
     *
     * @return 생성된 세션 정보
     */
    @PostMapping
    public ResponseEntity<SessionResponse> createSession() {
        log.info("POST /sessions - Creating new session");

        Session session = sessionService.createSession();
        SessionResponse response = sessionService.toSessionResponse(session);

        log.info("Created session: {}", session.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 세션 조회.
     * GET /api/v1/sessions/{sessionId}
     *
     * @param sessionId 세션 ID
     * @return 세션 정보
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponse> getSession(@PathVariable String sessionId) {
        log.info("GET /sessions/{}", sessionId);

        Session session = sessionService.getSession(sessionId);
        SessionResponse response = sessionService.toSessionResponse(session);

        return ResponseEntity.ok(response);
    }

    /**
     * 세션 삭제.
     * DELETE /api/v1/sessions/{sessionId}
     *
     * @param sessionId 세션 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable String sessionId) {
        log.info("DELETE /sessions/{}", sessionId);

        sessionService.deleteSession(sessionId);

        return ResponseEntity.noContent().build();
    }
}
