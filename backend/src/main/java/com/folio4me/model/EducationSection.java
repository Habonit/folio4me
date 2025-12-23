package com.folio4me.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 학력 섹션 (Step 3).
 * display 플래그로 노출 여부 제어.
 */
public class EducationSection {

    /** 노출 여부 */
    private boolean display;

    /** 학력 항목 목록 */
    private List<EducationItem> items;

    public EducationSection() {
        this.display = true;
        this.items = new ArrayList<>();
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public List<EducationItem> getItems() {
        return items;
    }

    public void setItems(List<EducationItem> items) {
        this.items = items;
    }
}
