package com.folio4me.model;

/**
 * 포트폴리오 콘텐츠 수집 대화의 12단계를 정의합니다.
 *
 * <p>각 Step은 수집할 데이터 영역과 표시 이름을 가집니다.</p>
 */
public enum ConversationStep {

    /** Step 0: 희망 직무 확인 (Gate) */
    STEP_0_GATE("희망 직무 확인", "meta"),

    /** Step 1: 기본 개인정보 */
    STEP_1_PERSONAL_INFO("기본 개인정보", "personalInfo"),

    /** Step 2: 이력 (경력) */
    STEP_2_WORK_EXPERIENCE("이력", "workExperience"),

    /** Step 3: 학력 */
    STEP_3_EDUCATION("학력", "education"),

    /** Step 4: 대표 프로젝트 */
    STEP_4_REPRESENTATIVE_PROJECTS("대표 프로젝트", "representativeProjects"),

    /** Step 5: 일반 프로젝트 */
    STEP_5_PROJECTS("일반 프로젝트", "projects"),

    /** Step 6: 수상 경력 */
    STEP_6_AWARDS("수상 경력", "awards"),

    /** Step 7: 주요 대외활동 */
    STEP_7_MAJOR_ACTIVITIES("주요 대외활동", "activities.major"),

    /** Step 8: 그 외 대외활동 */
    STEP_8_OTHER_ACTIVITIES("그 외 대외활동", "activities.minor"),

    /** Step 9: 자격증 */
    STEP_9_CERTIFICATIONS("자격증", "certifications"),

    /** Step 10: 기술 역량 */
    STEP_10_TECHNICAL_SKILLS("기술 역량", "technicalSkills"),

    /** Step 11: 자기소개 */
    STEP_11_ABOUT("자기소개", "about");

    private final String displayName;
    private final String jsonPath;

    ConversationStep(String displayName, String jsonPath) {
        this.displayName = displayName;
        this.jsonPath = jsonPath;
    }

    /**
     * 사용자에게 표시할 Step 이름을 반환합니다.
     *
     * @return Step 표시 이름
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 포트폴리오 JSON에서 해당 Step의 경로를 반환합니다.
     *
     * @return JSON 경로
     */
    public String getJsonPath() {
        return jsonPath;
    }

    /**
     * 다음 Step을 반환합니다.
     *
     * @return 다음 Step, 마지막이면 null
     */
    public ConversationStep next() {
        int nextOrdinal = this.ordinal() + 1;
        if (nextOrdinal >= values().length) {
            return null;
        }
        return values()[nextOrdinal];
    }

    /**
     * 현재 Step이 마지막인지 확인합니다.
     *
     * @return 마지막 Step이면 true
     */
    public boolean isLast() {
        return this == STEP_11_ABOUT;
    }

    /**
     * 현재 Step이 반복 입력을 지원하는지 확인합니다.
     * Step 2~9는 복수 항목 입력을 지원합니다.
     *
     * @return 반복 입력 지원 시 true
     */
    public boolean supportsMultipleEntries() {
        return this.ordinal() >= STEP_2_WORK_EXPERIENCE.ordinal()
                && this.ordinal() <= STEP_9_CERTIFICATIONS.ordinal();
    }
}
