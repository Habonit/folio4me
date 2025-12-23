package com.folio4me.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.folio4me.dto.ConversationRequest;
import com.folio4me.dto.ConversationResponse;
import com.folio4me.model.ConversationStep;
import com.folio4me.model.Portfolio;
import com.folio4me.model.Session;
import com.folio4me.service.ConversationService;
import com.folio4me.service.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ConversationController 계약 테스트.
 * POST /sessions/{id}/messages 엔드포인트 검증.
 */
@WebMvcTest(ConversationController.class)
class ConversationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConversationService conversationService;

    @MockBean
    private SessionService sessionService;

    @Nested
    @DisplayName("POST /api/v1/sessions/{id}/messages")
    class PostMessagesTests {

        @Test
        @DisplayName("유효한 요청 시 200 OK와 응답 반환")
        void shouldReturn200WithResponse() throws Exception {
            // Given
            String sessionId = "test-session-id";
            ConversationRequest request = new ConversationRequest("백엔드 개발자");
            ConversationResponse response = new ConversationResponse(
                "안녕하세요! 기술 직군이시군요.",
                ConversationStep.STEP_0_GATE,
                false
            );

            when(conversationService.processMessage(eq(sessionId), any(ConversationRequest.class)))
                .thenReturn(response);

            // When & Then
            mockMvc.perform(post("/api/v1/sessions/{id}/messages", sessionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("안녕하세요! 기술 직군이시군요."))
                .andExpect(jsonPath("$.currentStep").value("STEP_0_GATE"))
                .andExpect(jsonPath("$.completed").value(false));
        }

        @Test
        @DisplayName("빈 메시지 요청 시 400 Bad Request")
        void shouldReturn400ForEmptyMessage() throws Exception {
            // Given
            String sessionId = "test-session-id";
            ConversationRequest request = new ConversationRequest("");

            // When & Then
            mockMvc.perform(post("/api/v1/sessions/{id}/messages", sessionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("존재하지 않는 세션 ID 요청 시 404 Not Found")
        void shouldReturn404ForInvalidSession() throws Exception {
            // Given
            String sessionId = "non-existent-session";
            ConversationRequest request = new ConversationRequest("test message");

            when(conversationService.processMessage(eq(sessionId), any(ConversationRequest.class)))
                .thenThrow(new com.folio4me.exception.SessionNotFoundException(sessionId));

            // When & Then
            mockMvc.perform(post("/api/v1/sessions/{id}/messages", sessionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("SESSION_NOT_FOUND"));
        }

        @Test
        @DisplayName("대화 완료 시 completed=true 반환")
        void shouldReturnCompletedTrueWhenConversationEnds() throws Exception {
            // Given
            String sessionId = "test-session-id";
            ConversationRequest request = new ConversationRequest("네, 확정합니다");
            ConversationResponse response = new ConversationResponse(
                "포트폴리오 작성이 완료되었습니다!",
                ConversationStep.STEP_11_ABOUT,
                true
            );

            when(conversationService.processMessage(eq(sessionId), any(ConversationRequest.class)))
                .thenReturn(response);

            // When & Then
            mockMvc.perform(post("/api/v1/sessions/{id}/messages", sessionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/sessions/{id}/portfolio")
    class GetPortfolioTests {

        @Test
        @DisplayName("유효한 세션의 포트폴리오 조회 시 200 OK")
        void shouldReturn200WithPortfolio() throws Exception {
            // Given
            String sessionId = "test-session-id";
            Session session = new Session(sessionId);
            Portfolio portfolio = session.getPortfolio();
            portfolio.getMeta().setTargetRole("백엔드 개발자");
            portfolio.getMeta().setStatus("draft");

            when(sessionService.getSession(sessionId)).thenReturn(session);

            // When & Then
            mockMvc.perform(get("/api/v1/sessions/{id}/portfolio", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.targetRole").value("백엔드 개발자"))
                .andExpect(jsonPath("$.meta.status").value("draft"));
        }

        @Test
        @DisplayName("존재하지 않는 세션의 포트폴리오 조회 시 404")
        void shouldReturn404ForInvalidSession() throws Exception {
            // Given
            String sessionId = "non-existent-session";

            when(sessionService.getSession(sessionId))
                .thenThrow(new com.folio4me.exception.SessionNotFoundException(sessionId));

            // When & Then
            mockMvc.perform(get("/api/v1/sessions/{id}/portfolio", sessionId))
                .andExpect(status().isNotFound());
        }
    }
}
