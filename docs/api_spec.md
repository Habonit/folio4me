# API 명세서

---

## 1. 개요

이 문서는 **folio4me 백엔드**에서 구현한 API 엔드포인트를 정의합니다.

### Base URL

| 환경 | URL |
|:-----|:----|
| Local | `http://localhost:8080/api/v1` |
| Development | `https://dev-api.folio4me.com/api/v1` |
| Production | `https://api.folio4me.com/api/v1` |

### 공통 헤더

| 헤더 | 필수 | 설명 |
|:-----|:-----|:-----|
| `Content-Type` | O | `application/json` |

### 공통 응답 형식

**성공**
```json
{
  "id": "...",
  "currentStep": "STEP_0_GATE",
  "status": "ACTIVE",
  ...
}
```

**실패**
```json
{
  "status": 400,
  "error": "INVALID_INPUT",
  "message": "에러 메시지",
  "path": "/api/v1/sessions/xxx/messages",
  "timestamp": "2025-12-24T10:30:00"
}
```

---

## 2. API 목록

| API ID | 엔드포인트 | Method | 설명 | 상태 |
|:-------|:----------|:-------|:-----|:-----|
| API-001 | `/sessions` | POST | 세션 생성 | ✅ 완료 |
| API-002 | `/sessions/{sessionId}` | GET | 세션 조회 | ✅ 완료 |
| API-003 | `/sessions/{sessionId}` | DELETE | 세션 삭제 | ✅ 완료 |
| API-004 | `/sessions/{sessionId}/messages` | POST | 대화 메시지 전송 | ✅ 완료 |
| API-005 | `/sessions/{sessionId}/portfolio` | GET | 포트폴리오 조회 | ✅ 완료 |
| API-006 | `/sessions/{sessionId}/prompt` | GET | 현재 단계 프롬프트 조회 | ✅ 완료 |

---

## 3. API 상세 스펙

### API-001: 세션 생성

| 항목 | 내용 |
|:-----|:-----|
| **엔드포인트** | `POST /sessions` |
| **인증** | 불필요 |
| **상태** | ✅ 완료 |

#### Request

**Headers**
| 헤더 | 필수 | 값 |
|:-----|:-----|:---|
| `Content-Type` | O | `application/json` |

**Body**

없음

#### Response

**Success (201 Created)**
| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `id` | string | 세션 UUID |
| `currentStep` | string | 현재 대화 단계 (STEP_0_GATE) |
| `status` | string | 세션 상태 (ACTIVE) |
| `createdAt` | string | 생성 시각 (ISO 8601) |
| `lastActivityAt` | string | 마지막 활동 시각 (ISO 8601) |
| `progressPercentage` | number | 대화 진행률 (0-100) |

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "currentStep": "STEP_0_GATE",
  "status": "ACTIVE",
  "createdAt": "2025-12-24T10:30:00",
  "lastActivityAt": "2025-12-24T10:30:00",
  "progressPercentage": 0
}
```

---

### API-002: 세션 조회

| 항목 | 내용 |
|:-----|:-----|
| **엔드포인트** | `GET /sessions/{sessionId}` |
| **인증** | 불필요 |
| **상태** | ✅ 완료 |

#### Request

**Path Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|:---------|:-----|:-----|:-----|
| `sessionId` | string | O | 세션 UUID |

#### Response

**Success (200 OK)**
| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `id` | string | 세션 UUID |
| `currentStep` | string | 현재 대화 단계 |
| `status` | string | 세션 상태 |
| `createdAt` | string | 생성 시각 (ISO 8601) |
| `lastActivityAt` | string | 마지막 활동 시각 (ISO 8601) |
| `progressPercentage` | number | 대화 진행률 (0-100) |

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "currentStep": "STEP_3_EDUCATION",
  "status": "ACTIVE",
  "createdAt": "2025-12-24T10:30:00",
  "lastActivityAt": "2025-12-24T10:45:00",
  "progressPercentage": 25
}
```

**Error**
| HTTP | 에러 코드 | 상황 |
|:-----|:---------|:-----|
| 404 | SESSION_NOT_FOUND | 세션이 존재하지 않음 |

---

### API-003: 세션 삭제

| 항목 | 내용 |
|:-----|:-----|
| **엔드포인트** | `DELETE /sessions/{sessionId}` |
| **인증** | 불필요 |
| **상태** | ✅ 완료 |

#### Request

**Path Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|:---------|:-----|:-----|:-----|
| `sessionId` | string | O | 세션 UUID |

#### Response

**Success (204 No Content)**

응답 본문 없음

**Error**
| HTTP | 에러 코드 | 상황 |
|:-----|:---------|:-----|
| 404 | SESSION_NOT_FOUND | 세션이 존재하지 않음 |

---

### API-004: 대화 메시지 전송

| 항목 | 내용 |
|:-----|:-----|
| **엔드포인트** | `POST /sessions/{sessionId}/messages` |
| **인증** | 불필요 |
| **상태** | ✅ 완료 |

#### Request

**Path Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|:---------|:-----|:-----|:-----|
| `sessionId` | string | O | 세션 UUID |

**Body**
| 필드 | 타입 | 필수 | 설명 | 제약조건 |
|:-----|:-----|:-----|:-----|:---------|
| `message` | string | O | 사용자 메시지 | 빈 문자열 불가 |

```json
{
  "message": "백엔드 개발자입니다."
}
```

#### Response

**Success (200 OK)**
| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `message` | string | AI 응답 메시지 |
| `currentStep` | string | 현재 대화 단계 |
| `completed` | boolean | 대화 완료 여부 |
| `expectedInputType` | string | 다음 예상 입력 유형 (nullable) |

```json
{
  "message": "백엔드 개발자시군요! 그럼 포트폴리오 작성을 시작하겠습니다.\n\n기본 개인정보를 입력받겠습니다.\n입력 필드: 이름, GitHub 주소, 이메일\n\n순서대로 입력해주세요.",
  "currentStep": "STEP_1_PERSONAL_INFO",
  "completed": false,
  "expectedInputType": "text"
}
```

**Error**
| HTTP | 에러 코드 | 상황 |
|:-----|:---------|:-----|
| 400 | INVALID_INPUT | 메시지가 비어있음 |
| 400 | INVALID_PERIOD | 기간 형식 오류 |
| 404 | SESSION_NOT_FOUND | 세션이 존재하지 않음 |

---

### API-005: 포트폴리오 조회

| 항목 | 내용 |
|:-----|:-----|
| **엔드포인트** | `GET /sessions/{sessionId}/portfolio` |
| **인증** | 불필요 |
| **상태** | ✅ 완료 |

#### Request

**Path Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|:---------|:-----|:-----|:-----|
| `sessionId` | string | O | 세션 UUID |

#### Response

**Success (200 OK)**
| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `meta` | object | 메타 정보 (targetRole, createdAt, status) |
| `personalInfo` | object | 개인정보 (name, github, email, profileImage, confirmed) |
| `workExperience` | array | 경력 목록 |
| `education` | object | 학력 섹션 (display, items[], confirmed) |
| `representativeProjects` | array | 대표 프로젝트 목록 |
| `projects` | array | 일반 프로젝트 목록 |
| `awards` | array | 수상 경력 목록 |
| `activities` | object | 대외활동 (major[], minor[]) |
| `certifications` | array | 자격증 목록 |
| `technicalSkills` | object | 기술 역량 (strong[], knowledgeable[], confirmed) |
| `about` | object | 자기소개 (sentences[], confirmed) |

```json
{
  "meta": {
    "targetRole": "백엔드 개발자",
    "createdAt": "2025-12-24T10:30:00",
    "status": "IN_PROGRESS"
  },
  "personalInfo": {
    "name": "홍길동",
    "github": "https://github.com/honggildong",
    "email": "hong@example.com",
    "profileImage": null,
    "confirmed": true
  },
  "workExperience": [],
  "education": {
    "display": true,
    "items": [],
    "confirmed": false
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

**Error**
| HTTP | 에러 코드 | 상황 |
|:-----|:---------|:-----|
| 404 | SESSION_NOT_FOUND | 세션이 존재하지 않음 |

---

### API-006: 현재 단계 프롬프트 조회

| 항목 | 내용 |
|:-----|:-----|
| **엔드포인트** | `GET /sessions/{sessionId}/prompt` |
| **인증** | 불필요 |
| **상태** | ✅ 완료 |

#### Request

**Path Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|:---------|:-----|:-----|:-----|
| `sessionId` | string | O | 세션 UUID |

#### Response

**Success (200 OK)**
| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `message` | string | 현재 단계 초기 프롬프트 |
| `currentStep` | string | 현재 대화 단계 |
| `completed` | boolean | 대화 완료 여부 (항상 false) |

```json
{
  "message": "안녕하세요! 포트폴리오 생성을 도와드리겠습니다.\n본인의 희망 직무는 어떻게 되나요?",
  "currentStep": "STEP_0_GATE",
  "completed": false
}
```

**Error**
| HTTP | 에러 코드 | 상황 |
|:-----|:---------|:-----|
| 404 | SESSION_NOT_FOUND | 세션이 존재하지 않음 |

---

## 4. 대화 단계 (ConversationStep)

| Step | 상수명 | 표시명 | 설명 |
|:-----|:------|:------|:-----|
| 0 | STEP_0_GATE | 희망 직무 확인 | 서비스 대상 여부 확인 |
| 1 | STEP_1_PERSONAL_INFO | 기본 개인정보 | 이름, GitHub, 이메일, 증명사진 |
| 2 | STEP_2_WORK_EXPERIENCE | 이력 | 경력 정보 (복수 입력 가능) |
| 3 | STEP_3_EDUCATION | 학력 | 학력 정보 (복수 입력 가능) |
| 4 | STEP_4_REPRESENTATIVE_PROJECTS | 대표 프로젝트 | 대표 프로젝트 (복수 입력 가능) |
| 5 | STEP_5_PROJECTS | 일반 프로젝트 | 일반 프로젝트 (복수 입력 가능) |
| 6 | STEP_6_AWARDS | 수상 경력 | 수상 내역 (복수 입력 가능) |
| 7 | STEP_7_MAJOR_ACTIVITIES | 주요 대외활동 | 주요 활동 (복수 입력 가능) |
| 8 | STEP_8_OTHER_ACTIVITIES | 그 외 대외활동 | 기타 활동 (복수 입력 가능) |
| 9 | STEP_9_CERTIFICATIONS | 자격증 | 자격증/시험 (복수 입력 가능) |
| 10 | STEP_10_TECHNICAL_SKILLS | 기술 역량 | Strong, Knowledgeable |
| 11 | STEP_11_ABOUT | 자기소개 | 3문장 자기소개 |

---

## 5. 세션 상태 (SessionStatus)

| 상태 | 설명 |
|:-----|:-----|
| `ACTIVE` | 진행 중 |
| `COMPLETED` | 완료됨 (about.confirmed = true) |
| `TERMINATED` | 종료됨 (비기술 직군 등) |
| `EXPIRED` | 만료됨 (TTL 초과) |

---

## 6. 상태 범례

| 상태 | 설명 |
|:-----|:-----|
| ✅ 완료 | 구현 및 테스트 완료 |
| 🚧 진행중 | 현재 개발 중 |
| 📋 예정 | 구현 예정 |
| ⚠️ Deprecated | 더 이상 사용하지 않음 |

---

## 7. 참조

- [대화 흐름 명세서](./project/conversation-flow-spec.md)
- [포트폴리오 데이터 스키마](./project/portfolio-data-schema.md)
- [에러 명세서](./error_spec.md)

---
