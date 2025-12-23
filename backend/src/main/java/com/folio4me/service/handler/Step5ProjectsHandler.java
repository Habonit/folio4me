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
 * Step 5: Projects Handler.
 * 일반 프로젝트 수집.
 */
@Component
public class Step5ProjectsHandler implements StepHandler {

    private final AiService aiService;

    public Step5ProjectsHandler(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_5_PROJECTS;
    }

    @Override
    public String handle(Session session, String userMessage) {
        if (isSkipInput(userMessage)) {
            return "일반 프로젝트가 없으시군요. 다음으로 수상 경력을 입력해주세요.";
        }

        if (isCompleteInput(userMessage)) {
            List<Project> projects = session.getPortfolio().getProjects();
            if (projects.isEmpty()) {
                return "아직 입력된 프로젝트가 없습니다. 프로젝트를 입력하시거나 '없습니다'라고 답변해주세요.";
            }
            return "프로젝트 입력이 완료되었습니다. 다음으로 수상 경력을 입력해주세요.";
        }

        Project project = parseProject(userMessage);
        session.getPortfolio().getProjects().add(project);

        return String.format(
            "프로젝트를 추가했습니다!\n- 프로젝트명: %s\n- 기간: %s\n- 담당: %s\n- 기술스택: %s\n\n" +
            "추가 프로젝트가 있으시면 입력해주세요. 없으시면 '완료'라고 입력해주세요.",
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
        return "그 외 프로젝트를 입력해주세요. 사이드 프로젝트, 팀 프로젝트 등이 포함됩니다.\n" +
               "프로젝트명, 기간, 담당 역할, 사용 기술을 알려주세요.\n" +
               "(예: 사이드 프로젝트, 2022.06~2022.12, 풀스택 개발, React/Node.js)\n" +
               "추가할 프로젝트가 없으시면 '없습니다'라고 입력해주세요.";
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
