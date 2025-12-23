# folio4me Backend

AI 기반 포트폴리오 콘텐츠 수집 대화형 백엔드 서비스

## 환경

- **Java 17** 이상
- **Gradle** (Wrapper 포함)
- **Anthropic API Key** (Claude AI)

## 빠른 시작

```bash
# 1. 환경 설정
cp .env.example .env
# .env 파일에서 ANTHROPIC_API_KEY 설정

# 2. 빌드 & 테스트
./gradlew build

# 3. 실행
./gradlew bootRun
```

서버: `http://localhost:8080`

## 주요 명령어

```bash
./gradlew test              # 테스트 실행
./gradlew jacocoTestReport  # 커버리지 리포트 생성
./gradlew bootRun           # 개발 서버 실행
./gradlew build             # 빌드 (JAR 생성)
```

## API 테스트

```bash
# 세션 생성
curl -X POST http://localhost:8080/api/v1/sessions

# 메시지 전송
curl -X POST http://localhost:8080/api/v1/sessions/{sessionId}/messages \
  -H "Content-Type: application/json" \
  -d '{"message": "백엔드 개발자입니다"}'

# 포트폴리오 조회
curl http://localhost:8080/api/v1/sessions/{sessionId}/portfolio

# 헬스체크
curl http://localhost:8080/actuator/health
```

## 상세 문서

- [explain.md](./explain.md) - 자바 백엔드 초보자를 위한 상세 설명서
