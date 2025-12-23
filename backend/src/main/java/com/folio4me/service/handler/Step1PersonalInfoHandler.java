package com.folio4me.service.handler;

import com.folio4me.model.ConversationStep;
import com.folio4me.model.PersonalInfo;
import com.folio4me.model.Session;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Step 1: PersonalInfo Handler.
 * 개인정보 수집 (이름, 이메일, GitHub).
 */
@Component
public class Step1PersonalInfoHandler implements StepHandler {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    );
    private static final Pattern GITHUB_PATTERN = Pattern.compile(
        "(github\\.com/[a-zA-Z0-9_-]+|[a-zA-Z0-9_-]+)"
    );

    private final AiService aiService;

    public Step1PersonalInfoHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_1_PERSONAL_INFO;
    }

    @Override
    public String handle(Session session, String userMessage) {
        PersonalInfo info = session.getPortfolio().getPersonalInfo();

        // 이메일 추출
        Matcher emailMatcher = EMAIL_PATTERN.matcher(userMessage);
        if (emailMatcher.find()) {
            info.setEmail(emailMatcher.group());
        }

        // GitHub 추출
        Matcher githubMatcher = GITHUB_PATTERN.matcher(userMessage.toLowerCase());
        if (githubMatcher.find()) {
            String github = githubMatcher.group();
            if (!github.startsWith("github.com/")) {
                github = "github.com/" + github;
            }
            info.setGithub("https://" + github);
        }

        // 이름 추출 (이메일과 GitHub를 제외한 부분에서)
        String nameCandidate = userMessage
            .replaceAll(EMAIL_PATTERN.pattern(), "")
            .replaceAll("(?i)github\\.com/[a-zA-Z0-9_-]+", "")
            .replaceAll("https?://", "")
            .replaceAll("[,./]", " ")
            .trim();

        // 이름 후보에서 첫 번째 의미 있는 단어 추출
        String[] parts = nameCandidate.split("\\s+");
        for (String part : parts) {
            if (part.length() >= 2 && !part.matches(".*\\d+.*")) {
                info.setName(part);
                break;
            }
        }

        // 필수 필드 검증
        if (!isComplete(info)) {
            return getMissingFieldsPrompt(info);
        }

        info.setConfirmed(true);
        return String.format(
            "개인정보를 확인했습니다!\n- 이름: %s\n- 이메일: %s\n- GitHub: %s\n\n" +
            "다음으로 경력 사항을 입력해주세요. 회사명, 재직기간, 담당업무, 직급을 알려주세요.",
            info.getName(), info.getEmail(), info.getGithub()
        );
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        PersonalInfo info = session.getPortfolio().getPersonalInfo();
        return isComplete(info);
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "개인정보를 입력해주세요. 이름, 이메일, GitHub 주소를 알려주시겠어요?";
    }

    private boolean isComplete(PersonalInfo info) {
        return info.getName() != null && !info.getName().isEmpty() &&
               info.getEmail() != null && !info.getEmail().isEmpty() &&
               info.getGithub() != null && !info.getGithub().isEmpty();
    }

    private String getMissingFieldsPrompt(PersonalInfo info) {
        StringBuilder missing = new StringBuilder("다음 정보가 필요합니다:\n");
        if (info.getName() == null || info.getName().isEmpty()) {
            missing.append("- 이름\n");
        }
        if (info.getEmail() == null || info.getEmail().isEmpty()) {
            missing.append("- 이메일\n");
        }
        if (info.getGithub() == null || info.getGithub().isEmpty()) {
            missing.append("- GitHub 주소\n");
        }
        missing.append("\n다시 입력해주세요.");
        return missing.toString();
    }
}
