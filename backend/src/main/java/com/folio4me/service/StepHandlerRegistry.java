package com.folio4me.service;

import com.folio4me.model.ConversationStep;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * StepHandler 레지스트리.
 * ConversationStep별 핸들러 매핑 및 조회.
 */
@Component
public class StepHandlerRegistry {

    private final Map<ConversationStep, StepHandler> handlers;

    public StepHandlerRegistry(List<StepHandler> stepHandlers) {
        this.handlers = new EnumMap<>(ConversationStep.class);

        for (StepHandler handler : stepHandlers) {
            handlers.put(handler.getStep(), handler);
        }
    }

    /**
     * 특정 단계의 핸들러 조회.
     *
     * @param step 대화 단계
     * @return 해당 단계의 StepHandler
     * @throws IllegalArgumentException 핸들러가 등록되지 않은 경우
     */
    public StepHandler getHandler(ConversationStep step) {
        StepHandler handler = handlers.get(step);
        if (handler == null) {
            throw new IllegalArgumentException("핸들러가 등록되지 않은 단계: " + step);
        }
        return handler;
    }

    /**
     * 핸들러가 등록되어 있는지 확인.
     *
     * @param step 대화 단계
     * @return 등록 여부
     */
    public boolean hasHandler(ConversationStep step) {
        return handlers.containsKey(step);
    }

    /**
     * 등록된 핸들러 수.
     *
     * @return 핸들러 수
     */
    public int getHandlerCount() {
        return handlers.size();
    }
}
