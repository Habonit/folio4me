# Frontend Development Phases

> folio4me 프론트엔드 개발 단계 정의서

---

## 개요

folio4me 프론트엔드는 5개의 개발 단계로 구성됩니다. 각 단계는 백엔드 Phase와 연계되어 있으며, 순차적으로 진행됩니다.

### 기술 스택

| 항목 | 기술 |
|:-----|:-----|
| 기반 프레임워크 | Open WebUI (SvelteKit) |
| 스타일링 | Tailwind CSS |
| 상태 관리 | Svelte Store |
| 빌드 도구 | Vite |

### 백엔드 Phase와의 연관

```
┌────────────────────────────────────────────────────────────────────────────┐
│  Frontend                          Backend                                  │
├────────────────────────────────────────────────────────────────────────────┤
│  Step 1: 기초 인프라               (기반 설정)                              │
│  Step 2: 메인 대시보드             (프로젝트 관리)                          │
│  Step 3: 채팅 인터페이스           (UI 기반)                                │
│  Step 4: 콘텐츠 & 스타일   ───────  Phase 1 + Phase 2                       │
│  Step 5: 생성 & 제공       ───────  Phase 3                                 │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Step 1: 기초 인프라 및 인증

> 기존 Open WebUI의 인증 시스템을 활용하고 필요한 부분을 커스터마이징

### 목표

- 프로젝트 기본 구조 설정
- 인증 화면 커스터마이징
- 공통 레이아웃 구성

### 구현 항목

| 화면 ID | 화면명 | 설명 |
|:--------|:-------|:-----|
| A | Login | 로그인 화면 커스터마이징 |
| B | Sign up | 회원가입 화면 커스터마이징 |
| F | API Key Modal | OpenAI API Key 입력 모달 |
| - | Sidebar | 공통 사이드바 레이아웃 |

### 세부 작업

1. **Login 화면 (A)**
   - Username/Password 입력 폼
   - Sign in / Sign up 버튼
   - "Forgot password? Contact your admin." 문구

2. **Sign up 화면 (B)**
   - Name, Username, Email, Password, Confirm password 입력
   - Sign up / Cancel 버튼

3. **사이드바 레이아웃**
   - 상단: 로고 (클릭 시 Main으로 이동)
   - 하단: 설정 버튼 (API Key 모달 트리거)

4. **API Key Modal (F)**
   - OpenAI API Key 입력 필드
   - Save / Cancel 버튼
   - 키 유효성 검증

### 완료 조건

```
- 로그인/회원가입 기능 정상 동작
- 인증 상태에 따른 라우팅 보호 적용
- API Key 저장 및 조회 기능 구현
```

### 참조

- `frontend-flow.md` - 화면 A, B, F 명세

---

## Step 2: 메인 대시보드 (Board)

> 프로젝트 관리를 위한 게시판 형태의 메인 화면

### 목표

- 프로젝트 목록 관리 (CRUD)
- 진행 상태 표시

### 구현 항목

| 화면 ID | 화면명 | 설명 |
|:--------|:-------|:-----|
| C | Main (Board) | 프로젝트 게시판 |

### 세부 작업

1. **프로젝트 테이블**
   - 컬럼: Checkbox, Title, Status, Date
   - Title 기본값: `{userid}의 포트폴리오`
   - Status: 진행률 표시 (0% ~ 100%)
   - Date: 생성일자 자동 기록

2. **프로젝트 CRUD**
   - Create: 새 프로젝트 생성 (UUID 발급, 디렉토리 생성)
   - Read: 프로젝트 목록 조회
   - Update: 제목 수정 (우클릭 컨텍스트 메뉴)
   - Delete: 선택된 항목 삭제

3. **네비게이션**
   - 프로젝트 행 클릭 → Chat 화면 (D) 이동
   - Logout 버튼 → Login 화면 (A) 이동

### 데이터 구조

```json
{
  "projects": [
    {
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "title": "홍길동의 포트폴리오",
      "status": 0,
      "phase": 1,
      "currentStep": 0,
      "createdAt": "2025-12-21T10:00:00Z"
    }
  ]
}
```

### 완료 조건

```
- 프로젝트 생성/삭제/수정 기능 정상 동작
- 프로젝트 선택 시 Chat 화면으로 이동
- 진행률 실시간 반영
```

### 참조

- `frontend-flow.md` - 화면 C 명세

---

## Step 3: 채팅 인터페이스

> 대화형 콘텐츠 수집을 위한 채팅 UI

### 목표

- Chat Single/Split 모드 구현
- 사이드바 탭 네비게이션
- 마크다운 미리보기

### 구현 항목

| 화면 ID | 화면명 | 설명 |
|:--------|:-------|:-----|
| D | Chat (Single) | 전체 화면 채팅 |
| E | Chat (Split) | 채팅 + 마크다운 분할 |

### 세부 작업

1. **사이드바 탭 구조**
   ```
   📁 포트폴리오 내용 추출
      ├ Step 0: Gate
      ├ Step 1: Personal Info
      ├ Step 2: Work Experience
      ...
      └ Step 11: About
   🎨 포트폴리오 템플릿 선택
      ├ Step 1: Style Preference
      └ Step 2: Template Selection
   🚀 포트폴리오 생성
      ├ Step 3: Generation
      └ Step 4: Delivery
   ```

2. **Chat Single (D)**
   - 전체 화면 채팅 영역
   - 메시지 입력창 + Send 버튼
   - 사용자/봇 메시지 버블 구분

3. **Chat Split (E)**
   - 좌측 50%: 채팅 영역
   - 우측 50%: 마크다운 미리보기
   - Step 진행 시 자동 전환

4. **공통 컴포넌트**
   - ChatBubble: 메시지 버블 컴포넌트
   - MarkdownRenderer: 마크다운 렌더링 컴포넌트
   - ProgressIndicator: Step 진행 표시

### 모드 전환 로직

```
초기 상태: Chat Single (D)
Step 진행 시: Chat Split (E) 자동 전환
사용자 토글: 수동 전환 가능
```

### 완료 조건

```
- 채팅 메시지 송수신 정상 동작
- Single/Split 모드 전환 정상 동작
- 사이드바 탭 네비게이션 정상 동작
- 마크다운 미리보기 렌더링 정상 동작
```

### 참조

- `frontend-flow.md` - 화면 D, E 명세

---

## Step 4: 콘텐츠 & 스타일 통합 (Phase 1 + Phase 2)

> 백엔드 Phase 1, 2와 연동하여 콘텐츠 수집 및 스타일 선택 기능 구현

### 목표

- 대화 흐름에 따른 콘텐츠 수집 UI
- 스타일 선호도 입력 및 템플릿 선택 UI
- 진행률 관리

### 구현 항목 - Phase 1 (콘텐츠 수집)

| Step | 항목 | 주요 UI 요소 |
|:-----|:-----|:-------------|
| 0 | Gate | 희망 직무 입력 |
| 1 | Personal Info | 이름, GitHub, 이메일, 증명사진 업로드 |
| 2 | Work Experience | 반복 입력 폼, "추가" 버튼 |
| 3 | Education | 반복 입력 폼, 노출 여부 토글 |
| 4 | Representative Projects | 반복 입력 폼 |
| 5 | Projects | 반복 입력 폼 |
| 6 | Awards | 반복 입력 폼 |
| 7 | Major Activities | 반복 입력 폼 |
| 8 | Other Activities | 반복 입력 폼 |
| 9 | Certifications | 반복 입력 폼 |
| 10 | Technical Skills | Strong/Knowledgeable 분류 입력 |
| 11 | About | 자기소개 생성 및 확인 |

### 구현 항목 - Phase 2 (스타일 선택)

| Step | 항목 | 주요 UI 요소 |
|:-----|:-----|:-------------|
| 1 | Style Preference | 자유 텍스트 입력, 예시 제시 |
| 2 | Template Selection | 미리보기 이미지 표시, 선택 버튼 |

### 세부 작업

1. **이미지 업로드**
   - 증명사진 업로드 (Step 1)
   - 파일 형식 검증 (jpg, png)
   - 업로드 미리보기

2. **반복 입력 폼**
   - 항목 추가/삭제 버튼
   - 동적 폼 생성
   - 입력 완료 확인 로직

3. **진행률 계산**
   ```
   Phase 1: Step 0~11 (12 steps) → 80%
   Phase 2: Step 1~2 (2 steps) → 20%

   진행률 = (완료된 Step 수 / 전체 Step 수) × 100
   ```

4. **템플릿 미리보기**
   - preview.png 표시 (필수)
   - preview.gif 표시 (선택, 있을 경우)
   - 템플릿 title, description 표시
   - "이걸로 할게요" / "다른 거 볼래요" 버튼

5. **마크다운 미리보기 (Split 모드)**
   - Step별 수집 결과 실시간 표시
   - portfolio.json 구조에 맞게 렌더링

### API 연동

```
POST /api/chat          - 대화 메시지 전송
POST /api/upload        - 이미지 업로드
GET  /api/templates     - 템플릿 목록 조회
GET  /api/templates/{id}/preview - 템플릿 미리보기
POST /api/style/match   - LLM-as-a-Judge 템플릿 매칭
```

### 상태 저장

| 파일 | 저장 시점 | 내용 |
|:-----|:---------|:-----|
| portfolio.json | Phase 1 각 Step 완료 시 | 콘텐츠 데이터 |
| style.json | Phase 2 Step 2 완료 시 | 스타일 선택 데이터 |

### 완료 조건

```
Phase 1 완료 조건:
- portfolio.json의 about.confirmed = true

Phase 2 완료 조건:
- style.json의 selectedTemplate.confirmed = true
- style.json의 meta.status = "complete"
```

### 참조

- `conversation-flow-spec.md` - Phase 1 대화 흐름
- `style-and-generation-talk-flow.md` - Phase 2 대화 흐름
- `portfolio-data-schema.md` - 포트폴리오 데이터 스키마
- `style_data_schema.md` - 스타일 데이터 스키마

---

## Step 5: 생성 & 제공 (Phase 3)

> 백엔드 Phase 3과 연동하여 포트폴리오 생성 및 결과 제공

### 목표

- 포트폴리오 생성 진행 표시
- README.md 내용 표시
- 다운로드 기능

### 구현 항목 - Phase 3 (생성)

| Step | 항목 | 주요 UI 요소 |
|:-----|:-----|:-------------|
| 3 | Generation | 로딩 인디케이터, 진행 상태 |
| 4 | Delivery | README 표시, 다운로드 버튼 |

### 세부 작업

1. **생성 진행 표시 (Step 3)**
   - 로딩 인디케이터
   - 단계별 진행 메시지
     - "템플릿을 복사하는 중..."
     - "데이터를 적용하는 중..."
     - "포트폴리오를 생성하는 중..."

2. **결과 표시 (Step 4)**
   - README.md 마크다운 렌더링
   - 추가 작업 안내 (이미지 등)
   - 실행 방법 안내

3. **다운로드 기능**
   - src/ 폴더 압축 다운로드 (ZIP)
   - 다운로드 버튼
   - 파일명: `portfolio-{uuid}.zip`

4. **후속 액션**
   - "새 포트폴리오 만들기" → Main (C) 이동
   - "내용 수정하기" → Phase 1 재진입
   - "스타일 변경하기" → Phase 2 재진입

### API 연동

```
POST /api/generate      - 포트폴리오 생성 요청
GET  /api/status/{uuid} - 생성 상태 조회
GET  /api/download/{uuid} - 포트폴리오 다운로드
```

### 생성 프로세스

```
┌─────────────────────────────────────────────────────────────────┐
│  1. style.json에서 templateId 추출                               │
│  2. templates/{templateId}/src/ → data/{uuid}/src/ 복사          │
│  3. data/{uuid}/*.json → data/{uuid}/src/public/ 복사            │
│  4. 완료 후 README.md 내용 사용자에게 표시                        │
└─────────────────────────────────────────────────────────────────┘
```

### UI 상태

```
생성 중: 로딩 인디케이터 표시, 버튼 비활성화
생성 완료: README 표시, 다운로드 버튼 활성화
생성 실패: 에러 메시지 표시, 재시도 버튼
```

### 완료 조건

```
- src/ 디렉토리 생성 완료
- README.md 사용자에게 정상 표시
- 다운로드 기능 정상 동작
```

### 참조

- `style-and-generation-talk-flow.md` - Phase 3 흐름 (Step 3, 4)
- `template-modification-guide.md` - 템플릿 README.md 규칙
- `development.md` - Phase 3 상세

---

## 단계별 의존성

```
Step 1 ──────────────────────────────────────────────────────────▶
        └─▶ Step 2 ──────────────────────────────────────────────▶
                   └─▶ Step 3 ──────────────────────────────────▶
                              └─▶ Step 4 ──────────────────────▶
                                         └─▶ Step 5 ──────────▶

의존 관계:
- Step 2는 Step 1의 인증 시스템 필요
- Step 3는 Step 2의 프로젝트 생성 기능 필요
- Step 4는 Step 3의 채팅 UI 필요
- Step 5는 Step 4의 콘텐츠/스타일 데이터 필요
```

---

## 공통 컴포넌트

| 컴포넌트 | 사용 화면 | 설명 |
|:---------|:----------|:-----|
| Sidebar | C, D, E | 로고, 탭, 설정 포함 |
| Header | C, D, E | Logout 버튼 |
| Modal | F | 모달 래퍼 컴포넌트 |
| ChatBubble | D, E | 사용자/봇 메시지 구분 |
| MarkdownRenderer | E | 마크다운 렌더링 |
| BoardTable | C | 프로젝트 테이블 |
| LoadingIndicator | 전체 | 로딩 상태 표시 |
| ImageUploader | D, E | 이미지 업로드 |
| TemplatePreview | D, E | 템플릿 미리보기 |

---

## 디렉토리 구조 (예상)

```
front/src/
├── lib/
│   ├── components/
│   │   ├── common/
│   │   │   ├── Sidebar.svelte
│   │   │   ├── Header.svelte
│   │   │   ├── Modal.svelte
│   │   │   └── LoadingIndicator.svelte
│   │   ├── chat/
│   │   │   ├── ChatBubble.svelte
│   │   │   ├── ChatInput.svelte
│   │   │   └── MarkdownRenderer.svelte
│   │   ├── board/
│   │   │   ├── BoardTable.svelte
│   │   │   └── ProjectRow.svelte
│   │   └── portfolio/
│   │       ├── ImageUploader.svelte
│   │       └── TemplatePreview.svelte
│   ├── stores/
│   │   ├── auth.ts
│   │   ├── project.ts
│   │   └── chat.ts
│   └── api/
│       ├── auth.ts
│       ├── project.ts
│       ├── chat.ts
│       └── template.ts
├── routes/
│   ├── +layout.svelte
│   ├── +page.svelte          ← Login (A)
│   ├── signup/
│   │   └── +page.svelte      ← Sign up (B)
│   ├── board/
│   │   └── +page.svelte      ← Main Board (C)
│   └── chat/
│       └── [uuid]/
│           └── +page.svelte  ← Chat (D, E)
└── app.html
```

---

## 변경 이력

| 날짜 | 버전 | 변경 내용 |
|:-----|:-----|:----------|
| 2025-12-21 | 1.0.0 | 최초 작성 |
