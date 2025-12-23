package com.folio4me.model;

import java.util.List;

/**
 * 수상 경력 (Step 6).
 */
public class Award {

    /** 수상명 */
    private String title;

    /** 기간 (YYYYMMDD~YYYYMMDD) */
    private String period;

    /** 수상 내용 */
    private String content;

    /** 수상 기관 */
    private String organization;

    /** 관련 링크 목록 (nullable) */
    private List<String> links;

    /** 완성 여부 */
    private boolean confirmed;

    public Award() {
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
