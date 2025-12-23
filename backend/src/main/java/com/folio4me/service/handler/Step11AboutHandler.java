package com.folio4me.service.handler;

import com.folio4me.model.About;
import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Step 11: About Handler.
 * AI 기반 자기소개 3문장 생성 및 수정.
 */
@Component
public class Step11AboutHandler implements StepHandler {

    private static final String ABOUT_GENERATION_PROMPT = """
        당신은 포트폴리오 자기소개 문구를 작성하는 전문가입니다.
        사용자의 정보를 바탕으로 3문장의 자기소개를 작성해주세요.

        규칙:
        1. 정확히 3문장으로 작성
        2. 전문적이면서도 친근한 톤 유지
        3. 사용자의 강점과 경험을 자연스럽게 녹여내기
        4. 지원 직무에 맞는 키워드 사용

        형식:
        - 첫 문장: 자기 소개 및 현재 상태
        - 둘째 문장: 핵심 역량 또는 경험
        - 셋째 문장: 가치관 또는 목표
        """;

    private final AiService aiService;
    private boolean awaitingConfirmation = false;
    private List<String> generatedSentences = new ArrayList<>();

    public Step11AboutHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_11_ABOUT;
    }

    @Override
    public String handle(Session session, String userMessage) {
        About about = session.getPortfolio().getAbout();

        // 첫 진입 시 자기소개 생성
        if (!awaitingConfirmation && about.getSentences().isEmpty()) {
            return generateAbout(session);
        }

        // 확정 응답 확인
        if (isConfirmationInput(userMessage)) {
            about.setConfirmed(true);
            awaitingConfirmation = false;
            return "자기소개가 확정되었습니다! 포트폴리오 작성이 완료되었습니다. 🎉\n" +
                   "이제 스타일 선택 단계로 넘어갈 수 있습니다.";
        }

        // 수정 요청 확인
        if (isModificationRequest(userMessage)) {
            return regenerateAbout(session, userMessage);
        }

        // 그 외 응답은 수정 요청으로 처리
        return "자기소개를 수정하시겠어요? 구체적인 수정 방향을 알려주시거나,\n" +
               "'확정', '확인', '네'라고 답변해주시면 현재 내용으로 저장됩니다.";
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        return session.getPortfolio().getAbout().isConfirmed();
    }

    @Override
    public String getInitialPrompt(Session session) {
        return generateAbout(session);
    }

    private String generateAbout(Session session) {
        awaitingConfirmation = true;

        // 포트폴리오 정보를 바탕으로 자기소개 생성
        String context = buildContextFromPortfolio(session);
        String prompt = ABOUT_GENERATION_PROMPT + "\n\n사용자 정보:\n" + context;

        String aiResponse = aiService.generateResponse(prompt, "자기소개 3문장을 생성해주세요.");

        // AI 응답에서 문장 추출
        generatedSentences = extractSentences(aiResponse);

        About about = session.getPortfolio().getAbout();
        about.setSentences(generatedSentences);

        return "자기소개 초안을 작성했습니다:\n\n" +
               formatSentences(generatedSentences) +
               "\n\n이 내용으로 확정하시겠어요? 수정이 필요하시면 수정 방향을 알려주세요.\n" +
               "(예: '더 전문적으로', '좀 더 친근하게', '기술 강조해줘' 등)";
    }

    private String regenerateAbout(Session session, String modificationRequest) {
        String context = buildContextFromPortfolio(session);
        String prompt = ABOUT_GENERATION_PROMPT +
                       "\n\n사용자 정보:\n" + context +
                       "\n\n수정 요청: " + modificationRequest;

        String aiResponse = aiService.generateResponse(prompt, "수정된 자기소개 3문장을 생성해주세요.");

        generatedSentences = extractSentences(aiResponse);

        About about = session.getPortfolio().getAbout();
        about.setSentences(generatedSentences);

        return "수정된 자기소개입니다:\n\n" +
               formatSentences(generatedSentences) +
               "\n\n이 내용으로 확정하시겠어요?";
    }

    private String buildContextFromPortfolio(Session session) {
        var portfolio = session.getPortfolio();
        StringBuilder context = new StringBuilder();

        context.append("- 희망 직무: ").append(portfolio.getMeta().getTargetRole()).append("\n");

        if (portfolio.getPersonalInfo().getName() != null) {
            context.append("- 이름: ").append(portfolio.getPersonalInfo().getName()).append("\n");
        }

        if (!portfolio.getWorkExperience().isEmpty()) {
            context.append("- 경력: ");
            portfolio.getWorkExperience().forEach(exp ->
                context.append(exp.getCompany()).append("(").append(exp.getPosition()).append("), ")
            );
            context.append("\n");
        }

        if (!portfolio.getTechnicalSkills().getStrong().isEmpty()) {
            context.append("- 주요 기술: ")
                   .append(String.join(", ", portfolio.getTechnicalSkills().getStrong()))
                   .append("\n");
        }

        if (!portfolio.getRepresentativeProjects().isEmpty()) {
            context.append("- 대표 프로젝트: ");
            portfolio.getRepresentativeProjects().forEach(proj ->
                context.append(proj.getName()).append(", ")
            );
            context.append("\n");
        }

        return context.toString();
    }

    private List<String> extractSentences(String text) {
        // 문장 단위로 분리
        String[] sentences = text.split("[.。]");
        return Arrays.stream(sentences)
            .map(String::trim)
            .filter(s -> s.length() > 10) // 너무 짧은 문장 제외
            .limit(3) // 최대 3문장
            .toList();
    }

    private String formatSentences(List<String> sentences) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < sentences.size(); i++) {
            formatted.append((i + 1)).append(". ").append(sentences.get(i)).append(".\n");
        }
        return formatted.toString();
    }

    private boolean isConfirmationInput(String userMessage) {
        String normalized = userMessage.trim().toLowerCase();
        return normalized.equals("확정") ||
               normalized.equals("확인") ||
               normalized.equals("네") ||
               normalized.equals("예") ||
               normalized.equals("좋아요") ||
               normalized.equals("ok") ||
               normalized.equals("yes") ||
               normalized.contains("확정합니다") ||
               normalized.contains("이걸로") ||
               normalized.contains("괜찮");
    }

    private boolean isModificationRequest(String userMessage) {
        String normalized = userMessage.trim().toLowerCase();
        return normalized.contains("수정") ||
               normalized.contains("변경") ||
               normalized.contains("바꿔") ||
               normalized.contains("다시") ||
               normalized.contains("더") ||
               normalized.contains("좀") ||
               normalized.contains("조금") ||
               normalized.contains("너무") ||
               normalized.contains("강조") ||
               normalized.contains("톤") ||
               normalized.contains("스타일");
    }
}
