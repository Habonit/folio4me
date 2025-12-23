package com.folio4me.service.handler;

import com.folio4me.model.Activity;
import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Step 7: MajorActivities Handler.
 * 주요 대외활동 수집 (이력서 포함).
 */
@Component
public class Step7MajorActivitiesHandler implements StepHandler {

    private final AiService aiService;

    public Step7MajorActivitiesHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_7_MAJOR_ACTIVITIES;
    }

    @Override
    public String handle(Session session, String userMessage) {
        if (isSkipInput(userMessage)) {
            return "주요 대외활동이 없으시군요. 다음으로 그 외 대외활동을 입력해주세요.";
        }

        if (isCompleteInput(userMessage)) {
            List<Activity> activities = session.getPortfolio().getActivities().getMajor();
            if (activities.isEmpty()) {
                return "아직 입력된 주요 대외활동이 없습니다. 활동을 입력하시거나 '없습니다'라고 답변해주세요.";
            }
            return "주요 대외활동 입력이 완료되었습니다. 다음으로 그 외 대외활동을 입력해주세요.";
        }

        Activity activity = parseActivity(userMessage);
        activity.setIncludeInResume(true); // 주요 활동은 이력서 포함
        session.getPortfolio().getActivities().getMajor().add(activity);

        return String.format(
            "주요 대외활동을 추가했습니다!\n- 활동명: %s\n- 기간: %s\n- 내용: %s\n\n" +
            "추가 주요 대외활동이 있으시면 입력해주세요. 없으시면 '완료'라고 입력해주세요.",
            activity.getTitle(), activity.getPeriod(), activity.getContent()
        );
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        return isSkipInput(userMessage) || isCompleteInput(userMessage);
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "주요 대외활동을 입력해주세요. 이력서에 포함할 중요한 활동입니다.\n" +
               "오픈소스 기여, 커뮤니티 활동, 봉사활동 등이 포함됩니다.\n" +
               "활동명, 기간, 활동 내용을 알려주세요.\n" +
               "(예: 오픈소스 컨트리뷰터, 2022~2023, Kubernetes 프로젝트 기여)\n" +
               "주요 대외활동이 없으시면 '없습니다'라고 입력해주세요.";
    }

    private Activity parseActivity(String userMessage) {
        Activity activity = new Activity();
        String[] parts = userMessage.split("[,，]");

        if (parts.length >= 1) {
            activity.setTitle(parts[0].trim());
        }
        if (parts.length >= 2) {
            activity.setPeriod(normalizePeriod(parts[1].trim()));
        }
        if (parts.length >= 3) {
            activity.setContent(parts[2].trim());
        }

        activity.setConfirmed(true);
        return activity;
    }

    private String normalizePeriod(String period) {
        return period.replaceAll("[\\s\\.]", "")
            .replaceAll("~", "~")
            .replaceAll("-", "~");
    }
}
