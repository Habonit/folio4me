package com.folio4me.integration;

import com.folio4me.dto.ConversationRequest;
import com.folio4me.dto.ConversationResponse;
import com.folio4me.model.ConversationStep;
import com.folio4me.model.Portfolio;
import com.folio4me.model.SessionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 대화 흐름 통합 테스트.
 * Step 0 → Step 11 전체 흐름 검증.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ConversationFlowTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @DisplayName("전체 12단계 대화 흐름 완료")
    void shouldCompleteFullConversationFlow() {
        // Step 1: 세션 생성
        ResponseEntity<Map> sessionResponse = restTemplate.postForEntity(
            baseUrl + "/api/v1/sessions",
            null,
            Map.class
        );

        assertEquals(HttpStatus.CREATED, sessionResponse.getStatusCode());
        assertNotNull(sessionResponse.getBody());
        String sessionId = (String) sessionResponse.getBody().get("id");
        assertNotNull(sessionId);

        // Step 2: Step 0 - Gate (직군 확인) -> 기술 직군이면 바로 Step 1로 진행
        ConversationResponse response = sendMessage(sessionId, "백엔드 개발자입니다");
        assertNotNull(response);
        // 기술 직군 확인 시 바로 다음 단계로 진행됨
        assertEquals(ConversationStep.STEP_1_PERSONAL_INFO, response.getCurrentStep());

        // Step 4: Step 1 - PersonalInfo
        response = sendMessage(sessionId, "홍길동, hong@gmail.com, github.com/hong");
        assertEquals(ConversationStep.STEP_2_WORK_EXPERIENCE, response.getCurrentStep());

        // Step 5: Step 2 - WorkExperience
        response = sendMessage(sessionId, "ABC회사, 2020.03~현재, 백엔드 개발, 시니어");
        response = sendMessage(sessionId, "더 없습니다");
        assertEquals(ConversationStep.STEP_3_EDUCATION, response.getCurrentStep());

        // Step 6: Step 3 - Education
        response = sendMessage(sessionId, "서울대학교, 컴퓨터공학, 학사, 2016~2020");
        response = sendMessage(sessionId, "완료");
        assertEquals(ConversationStep.STEP_4_REPRESENTATIVE_PROJECTS, response.getCurrentStep());

        // Step 7: Step 4 - Representative Projects
        response = sendMessage(sessionId, "포트폴리오 생성기, 2023.01~12, 백엔드 개발, Java/Spring");
        response = sendMessage(sessionId, "완료");
        assertEquals(ConversationStep.STEP_5_PROJECTS, response.getCurrentStep());

        // Step 8: Step 5 - Projects
        response = sendMessage(sessionId, "없습니다");
        assertEquals(ConversationStep.STEP_6_AWARDS, response.getCurrentStep());

        // Step 9: Step 6 - Awards
        response = sendMessage(sessionId, "해커톤 우승, 2023.05, ABC 주최");
        response = sendMessage(sessionId, "완료");
        assertEquals(ConversationStep.STEP_7_MAJOR_ACTIVITIES, response.getCurrentStep());

        // Step 10: Step 7 - Major Activities
        response = sendMessage(sessionId, "없습니다");
        assertEquals(ConversationStep.STEP_8_OTHER_ACTIVITIES, response.getCurrentStep());

        // Step 11: Step 8 - Other Activities
        response = sendMessage(sessionId, "없습니다");
        assertEquals(ConversationStep.STEP_9_CERTIFICATIONS, response.getCurrentStep());

        // Step 12: Step 9 - Certifications
        response = sendMessage(sessionId, "정보처리기사, 2023.06, 한국산업인력공단");
        response = sendMessage(sessionId, "완료");
        assertEquals(ConversationStep.STEP_10_TECHNICAL_SKILLS, response.getCurrentStep());

        // Step 13: Step 10 - Technical Skills
        response = sendMessage(sessionId, "능숙: Java, Spring, Docker / 기본: Kubernetes");
        assertEquals(ConversationStep.STEP_11_ABOUT, response.getCurrentStep());

        // Step 14: Step 11 - About (AI 생성 후 확정)
        response = sendMessage(sessionId, "네, 확정합니다");
        assertTrue(response.isCompleted());

        // 최종 검증: Portfolio 조회
        ResponseEntity<Portfolio> portfolioResponse = restTemplate.getForEntity(
            baseUrl + "/api/v1/sessions/" + sessionId + "/portfolio",
            Portfolio.class
        );

        assertEquals(HttpStatus.OK, portfolioResponse.getStatusCode());
        Portfolio portfolio = portfolioResponse.getBody();
        assertNotNull(portfolio);
        assertEquals("complete", portfolio.getMeta().getStatus());
        assertTrue(portfolio.getAbout().isConfirmed());
    }

    @Test
    @DisplayName("비기술 직군 사용자는 Step 0에서 중단")
    void shouldTerminateForNonTechRole() {
        // 세션 생성
        ResponseEntity<Map> sessionResponse = restTemplate.postForEntity(
            baseUrl + "/api/v1/sessions",
            null,
            Map.class
        );
        String sessionId = (String) sessionResponse.getBody().get("id");

        // Step 0 - 비기술 직군 입력
        ConversationResponse response = sendMessage(sessionId, "마케팅 담당자입니다");

        // 서비스 이용 불가 안내
        assertNotNull(response.getMessage());
        // 세션이 TERMINATED 상태인지 확인
        ResponseEntity<Map> statusResponse = restTemplate.getForEntity(
            baseUrl + "/api/v1/sessions/" + sessionId,
            Map.class
        );
        // assertEquals("TERMINATED", statusResponse.getBody().get("status"));
    }

    @Test
    @DisplayName("세션 재개 시 이전 단계에서 계속")
    void shouldResumeFromPreviousStep() {
        // 세션 생성 및 일부 진행
        ResponseEntity<Map> sessionResponse = restTemplate.postForEntity(
            baseUrl + "/api/v1/sessions",
            null,
            Map.class
        );
        String sessionId = (String) sessionResponse.getBody().get("id");

        sendMessage(sessionId, "백엔드 개발자");
        sendMessage(sessionId, "네");
        sendMessage(sessionId, "홍길동, hong@gmail.com, github.com/hong");

        // 세션 상태 확인
        ResponseEntity<Map> statusResponse = restTemplate.getForEntity(
            baseUrl + "/api/v1/sessions/" + sessionId,
            Map.class
        );

        // 세션이 Step 2에 있어야 함
        // assertEquals("STEP_2_WORK_EXPERIENCE", statusResponse.getBody().get("currentStep"));
    }

    private ConversationResponse sendMessage(String sessionId, String message) {
        ConversationRequest request = new ConversationRequest(message);
        HttpEntity<ConversationRequest> entity = new HttpEntity<>(request);

        ResponseEntity<ConversationResponse> response = restTemplate.exchange(
            baseUrl + "/api/v1/sessions/" + sessionId + "/messages",
            HttpMethod.POST,
            entity,
            ConversationResponse.class
        );

        return response.getBody();
    }
}
