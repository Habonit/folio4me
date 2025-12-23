# API 명세서

---

## 1. 개요

이 문서는 **<< 레포지토리명 >>** 에서 구현한 API 엔드포인트를 정의합니다.

### Base URL

| 환경 | URL |
|:-----|:----|
| Local | `http://localhost:8080/api/v1` |
| Development | `https://dev-api.example.com/api/v1` |
| Production | `https://api.example.com/api/v1` |

### 공통 헤더

| 헤더 | 필수 | 설명 |
|:-----|:-----|:-----|
| `Content-Type` | O | `application/json` |
| `Authorization` | △ | `Bearer {token}` (인증 필요 API) |

### 공통 응답 형식

```json
// 성공
{
  "success": true,
  "data": { ... },
  "message": null
}

// 실패
{
  "success": false,
  "data": null,
  "error": {
    "code": "ERR-XXX-001",
    "message": "에러 메시지"
  }
}
```

---

## 2. API 목록

| API ID | 기능 ID | 엔드포인트 | Method | 설명 | 상태 |
|:-------|:-------|:----------|:-------|:-----|:-----|
| API-001 | F-001 | `/users` | POST | 사용자 생성 | ✅ 완료 |
| API-002 | F-002 | `/users/{id}` | GET | 사용자 조회 | ✅ 완료 |
| API-003 | F-003 | `/users/{id}` | PUT | 사용자 수정 | 🚧 진행중 |
| API-004 | F-004 | `/users/{id}` | DELETE | 사용자 삭제 | 📋 예정 |

> **기능 ID**: [기능 정의서](<<Notion 링크>>)의 ID 참조

---

## 3. API 상세 스펙

### API-001: 사용자 생성

| 항목 | 내용 |
|:-----|:-----|
| **기능 정의서** | [F-001 사용자 생성](<<Notion 링크>>) |
| **엔드포인트** | `POST /users` |
| **인증** | 불필요 |
| **상태** | ✅ 완료 |

#### Request

**Headers**
| 헤더 | 필수 | 값 |
|:-----|:-----|:---|
| `Content-Type` | O | `application/json` |

**Body**
| 필드 | 타입 | 필수 | 설명 | 제약조건 |
|:-----|:-----|:-----|:-----|:---------|
| `email` | string | O | 이메일 주소 | 이메일 형식 |
| `password` | string | O | 비밀번호 | 8자 이상 |
| `name` | string | O | 사용자 이름 | 2-20자 |

```json
{
  "email": "user@example.com",
  "password": "password123",
  "name": "홍길동"
}
```

#### Response

**Success (201 Created)**
| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `id` | string | 사용자 ID |
| `email` | string | 이메일 |
| `name` | string | 이름 |
| `createdAt` | string | 생성일시 (ISO 8601) |

```json
{
  "success": true,
  "data": {
    "id": "user_123",
    "email": "user@example.com",
    "name": "홍길동",
    "createdAt": "2025-01-15T10:30:00Z"
  }
}
```

**Error**
| HTTP | 에러 코드 | 상황 |
|:-----|:---------|:-----|
| 400 | ERR-VALID-001 | 필수 필드 누락 |
| 400 | ERR-VALID-002 | 이메일 형식 오류 |
| 409 | ERR-BIZ-001 | 이미 존재하는 이메일 |

---


## 4. API 상세 스펙 템플릿

새 API 추가 시 아래 템플릿을 복사하여 사용합니다.

```markdown
### API-XXX: << API 이름 >>

| 항목 | 내용 |
|:-----|:-----|
| **기능 정의서** | [F-XXX 기능명](<<Notion 링크>>) |
| **엔드포인트** | `METHOD /path` |
| **인증** | 필요 / 불필요 |
| **상태** | ✅ 완료 / 🚧 진행중 / 📋 예정 |

#### Request

**Headers**
| 헤더 | 필수 | 값 |
|:-----|:-----|:---|
| `Content-Type` | O | `application/json` |
| `Authorization` | △ | `Bearer {token}` |

**Path Parameters** (필요시)
| 파라미터 | 타입 | 필수 | 설명 |
|:---------|:-----|:-----|:-----|
| `id` | string | O | 리소스 ID |

**Query Parameters** (필요시)
| 파라미터 | 타입 | 필수 | 기본값 | 설명 |
|:---------|:-----|:-----|:-------|:-----|
| `page` | number | X | 1 | 페이지 번호 |
| `limit` | number | X | 20 | 페이지 크기 |

**Body** (필요시)
| 필드 | 타입 | 필수 | 설명 | 제약조건 |
|:-----|:-----|:-----|:-----|:---------|
| `field1` | string | O | 필드 설명 | 제약조건 |

```json
{
  "field1": "value1"
}
```

#### Response

**Success (2XX)**
| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `field1` | string | 필드 설명 |

```json
{
  "success": true,
  "data": { ... }
}
```

**Error**
| HTTP | 에러 코드 | 상황 |
|:-----|:---------|:-----|
| 4XX | ERR-XXX-XXX | 에러 상황 |
```

---

## 5. 상태 범례

| 상태 | 설명 |
|:-----|:-----|
| ✅ 완료 | 구현 및 테스트 완료 |
| 🚧 진행중 | 현재 개발 중 |
| 📋 예정 | 구현 예정 |
| ⚠️ Deprecated | 더 이상 사용하지 않음 |

---

## 6. 참조

- [기능 정의서 (Notion)](<<Notion 링크>>)
- [에러 명세서](./error_spec.md)
- [인증 가이드](<<인증 문서 링크>>)

---