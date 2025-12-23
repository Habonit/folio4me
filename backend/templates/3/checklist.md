# Template 3 Modification Checklist

> kundol_portfolio 템플릿 수정 체크리스트

---

## 개요

| 항목 | 내용 |
|:-----|:-----|
| 템플릿 ID | 3 |
| 템플릿 이름 | kundol_portfolio |
| 기술 스택 | 정적 HTML, CSS, Vanilla JavaScript |
| 데이터 소스 | `data.js` (`window.siteContent`) → `./public/portfolio.json`으로 변경 |
| 특이사항 | 데이터가 이미 분리되어 있어 변환 용이 |

---

## 1. 데이터 로딩 변경

현재 `data.js`에서 `window.siteContent`로 데이터 제공 중.

- [x] `data.js` 제거
- [x] `main.js`에서 `./public/portfolio.json` fetch 로직 추가
- [x] `renderSite()` 호출 전 데이터 로드 대기

### 변경 예시
```javascript
// 기존
window.addEventListener("DOMContentLoaded", () => {
    renderSite(siteContent);
});

// 변경
window.addEventListener("DOMContentLoaded", async () => {
    const response = await fetch('./public/portfolio.json');
    const portfolio = await response.json();
    renderSite(transformToSiteContent(portfolio));
});
```

---

## 2. 제거할 섹션/기능 (스키마에 없음)

### navigation
- [ ] Blog 링크 제거 (외부 링크)
- [ ] 또는 스키마에 없으므로 네비게이션 단순화

### hero
- [ ] `achievements` 제거 (BOJ 랭크, Mocha Contributor 등 특수 항목)
- [ ] 또는 `awards`로 대체 가능

### projects
- [ ] `featured` 플래그 제거 (스키마에 없음)
- [ ] `backgroundColor` 제거 (스키마에 없음)
- [ ] `extraClasses` 제거 (스키마에 없음)

---

## 3. 스키마 매핑 변경

### navigation
| 기존 (data.js) | 스키마 필드 | 비고 |
|:---------------|:------------|:-----|
| `logo` | `personalInfo.name` | |
| `links[].github` | `personalInfo.github` | |
| 기타 links | - | 제거 또는 단순화 |

### hero
| 기존 (data.js) | 스키마 필드 | 비고 |
|:---------------|:------------|:-----|
| `intro.link.label` | `personalInfo.name` | |
| `subtitle` | `meta.targetRole` 또는 `about.sentences[0]` | |
| `achievements` | - | 제거 또는 `awards`로 대체 |

### projects
| 기존 (data.js) | 스키마 필드 | 비고 |
|:---------------|:------------|:-----|
| `title` | `representativeProjects[].name` / `projects[].name` | |
| `description` | `responsibility` 조합 | |
| `url` | `links[0]` | 첫 번째 링크 사용 |
| `backgroundImage` | - | 추가 이미지 필요 |
| `backgroundColor` | - | 제거 (고정 색상 사용) |

### about
| 기존 (data.js) | 스키마 필드 | 비고 |
|:---------------|:------------|:-----|
| `title` | "About" (고정) | |
| `paragraphs[]` | `about.sentences[]` | |

### tech
| 기존 (data.js) | 스키마 필드 | 비고 |
|:---------------|:------------|:-----|
| `categories[].label: "Strong"` | `technicalSkills.strong[]` | |
| `categories[].label: "Knowledgeable"` | `technicalSkills.knowledgeable[]` | |
| `categories[].label: "ETC"` | - | 제거 |

### awards
| 기존 (data.js) | 스키마 필드 | 비고 |
|:---------------|:------------|:-----|
| `items[].label` | `awards[].title` | |
| `items[].detail` | `awards[].content` + `organization` | |
| `items[].link` | `awards[].links[0]` | |
| `items[].stars` | - | 제거 |

---

## 4. 추가할 섹션 (스키마에 있으나 현재 없음)

- [ ] **Experience 섹션** 추가 (`workExperience[]`)
- [ ] **Education 섹션** 추가 (`education.items[]`)
- [ ] **Certifications 섹션** 추가 (`certifications[]`)
- [ ] **Activities 섹션** 추가 (`activities.major[]`)

### 추가 방법
1. `index.html`에 새 섹션 요소 추가
2. `main.js`에 렌더링 함수 추가
3. `main.css`에 스타일 추가

---

## 5. 조건부 렌더링

`main.js`의 각 렌더링 함수에서 데이터 유무 확인:

- [ ] `workExperience.length === 0` → Experience 섹션 숨김
- [ ] `education.display === false` → Education 섹션 숨김
- [ ] `representativeProjects.length === 0 && projects.length === 0` → Projects 섹션 숨김
- [ ] `awards.length === 0` → Awards 섹션 숨김
- [ ] `certifications.length === 0` → Certifications 섹션 숨김

---

## 6. 데이터 변환 함수

`portfolio.json` → `siteContent` 형식 변환 필요:

```javascript
function transformToSiteContent(portfolio) {
    return {
        navigation: {
            logo: portfolio.personalInfo.name,
            logoAccent: "_",
            links: [
                { label: "Projects", href: "#projects" },
                { label: "About", href: "#about" },
                // ...
                portfolio.personalInfo.github && {
                    iconClass: "fab fa-github",
                    href: portfolio.personalInfo.github,
                    external: true
                }
            ].filter(Boolean)
        },
        hero: {
            intro: { text: "Hi I'm", link: { label: portfolio.personalInfo.name, href: portfolio.personalInfo.github || "#" } },
            subtitle: portfolio.meta.targetRole
        },
        projects: [...portfolio.representativeProjects, ...portfolio.projects].map(p => ({
            title: p.name,
            description: Array.isArray(p.responsibility) ? p.responsibility.join(", ") : p.responsibility,
            url: p.links?.[0] || "#",
            // backgroundImage 는 추가 필요
        })),
        about: {
            title: "About",
            paragraphs: portfolio.about.sentences
        },
        tech: {
            title: "Tech",
            categories: [
                { label: "Strong", items: portfolio.technicalSkills.strong.join(" / ") },
                { label: "Knowledgeable", items: portfolio.technicalSkills.knowledgeable.join(" / ") }
            ]
        },
        awards: {
            title: "Awards",
            items: portfolio.awards.map(a => ({
                label: a.title,
                detail: `${a.period} / ${a.content} / ${a.organization}`,
                link: a.links?.[0] ? { href: a.links[0], text: "link" } : null
            }))
        }
    };
}
```

- [ ] 변환 함수 구현
- [ ] Period 형식 변환 포함

---

## 7. Period 형식 변환

스키마 형식: `YYYYMMDD~YYYYMMDD` 또는 `YYYYMMDD~present`

- [ ] 날짜 형식 변환 함수 생성
- [ ] "20.07" 또는 "2020.07" 형태로 변환
- [ ] awards, workExperience, education 등에 적용

---

## 8. 파일 수정 체크리스트

### 수정 필요
- [x] `main.js` - fetch 로직 추가, 변환 함수 추가
- [x] `index.html` - 새 섹션 요소 추가 (Experience, Education 등)
- [x] `main.css` - 새 섹션 스타일 추가

### 제거
- [x] `data.js` - 더 이상 필요 없음

### 신규 생성
- [x] `README.md`
- [x] `public/` 폴더 (생성 시 json 파일 복사될 위치)

---

## 9. README.md 작성

- [x] `src/README.md` 생성

### 실행 방법
```markdown
## 실행 방법

1. 로컬 서버 실행 (다음 중 하나 선택)

   **옵션 A: Python**
   ```bash
   python -m http.server 8000
   ```

   **옵션 B: Node.js (http-server)**
   ```bash
   npx http-server
   ```

   **옵션 C: VS Code Live Server**
   - VS Code에서 Live Server 확장 설치
   - index.html 우클릭 → "Open with Live Server"

2. 브라우저에서 확인
   ```
   http://localhost:8000
   ```

> 참고: `file://` 프로토콜로 직접 열면 fetch가 작동하지 않습니다.
> 반드시 로컬 서버를 통해 실행하세요.
```

### 필요한 이미지 (예상)

| 파일명 | 용도 | 권장 크기 | 필수 여부 |
|--------|------|----------|----------|
| project-*.gif/.png | 프로젝트 배경 이미지 | 600x400 | 권장 |

> 프로필 이미지는 현재 템플릿에서 사용하지 않음

---

## 10. 테스트

- [ ] `data/sample/a7b8c9d0-...` (Template 3, Full)로 테스트
- [ ] `data/sample/b8c9d0e1-...` (Template 3, Partial)로 테스트
- [ ] `data/sample/c9d0e1f2-...` (Template 3, Minimal)로 테스트
- [ ] null 값 처리 확인
- [ ] 빈 배열 처리 확인
- [ ] CORS 이슈 없는지 확인 (로컬 서버 사용)

---

## 변경 이력

| 날짜 | 내용 |
|:-----|:-----|
| 2025-12-19 | 체크리스트 최초 작성 |
| 2025-12-19 | 템플릿 수정 완료 - main.js, index.html, main.css, README.md |
