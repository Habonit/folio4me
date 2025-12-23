package com.folio4me.service.handler;

import com.folio4me.model.ConversationStep;
import com.folio4me.model.Project;
import com.folio4me.model.Session;
import com.folio4me.service.AiService;
import com.folio4me.service.StepHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Step 4: RepresentativeProjects Handler.
 * 대표 프로젝트 수집.
 */
@Component
public class Step4RepresentativeProjectsHandler implements StepHandler {

    private final AiService aiService;

    public Step4RepresentativeProjectsHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_4_REPRESENTATIVE_PROJECTS;
    }

    @Override
    public String handle(Session session, String userMessage) {
        if (isSkipInput(userMessage)) {
            return "대표 프로젝트가 없으시군요. 다음으로 일반 프로젝트를 입력해주세요.";
        }

        if (isCompleteInput(userMessage)) {
            List<Project> projects = session.getPortfolio().getRepresentativeProjects();
            if (projects.isEmpty()) {
                return "아직 입력된 대표 프로젝트가 없습니다. 프로젝트를 입력하시거나 '없습니다'라고 답변해주세요.";
            }
            return "대표 프로젝트 입력이 완료되었습니다. 다음으로 일반 프로젝트를 입력해주세요.";
        }

        Project project = parseProject(userMessage);
        session.getPortfolio().getRepresentativeProjects().add(project);

        return String.format(
            "대표 프로젝트를 추가했습니다!\n- 프로젝트명: %s\n- 기간: %s\n- 담당: %s\n- 기술스택: %s\n\n" +
            "추가 대표 프로젝트가 있으시면 입력해주세요. 없으시면 '완료'라고 입력해주세요.",
            project.getName(),
            project.getPeriod(),
            String.join(", ", project.getResponsibility()),
            String.join(", ", project.getTechStack())
        );
    }

    @Override
    public boolean shouldAdvance(Session session, String userMessage) {
        return isSkipInput(userMessage) || isCompleteInput(userMessage);
    }

    @Override
    public String getInitialPrompt(Session session) {
        return "대표 프로젝트를 입력해주세요. 가장 자신 있는 프로젝트입니다.\n" +
               "프로젝트명, 기간, 담당 역할, 사용 기술을 알려주세요.\n" +
               "(예: 포트폴리오 생성기, 2023.01~2023.12, 백엔드 개발, Java/Spring/Docker)\n" +
               "대표 프로젝트가 없으시면 '없습니다'라고 입력해주세요.";
    }

    private Project parseProject(String userMessage) {
        Project project = new Project();
        String[] parts = userMessage.split("[,，]");

        if (parts.length >= 1) {
            project.setName(parts[0].trim());
        }
        if (parts.length >= 2) {
            project.setPeriod(normalizePeriod(parts[1].trim()));
        }
        if (parts.length >= 3) {
            String[] responsibilities = parts[2].trim().split("[/|]");
            project.setResponsibility(Arrays.stream(responsibilities)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList());
        }
        if (parts.length >= 4) {
            String[] techStack = parts[3].trim().split("[/|]");
            project.setTechStack(Arrays.stream(techStack)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList());
        }

        project.setConfirmed(true);
        return project;
    }

    private String normalizePeriod(String period) {
        return period.replaceAll("[\\s\\.]", "")
            .replaceAll("~", "~")
            .replaceAll("-", "~");
    }
}
