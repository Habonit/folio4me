# folio4me Backend

포트폴리오 콘텐츠 수집을 위한 대화형 백엔드 서비스입니다.

## 기술 스택

- Java 17
- Spring Boot 3.2.1
- Gradle (Kotlin DSL)

## 사전 요구사항

- **Java 17** 이상
- **Gradle** (Wrapper 포함)
- **Anthropic API Key** (Claude AI 사용)

## 환경 설정

1. `.env` 파일 생성 (`.env.example` 참조)

```bash
cp .env.example .env
```

2. `.env` 파일에서 `ANTHROPIC_API_KEY` 설정

```
SERVER_PORT=8080
DATA_PATH=./data
AI_PROVIDER=anthropic
ANTHROPIC_API_KEY=sk-ant-api03-xxxxx  # 실제 API 키로 교체
AI_MODEL=claude-3-5-sonnet-20241022
LOG_LEVEL=INFO
```

## 빌드 및 실행

### 빌드

```bash
cd backend
./gradlew build
```

### 테스트 실행

```bash
./gradlew test
```

### 애플리케이션 실행

```bash
./gradlew bootRun
```

서버가 `http://localhost:8080`에서 실행됩니다.

## API 엔드포인트

### 세션 관리

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/sessions` | 새 세션 생성 |
| GET | `/api/v1/sessions/{sessionId}` | 세션 조회 |
| DELETE | `/api/v1/sessions/{sessionId}` | 세션 삭제 |

### 대화

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/sessions/{sessionId}/messages` | 대화 메시지 전송 |
| GET | `/api/v1/sessions/{sessionId}/portfolio` | 포트폴리오 조회 |
| GET | `/api/v1/sessions/{sessionId}/prompt` | 현재 단계 프롬프트 조회 |

### 헬스체크

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/actuator/health` | 서버 상태 확인 |

## 대화 단계 (Conversation Steps)

| Step | 단계명 | 설명 |
|------|--------|------|
| 0 | GATE | 시작 관문 |
| 1 | PERSONAL_INFO | 기본 정보 수집 |
| 2 | WORK_EXPERIENCE | 경력 사항 |
| 3 | EDUCATION | 학력 정보 |
| 4 | REPRESENTATIVE_PROJECTS | 대표 프로젝트 |
| 5 | PROJECTS | 기타 프로젝트 |
| 6 | AWARDS | 수상 내역 |
| 7 | MAJOR_ACTIVITIES | 주요 활동 |
| 8 | OTHER_ACTIVITIES | 기타 활동 |
| 9 | CERTIFICATIONS | 자격증 |
| 10 | TECHNICAL_SKILLS | 기술 스택 |
| 11 | ABOUT | 자기소개 |

## 사용 예시

### 1. 세션 생성

```bash
curl -X POST http://localhost:8080/api/v1/sessions
```

응답:
```json
{
  "id": "uuid-session-id",
  "currentStep": "GATE",
  "status": "IN_PROGRESS",
  "createdAt": "2024-12-23T10:00:00"
}
```

### 2. 메시지 전송

```bash
curl -X POST http://localhost:8080/api/v1/sessions/{sessionId}/messages \
  -H "Content-Type: application/json" \
  -d '{"message": "안녕하세요, 포트폴리오를 만들고 싶습니다."}'
```

### 3. 포트폴리오 조회

```bash
curl http://localhost:8080/api/v1/sessions/{sessionId}/portfolio
```

## 프로젝트 구조

```
backend/
├── src/main/java/com/folio4me/
│   ├── FolioApplication.java      # 메인 진입점
│   ├── controller/                # REST 컨트롤러
│   │   ├── ConversationController.java
│   │   └── SessionController.java
│   ├── service/                   # 비즈니스 로직
│   │   ├── ConversationService.java
│   │   ├── SessionService.java
│   │   ├── AiService.java
│   │   └── handler/               # 단계별 핸들러
│   ├── model/                     # 도메인 모델
│   ├── dto/                       # 데이터 전송 객체
│   ├── config/                    # 설정
│   └── exception/                 # 예외 처리
├── src/main/resources/
│   └── application.yml            # 애플리케이션 설정
├── templates/                     # 포트폴리오 템플릿
├── build.gradle.kts
└── .env.example
```
