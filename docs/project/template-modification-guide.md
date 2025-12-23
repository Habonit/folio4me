# Template Modification Guide

> folio4me 템플릿 수정 가이드

---

## 1. 개요

### 1.1 목적

템플릿이 `data/sample/*`의 데이터를 범용적으로 렌더링할 수 있도록 수정하는 방법론을 정의합니다.

### 1.2 핵심 원칙

```
템플릿의 디자인/레이아웃은 유지
데이터는 오직 public/portfolio.json 기준 (통일된 경로)
```

### 1.3 관련 문서

- `portfolio-data-schema.md`: 포트폴리오 데이터 스키마
- `template_schema.md`: 템플릿 구조 및 README.md 규칙
- `development.md`: 전체 개발 단계 (Phase 3)

---

## 2. 수정 원칙

### 2.1 데이터 기준

- 모든 데이터는 `public/portfolio.json`에서 가져옴 (통일된 경로)
- `portfolio-data-schema.md` 스키마를 준수
- 템플릿 내부에 하드코딩된 데이터는 모두 제거

```
data/{uuid}/
├── portfolio.json    ← 원본 데이터
├── style.json        ← 원본 스타일
└── src/              ← 템플릿 + 복사된 데이터
    ├── public/
    │   ├── portfolio.json  ← 복사됨 (템플릿이 참조)
    │   └── style.json      ← 복사됨
    └── ...             ← 템플릿 파일들
```

**데이터 접근 경로 (통일 규칙):**
- Next.js 템플릿: `/portfolio.json` (public 폴더 규칙)
- 정적 HTML 템플릿: `./public/portfolio.json`

### 2.2 유지할 것

| 항목 | 설명 |
|:-----|:-----|
| 레이아웃 | 섹션 배치, 그리드 구조 |
| 색상/테마 | 템플릿 고유의 색상 팔레트 |
| 애니메이션 | 스크롤, 호버 등 인터랙션 효과 |
| 타이포그래피 | 폰트, 크기, 간격 |
| 컴포넌트 스타일 | 카드, 버튼, 뱃지 등 UI 요소 |

### 2.3 제거 또는 조건부 숨김

#### 제거 대상

스키마에 존재하지 않는 항목은 완전히 제거:

| 템플릿에만 있는 항목 | 처리 |
|:-----|:-----|
| testimonials (고객 후기) | 제거 |
| clients (클라이언트 로고) | 제거 |
| blog (블로그 섹션) | 제거 |
| 기타 스키마에 없는 항목 | 제거 |

#### 조건부 숨김

스키마에 있지만 데이터가 없는 경우 해당 섹션 숨김:

```javascript
// 예시: 수상 내역이 없으면 섹션 숨김
if (portfolio.awards && portfolio.awards.length > 0) {
  // Awards 섹션 렌더링
}
```

| 조건 | 처리 |
|:-----|:-----|
| 배열이 빈 경우 `[]` | 해당 섹션 숨김 |
| 값이 `null`인 경우 | 해당 필드/섹션 숨김 |
| `display: false`인 경우 | 해당 섹션 숨김 (예: education) |

---

## 3. 스키마 필드 ↔ 섹션 매핑

템플릿의 각 섹션이 어떤 스키마 필드를 사용해야 하는지 가이드:

| 스키마 필드 | 용도 | 섹션 예시 |
|:-----|:-----|:-----|
| `personalInfo` | 이름, 이메일, GitHub, 프로필 이미지 | Hero, Header, Sidebar |
| `about` | 자기소개 문장들 | About 섹션 |
| `workExperience[]` | 경력 사항 | Experience 섹션 |
| `education` | 학력 사항 | Education 섹션 |
| `representativeProjects[]` | 대표 프로젝트 | Featured Projects |
| `projects[]` | 일반 프로젝트 | Projects 섹션 |
| `technicalSkills` | 기술 스택 (strong/knowledgeable) | Skills 섹션 |
| `awards[]` | 수상 내역 | Awards 섹션 |
| `activities` | 활동 (major/minor) | Activities 섹션 |
| `certifications[]` | 자격증 | Certifications 섹션 |

### 필드별 세부 매핑

#### personalInfo

```json
{
  "name": "프로필 이름",
  "email": "연락처",
  "github": "GitHub 링크",
  "profileImage": "프로필 이미지 URL (null 가능)"
}
```

#### technicalSkills

```json
{
  "strong": ["주력 기술들"],
  "knowledgeable": ["사용 가능한 기술들"]
}
```

#### workExperience / projects

```json
{
  "period": "YYYYMMDD~YYYYMMDD 또는 YYYYMMDD~present",
  "responsibility": "문자열 또는 배열",
  "techStack": ["기술 스택"],
  "links": ["관련 링크"] // null 가능
}
```

---

## 4. README.md 작성 규칙

각 템플릿의 `src/README.md`는 사용자에게 표시되며, 다음 내용을 포함해야 합니다.

### 4.1 필수 섹션

1. **실행 방법** - localhost에 배포하는 방법
2. **추가 이미지** - 필요한 정적 이미지 (있는 경우)

### 4.2 실행 방법 작성

템플릿 기술 스택에 따라 작성:

**Next.js / React 기반:**
```markdown
## 실행 방법

1. 의존성 설치
   ```bash
   npm install
   ```

2. 개발 서버 실행
   ```bash
   npm run dev
   ```

3. 브라우저에서 확인
   ```
   http://localhost:3000
   ```
```

**정적 HTML 기반:**
```markdown
## 실행 방법

1. 로컬 서버 실행 (다음 중 하나 선택)

   **옵션 A: Python**
   ```bash
   python -m http.server 8000
   ```

   **옵션 B: Node.js (http-server)**
   ```bash
   npx http-server -p 8000
   ```

2. 브라우저에서 확인
   ```
   http://localhost:8000
   ```

> `file://` 프로토콜로 직접 열면 fetch가 작동하지 않습니다.
> 반드시 로컬 서버를 통해 실행하세요.
```

### 4.3 추가 이미지 작성

**이미지가 필요한 경우:**
```markdown
## 추가 작업

다음 이미지를 추가해주세요:

| 파일명 | 용도 | 권장 크기 | 필수 여부 |
|--------|------|----------|----------|
| profile.jpg | 프로필 사진 | 400x400 | 권장 |
| project-1.png | 프로젝트 썸네일 | 800x450 | 선택 |
```

**이미지가 필요 없는 경우:**
```markdown
## 추가 작업

별도 작업 없이 바로 사용 가능합니다.
```

### 4.4 전체 README.md 템플릿

```markdown
# 포트폴리오 생성 완료

{템플릿 이름} 템플릿으로 포트폴리오가 생성되었습니다.

## 환경 구성

[의존성 설치 방법 - npm install 등]

## 실행 방법

[localhost에서 실행하는 구체적인 명령어]

## 추가 이미지

[필요한 이미지 목록과 각 이미지의 역할]

| 파일명 | 용도 | 권장 크기 | 저장 위치 | 필수 여부 |
|--------|------|----------|----------|----------|
| profile.jpg | 프로필 사진 | 400x400 | public/ | 선택 |

> 이미지가 필요 없는 경우: "별도 추가 이미지 없이 바로 사용 가능합니다."

## 참고 사항

- 포트폴리오 데이터: `public/portfolio.json`
- 데이터 수정 시 위 파일을 편집하세요.
```

> **Note**: README.md 작성에 대한 상세 가이드는 `template_schema.md`의 4.3절을 참조하세요.

---

## 5. 데이터 로딩 패턴

### 5.1 JavaScript (정적 HTML)

```javascript
async function loadPortfolio() {
  const response = await fetch('./public/portfolio.json');
  const portfolio = await response.json();
  return portfolio;
}

// 사용
loadPortfolio().then(data => {
  renderProfile(data.personalInfo);
  renderSkills(data.technicalSkills);
  // ...
});
```

> **Note**: 정적 HTML에서는 `./public/portfolio.json` 경로를 사용합니다.
> `file://` 프로토콜로 직접 열면 fetch가 작동하지 않으므로 반드시 로컬 서버를 통해 실행하세요.

### 5.2 Next.js / React

**클라이언트 사이드 (권장):**
```typescript
'use client';
import { useEffect, useState } from 'react';

function usePortfolio() {
  const [data, setData] = useState(null);

  useEffect(() => {
    fetch('/portfolio.json')  // public/ 폴더의 파일은 /로 접근
      .then(res => res.json())
      .then(setData);
  }, []);

  return data;
}
```

**서버 사이드:**
```typescript
// app/page.tsx
import { promises as fs } from 'fs';
import path from 'path';

async function getPortfolioData() {
  const filePath = path.join(process.cwd(), 'public', 'portfolio.json');
  const fileContents = await fs.readFile(filePath, 'utf8');
  return JSON.parse(fileContents);
}

export default async function Page() {
  const portfolio = await getPortfolioData();
  // ...
}
```

> **Note**: Next.js에서 `public/` 폴더의 파일은 `/portfolio.json`으로 접근 가능합니다.
> 서버 사이드에서는 `public/portfolio.json` 경로를 명시적으로 사용해야 합니다.

---

## 6. 변경 이력

| 날짜 | 버전 | 변경 내용 |
|:-----|:-----|:----------|
| 2025-12-19 | 1.0.0 | 최초 작성 |
| 2025-12-19 | 1.1.0 | 데이터 참조 경로 변경: `../portfolio.json` → `./portfolio.json` (같은 디렉토리) |
| 2025-12-19 | 1.2.0 | 데이터 참조 경로 통일: `public/portfolio.json`, 데이터 로딩 패턴 업데이트, README.md 템플릿에 환경 구성/추가 이미지 역할 추가 |
