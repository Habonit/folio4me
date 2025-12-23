package com.folio4me.service.handler;

import com.folio4me.model.Award;
import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Step 6: Awards Handler.
 * 수상 경력 수집.
 */
@Component
public class Step6AwardsHandler implements StepHandler {

    private final AiService aiService;

    public Step6AwardsHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_6_AWARDS;
    }

    @Override
    public String handle(Session session, String userMessage) {
        if (isSkipInput(userMessage)) {
            return "수상 경력이 없으시군요. 다음으로 주요 대외활동을 입력해주세요.";
        }

        if (isCompleteInput(userMessage)) {
            List<Award> awards = session.getPortfolio().getAwards();
            if (awards.isEmpty()) {
                return "아직 입력된 수상 경력이 없습니다. 수상 내역을 입력하시거나 '없습니다'라고 답변해주세요.";
            }
            return "수상 경력 입력이 완료되었습니다. 다음으로 주요 대외활동을 입력해주세요.";
        }

        Award award = parseAward(userMessage);
        session.getPortfolio().getAwards().add(award);

        return String.format(
            "수상 경력을 추가했습니다!\n- 수상명: %s\n- 수상일: %s\n- 주최 기관: %s\n\n" +
            "추가 수상 경력이 있으시면 입력해주세요. 없으시면 '완료'라고 입력해주세요.",
            award.getTitle(), award.getPeriod(), award.getOrganization()
        );
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        return isSkipInput(userMessage) || isCompleteInput(userMessage);
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "수상 경력을 입력해주세요. 해커톤, 공모전, 사내 수상 등이 포함됩니다.\n" +
               "수상명, 수상일, 주최 기관을 알려주세요.\n" +
               "(예: 해커톤 대상, 2023.05, ABC 주최)\n" +
               "수상 경력이 없으시면 '없습니다'라고 입력해주세요.";
    }

    private Award parseAward(String userMessage) {
        Award award = new Award();
        String[] parts = userMessage.split("[,，]");

        if (parts.length >= 1) {
            award.setTitle(parts[0].trim());
        }
        if (parts.length >= 2) {
            award.setPeriod(normalizePeriod(parts[1].trim()));
        }
        if (parts.length >= 3) {
            award.setOrganization(parts[2].trim());
        }
        if (parts.length >= 4) {
            award.setContent(parts[3].trim());
        }

        award.setConfirmed(true);
        return award;
    }

    private String normalizePeriod(String period) {
        return period.replaceAll("[\\s\\.]", "")
            .replaceAll("~", "~")
            .replaceAll("-", "~");
    }
}
