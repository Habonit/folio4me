package com.folio4me.model;

import java.util.List;

/**
 * 개별 대외활동 항목.
 */
public class Activity {

    /** 제목 */
    private String title;

    /** 기간 (YYYYMMDD~YYYYMMDD) */
    private String period;

    /** 활동 내용 */
    private String content;

    /** 관련 링크 목록 (nullable) */
    private List<String> links;

    /** 이력서 포함 여부 */
    private boolean includeInResume;

    /** 완성 여부 */
    private boolean confirmed;

    public Activity() {
        this.confirmed = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public boolean isIncludeInResume() {
        return includeInResume;
    }

    public void setIncludeInResume(boolean includeInResume) {
        this.includeInResume = includeInResume;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
