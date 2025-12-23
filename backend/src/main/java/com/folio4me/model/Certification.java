package com.folio4me.model;

import java.util.List;

/**
 * 자격증/시험 정보 (Step 9).
 */
public class Certification {

    /** 자격증/시험명 */
    private String title;

    /** 기간 (YYYYMMDD~YYYYMMDD) */
    private String period;

    /** 발급/주관 기관 */
    private String organization;

    /** 관련 링크 목록 (nullable) */
    private List<String> links;

    /** 완성 여부 */
    private boolean confirmed;

    public Certification() {
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
