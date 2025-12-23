# Template 1 Modification Checklist

> bento_portfolio 템플릿 수정 체크리스트

---

## 개요

| 항목 | 내용 |
|:-----|:-----|
| 템플릿 ID | 1 |
| 템플릿 이름 | bento_portfolio |
| 기술 스택 | Next.js, React, TypeScript, Tailwind CSS |
| 데이터 소스 | `lib/portfolio-data.ts` → `public/portfolio.json`으로 변경 (fetch: `/portfolio.json`) |

---

## 1. 데이터 로딩 변경

- [x] `lib/portfolio-data.ts` 수정 - TypeScript 타입 및 변환 함수
- [x] `public/portfolio.json`을 fetch하는 유틸 함수 생성 (`fetchPortfolioData` → `/portfolio.json`)
- [x] 각 컴포넌트에서 새로운 데이터 로딩 방식 적용

---

## 2. 제거할 섹션/기능 (스키마에 없음)

### page.tsx
- [x] 네비게이션에서 `blog` 제거
- [x] `BlogSection` import 및 렌더링 제거

### about-section.tsx
- [x] "What I'm Doing" 섹션 제거 (`services`)
- [x] "Testimonials" 섹션 제거 (`testimonials`)
- [x] "Clients" 섹션 제거 (`clients`)

### profile-sidebar.tsx
- [x] `phone` 필드 제거
- [x] `birthday` 필드 제거
- [x] `location` 필드 제거
- [x] `social.twitter` 제거
- [x] `social.instagram` 제거

### portfolio-section.tsx
- [x] `category` 필터 기능 제거 (스키마에 category 없음)
- [x] `liveUrl`, `githubUrl` → `links[]`로 매핑

### contact-section-new.tsx
- [x] 폼 전송 기능 제거
- [x] 단순 연락처 정보 표시로 변경 (email, github)

### blog-section.tsx
- [x] 렌더링에서 제거 (파일 보존)

---

## 3. 스키마 매핑 변경

### personalInfo (profile-sidebar.tsx)
| 기존 필드 | 스키마 필드 | 비고 |
|:----------|:------------|:-----|
| `name` | `personalInfo.name` | ✅ |
| `title` | `meta.targetRole` | ✅ |
| `avatar` | `personalInfo.profileImage` | ✅ null 가능 |
| `email` | `personalInfo.email` | ✅ |
| `social.github` | `personalInfo.github` | ✅ |

### about (about-section.tsx)
| 기존 필드 | 스키마 필드 | 비고 |
|:----------|:------------|:-----|
| `description[]` | `about.sentences[]` | ✅ |

### resume (resume-section.tsx)
| 기존 필드 | 스키마 필드 | 비고 |
|:----------|:------------|:-----|
| `education[].title` | `education.items[].school` | ✅ |
| `education[].period` | `education.items[].period` | ✅ 형식 변환 |
| `education[].description` | `education.items[].major` | ✅ |
| `experience[].title` | `workExperience[].position` | ✅ |
| `experience[].period` | `workExperience[].period` | ✅ 형식 변환 |
| `experience[].description` | `workExperience[].responsibility` | ✅ |
| `skills[].name/level` | `technicalSkills.strong/knowledgeable` | ✅ |

### portfolio (portfolio-section.tsx)
| 기존 필드 | 스키마 필드 | 비고 |
|:----------|:------------|:-----|
| `projects[].title` | `representativeProjects[].name` / `projects[].name` | ✅ |
| `projects[].description` | `responsibility` 조합 | ✅ |
| `projects[].tech` | `techStack[]` | ✅ |
| `projects[].link` | `links[0]` | ✅ |
| `projects[].period` | `period` | ✅ 형식 변환 |

---

## 4. 추가할 섹션 (스키마에 있으나 템플릿에 없음)

- [x] **Awards 섹션** - Resume 섹션 내 통합
- [x] **Activities 섹션** - Resume 섹션 내 통합
- [x] **Certifications 섹션** - Resume 섹션 내 통합

---

## 5. 조건부 렌더링

각 섹션에서 데이터 유무 확인:

- [x] `education.display === false` → Education 섹션 숨김
- [x] `workExperience.length === 0` → Experience 섹션 숨김
- [x] `representativeProjects.length === 0 && projects.length === 0` → Portfolio 탭 숨김
- [x] `awards.length === 0` → Awards 섹션 숨김
- [x] `activities.major.length === 0` → Activities 섹션 숨김
- [x] `certifications.length === 0` → Certifications 섹션 숨김
- [x] `personalInfo.profileImage === null` → placeholder 사용

---

## 6. Period 형식 변환

스키마 형식: `YYYYMMDD~YYYYMMDD` 또는 `YYYYMMDD~present`

- [x] 날짜 형식 변환 유틸 함수 생성 (`formatPeriod`)
- [x] education, workExperience, projects 등에 적용

---

## 7. 네비게이션 탭 조정

현재: `['about', 'resume', 'portfolio', 'blog', 'contact']`

변경 후:
- [x] `blog` 제거
- [x] 동적 네비게이션 - 데이터 유무에 따라 탭 표시

결과: `['about', 'resume', 'portfolio', 'contact']` (데이터 유무에 따라 동적)

---

## 8. README.md 작성

- [x] `src/README.md` 생성

### 필요한 이미지

| 파일명 | 용도 | 권장 크기 | 필수 여부 |
|--------|------|----------|----------|
| - | 프로필 사진 (profileImage) | 200x200 | 선택 |

> 프로필 이미지가 없으면 placeholder 사용

---

## 9. 파일 수정 체크리스트

### 수정 완료
- [x] `app/page.tsx`
- [x] `lib/portfolio-data.ts`
- [x] `components/profile-sidebar.tsx`
- [x] `components/about-section.tsx`
- [x] `components/resume-section.tsx`
- [x] `components/portfolio-section.tsx`
- [x] `components/contact-section-new.tsx`

### 신규 생성
- [x] `src/README.md`

---

## 10. 테스트

- [ ] `data/sample/a1b2c3d4-...` (Template 1, Full 데이터)로 테스트
- [ ] `data/sample/b2c3d4e5-...` (Template 1, Partial 데이터)로 테스트
- [ ] `data/sample/c3d4e5f6-...` (Template 1, Minimal 데이터)로 테스트
- [ ] null 값 처리 확인
- [ ] 빈 배열 처리 확인

---

## 변경 이력

| 날짜 | 내용 |
|:-----|:-----|
| 2025-12-19 | 체크리스트 최초 작성 |
| 2025-12-19 | 템플릿 수정 완료 - 모든 컴포넌트 및 README.md |
