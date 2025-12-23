package com.folio4me.service;

import com.folio4me.dto.SessionResponse;
import com.folio4me.exception.SessionNotFoundException;
import com.folio4me.model.Portfolio;
import com.folio4me.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리 서비스.
 * UUID 기반 세션 생성, 조회, 삭제.
 */
@Service
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final StorageService storageService;

    public SessionService(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 새 세션 생성.
     *
     * @return 생성된 세션
     */
    public Session createSession() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId);

        // 포트폴리오 초기화 및 메타 정보 설정
        session.getPortfolio().getMeta().setCreatedAt(java.time.LocalDateTime.now());
        session.getPortfolio().getMeta().setStatus("draft");

        sessions.put(sessionId, session);

        // 세션 디렉토리 및 초기 파일 생성
        storageService.createSessionDirectory(sessionId);
        storageService.savePortfolio(sessionId, session.getPortfolio());

        log.info("Created new session: {}", sessionId);
        return session;
    }

    /**
     * 세션 조회.
     *
     * @param sessionId 세션 ID
     * @return 세션
     * @throws SessionNotFoundException 세션을 찾을 수 없는 경우
     */
    public Session getSession(String sessionId) {
        Session session = sessions.get(sessionId);

        if (session == null) {
            // 파일에서 세션 복구 시도
            session = tryLoadSessionFromStorage(sessionId);
        }

        if (session == null) {
            throw new SessionNotFoundException(sessionId);
        }

        return session;
    }

    /**
     * 세션 삭제.
     *
     * @param sessionId 세션 ID
     */
    public void deleteSession(String sessionId) {
        Session session = sessions.remove(sessionId);
        if (session == null) {
            throw new SessionNotFoundException(sessionId);
        }
        storageService.deleteSessionDirectory(sessionId);
        log.info("Deleted session: {}", sessionId);
    }

    /**
     * 세션 응답 DTO 생성.
     *
     * @param session 세션
     * @return 세션 응답 DTO
     */
    public SessionResponse toSessionResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setCurrentStep(session.getCurrentStep());
        response.setStatus(session.getStatus());
        response.setCreatedAt(session.getCreatedAt());
        response.setLastActivityAt(session.getLastActivityAt());
        response.setProgressPercentage(calculateProgress(session));
        return response;
    }

    private Session tryLoadSessionFromStorage(String sessionId) {
        Portfolio portfolio = storageService.loadPortfolio(sessionId);
        if (portfolio == null) {
            return null;
        }

        Session session = new Session(sessionId);
        session.setPortfolio(portfolio);
        sessions.put(sessionId, session);

        log.info("Restored session from storage: {}", sessionId);
        return session;
    }

    private int calculateProgress(Session session) {
        int currentStepIndex = session.getCurrentStep().ordinal();
        int totalSteps = 12; // 0-11
        return (int) ((currentStepIndex / (double) totalSteps) * 100);
    }
}
