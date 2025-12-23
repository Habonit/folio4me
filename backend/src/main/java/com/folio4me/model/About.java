package com.folio4me.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 자기소개 (Step 11).
 * Phase 1 완료 조건: confirmed = true
 */
public class About {

    /** 자기소개 3문장 */
    private List<String> sentences;

    /** 사용자 확정 여부 */
    private boolean confirmed;

    public About() {
        this.sentences = new ArrayList<>();
        this.confirmed = false;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void setSentences(List<String> sentences) {
        this.sentences = sentences;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
