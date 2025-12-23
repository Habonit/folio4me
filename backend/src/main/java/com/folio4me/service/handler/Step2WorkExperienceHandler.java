package com.folio4me.service.handler;

import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.model.WorkExperience;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Step 2: WorkExperience Handler.
 * 경력 정보 수집.
 */
@Component
public class Step2WorkExperienceHandler implements StepHandler {

    private final AiService aiService;

    public Step2WorkExperienceHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_2_WORK_EXPERIENCE;
    }

    @Override
    public String handle(Session session, String userMessage) {
        if (isSkipInput(userMessage)) {
            return "경력 사항이 없으시군요. 다음으로 학력 사항을 입력해주세요. " +
                   "학교명, 전공, 학위, 재학기간을 알려주세요.";
        }

        if (isCompleteInput(userMessage)) {
            List<WorkExperience> experiences = session.getPortfolio().getWorkExperience();
            if (experiences.isEmpty()) {
                return "아직 입력된 경력이 없습니다. 경력 정보를 입력하시거나 '없습니다'라고 답변해주세요.";
            }
            return "경력 입력이 완료되었습니다. 다음으로 학력 사항을 입력해주세요.";
        }

        // 경력 정보 파싱 및 저장
        WorkExperience experience = parseWorkExperience(userMessage);
        session.getPortfolio().getWorkExperience().add(experience);

        return String.format(
            "경력 정보를 추가했습니다!\n- 회사: %s\n- 기간: %s\n- 직무: %s\n- 직급: %s\n\n" +
            "추가 경력이 있으시면 입력해주세요. 없으시면 '완료'라고 입력해주세요.",
            experience.getCompany(),
            experience.getPeriod(),
            experience.getResponsibility(),
            experience.getPosition()
        );
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        return isSkipInput(userMessage) || isCompleteInput(userMessage);
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "경력 사항을 입력해주세요. 회사명, 재직기간, 담당업무, 직급을 알려주세요.\n" +
               "(예: ABC회사, 2020.03~현재, 백엔드 개발, 시니어 개발자)\n" +
               "경력이 없으시면 '없습니다'라고 입력해주세요.";
    }

    private WorkExperience parseWorkExperience(String userMessage) {
        WorkExperience experience = new WorkExperience();
        String[] parts = userMessage.split("[,，]");

        if (parts.length >= 1) {
            experience.setCompany(parts[0].trim());
        }
        if (parts.length >= 2) {
            experience.setPeriod(normalizePeriod(parts[1].trim()));
        }
        if (parts.length >= 3) {
            experience.setResponsibility(parts[2].trim());
        }
        if (parts.length >= 4) {
            experience.setPosition(parts[3].trim());
        }

        experience.setConfirmed(true);
        return experience;
    }

    private String normalizePeriod(String period) {
        // 기간 형식 정규화 (YYYYMMDD~YYYYMMDD 또는 present)
        String normalized = period.replaceAll("[\\s\\.]", "")
            .replaceAll("~", "~")
            .replaceAll("-", "~")
            .replaceAll("현재", "present")
            .replaceAll("진행중", "present");

        // 연월만 있으면 01일 추가
        if (normalized.matches("\\d{6}~\\d{6}")) {
            String[] parts = normalized.split("~");
            return parts[0] + "01~" + parts[1] + "01";
        }
        if (normalized.matches("\\d{6}~present")) {
            return normalized.substring(0, 6) + "01~present";
        }

        return normalized;
    }
}
