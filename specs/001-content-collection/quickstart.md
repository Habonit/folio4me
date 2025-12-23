# Quickstart: Backend Phase 1 - Content Collection

**Branch**: `001-content-collection` | **Date**: 2025-12-23

이 가이드는 Content Collection 백엔드를 로컬에서 실행하는 방법을 설명합니다.

---

## 1. 사전 요구사항

### 1.1 필수 소프트웨어

| 소프트웨어 | 버전 | 설치 확인 명령 |
|-----------|------|---------------|
| Java | 17+ | `java --version` |
| Gradle | 8.x | `gradle --version` |
| Git | 2.x | `git --version` |

### 1.2 API 키

- **Anthropic API Key**: Claude API 호출에 필요
- 발급: https://console.anthropic.com/

---

## 2. 프로젝트 설정

### 2.1 저장소 클론

```bash
git clone <repository-url>
cd folio4me
git checkout 001-content-collection
```

### 2.2 환경 변수 설정

```bash
# backend/.env 파일 생성
cp backend/.env.example backend/.env
```

`.env` 파일 편집:

```env
# AI API 설정
ANTHROPIC_API_KEY=sk-ant-xxxxx

# 서버 설정
SERVER_PORT=8080

# 데이터 저장 경로
DATA_PATH=./data

# 로깅 레벨
LOG_LEVEL=INFO
```

### 2.3 의존성 설치

```bash
cd backend
./gradlew build --refresh-dependencies
```

---

## 3. 애플리케이션 실행

### 3.1 개발 모드 실행

```bash
cd backend
./gradlew bootRun
```

### 3.2 실행 확인

```bash
curl http://localhost:8080/actuator/health
```

예상 응답:
```json
{"status": "UP"}
```

---

## 4. API 테스트

### 4.1 새 세션 생성

```bash
curl -X POST http://localhost:8080/api/v1/sessions \
  -H "Content-Type: application/json"
```

예상 응답:
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "currentStep": "STEP_0_GATE",
  "status": "ACTIVE"
}
```

### 4.2 메시지 전송 (희망 직무 입력)

```bash
SESSION_ID="550e8400-e29b-41d4-a716-446655440000"

curl -X POST "http://localhost:8080/api/v1/sessions/${SESSION_ID}/messages" \
  -H "Content-Type: application/json" \
  -d '{"message": "백엔드 개발자입니다."}'
```

### 4.3 포트폴리오 상태 확인

```bash
curl "http://localhost:8080/api/v1/sessions/${SESSION_ID}/portfolio"
```

---

## 5. 테스트 실행

### 5.1 전체 테스트

```bash
cd backend
./gradlew test
```

### 5.2 특정 테스트 클래스 실행

```bash
./gradlew test --tests "com.folio4me.service.ConversationServiceTest"
```

### 5.3 테스트 커버리지 리포트

```bash
./gradlew jacocoTestReport
```

리포트 위치: `backend/build/reports/jacoco/test/html/index.html`

---

## 6. 디렉토리 구조 확인

### 6.1 소스 코드

```
backend/
├── build.gradle.kts
├── settings.gradle.kts
├── src/
│   ├── main/
│   │   ├── java/com/folio4me/
│   │   │   ├── FolioApplication.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── service/
│   │   │   ├── dto/
│   │   │   └── exception/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/folio4me/
└── data/
    └── {uuid}/
        └── portfolio.json
```

### 6.2 생성되는 데이터

```
data/
├── 550e8400-e29b-41d4-a716-446655440000/
│   ├── portfolio.json
│   └── images/
│       └── profile.jpg
└── ...
```

---

## 7. 문제 해결

### 7.1 포트 충돌

```bash
# 8080 포트 사용 중인 프로세스 확인
lsof -i :8080

# 다른 포트로 실행
SERVER_PORT=8081 ./gradlew bootRun
```

### 7.2 API 키 오류

```
Error: Invalid API Key
```

해결:
1. `.env` 파일의 `ANTHROPIC_API_KEY` 확인
2. API 키 형식: `sk-ant-api03-...`
3. Anthropic 콘솔에서 키 재발급

### 7.3 Gradle 빌드 오류

```bash
# Gradle 캐시 정리
./gradlew clean build --refresh-dependencies

# 또는 캐시 디렉토리 삭제
rm -rf ~/.gradle/caches
```

---

## 8. 개발 워크플로우

### 8.1 TDD 사이클

```bash
# 1. 테스트 작성 (Red)
./gradlew test  # 실패 확인

# 2. 구현 (Green)
./gradlew test  # 통과 확인

# 3. 리팩토링 (Refactor)
./gradlew test  # 여전히 통과 확인
```

### 8.2 커밋 전 체크리스트

```bash
# 1. 테스트 통과
./gradlew test

# 2. 린트 검사
./gradlew checkstyleMain

# 3. 타입 체크 (컴파일)
./gradlew compileJava
```

---

## 9. 다음 단계

Phase 1 완료 후:
1. `about.confirmed = true` 상태 확인
2. `meta.status = "complete"` 상태 확인
3. Phase 2 (Style Selection) 진행

---

## 10. 유용한 명령어

| 명령어 | 설명 |
|--------|------|
| `./gradlew bootRun` | 개발 서버 실행 |
| `./gradlew test` | 테스트 실행 |
| `./gradlew build` | 빌드 |
| `./gradlew clean` | 빌드 정리 |
| `./gradlew dependencies` | 의존성 트리 출력 |
