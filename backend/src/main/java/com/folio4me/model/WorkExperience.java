package com.folio4me.model;

import java.util.List;

/**
 * 경력 정보 (Step 2).
 */
public class WorkExperience {

    /** 회사명 */
    private String company;

    /** 기간 (YYYYMMDD~YYYYMMDD) */
    private String period;

    /** 담당 직무 */
    private String responsibility;

    /** 직급/직책 */
    private String position;

    /** 관련 링크 목록 (nullable) */
    private List<String> links;

    /** 완성 여부 */
    private boolean confirmed;

    public WorkExperience() {
        this.confirmed = false;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
