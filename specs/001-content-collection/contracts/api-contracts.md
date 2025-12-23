# API Contracts: Backend Phase 1 - Content Collection

**Branch**: `001-content-collection` | **Date**: 2025-12-23
**Base URL**: `/api/v1`

---

## 1. 세션 관리 API

### 1.1 POST /sessions - 새 세션 생성

**설명**: 새 포트폴리오 작성 세션을 생성하고 UUID를 발급합니다.

**Request**:
```http
POST /api/v1/sessions
Content-Type: application/json
```

**Response (201 Created)**:
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "currentStep": "STEP_0_GATE",
  "status": "ACTIVE",
  "createdAt": "2025-12-23T10:00:00"
}
```

**Side Effects**:
- `data/{sessionId}/` 디렉토리 생성
- `data/{sessionId}/portfolio.json` 초기화

---

### 1.2 GET /sessions/{sessionId} - 세션 상태 조회

**설명**: 세션의 현재 상태를 조회합니다.

**Request**:
```http
GET /api/v1/sessions/550e8400-e29b-41d4-a716-446655440000
```

**Response (200 OK)**:
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "currentStep": "STEP_2_WORK_EXPERIENCE",
  "status": "ACTIVE",
  "createdAt": "2025-12-23T10:00:00",
  "lastActivityAt": "2025-12-23T10:05:30"
}
```

**Response (404 Not Found)**:
```json
{
  "error": "SESSION_NOT_FOUND",
  "message": "세션을 찾을 수 없습니다."
}
```

---

### 1.3 DELETE /sessions/{sessionId} - 세션 삭제

**설명**: 세션과 관련 데이터를 삭제합니다.

**Request**:
```http
DELETE /api/v1/sessions/550e8400-e29b-41d4-a716-446655440000
```

**Response (204 No Content)**:
(응답 본문 없음)

**Side Effects**:
- `data/{sessionId}/` 디렉토리 및 하위 파일 삭제

---

## 2. 대화 API

### 2.1 POST /sessions/{sessionId}/messages - 메시지 전송

**설명**: 사용자 메시지를 전송하고 AI 응답을 받습니다.

**Request**:
```http
POST /api/v1/sessions/550e8400-e29b-41d4-a716-446655440000/messages
Content-Type: application/json

{
  "message": "백엔드 개발자입니다."
}
```

**Response (200 OK)**:
```json
{
  "message": "백엔드 개발자시군요! 그럼 포트폴리오 작성을 시작하겠습니다.\n\n기본 개인정보를 입력받겠습니다.\n입력 필드: 이름, GitHub 주소, 이메일\n\n순서대로 입력해주세요.",
  "currentStep": "STEP_1_PERSONAL_INFO",
  "completed": false
}
```

**Response (400 Bad Request - 주제 이탈)**:
```json
{
  "message": "해당 질문에는 답변드리기 어렵습니다.\n현재 이력(Work Experience) 정보를 입력 중입니다.\n재직했던 회사가 있으신가요?",
  "currentStep": "STEP_2_WORK_EXPERIENCE",
  "completed": false,
  "topicDivergence": true
}
```

**Response (200 OK - Phase 1 완료)**:
```json
{
  "message": "자기소개가 확정되었습니다.\n\n포트폴리오 콘텐츠 수집이 모두 완료되었습니다!\n이제 포트폴리오 스타일을 선택하는 단계로 넘어갑니다.",
  "currentStep": "STEP_11_ABOUT",
  "completed": true
}
```

---

### 2.2 GET /sessions/{sessionId}/portfolio - 포트폴리오 조회

**설명**: 현재까지 수집된 포트폴리오 데이터를 조회합니다.

**Request**:
```http
GET /api/v1/sessions/550e8400-e29b-41d4-a716-446655440000/portfolio
```

**Response (200 OK)**:
```json
{
  "meta": {
    "targetRole": "백엔드 개발자",
    "createdAt": "2025-12-23T10:00:00",
    "status": "draft"
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

---

## 3. 이미지 업로드 API

### 3.1 POST /sessions/{sessionId}/images - 증명사진 업로드

**설명**: 증명사진을 업로드합니다 (Step 1에서 사용).

**Request**:
```http
POST /api/v1/sessions/550e8400-e29b-41d4-a716-446655440000/images
Content-Type: multipart/form-data

file: (binary)
```

**Response (201 Created)**:
```json
{
  "imagePath": "/data/550e8400-e29b-41d4-a716-446655440000/images/profile.jpg",
  "message": "증명사진이 등록되었습니다."
}
```

**Response (400 Bad Request - 잘못된 파일)**:
```json
{
  "error": "INVALID_IMAGE",
  "message": "지원하지 않는 이미지 형식입니다. JPG, PNG 파일만 업로드 가능합니다."
}
```

**Response (400 Bad Request - 부적절한 이미지)**:
```json
{
  "error": "INAPPROPRIATE_IMAGE",
  "message": "공식적인 증명사진을 업로드해주세요. (예: 정장 사진, 단정한 복장)"
}
```

---

## 4. 에러 응답 형식

### 4.1 공통 에러 응답

```json
{
  "error": "ERROR_CODE",
  "message": "사용자에게 표시할 한글 메시지",
  "details": {
    "field": "추가 정보 (선택)"
  }
}
```

### 4.2 에러 코드 목록

| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| SESSION_NOT_FOUND | 404 | 세션을 찾을 수 없음 |
| INVALID_INPUT | 400 | 잘못된 입력 형식 |
| INVALID_PERIOD | 400 | 기간 형식 오류 |
| INVALID_EMAIL | 400 | 이메일 형식 오류 |
| INVALID_URL | 400 | URL 형식 오류 |
| INVALID_IMAGE | 400 | 지원하지 않는 이미지 형식 |
| INAPPROPRIATE_IMAGE | 400 | 부적절한 이미지 |
| NON_TECH_ROLE | 200 | 비기술 직군 (세션 종료) |
| STORAGE_ERROR | 500 | 파일 저장 오류 |
| AI_SERVICE_ERROR | 503 | AI 서비스 오류 |

---

## 5. Step별 예상 대화 흐름

### 5.1 Step 0 → Step 1 전이

**Request**:
```json
{"message": "백엔드 개발자입니다."}
```

**Response**:
```json
{
  "message": "백엔드 개발자시군요! 그럼 포트폴리오 작성을 시작하겠습니다.\n\n기본 개인정보를 입력받겠습니다.\n입력 필드: 이름, GitHub 주소, 이메일\n\n순서대로 입력해주세요.",
  "currentStep": "STEP_1_PERSONAL_INFO",
  "completed": false
}
```

### 5.2 Step 0 - 비기술 직군 (세션 종료)

**Request**:
```json
{"message": "마케터입니다."}
```

**Response**:
```json
{
  "message": "죄송합니다. 현재는 기술 직군(개발자, AI 엔지니어, MLOps 등)을 위한 포트폴리오 생성만 지원하고 있습니다. 추후 업데이트 예정입니다.",
  "currentStep": "STEP_0_GATE",
  "completed": false,
  "sessionTerminated": true
}
```

### 5.3 Step 11 - Phase 1 완료

**Request**:
```json
{"message": "네, 좋아요."}
```

**Response**:
```json
{
  "message": "자기소개가 확정되었습니다.\n\n포트폴리오 콘텐츠 수집이 모두 완료되었습니다!\n이제 포트폴리오 스타일을 선택하는 단계로 넘어갑니다.",
  "currentStep": "STEP_11_ABOUT",
  "completed": true
}
```

---

## 6. 검증 규칙

### 6.1 기간 형식 검증

```
패턴: ^\\d{8}~(\\d{8}|present)$
예시: 20230301~20231231, 20230301~present
```

### 6.2 이메일 형식 검증

```
패턴: ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$
```

### 6.3 GitHub URL 형식 검증

```
패턴: ^https://github\\.com/[a-zA-Z0-9_-]+$
```

---

## 7. 이미지 업로드 제약

| 항목 | 제약 |
|------|------|
| 허용 형식 | JPG, PNG |
| 최대 크기 | 5MB |
| 저장 경로 | `data/{sessionId}/images/profile.{ext}` |
