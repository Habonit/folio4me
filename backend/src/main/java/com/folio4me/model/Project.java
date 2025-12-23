package com.folio4me.model;

import java.util.List;

/**
 * 프로젝트 정보 (Step 4, 5).
 * 대표 프로젝트와 일반 프로젝트 모두 동일 구조.
 */
public class Project {

    /** 프로젝트명 */
    private String name;

    /** 기간 (YYYYMMDD~YYYYMMDD) */
    private String period;

    /** 담당 직무 목록 */
    private List<String> responsibility;

    /** 기술 스택 목록 */
    private List<String> techStack;

    /** 관련 링크 목록 (nullable) */
    private List<String> links;

    /** 완성 여부 */
    private boolean confirmed;

    public Project() {
        this.confirmed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<String> getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(List<String> responsibility) {
        this.responsibility = responsibility;
    }

    public List<String> getTechStack() {
        return techStack;
    }

    public void setTechStack(List<String> techStack) {
        this.techStack = techStack;
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
