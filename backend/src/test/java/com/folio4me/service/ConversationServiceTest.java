package com.folio4me.service;

import com.folio4me.dto.ConversationRequest;
import com.folio4me.dto.ConversationResponse;
import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.model.SessionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * ConversationService 단위 테스트.
 * TDD - 테스트를 먼저 작성하고 실패 확인 후 구현.
 */
@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private StepHandlerRegistry stepHandlerRegistry;

    @Mock
    private StepHandler stepHandler;

    @Mock
    private StorageService storageService;

    private ConversationService conversationService;

    @BeforeEach
    void setUp() {
        conversationService = new ConversationService(
            sessionService,
            stepHandlerRegistry,
            storageService
        );
    }

    @Nested
    @DisplayName("processMessage 테스트")
    class ProcessMessageTests {

        @Test
        @DisplayName("유효한 세션 ID로 메시지 처리 시 응답 반환")
        void shouldProcessMessageWithValidSession() {
            // Given
            String sessionId = "test-session-id";
            String userMessage = "백엔드 개발자";
            Session session = new Session(sessionId);
            session.setCurrentStep(ConversationStep.STEP_0_GATE);

            ConversationRequest request = new ConversationRequest(userMessage);

            when(sessionService.getSession(sessionId)).thenReturn(session);
            when(stepHandlerRegistry.getHandler(ConversationStep.STEP_0_GATE))
                .thenReturn(stepHandler);
            when(stepHandler.handle(eq(session), eq(userMessage)))
                .thenReturn("안녕하세요! 개인정보를 입력해주세요.");

            // When
            ConversationResponse response = conversationService.processMessage(sessionId, request);

            // Then
            assertNotNull(response);
            assertEquals("안녕하세요! 개인정보를 입력해주세요.", response.getMessage());
            verify(storageService).savePortfolio(eq(sessionId), any());
        }

        @Test
        @DisplayName("Step 0에서 기술 직군 확인 시 다음 단계로 진행")
        void shouldAdvanceToNextStepAfterGateConfirmation() {
            // Given
            String sessionId = "test-session-id";
            String userMessage = "네, 맞습니다";
            Session session = new Session(sessionId);
            session.setCurrentStep(ConversationStep.STEP_0_GATE);

            ConversationRequest request = new ConversationRequest(userMessage);

            // Step 0 핸들러를 위한 별도 mock 생성
            StepHandler nextStepHandler = mock(StepHandler.class);

            when(sessionService.getSession(sessionId)).thenReturn(session);
            when(stepHandlerRegistry.getHandler(ConversationStep.STEP_0_GATE))
                .thenReturn(stepHandler);
            when(stepHandlerRegistry.getHandler(ConversationStep.STEP_1_PERSONAL_INFO))
                .thenReturn(nextStepHandler);
            when(stepHandler.handle(eq(session), eq(userMessage)))
                .thenReturn("개인정보를 입력해주세요.");
            when(stepHandler.shouldAdvance(session, userMessage)).thenReturn(true);
            when(nextStepHandler.getInitialPrompt(session))
                .thenReturn("이름, 이메일, GitHub 주소를 알려주세요.");

            // When
            ConversationResponse response = conversationService.processMessage(sessionId, request);

            // Then
            assertNotNull(response);
            assertEquals(ConversationStep.STEP_1_PERSONAL_INFO, session.getCurrentStep());
        }

        @Test
        @DisplayName("마지막 단계(Step 11) 완료 시 세션 상태가 COMPLETED로 변경")
        void shouldCompleteSessionAfterLastStep() {
            // Given
            String sessionId = "test-session-id";
            String userMessage = "네, 확정합니다";
            Session session = new Session(sessionId);
            session.setCurrentStep(ConversationStep.STEP_11_ABOUT);

            ConversationRequest request = new ConversationRequest(userMessage);

            when(sessionService.getSession(sessionId)).thenReturn(session);
            when(stepHandlerRegistry.getHandler(ConversationStep.STEP_11_ABOUT))
                .thenReturn(stepHandler);
            when(stepHandler.handle(eq(session), eq(userMessage)))
                .thenReturn("포트폴리오 작성이 완료되었습니다!");
            when(stepHandler.shouldAdvance(session, userMessage)).thenReturn(true);

            // When
            ConversationResponse response = conversationService.processMessage(sessionId, request);

            // Then
            assertNotNull(response);
            assertTrue(response.isCompleted());
            assertEquals(SessionStatus.COMPLETED, session.getStatus());
        }
    }

    @Nested
    @DisplayName("getCurrentStep 테스트")
    class GetCurrentStepTests {

        @Test
        @DisplayName("세션의 현재 단계 반환")
        void shouldReturnCurrentStep() {
            // Given
            String sessionId = "test-session-id";
            Session session = new Session(sessionId);
            session.setCurrentStep(ConversationStep.STEP_3_EDUCATION);

            when(sessionService.getSession(sessionId)).thenReturn(session);

            // When
            ConversationStep currentStep = conversationService.getCurrentStep(sessionId);

            // Then
            assertEquals(ConversationStep.STEP_3_EDUCATION, currentStep);
        }
    }

    @Nested
    @DisplayName("대화 흐름 테스트")
    class ConversationFlowTests {

        @Test
        @DisplayName("12단계 순차 진행 확인")
        void shouldProgressThroughAllSteps() {
            // Given
            String sessionId = "test-session-id";
            Session session = new Session(sessionId);

            // 모든 단계를 순차적으로 진행
            ConversationStep[] expectedOrder = {
                ConversationStep.STEP_0_GATE,
                ConversationStep.STEP_1_PERSONAL_INFO,
                ConversationStep.STEP_2_WORK_EXPERIENCE,
                ConversationStep.STEP_3_EDUCATION,
                ConversationStep.STEP_4_REPRESENTATIVE_PROJECTS,
                ConversationStep.STEP_5_PROJECTS,
                ConversationStep.STEP_6_AWARDS,
                ConversationStep.STEP_7_MAJOR_ACTIVITIES,
                ConversationStep.STEP_8_OTHER_ACTIVITIES,
                ConversationStep.STEP_9_CERTIFICATIONS,
                ConversationStep.STEP_10_TECHNICAL_SKILLS,
                ConversationStep.STEP_11_ABOUT
            };

            // When & Then
            ConversationStep currentStep = ConversationStep.STEP_0_GATE;
            for (int i = 0; i < expectedOrder.length; i++) {
                assertEquals(expectedOrder[i], currentStep);
                if (!currentStep.isLast()) {
                    currentStep = currentStep.next();
                }
            }
            assertTrue(ConversationStep.STEP_11_ABOUT.isLast());
        }
    }
}
