package com.folio4me.model;

/**
 * 기본 개인정보 (Step 1).
 */
public class PersonalInfo {

    /** 이름 */
    private String name;

    /** GitHub 주소 */
    private String github;

    /** 이메일 */
    private String email;

    /** 증명사진 경로 (nullable) */
    private String profileImage;

    /** 완성 여부 */
    private boolean confirmed;

    public PersonalInfo() {
        this.confirmed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
