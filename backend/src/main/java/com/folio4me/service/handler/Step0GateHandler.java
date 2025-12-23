package com.folio4me.service.handler;

import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.model.SessionStatus;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

/**
 * Step 0: Gate Handler.
 * 기술 직군 판별 및 서비스 이용 가능 여부 확인.
 */
@Component
public class Step0GateHandler implements StepHandler {

    private static final String SYSTEM_PROMPT = """
        당신은 포트폴리오 생성 서비스의 안내 AI입니다.
        사용자가 기술 직군(개발자, 엔지니어, 데이터 사이언티스트 등)인지 확인해야 합니다.

        기술 직군 키워드: 개발자, 엔지니어, 프로그래머, 데이터 사이언티스트, DevOps, SRE,
        백엔드, 프론트엔드, 풀스택, 모바일, 안드로이드, iOS, 웹개발,
        머신러닝, AI, 인공지능, 클라우드, 시스템, 네트워크, 보안

        사용자가 기술 직군이면 환영 인사와 함께 서비스 이용 가능을 안내하세요.
        비기술 직군이면 정중히 서비스 이용이 어렵다고 안내하세요.
        """;

    private final AiService aiService;
    private boolean isTechRole = false;

    public Step0GateHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_0_GATE;
    }

    @Override
    public String handle(Session session, String userMessage) {
        isTechRole = isTechRoleInput(userMessage);

        if (isTechRole) {
            session.getPortfolio().getMeta().setTargetRole(userMessage.trim());
            return "기술 직군이시군요! 포트폴리오 생성을 시작하겠습니다. " +
                   "먼저 개인정보를 입력해주세요. 이름, 이메일, GitHub 주소를 알려주시겠어요?";
        } else {
            session.setStatus(SessionStatus.TERMINATED);
            return "죄송합니다. 현재 folio4me 서비스는 기술 직군(개발자, 엔지니어 등)을 대상으로 " +
                   "포트폴리오 생성을 지원하고 있습니다. " +
                   "향후 다른 직군으로 서비스가 확장되면 다시 찾아주세요!";
        }
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        return isTechRole;
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "안녕하세요! folio4me 포트폴리오 생성 서비스입니다. " +
               "현재 직무 또는 희망하는 직무가 무엇인가요? (예: 백엔드 개발자, 프론트엔드 엔지니어)";
    }

    private boolean isTechRoleInput(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return false;
        }

        String normalized = userMessage.toLowerCase();
        String[] techKeywords = {
            "개발", "developer", "engineer", "프로그래머", "programmer",
            "백엔드", "backend", "프론트엔드", "frontend", "풀스택", "fullstack",
            "데이터", "data", "scientist", "머신러닝", "machine learning", "ml",
            "ai", "인공지능", "devops", "sre", "클라우드", "cloud",
            "안드로이드", "android", "ios", "모바일", "mobile",
            "웹", "web", "시스템", "system", "네트워크", "network",
            "보안", "security", "dba", "데이터베이스", "database",
            "qa", "테스트", "test", "인프라", "infra"
        };

        for (String keyword : techKeywords) {
            if (normalized.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
