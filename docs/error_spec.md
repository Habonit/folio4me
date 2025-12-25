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
| UNKNOWN_ERROR | - | 응답 파싱 실패 (Frontend) | ✅ |

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

## Frontend 에러 처리

### 에러 파싱 함수

```typescript
// lib/api/error.ts
interface ApiError {
  status: number;
  error: string;
  message: string;
  path: string;
  timestamp: string;
}

async function parseError(response: Response): Promise<ApiError> {
  try {
    return await response.json();
  } catch {
    return {
      status: response.status,
      error: 'UNKNOWN_ERROR',
      message: response.statusText,
      path: '',
      timestamp: new Date().toISOString()
    };
  }
}
```

### 에러 코드 상수

```typescript
// lib/api/error.ts
const ErrorCodes = {
  SESSION_NOT_FOUND: 'SESSION_NOT_FOUND',
  INVALID_INPUT: 'INVALID_INPUT',
  INVALID_PERIOD: 'INVALID_PERIOD',
  STORAGE_ERROR: 'STORAGE_ERROR',
  INTERNAL_SERVER_ERROR: 'INTERNAL_SERVER_ERROR',
  UNKNOWN_ERROR: 'UNKNOWN_ERROR'
} as const;

type ErrorCode = typeof ErrorCodes[keyof typeof ErrorCodes];
```

### 에러 핸들링 예시

```typescript
// chat/[sessionId]/+page.svelte
import { api, ErrorCodes } from '$lib/api';
import { chatStore } from '$lib/stores';

async function handleSendMessage(message: string) {
  chatStore.update(s => ({ ...s, isLoading: true, error: null }));

  try {
    const response = await api.conversation.sendMessage(sessionId, message);
    // ... 성공 처리
  } catch (error) {
    const apiError = error as ApiError;

    switch (apiError.error) {
      case ErrorCodes.SESSION_NOT_FOUND:
        // 세션 만료 - 새 세션 생성 유도
        chatStore.update(s => ({
          ...s,
          isLoading: false,
          error: '세션이 만료되었습니다. 새로 시작해주세요.'
        }));
        break;

      case ErrorCodes.INVALID_INPUT:
        // 입력 오류 - 인라인 에러 표시
        chatStore.update(s => ({
          ...s,
          isLoading: false,
          error: apiError.message
        }));
        break;

      case ErrorCodes.INVALID_PERIOD:
        // 기간 형식 오류 - 형식 안내
        chatStore.update(s => ({
          ...s,
          isLoading: false,
          error: '기간 형식을 확인해주세요. (예: 20230301~20231231)'
        }));
        break;

      default:
        // 기타 오류
        chatStore.update(s => ({
          ...s,
          isLoading: false,
          error: '오류가 발생했습니다. 다시 시도해주세요.'
        }));
    }
  }
}
```

### 네트워크 에러 처리

```typescript
// lib/api/error.ts
async function fetchWithRetry<T>(
  fetcher: () => Promise<T>,
  maxRetries: number = 3
): Promise<T> {
  let lastError: Error | null = null;

  for (let i = 0; i < maxRetries; i++) {
    try {
      return await fetcher();
    } catch (error) {
      lastError = error as Error;

      // 네트워크 에러인 경우에만 재시도
      if (error instanceof TypeError && error.message === 'Failed to fetch') {
        await new Promise(resolve => setTimeout(resolve, 1000 * (i + 1)));
        continue;
      }

      throw error;
    }
  }

  throw lastError;
}
```

### Edge Case 에러 처리

| 상황 | 에러 코드 | Frontend 처리 |
|:-----|:---------|:-------------|
| 네트워크 끊김 | `NETWORK_ERROR` | 재시도 옵션 제공 |
| 세션 만료 | `SESSION_NOT_FOUND` | 재로그인 안내 메시지 |
| 잘못된 입력 | `INVALID_INPUT` | 인라인 에러 메시지 |
| 기간 형식 오류 | `INVALID_PERIOD` | 형식 안내 및 재입력 유도 |
| 이미지 업로드 실패 | `STORAGE_ERROR` | 에러 메시지 및 재시도 버튼 |
| 서버 오류 | `INTERNAL_SERVER_ERROR` | 일반 에러 메시지 |

---

## Frontend 에러 요약

| 에러 코드 | HTTP | 발생 조건 | UI 처리 |
|:----------|:-----|:---------|:--------|
| SESSION_NOT_FOUND | 404 | 세션 조회/삭제/메시지 전송 | 새 세션 생성 유도 |
| INVALID_INPUT | 400 | 빈 메시지 전송 | 인라인 에러 표시 |
| INVALID_PERIOD | 400 | 기간 형식 오류 | 형식 안내 표시 |
| STORAGE_ERROR | 500 | 파일 저장 실패 | 재시도 버튼 표시 |
| INTERNAL_SERVER_ERROR | 500 | 서버 내부 오류 | 일반 에러 표시 |
| UNKNOWN_ERROR | - | 응답 파싱 실패 | 일반 에러 표시 |

---

## 참조

- [API 명세서](./api_spec.md)
- [GlobalExceptionHandler.java](../backend/src/main/java/com/folio4me/exception/GlobalExceptionHandler.java)
- [Frontend API Client Contract](../specs/002-frontend-web/contracts/api-client.md)

---
