package com.folio4me.service.handler;

import com.folio4me.model.ConversationStep;
import com.folio4me.model.EducationItem;
import com.folio4me.model.Session;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Step 3: Education Handler.
 * 학력 정보 수집.
 */
@Component
public class Step3EducationHandler implements StepHandler {

    private final AiService aiService;

    public Step3EducationHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_3_EDUCATION;
    }

    @Override
    public String handle(Session session, String userMessage) {
        if (isSkipInput(userMessage)) {
            session.getPortfolio().getEducation().setDisplay(false);
            return "학력 사항을 표시하지 않겠습니다. 다음으로 대표 프로젝트를 입력해주세요.";
        }

        if (isCompleteInput(userMessage)) {
            List<EducationItem> items = session.getPortfolio().getEducation().getItems();
            if (items.isEmpty()) {
                return "아직 입력된 학력이 없습니다. 학력 정보를 입력하시거나 '없습니다'라고 답변해주세요.";
            }
            session.getPortfolio().getEducation().setDisplay(true);
            return "학력 입력이 완료되었습니다. 다음으로 대표 프로젝트를 입력해주세요.";
        }

        EducationItem item = parseEducation(userMessage);
        session.getPortfolio().getEducation().getItems().add(item);

        return String.format(
            "학력 정보를 추가했습니다!\n- 학교: %s\n- 전공: %s\n- 학위: %s\n- 기간: %s\n\n" +
            "추가 학력이 있으시면 입력해주세요. 없으시면 '완료'라고 입력해주세요.",
            item.getSchool(), item.getMajor(), item.getDegree(), item.getPeriod()
        );
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        return isSkipInput(userMessage) || isCompleteInput(userMessage);
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "학력 사항을 입력해주세요. 학교명, 전공, 학위, 재학기간을 알려주세요.\n" +
               "(예: 서울대학교, 컴퓨터공학, 학사, 2016~2020)\n" +
               "학력을 표시하지 않으시려면 '없습니다'라고 입력해주세요.";
    }

    private EducationItem parseEducation(String userMessage) {
        EducationItem item = new EducationItem();
        String[] parts = userMessage.split("[,，]");

        if (parts.length >= 1) {
            item.setSchool(parts[0].trim());
        }
        if (parts.length >= 2) {
            item.setMajor(parts[1].trim());
        }
        if (parts.length >= 3) {
            item.setDegree(parts[2].trim());
        }
        if (parts.length >= 4) {
            item.setPeriod(normalizePeriod(parts[3].trim()));
        }

        item.setConfirmed(true);
        return item;
    }

    private String normalizePeriod(String period) {
        return period.replaceAll("[\\s\\.]", "")
            .replaceAll("~", "~")
            .replaceAll("-", "~");
    }
}
