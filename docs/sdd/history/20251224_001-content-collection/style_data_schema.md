# Style Data Schema

> folio4me 스타일 선택 데이터 스키마 명세서

---

## 1. 개요

이 문서는 스타일 질의 대화를 통해 저장되는 데이터의 구조를 정의합니다.

### 1.1 목적

- 스타일 질의를 통해 수집된 데이터의 일관된 저장 구조 정의
- 포트폴리오 생성 시 템플릿 선택 정보 제공

### 1.2 데이터 포맷

- JSON 형식

### 1.3 관련 문서

- `style-and-generation-talk-flow.md`: 본 스키마 데이터를 수집하는 대화 흐름
- `template_schema.md`: 템플릿 메타데이터 스키마 (selectedTemplate과 연관)
- `development.md`: 전체 개발 단계 정의 (Phase 2 Output)

### 1.4 저장 경로

- 경로: `data/{uuid}/style.json`
- UUID: 세션 시작 시 발급된 UUID (portfolio.json과 동일)

**디렉토리 구조 예시**:
```
/data
  └── 550e8400-e29b-41d4-a716-446655440000/
      ├── portfolio.json    ← 콘텐츠 수집 산출물
      └── style.json        ← 스타일 선택 산출물
```

---

## 2. 스키마 구조 요약

| 섹션 | Key | 설명 |
|:-----|:----|:-----|
| 메타 정보 | `meta` | 생성 정보, 상태 |
| 스타일 선호도 | `stylePreference` | 사용자가 입력한 스타일 설명 |
| 선택된 템플릿 | `selectedTemplate` | 최종 선택된 템플릿 정보 |

---

## 3. 공통 구조

### 3.1 confirmed 필드

- 모든 항목에 `confirmed` 필드 존재
- 기본값: `false`
- 항목이 확정되면 `true`로 변경
- 모든 항목이 `confirmed: true`일 때 생성 단계로 이동 가능

### 3.2 상태 전이

```
stylePreference.confirmed: false
        ↓ (사용자 스타일 입력)
stylePreference.confirmed: true
        ↓
selectedTemplate.confirmed: false
        ↓ (템플릿 선택 확정)
selectedTemplate.confirmed: true
        ↓
meta.status: "complete"
        ↓
생성 단계 진입
```

---

## 4. 섹션별 상세 스키마

### 4.1 meta

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `createdAt` | datetime | 생성 일시 (스타일 질의 단계 진입 시, Step 1 시작 시점) |
| `updatedAt` | datetime | 수정 일시 |
| `status` | string | draft \| complete |

```json
{
  "meta": {
    "createdAt": "datetime",
    "updatedAt": "datetime",
    "status": "draft | complete"
  }
}
```

---

### 4.2 stylePreference

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `userInput` | string | 사용자가 입력한 스타일 설명 (평문) |
| `history` | array | 스타일 입력 이력 (거부 시 누적) |
| `confirmed` | boolean | 스타일 입력 완료 여부 (기본값: false) |

```json
{
  "stylePreference": {
    "userInput": "string",
    "history": [
      {
        "input": "string",
        "timestamp": "datetime"
      }
    ],
    "confirmed": false
  }
}
```

**필드 설명**:
- `userInput`: 현재(최종) 스타일 설명
- `history`: 사용자가 "다른 거 볼래요" 선택 시 이전 입력 기록 누적

---

### 4.3 selectedTemplate

| 필드 | 타입 | 설명 |
|:-----|:-----|:-----|
| `templateId` | number \| null | 선택된 템플릿 ID (예: 1) |
| `title` | string \| null | 템플릿 이름 (metadata.json의 title) |
| `description` | string \| null | 템플릿 설명 (metadata.json의 description) |
| `link` | string \| null | 템플릿 원본 출처 URL (metadata.json의 link) |
| `confirmed` | boolean | 템플릿 선택 확정 여부 (기본값: false) |

```json
{
  "selectedTemplate": {
    "templateId": 1,
    "title": "string | null",
    "description": "string | null",
    "link": "string | null",
    "confirmed": false
  }
}
```

**필드 설명**:
- 템플릿 추천 후 사용자가 "이걸로 할게요" 선택 시 `confirmed: true`
- "다른 거 볼래요" 선택 시 `confirmed: false` 유지, stylePreference로 복귀

---

## 5. 전체 스키마 (Full Schema)

```json
{
  "meta": {
    "createdAt": "datetime",
    "updatedAt": "datetime",
    "status": "draft | complete"
  },

  "stylePreference": {
    "userInput": "string",
    "history": [
      {
        "input": "string",
        "timestamp": "datetime"
      }
    ],
    "confirmed": false
  },

  "selectedTemplate": {
    "templateId": 1,
    "title": "string | null",
    "description": "string | null",
    "link": "string | null",
    "confirmed": false
  }
}
```

---

## 6. 완성 조건

### 6.1 생성 단계 진입 조건

다음 조건이 모두 충족되어야 생성 단계로 진입:

| 조건 | 필드 | 값 |
|:-----|:-----|:---|
| 스타일 입력 완료 | `stylePreference.confirmed` | `true` |
| 템플릿 선택 완료 | `selectedTemplate.confirmed` | `true` |
| 전체 상태 | `meta.status` | `complete` |

### 6.2 상태 업데이트 규칙

```
1. 사용자 스타일 입력 시:
   → stylePreference.userInput 업데이트
   → stylePreference.confirmed = true

2. LLM 템플릿 추천 시:
   → selectedTemplate 필드들 업데이트 (confirmed 제외)
   → selectedTemplate.confirmed = false

3. 사용자 "이걸로 할게요" 선택 시:
   → selectedTemplate.confirmed = true
   → meta.status = "complete"

4. 사용자 "다른 거 볼래요" 선택 시:
   → stylePreference.history에 현재 입력 추가
   → stylePreference.userInput = null
   → stylePreference.confirmed = false
   → selectedTemplate 초기화
```

---

## 7. 포트폴리오 생성 입력

### 7.1 생성에 필요한 데이터

```
┌─────────────────────────────────────────────────────────────────┐
│  portfolio.json (콘텐츠)                                        │
│  • personalInfo, workExperience, projects, ...                  │
└─────────────────────────────────────────────────────────────────┘
                              +
┌─────────────────────────────────────────────────────────────────┐
│  style.json (스타일)                                            │
│  • selectedTemplate.templateId → 템플릿 선택                    │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│  템플릿 바인딩                                                   │
│  • templates/{templateId}/src/ → data/{uuid}/src/ 복사          │
│  • *.json → data/{uuid}/src/public/ 복사                        │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│  최종 포트폴리오 결과물                                          │
│  • 템플릿은 public/portfolio.json을 참조하여 렌더링              │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8. 변경 이력

| 날짜 | 버전 | 변경 내용 |
|:-----|:-----|:----------|
| 2025-12-18 | 1.0.0 | 최초 작성 |
| 2025-12-19 | 1.1.0 | meta.createdAt 생성 시점 명시 (스타일 질의 단계 진입 시, Step 1 시작 시점) |
| 2025-12-19 | 1.2.0 | selectedTemplate에 `link` 필드 추가, `templateId` 타입 number로 변경 |
| 2025-12-19 | 1.3.0 | 포트폴리오 생성 입력 흐름 업데이트: json 파일을 `src/public/` 폴더로 통일 |