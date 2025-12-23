package com.folio4me.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 포트폴리오 루트 모델.
 * 12단계 대화를 통해 수집된 모든 데이터를 포함.
 */
public class Portfolio {

    /** 메타 정보 */
    private Meta meta;

    /** 개인정보 (Step 1) */
    private PersonalInfo personalInfo;

    /** 경력 목록 (Step 2) */
    private List<WorkExperience> workExperience;

    /** 학력 섹션 (Step 3) */
    private EducationSection education;

    /** 대표 프로젝트 목록 (Step 4) */
    private List<Project> representativeProjects;

    /** 일반 프로젝트 목록 (Step 5) */
    private List<Project> projects;

    /** 수상 경력 목록 (Step 6) */
    private List<Award> awards;

    /** 대외활동 섹션 (Step 7, 8) */
    private Activities activities;

    /** 자격증 목록 (Step 9) */
    private List<Certification> certifications;

    /** 기술 역량 (Step 10) */
    private TechnicalSkills technicalSkills;

    /** 자기소개 (Step 11) */
    private About about;

    public Portfolio() {
        this.meta = new Meta();
        this.personalInfo = new PersonalInfo();
        this.workExperience = new ArrayList<>();
        this.education = new EducationSection();
        this.representativeProjects = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.awards = new ArrayList<>();
        this.activities = new Activities();
        this.certifications = new ArrayList<>();
        this.technicalSkills = new TechnicalSkills();
        this.about = new About();
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public List<WorkExperience> getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(List<WorkExperience> workExperience) {
        this.workExperience = workExperience;
    }

    public EducationSection getEducation() {
        return education;
    }

    public void setEducation(EducationSection education) {
        this.education = education;
    }

    public List<Project> getRepresentativeProjects() {
        return representativeProjects;
    }

    public void setRepresentativeProjects(List<Project> representativeProjects) {
        this.representativeProjects = representativeProjects;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public Activities getActivities() {
        return activities;
    }

    public void setActivities(Activities activities) {
        this.activities = activities;
    }

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    public TechnicalSkills getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(TechnicalSkills technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }
}
