package com.folio4me.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 기술 역량 (Step 10).
 */
public class TechnicalSkills {

    /** 능숙한 기술 목록 */
    private List<String> strong;

    /** 기본 이해 기술 목록 */
    private List<String> knowledgeable;

    /** 완성 여부 */
    private boolean confirmed;

    public TechnicalSkills() {
        this.strong = new ArrayList<>();
        this.knowledgeable = new ArrayList<>();
        this.confirmed = false;
    }

    public List<String> getStrong() {
        return strong;
    }

    public void setStrong(List<String> strong) {
        this.strong = strong;
    }

    public List<String> getKnowledgeable() {
        return knowledgeable;
    }

    public void setKnowledgeable(List<String> knowledgeable) {
        this.knowledgeable = knowledgeable;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
