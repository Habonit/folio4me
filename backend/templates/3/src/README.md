# 포트폴리오 생성 완료

kundol_portfolio 템플릿으로 포트폴리오가 생성되었습니다.

## 환경 구성

별도 의존성 설치가 필요하지 않습니다.

## 실행 방법

로컬 웹서버를 통해 실행해야 합니다. (fetch API 사용을 위해 필수)

### 옵션 A: Python (권장)

```bash
python -m http.server 8000
```

브라우저에서 `http://localhost:8000` 접속

### 옵션 B: Node.js (http-server)

```bash
npx http-server -p 8000
```

브라우저에서 `http://localhost:8000` 접속

### 옵션 C: VS Code Live Server

1. VS Code에서 Live Server 확장 설치
2. `index.html` 우클릭 → "Open with Live Server"

> `file://` 프로토콜로 직접 열면 fetch가 작동하지 않습니다.
> 반드시 로컬 서버를 통해 실행하세요.

## 추가 이미지

별도 추가 이미지 없이 바로 사용 가능합니다.

### 선택사항: 프로젝트 배경 이미지

프로젝트 카드에 배경 이미지를 추가하려면:

1. 이미지 파일을 `imgs/` 디렉토리에 추가
2. `main.js`의 `transformToSiteContent` 함수에서 `backgroundImage` 경로 설정

| 파일명 | 용도 | 권장 크기 | 저장 위치 | 필수 여부 |
|--------|------|----------|----------|----------|
| project-*.gif/.png | 프로젝트 배경 이미지 | 600x400 | imgs/ | 선택 |

> 이미지가 없어도 기본 배경색이 적용됩니다.

## 참고 사항

- 포트폴리오 데이터: `public/portfolio.json`
- 데이터 수정 시 위 파일을 편집하세요.
- 페이지 새로고침으로 변경사항 확인
