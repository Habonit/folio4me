package com.folio4me.model;

/**
 * 개별 학력 항목.
 */
public class EducationItem {

    /** 학교명 */
    private String school;

    /** 학위 */
    private String degree;

    /** 학과 */
    private String major;

    /** 기간 (YYYYMMDD~YYYYMMDD) */
    private String period;

    /** 완성 여부 */
    private boolean confirmed;

    public EducationItem() {
        this.confirmed = false;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
