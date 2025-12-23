# Portfolio Data Schema

> folio4me 포트폴리오 데이터 스키마 명세서

---

## 1. 개요

이 문서는 포트폴리오 콘텐츠 수집 대화를 통해 저장되는 데이터의 구조를 정의합니다.

### 1.1 목적

- 대화를 통해 수집된 데이터의 일관된 저장 구조 정의
- 포트폴리오 생성 시 참조할 데이터 스키마 제공

### 1.2 데이터 포맷

- JSON 형식

### 1.3 관련 문서

- `conversation-flow-spec.md`: 본 스키마 데이터를 수집하는 대화 흐름
- `style-and-generation-talk-flow.md`: 본 스키마를 입력으로 사용하는 생성 흐름
- `development.md`: 전체 개발 단계 정의 (Phase 1 Output)

### 1.4 저장 경로

- 경로: `data/{uuid}/portfolio.json`
- UUID 발급 시점: 세션 시작 시 (Step 0 진입 전)
- UUID 형식: UUID v4

**디렉토리 구조 예시**:
```
/data
  ├── 550e8400-e29b-41d4-a716-446655440000/
  │   └── portfolio.json
  ├── 6ba7b810-9dad-11d1-80b4-00c04fd430c8/
  │   └── portfolio.json
  └── ...
```

---

## 2. 스키마 구조 요약

| 섹션 | Key | 설명 |
|:-----|:----|:-----|
| 메타 정보 | `meta` | 생성 정보, 상태 |
| 개인정보 | `personalInfo` | 이름, GitHub, 이메일, 증명사진 |
| 이력 | `workExperience` | 회사, 기간, 직무, 직급, 관련 링크 |
| 학력 | `education` | 학교, 학위, 학과, 기간 |
| 대표 프로젝트 | `representativeProjects` | 주요 프로젝트 |
| 프로젝트 | `projects` | 일반 프로젝트 |
| 수상 경력 | `awards` | 수상명, 기관, 내용, 관련 링크 |
| 대외활동 | `activities` | major / minor 구분 |
| 자격증 | `certifications` | 자격증/시험 응시 내역 |
| 기술 스택 | `technicalSkills` | strong / knowledgeable |
| 소개 | `about` | 3문장 자기소개 |

---

## 3. 공통 구조

### 3.1 기간 포맷

- 형식: `YYYYMMDD~YYYYMMDD`
- 예시: `20230301~20231231`
- 진행 중: `20230301~present`

### 3.2 confirmed 필드

- 모든 항목에 `confirmed` 필드 존재
- 기본값: `false`
- 항목의 모든 필수 필드가 채워지면 `true`로 변경
- 단계(Step)의 모든 항목이 `confirmed: true`일 때 다음 단계로 이동 가능

### 3.3 Null 허용 규칙

사용자가 특정 필드 또는 항목 입력을 거부할 경우, `null` 값으로 저장합니다.

### 3.4 Null 렌더링 규칙

| 레벨 | 조건 | 렌더링 |
|:-----|:-----|:-------|
| **필드 레벨** | 특정 필드가 `null` | 해당 필드만 화면에 표시 안 함 |
| **항목/섹션 레벨** | 배열이 빈 배열 `[]` | 해당 섹션 전체가 화면에 표시 안 함 |

**예시:**
- 프로젝트의 `links`가 `null` → 프로젝트는 표시, 관련 링크 필드만 숨김
- `workExperience`가 `[]` (빈 배열) → Work Experience 섹션 전체 숨김

---

## 4. 섹션별 상세 스키마

### 4.1 meta

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `targetRole` | string | 희망 직무 |
| `createdAt` | datetime | 생성 일시 (콘텐츠 수집 단계 진입 시, Step 0 시작 시점) |
| `status` | string | draft \| complete |

```json
{
  "meta": {
    "targetRole": "string",
    "createdAt": "datetime",
    "status": "draft | complete"
  }
}
```

---

### 4.2 personalInfo

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `name` | string | 이름 |
| `github` | url | GitHub 주소 |
| `email` | email | 이메일 |
| `profileImage` | url \| null | 증명사진 경로 |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

```json
{
  "personalInfo": {
    "name": "string",
    "github": "url",
    "email": "email",
    "profileImage": "url | null",
    "confirmed": false
  }
}
```

---

### 4.3 workExperience

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `company` | string | 회사명 |
| `period` | string | 기간 (YYYYMMDD~YYYYMMDD) |
| `responsibility` | string | 담당 직무 |
| `position` | string | 직급(직책) |
| `links` | string[] \| null | 관련 링크 URL 목록 |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

```json
{
  "workExperience": [
    {
      "company": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "responsibility": "string",
      "position": "string",
      "links": ["https://example.com"],
      "confirmed": false
    }
  ]
}
```

**렌더링 규칙:**
- `workExperience`가 `[]` → 섹션 전체 숨김
- 개별 항목의 `links`가 `null` → 해당 항목의 관련 링크 필드만 숨김

---

### 4.4 education

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `display` | boolean | 노출 여부 |
| `school` | string | 학교명 |
| `degree` | string | 학위 |
| `major` | string | 학과 |
| `period` | string | 기간 (YYYYMMDD~YYYYMMDD) |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

```json
{
  "education": {
    "display": true,
    "items": [
      {
        "school": "string",
        "degree": "string",
        "major": "string",
        "period": "YYYYMMDD~YYYYMMDD",
        "confirmed": false
      }
    ]
  }
}
```

**렌더링 규칙:**
- `display`가 `false` → 섹션 전체 숨김
- `items`가 `[]` → 섹션 전체 숨김

---

### 4.5 representativeProjects

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `name` | string | 프로젝트명 |
| `period` | string | 기간 (YYYYMMDD~YYYYMMDD) |
| `responsibility` | string[] | 담당 직무 목록 |
| `techStack` | string[] | 기술 스택 목록 |
| `links` | string[] \| null | 관련 링크 URL 목록 |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

```json
{
  "representativeProjects": [
    {
      "name": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "responsibility": ["백엔드 개발", "API 설계"],
      "techStack": ["Python", "FastAPI"],
      "links": ["https://github.com/..."],
      "confirmed": false
    }
  ]
}
```

**렌더링 규칙:**
- `representativeProjects`가 `[]` → 섹션 전체 숨김
- 개별 항목의 `links`가 `null` → 해당 항목의 관련 링크 필드만 숨김

---

### 4.6 projects

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `name` | string | 프로젝트명 |
| `period` | string | 기간 (YYYYMMDD~YYYYMMDD) |
| `responsibility` | string[] | 담당 직무 목록 |
| `techStack` | string[] | 기술 스택 목록 |
| `links` | string[] \| null | 관련 링크 URL 목록 |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

```json
{
  "projects": [
    {
      "name": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "responsibility": ["프론트엔드 개발"],
      "techStack": ["React", "TypeScript"],
      "links": ["https://demo.example.com"],
      "confirmed": false
    }
  ]
}
```

**렌더링 규칙:**
- `projects`가 `[]` → 섹션 전체 숨김
- 개별 항목의 `links`가 `null` → 해당 항목의 관련 링크 필드만 숨김

---

### 4.7 awards

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `title` | string | 수상명 |
| `period` | string | 기간 (YYYYMMDD~YYYYMMDD) |
| `content` | string | 수상 내용 |
| `organization` | string | 수상 기관 |
| `links` | string[] \| null | 관련 링크 URL 목록 |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

```json
{
  "awards": [
    {
      "title": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "content": "string",
      "organization": "string",
      "links": ["https://award-page.com"],
      "confirmed": false
    }
  ]
}
```

**렌더링 규칙:**
- `awards`가 `[]` → 섹션 전체 숨김
- 개별 항목의 `links`가 `null` → 해당 항목의 관련 링크 필드만 숨김

---

### 4.8 activities

#### 4.8.1 major (주요 대외활동)

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `title` | string | 제목 |
| `period` | string | 기간 (YYYYMMDD~YYYYMMDD) |
| `content` | string | 활동 내용 |
| `links` | string[] \| null | 관련 링크 URL 목록 |
| `includeInResume` | boolean | 이력서 포함 여부 (항상 true) |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

#### 4.8.2 minor (그 외 대외활동)

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `title` | string | 제목 |
| `period` | string | 기간 (YYYYMMDD~YYYYMMDD) |
| `content` | string | 활동 내용 |
| `links` | string[] \| null | 관련 링크 URL 목록 |
| `includeInResume` | boolean | 이력서 포함 여부 (항상 false) |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

```json
{
  "activities": {
    "major": [
      {
        "title": "string",
        "period": "YYYYMMDD~YYYYMMDD",
        "content": "string",
        "links": ["https://activity-link.com"],
        "includeInResume": true,
        "confirmed": false
      }
    ],
    "minor": [
      {
        "title": "string",
        "period": "YYYYMMDD~YYYYMMDD",
        "content": "string",
        "links": ["https://other-activity.com"],
        "includeInResume": false,
        "confirmed": false
      }
    ]
  }
}
```

**렌더링 규칙:**
- `major`가 `[]` → Major Activities 섹션 숨김
- `minor`가 `[]` → Other Activities 섹션 숨김
- 개별 항목의 `links`가 `null` → 해당 항목의 관련 링크 필드만 숨김

---

### 4.9 certifications

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `title` | string | 자격증/시험명 |
| `period` | string | 기간 (YYYYMMDD~YYYYMMDD) |
| `organization` | string | 발급/주관 기관 |
| `links` | string[] \| null | 관련 링크 URL 목록 |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

```json
{
  "certifications": [
    {
      "title": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "organization": "string",
      "links": ["https://certification.com"],
      "confirmed": false
    }
  ]
}
```

**렌더링 규칙:**
- `certifications`가 `[]` → 섹션 전체 숨김
- 개별 항목의 `links`가 `null` → 해당 항목의 관련 링크 필드만 숨김

---

### 4.10 technicalSkills

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `strong` | string[] | 능숙한 기술 목록 |
| `knowledgeable` | string[] | 기본 이해 기술 목록 |
| `confirmed` | boolean | 완성 여부 (기본값: false) |

```json
{
  "technicalSkills": {
    "strong": ["Python", "FastAPI", "Docker"],
    "knowledgeable": ["Kubernetes", "AWS"],
    "confirmed": false
  }
}
```

---

### 4.11 about

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `sentences` | string[] | 자기소개 3문장 |
| `confirmed` | boolean | 사용자 확인 여부 (기본값: false) |

```json
{
  "about": {
    "sentences": ["첫 번째 문장", "두 번째 문장", "세 번째 문장"],
    "confirmed": false
  }
}
```

---

## 5. 전체 스키마 (Full Schema)

```json
{
  "meta": {
    "targetRole": "string",
    "createdAt": "datetime",
    "status": "draft | complete"
  },

  "personalInfo": {
    "name": "string",
    "github": "url",
    "email": "email",
    "profileImage": "url | null",
    "confirmed": false
  },

  "workExperience": [
    {
      "company": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "responsibility": "string",
      "position": "string",
      "links": ["https://example.com"],
      "confirmed": false
    }
  ],

  "education": {
    "display": true,
    "items": [
      {
        "school": "string",
        "degree": "string",
        "major": "string",
        "period": "YYYYMMDD~YYYYMMDD",
        "confirmed": false
      }
    ]
  },

  "representativeProjects": [
    {
      "name": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "responsibility": ["string"],
      "techStack": ["string"],
      "links": ["https://example.com"],
      "confirmed": false
    }
  ],

  "projects": [
    {
      "name": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "responsibility": ["string"],
      "techStack": ["string"],
      "links": ["https://example.com"],
      "confirmed": false
    }
  ],

  "awards": [
    {
      "title": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "content": "string",
      "organization": "string",
      "links": ["https://example.com"],
      "confirmed": false
    }
  ],

  "activities": {
    "major": [
      {
        "title": "string",
        "period": "YYYYMMDD~YYYYMMDD",
        "content": "string",
        "links": ["https://example.com"],
        "includeInResume": true,
        "confirmed": false
      }
    ],
    "minor": [
      {
        "title": "string",
        "period": "YYYYMMDD~YYYYMMDD",
        "content": "string",
        "links": ["https://example.com"],
        "includeInResume": false,
        "confirmed": false
      }
    ]
  },

  "certifications": [
    {
      "title": "string",
      "period": "YYYYMMDD~YYYYMMDD",
      "organization": "string",
      "links": ["https://example.com"],
      "confirmed": false
    }
  ],

  "technicalSkills": {
    "strong": ["string"],
    "knowledgeable": ["string"],
    "confirmed": false
  },

  "about": {
    "sentences": ["string", "string", "string"],
    "confirmed": false
  }
}
```

---

## 6. 변경 이력

| 날짜 | 버전 | 변경 내용 |
|:-----|:-----|:----------|
| 2025-12-18 | 1.0.0 | 최초 작성 |
| 2025-12-18 | 1.1.0 | customFields, customData, descriptions 제거 / 고정 스키마 구조로 변경 |
| 2025-12-18 | 1.2.0 | certifications 섹션 추가, profileImage 필드 추가, workExperience/awards에 links 필드 추가, Null 허용 규칙 명시 |
| 2025-12-18 | 1.2.1 | Null 렌더링 규칙 명확화 (필드 레벨 / 항목·섹션 레벨 구분) |
| 2025-12-18 | 1.3.0 | 모든 항목에 confirmed 필드 추가 (기본값: false) |
| 2025-12-19 | 1.4.0 | 모든 links 필드 타입을 `string[] \| null`로 통일, 모든 array 필드 타입을 `string[]`로 명시, JSON 예시 구체화 |