# Data Model: Backend Phase 1 - Content Collection

**Branch**: `001-content-collection` | **Date**: 2025-12-23
**Input**: portfolio-data-schema.md, conversation-flow-spec.md, research.md

---

## 1. 엔티티 개요

### 1.1 핵심 엔티티

| 엔티티 | 설명 | 저장 위치 |
|--------|------|----------|
| Session | 세션 상태 관리 | 메모리 (추후 Redis 고려) |
| Portfolio | 포트폴리오 전체 데이터 | `data/{uuid}/portfolio.json` |

### 1.2 Portfolio 하위 엔티티

| 엔티티 | 대응 Step | 복수 여부 |
|--------|----------|----------|
| Meta | Step 0 | 단일 |
| PersonalInfo | Step 1 | 단일 |
| WorkExperience | Step 2 | 복수 (List) |
| Education | Step 3 | 복수 (List) + display 플래그 |
| Project | Step 4, 5 | 복수 (대표/일반 분리) |
| Award | Step 6 | 복수 (List) |
| Activity | Step 7, 8 | 복수 (major/minor 분리) |
| Certification | Step 9 | 복수 (List) |
| TechnicalSkills | Step 10 | 단일 |
| About | Step 11 | 단일 |

---

## 2. 세션 관리 엔티티

### 2.1 Session

```java
package com.folio4me.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 사용자 세션 정보를 관리하는 엔티티.
 * 대화 진행 상태와 현재 Step을 추적합니다.
 */
public class Session {

    /** 세션 고유 식별자 (UUID v4) */
    private UUID id;

    /** 세션 생성 시간 */
    private LocalDateTime createdAt;

    /** 현재 대화 단계 */
    private ConversationStep currentStep;

    /** 세션 상태 */
    private SessionStatus status;

    /** 마지막 활동 시간 */
    private LocalDateTime lastActivityAt;

    // Constructors, Getters, Setters
}
```

### 2.2 ConversationStep (Enum)

```java
package com.folio4me.model;

/**
 * 포트폴리오 콘텐츠 수집 대화의 12단계를 정의합니다.
 */
public enum ConversationStep {
    STEP_0_GATE("희망 직무 확인", "meta"),
    STEP_1_PERSONAL_INFO("기본 개인정보", "personalInfo"),
    STEP_2_WORK_EXPERIENCE("이력", "workExperience"),
    STEP_3_EDUCATION("학력", "education"),
    STEP_4_REPRESENTATIVE_PROJECTS("대표 프로젝트", "representativeProjects"),
    STEP_5_PROJECTS("일반 프로젝트", "projects"),
    STEP_6_AWARDS("수상 경력", "awards"),
    STEP_7_MAJOR_ACTIVITIES("주요 대외활동", "activities.major"),
    STEP_8_OTHER_ACTIVITIES("그 외 대외활동", "activities.minor"),
    STEP_9_CERTIFICATIONS("자격증", "certifications"),
    STEP_10_TECHNICAL_SKILLS("기술 역량", "technicalSkills"),
    STEP_11_ABOUT("자기소개", "about");

    private final String displayName;
    private final String jsonPath;

    // Constructor, Getters, next() method
}
```

### 2.3 SessionStatus (Enum)

```java
package com.folio4me.model;

/**
 * 세션의 상태를 정의합니다.
 */
public enum SessionStatus {
    /** 진행 중 */
    ACTIVE,

    /** 완료됨 (about.confirmed = true) */
    COMPLETED,

    /** 종료됨 (비기술 직군 등) */
    TERMINATED,

    /** 만료됨 (TTL 초과) */
    EXPIRED
}
```

---

## 3. 포트폴리오 엔티티

### 3.1 Portfolio (루트)

```java
package com.folio4me.model;

import java.util.List;

/**
 * 포트폴리오 전체 데이터를 담는 루트 엔티티.
 * JSON 파일로 직렬화됩니다.
 */
public class Portfolio {

    private Meta meta;
    private PersonalInfo personalInfo;
    private List<WorkExperience> workExperience;
    private EducationSection education;
    private List<Project> representativeProjects;
    private List<Project> projects;
    private List<Award> awards;
    private Activities activities;
    private List<Certification> certifications;
    private TechnicalSkills technicalSkills;
    private About about;

    // Constructors, Getters, Setters
}
```

### 3.2 Meta

```java
package com.folio4me.model;

import java.time.LocalDateTime;

/**
 * 포트폴리오 메타 정보.
 */
public class Meta {

    /** 희망 직무 */
    private String targetRole;

    /** 생성 일시 */
    private LocalDateTime createdAt;

    /** 상태: draft | complete */
    private String status;

    // Constructors, Getters, Setters
}
```

### 3.3 PersonalInfo

```java
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

    // Constructors, Getters, Setters
}
```

### 3.4 WorkExperience

```java
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

    // Constructors, Getters, Setters
}
```

### 3.5 EducationSection

```java
package com.folio4me.model;

import java.util.List;

/**
 * 학력 섹션 (Step 3).
 * display 플래그로 노출 여부 제어.
 */
public class EducationSection {

    /** 노출 여부 */
    private boolean display;

    /** 학력 항목 목록 */
    private List<EducationItem> items;

    // Constructors, Getters, Setters
}
```

### 3.6 EducationItem

```java
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

    // Constructors, Getters, Setters
}
```

### 3.7 Project

```java
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

    // Constructors, Getters, Setters
}
```

### 3.8 Award

```java
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

    // Constructors, Getters, Setters
}
```

### 3.9 Activities

```java
package com.folio4me.model;

import java.util.List;

/**
 * 대외활동 섹션 (Step 7, 8).
 */
public class Activities {

    /** 주요 대외활동 (이력서 포함) */
    private List<Activity> major;

    /** 그 외 대외활동 (이력서 미포함) */
    private List<Activity> minor;

    // Constructors, Getters, Setters
}
```

### 3.10 Activity

```java
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

    // Constructors, Getters, Setters
}
```

### 3.11 Certification

```java
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

    // Constructors, Getters, Setters
}
```

### 3.12 TechnicalSkills

```java
package com.folio4me.model;

import java.util.List;

/**
 * 기술 역량 (Step 10).
 */
public class TechnicalSkills {

    /** 능숙한 기술 목록 */
    private List<String> strong;

    /** 기본 이해 기술 목록 */
    private List<String> knowledgeable;

    /** 완성 여부 */
    private boolean confirmed;

    // Constructors, Getters, Setters
}
```

### 3.13 About

```java
package com.folio4me.model;

import java.util.List;

/**
 * 자기소개 (Step 11).
 * Phase 1 완료 조건: confirmed = true
 */
public class About {

    /** 자기소개 3문장 */
    private List<String> sentences;

    /** 사용자 확정 여부 */
    private boolean confirmed;

    // Constructors, Getters, Setters
}
```

---

## 4. DTO 정의

### 4.1 ConversationRequest

```java
package com.folio4me.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 대화 요청 DTO.
 */
public record ConversationRequest(
    @NotBlank(message = "메시지를 입력해주세요.")
    String message
) {}
```

### 4.2 ConversationResponse

```java
package com.folio4me.dto;

import com.folio4me.model.ConversationStep;

/**
 * 대화 응답 DTO.
 */
public record ConversationResponse(
    /** AI 응답 메시지 */
    String message,

    /** 현재 Step */
    ConversationStep currentStep,

    /** Phase 1 완료 여부 */
    boolean completed
) {}
```

### 4.3 SessionResponse

```java
package com.folio4me.dto;

import com.folio4me.model.ConversationStep;
import com.folio4me.model.SessionStatus;
import java.util.UUID;

/**
 * 세션 정보 응답 DTO.
 */
public record SessionResponse(
    UUID sessionId,
    ConversationStep currentStep,
    SessionStatus status
) {}
```

---

## 5. JSON 스키마 예시

### 5.1 portfolio.json (초기 상태)

```json
{
  "meta": {
    "targetRole": null,
    "createdAt": "2025-12-23T10:00:00",
    "status": "draft"
  },
  "personalInfo": {
    "name": null,
    "github": null,
    "email": null,
    "profileImage": null,
    "confirmed": false
  },
  "workExperience": [],
  "education": {
    "display": true,
    "items": []
  },
  "representativeProjects": [],
  "projects": [],
  "awards": [],
  "activities": {
    "major": [],
    "minor": []
  },
  "certifications": [],
  "technicalSkills": {
    "strong": [],
    "knowledgeable": [],
    "confirmed": false
  },
  "about": {
    "sentences": [],
    "confirmed": false
  }
}
```

### 5.2 portfolio.json (완료 상태)

```json
{
  "meta": {
    "targetRole": "백엔드 개발자",
    "createdAt": "2025-12-23T10:00:00",
    "status": "complete"
  },
  "personalInfo": {
    "name": "홍길동",
    "github": "https://github.com/honggildong",
    "email": "hong@example.com",
    "profileImage": "/data/uuid/images/profile.jpg",
    "confirmed": true
  },
  "workExperience": [
    {
      "company": "기원테크",
      "period": "20241111~20251218",
      "responsibility": "AI 모델 개발 및 서비스 배포",
      "position": "대리",
      "links": null,
      "confirmed": true
    }
  ],
  "education": {
    "display": true,
    "items": [
      {
        "school": "한국대학교",
        "degree": "학사",
        "major": "컴퓨터공학과",
        "period": "20150301~20190228",
        "confirmed": true
      }
    ]
  },
  "representativeProjects": [
    {
      "name": "folio4me",
      "period": "20241201~20251218",
      "responsibility": ["백엔드 개발", "API 설계"],
      "techStack": ["Python", "FastAPI"],
      "links": ["https://github.com/example/folio4me"],
      "confirmed": true
    }
  ],
  "projects": [],
  "awards": [],
  "activities": {
    "major": [],
    "minor": []
  },
  "certifications": [],
  "technicalSkills": {
    "strong": ["Python", "FastAPI", "Docker", "PostgreSQL"],
    "knowledgeable": ["Kubernetes", "AWS", "React"],
    "confirmed": true
  },
  "about": {
    "sentences": [
      "AI 모델 개발부터 서비스 배포까지 전 과정을 경험한 백엔드 개발자입니다.",
      "Python과 FastAPI를 활용한 API 설계에 강점이 있습니다.",
      "새로운 기술을 빠르게 습득하여 프로덕션 환경에 적용합니다."
    ],
    "confirmed": true
  }
}
```

---

## 6. 검증 규칙 요약

| 필드 | 검증 규칙 |
|------|----------|
| period | `YYYYMMDD~YYYYMMDD` 또는 `YYYYMMDD~present` |
| email | 이메일 형식 |
| github | URL 형식 (`https://github.com/...`) |
| links | URL 배열 또는 null |
| sentences (about) | 정확히 3개 문장 |
