package com.folio4me.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 대외활동 섹션 (Step 7, 8).
 */
public class Activities {

    /** 주요 대외활동 (이력서 포함) */
    private List<Activity> major;

    /** 그 외 대외활동 (이력서 미포함) */
    private List<Activity> minor;

    public Activities() {
        this.major = new ArrayList<>();
        this.minor = new ArrayList<>();
    }

    public List<Activity> getMajor() {
        return major;
    }

    public void setMajor(List<Activity> major) {
        this.major = major;
    }

    public List<Activity> getMinor() {
        return minor;
    }

    public void setMinor(List<Activity> minor) {
        this.minor = minor;
    }
}
