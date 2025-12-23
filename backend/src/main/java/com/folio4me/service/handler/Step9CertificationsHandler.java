package com.folio4me.service.handler;

import com.folio4me.model.Certification;
import com.folio4me.model.ConversationStep;
import com.folio4me.model.Session;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Step 9: Certifications Handler.
 * 자격증/시험 정보 수집.
 */
@Component
public class Step9CertificationsHandler implements StepHandler {

    private final AiService aiService;

    public Step9CertificationsHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_9_CERTIFICATIONS;
    }

    @Override
    public String handle(Session session, String userMessage) {
        if (isSkipInput(userMessage)) {
            return "자격증이 없으시군요. 다음으로 기술 스택을 입력해주세요.";
        }

        if (isCompleteInput(userMessage)) {
            List<Certification> certifications = session.getPortfolio().getCertifications();
            if (certifications.isEmpty()) {
                return "아직 입력된 자격증이 없습니다. 자격증을 입력하시거나 '없습니다'라고 답변해주세요.";
            }
            return "자격증 입력이 완료되었습니다. 다음으로 기술 스택을 입력해주세요.";
        }

        Certification certification = parseCertification(userMessage);
        session.getPortfolio().getCertifications().add(certification);

        return String.format(
            "자격증을 추가했습니다!\n- 자격증명: %s\n- 취득일: %s\n- 발급기관: %s\n\n" +
            "추가 자격증이 있으시면 입력해주세요. 없으시면 '완료'라고 입력해주세요.",
            certification.getTitle(), certification.getPeriod(), certification.getOrganization()
        );
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        return isSkipInput(userMessage) || isCompleteInput(userMessage);
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "자격증을 입력해주세요. 기술 관련 자격증, 어학 시험 등이 포함됩니다.\n" +
               "자격증명, 취득일, 발급기관을 알려주세요.\n" +
               "(예: 정보처리기사, 2023.06, 한국산업인력공단)\n" +
               "자격증이 없으시면 '없습니다'라고 입력해주세요.";
    }

    private Certification parseCertification(String userMessage) {
        Certification certification = new Certification();
        String[] parts = userMessage.split("[,，]");

        if (parts.length >= 1) {
            certification.setTitle(parts[0].trim());
        }
        if (parts.length >= 2) {
            certification.setPeriod(normalizePeriod(parts[1].trim()));
        }
        if (parts.length >= 3) {
            certification.setOrganization(parts[2].trim());
        }

        certification.setConfirmed(true);
        return certification;
    }

    private String normalizePeriod(String period) {
        return period.replaceAll("[\\s\\.]", "")
            .replaceAll("~", "~")
            .replaceAll("-", "~");
    }
}
