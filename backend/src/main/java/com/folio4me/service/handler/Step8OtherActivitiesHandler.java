package com.folio4me.service.handler;

import com.folio4me.model.Activity;
import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Step 8: OtherActivities Handler.
 * 그 외 대외활동 수집 (이력서 미포함).
 */
@Component
public class Step8OtherActivitiesHandler implements StepHandler {

    private final AiService aiService;

    public Step8OtherActivitiesHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_8_OTHER_ACTIVITIES;
    }

    @Override
    public String handle(Session session, String userMessage) {
        if (isSkipInput(userMessage)) {
            return "그 외 대외활동이 없으시군요. 다음으로 자격증을 입력해주세요.";
        }

        if (isCompleteInput(userMessage)) {
            List<Activity> activities = session.getPortfolio().getActivities().getMinor();
            if (activities.isEmpty()) {
                return "아직 입력된 기타 대외활동이 없습니다. 활동을 입력하시거나 '없습니다'라고 답변해주세요.";
            }
            return "기타 대외활동 입력이 완료되었습니다. 다음으로 자격증을 입력해주세요.";
        }

        Activity activity = parseActivity(userMessage);
        activity.setIncludeInResume(false); // 기타 활동은 이력서 미포함
        session.getPortfolio().getActivities().getMinor().add(activity);

        return String.format(
            "기타 대외활동을 추가했습니다!\n- 활동명: %s\n- 기간: %s\n- 내용: %s\n\n" +
            "추가 기타 대외활동이 있으시면 입력해주세요. 없으시면 '완료'라고 입력해주세요.",
            activity.getTitle(), activity.getPeriod(), activity.getContent()
        );
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        return isSkipInput(userMessage) || isCompleteInput(userMessage);
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "그 외 대외활동을 입력해주세요. 이력서에는 포함되지 않지만 포트폴리오에 표시됩니다.\n" +
               "스터디, 동아리, 취미 활동 등이 포함됩니다.\n" +
               "활동명, 기간, 활동 내용을 알려주세요.\n" +
               "(예: 알고리즘 스터디, 2021~2022, 주 1회 알고리즘 문제 풀이)\n" +
               "기타 대외활동이 없으시면 '없습니다'라고 입력해주세요.";
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
