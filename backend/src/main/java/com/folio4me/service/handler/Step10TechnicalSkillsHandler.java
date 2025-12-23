package com.folio4me.service.handler;

import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.model.TechnicalSkills;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Step 10: TechnicalSkills Handler.
 * 기술 스택 수집 (능숙/기본 분류).
 */
@Component
public class Step10TechnicalSkillsHandler implements StepHandler {

    private static final Pattern STRONG_PATTERN = Pattern.compile(
        "(?:능숙|숙련|전문|잘\\s*함|주력)[:\\s]*([^/]+)"
    );
    private static final Pattern KNOWLEDGEABLE_PATTERN = Pattern.compile(
        "(?:기본|이해|알고|경험|사용\\s*해봄)[:\\s]*([^/]+)"
    );

    private final AiService aiService;

    public Step10TechnicalSkillsHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_10_TECHNICAL_SKILLS;
    }

    @Override
    public String handle(Session session, String userMessage) {
        TechnicalSkills skills = session.getPortfolio().getTechnicalSkills();

        List<String> strong = new ArrayList<>();
        List<String> knowledgeable = new ArrayList<>();

        // 능숙 기술 추출
        Matcher strongMatcher = STRONG_PATTERN.matcher(userMessage);
        if (strongMatcher.find()) {
            String[] parts = strongMatcher.group(1).split("[,，\\s]+");
            strong.addAll(Arrays.stream(parts)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList());
        }

        // 기본 이해 기술 추출
        Matcher knowledgeableMatcher = KNOWLEDGEABLE_PATTERN.matcher(userMessage);
        if (knowledgeableMatcher.find()) {
            String[] parts = knowledgeableMatcher.group(1).split("[,，\\s]+");
            knowledgeable.addAll(Arrays.stream(parts)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList());
        }

        // 패턴 매칭이 안 되면 슬래시로 구분 시도
        if (strong.isEmpty() && knowledgeable.isEmpty()) {
            String[] sections = userMessage.split("/");
            if (sections.length >= 2) {
                strong.addAll(parseSkillList(sections[0]));
                knowledgeable.addAll(parseSkillList(sections[1]));
            } else {
                // 모든 기술을 능숙으로 분류
                strong.addAll(parseSkillList(userMessage));
            }
        }

        skills.setStrong(strong);
        skills.setKnowledgeable(knowledgeable);
        skills.setConfirmed(true);

        return String.format(
            "기술 스택을 등록했습니다!\n- 능숙: %s\n- 기본 이해: %s\n\n" +
            "마지막으로 자기소개 문구를 작성해드리겠습니다. 잠시만 기다려주세요.",
            String.join(", ", strong),
            String.join(", ", knowledgeable)
        );
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        TechnicalSkills skills = session.getPortfolio().getTechnicalSkills();
        return skills.isConfirmed();
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "기술 스택을 입력해주세요. 능숙한 기술과 기본 이해 기술을 구분해서 알려주세요.\n" +
               "(예: 능숙: Java, Spring, Docker / 기본: Kubernetes, AWS)\n" +
               "또는 그냥 기술명을 나열해주셔도 됩니다.";
    }

    private List<String> parseSkillList(String input) {
        String cleaned = input
            .replaceAll("(?:능숙|숙련|전문|기본|이해|알고|경험)[:\\s]*", "")
            .trim();
        String[] parts = cleaned.split("[,，\\s]+");
        return Arrays.stream(parts)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();
    }
}
