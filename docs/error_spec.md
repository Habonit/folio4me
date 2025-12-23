# Error Spec

---

## 이 레포에서 처리하는 에러

| 에러 코드 | HTTP | 에러 상황 | 상태 |
|:----------|:-----|:---------|:-----|
| SESSION_NOT_FOUND | 404 | 세션을 찾을 수 없음 | ✅ |
| INVALID_INPUT | 400 | 유효하지 않은 입력 | ✅ |
| INVALID_PERIOD | 400 | 유효하지 않은 기간 형식 | ✅ |
| STORAGE_ERROR | 500 | 저장소 오류 | ✅ |
| INTERNAL_SERVER_ERROR | 500 | 서버 내부 오류 | ✅ |

---

## 에러 응답 형식

모든 에러는 다음 형식으로 응답합니다:

```json
{
  "status": 400,
  "error": "INVALID_INPUT",
  "message": "메시지를 입력해주세요.",
  "path": "/api/v1/sessions/xxx/messages",
  "timestamp": "2025-12-24T10:30:00"
}
```

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `status` | number | HTTP 상태 코드 |
| `error` | string | 에러 코드 |
| `message` | string | 에러 메시지 (한글) |
| `path` | string | 요청 경로 |
| `timestamp` | string | 에러 발생 시각 (ISO 8601) |

---

## 에러 상세

### SESSION_NOT_FOUND: 세션을 찾을 수 없음

**HTTP 상태**: 404 Not Found

**발생 조건**: 존재하지 않는 sessionId로 API 호출

**발생 위치**:
- `GET /api/v1/sessions/{sessionId}`
- `DELETE /api/v1/sessions/{sessionId}`
- `POST /api/v1/sessions/{sessionId}/messages`
- `GET /api/v1/sessions/{sessionId}/portfolio`
- `GET /api/v1/sessions/{sessionId}/prompt`

```java
// SessionService.java
public Session getSession(String sessionId) {
    Session session = sessionStore.get(sessionId);
    if (session == null) {
        throw new SessionNotFoundException(sessionId);
    }
    return session;
}
```

**응답 예시**:
```json
{
  "status": 404,
  "error": "SESSION_NOT_FOUND",
  "message": "세션을 찾을 수 없습니다: 550e8400-e29b-41d4-a716-446655440000",
  "path": "/api/v1/sessions/550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2025-12-24T10:30:00"
}
```

---

### INVALID_INPUT: 유효하지 않은 입력

**HTTP 상태**: 400 Bad Request

**발생 조건**: 필수 필드 누락 또는 유효하지 않은 값 입력

**발생 위치**:
- `POST /api/v1/sessions/{sessionId}/messages` - 메시지가 비어있는 경우
- 각 Step Handler에서 입력 값 검증 실패 시

```java
// ConversationController.java
if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
    throw new InvalidInputException("message", "", "메시지를 입력해주세요.");
}
```

**응답 예시**:
```json
{
  "status": 400,
  "error": "INVALID_INPUT",
  "message": "메시지를 입력해주세요.",
  "path": "/api/v1/sessions/xxx/messages",
  "timestamp": "2025-12-24T10:30:00"
}
```

---

### INVALID_PERIOD: 유효하지 않은 기간 형식

**HTTP 상태**: 400 Bad Request

**발생 조건**: 기간 형식이 `YYYYMMDD~YYYYMMDD` 또는 `YYYYMMDD~present`가 아닌 경우

**발생 위치**:
- Step 2 (Work Experience) 입력 시
- Step 3 (Education) 입력 시
- Step 4~9 (Projects, Awards, Activities, Certifications) 입력 시

```java
// 기간 파싱 로직에서 발생
throw new InvalidPeriodException(period);
// 메시지: "유효하지 않은 기간 형식입니다: {입력값} (예: 20230301~20231231 또는 20230301~present)"
```

**유효한 형식**:
- `20230301~20231231` - 시작일~종료일
- `20230301~present` - 현재 진행 중

**응답 예시**:
```json
{
  "status": 400,
  "error": "INVALID_PERIOD",
  "message": "유효하지 않은 기간 형식입니다: 2023-03-01~2023-12-31 (예: 20230301~20231231 또는 20230301~present)",
  "path": "/api/v1/sessions/xxx/messages",
  "timestamp": "2025-12-24T10:30:00"
}
```

---

### STORAGE_ERROR: 저장소 오류

**HTTP 상태**: 500 Internal Server Error

**발생 조건**: 파일 시스템 읽기/쓰기 실패

**발생 위치**:
- 세션 데이터 저장/로드 시
- 포트폴리오 JSON 파일 저장 시
- 이미지 파일 저장 시

```java
// StorageService.java
public void savePortfolio(String sessionId, Portfolio portfolio) {
    try {
        // 파일 저장 로직
    } catch (IOException e) {
        throw new StorageException(path, "포트폴리오 저장 실패", e);
    }
}
```

**응답 예시**:
```json
{
  "status": 500,
  "error": "STORAGE_ERROR",
  "message": "포트폴리오 저장 실패",
  "path": "/api/v1/sessions/xxx/messages",
  "timestamp": "2025-12-24T10:30:00"
}
```

---

### INTERNAL_SERVER_ERROR: 서버 내부 오류

**HTTP 상태**: 500 Internal Server Error

**발생 조건**: 예상치 못한 서버 오류 발생

**발생 위치**: 모든 API 엔드포인트 (Catch-all 핸들러)

```java
// GlobalExceptionHandler.java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
    log.error("예상치 못한 오류 발생: {}", ex.getMessage(), ex);

    ErrorResponse error = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "INTERNAL_SERVER_ERROR",
        "서버 내부 오류가 발생했습니다.",
        request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
}
```

**응답 예시**:
```json
{
  "status": 500,
  "error": "INTERNAL_SERVER_ERROR",
  "message": "서버 내부 오류가 발생했습니다.",
  "path": "/api/v1/sessions/xxx/messages",
  "timestamp": "2025-12-24T10:30:00"
}
```

---

## 예외 클래스 구조

```
RuntimeException
├── SessionNotFoundException
│   └── sessionId: String
├── InvalidInputException
│   ├── fieldName: String
│   └── invalidValue: String
├── InvalidPeriodException
│   └── period: String
└── StorageException
    └── path: String
```

---

## 참조

- [API 명세서](./api_spec.md)
- [GlobalExceptionHandler.java](../backend/src/main/java/com/folio4me/exception/GlobalExceptionHandler.java)

---
