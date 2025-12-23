# 포트폴리오 생성 완료

bento_portfolio 템플릿으로 포트폴리오가 생성되었습니다.

## 환경 구성

### 필수 요구사항

- Node.js 18.x 이상
- npm 또는 pnpm

### 의존성 설치

```bash
npm install
# 또는
pnpm install
```

## 실행 방법

### 개발 서버 실행

```bash
npm run dev
```

브라우저에서 `http://localhost:3000` 접속

### 프로덕션 빌드

```bash
npm run build
npm run start
```

## 기술 스택

- Next.js 14 (App Router)
- React 18
- TypeScript
- Tailwind CSS
- Lucide Icons

## 추가 이미지

별도 추가 이미지 없이 바로 사용 가능합니다.

### 선택사항: 프로필 이미지

프로필 이미지를 추가하려면:

1. 이미지 파일을 `public/` 디렉토리에 추가
2. `public/portfolio.json`의 `personalInfo.profileImage` 경로 수정

| 파일명 | 용도 | 권장 크기 | 저장 위치 | 필수 여부 |
|--------|------|----------|----------|----------|
| profile.jpg | 프로필 사진 | 200x200 | public/ | 선택 |

> 이미지가 없어도 기본 placeholder가 적용됩니다.

## 참고 사항

- 포트폴리오 데이터: `public/portfolio.json`
- 데이터 수정 시 위 파일을 편집하세요.
- 페이지 새로고침으로 변경사항 확인
