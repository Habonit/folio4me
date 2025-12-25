# folio4me 백엔드 이해하기

자바/스프링 부트 초보자를 위한 상세 설명서입니다.

---

## 전체 개발 로드맵

folio4me는 **4단계**로 개발됩니다. 현재 백엔드는 **1단계**입니다.

```
┌─────────────────────────────────────────────────────────────────┐
│                     folio4me 개발 단계                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  [1단계] 콘텐츠 수집 ◀── 현재 (backend)                          │
│  └─ AI 대화를 통해 포트폴리오 내용 추출                           │
│  └─ 경력, 프로젝트, 기술스택 등 JSON 데이터 생성                   │
│                                                                 │
│  [2단계] 웹 테스트                                               │
│  └─ 웹 프론트엔드에서 대화 인터페이스 제공                         │
│  └─ 실시간으로 수집 과정 테스트                                   │
│                                                                 │
│  [3단계] 포트폴리오 형태 선택                                     │
│  └─ 웹에서 템플릿/디자인 선택                                     │
│  └─ 레이아웃, 스타일 커스터마이징                                 │
│                                                                 │
│  [4단계] 포트폴리오 생성                                         │
│  └─ 수집된 콘텐츠 + 선택된 템플릿 결합                            │
│  └─ 최종 포트폴리오 웹사이트/PDF 생성                             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 1. 이 프로젝트는 무엇인가?

folio4me 백엔드는 **AI 대화를 통해 사용자의 포트폴리오 정보를 수집**하는 서비스입니다.

### 동작 방식
1. 사용자가 세션을 생성합니다
2. AI가 질문을 하고, 사용자가 대답합니다
3. 12단계에 걸쳐 정보를 수집합니다 (개인정보 → 경력 → 학력 → ... → 자기소개)
4. 완료되면 JSON 형태의 포트폴리오 데이터가 생성됩니다

---

## 2. 프로젝트 구조

```
backend/
├── src/main/java/com/folio4me/   # 메인 소스 코드
│   ├── FolioApplication.java     # 진입점 (main 함수)
│   ├── controller/               # REST API 엔드포인트
│   ├── service/                  # 비즈니스 로직
│   ├── model/                    # 데이터 구조 (Entity)
│   ├── dto/                      # 요청/응답 객체
│   ├── config/                   # 설정 클래스
│   └── exception/                # 예외 처리
├── src/main/resources/
│   └── application.yml           # 스프링 설정 파일
├── src/test/java/                # 테스트 코드
├── build.gradle.kts              # 빌드 설정 (의존성 관리)
└── .env                          # 환경 변수 (API 키 등)
```

---

## 3. 핵심 개념 설명

### 3.1 Spring Boot란?

**Spring Boot**는 자바 웹 애플리케이션을 쉽게 만들게 해주는 프레임워크입니다.

- 서버 설정을 자동으로 해줍니다
- 의존성 주입(DI)으로 객체 관리를 해줍니다
- 어노테이션(@)으로 설정을 간편하게 합니다

### 3.2 계층 구조 (Layer Architecture)

스프링에서는 코드를 역할별로 분리합니다:

```
[클라이언트] → [Controller] → [Service] → [Repository/Storage]
                    ↓              ↓              ↓
              HTTP 요청 처리   비즈니스 로직    데이터 저장
```

### 3.3 주요 어노테이션

```java
@RestController  // 이 클래스는 REST API를 처리합니다
@Service         // 이 클래스는 비즈니스 로직을 담당합니다
@Autowired       // 스프링이 자동으로 객체를 주입해줍니다
@PostMapping     // POST 요청을 처리합니다
@GetMapping      // GET 요청을 처리합니다
```

---

## 4. 코드 흐름 따라가기

### 4.1 세션 생성 요청이 들어오면?

```
POST /api/v1/sessions
        ↓
SessionController.createSession()
        ↓
SessionService.createSession()
        ↓
새 Session 객체 생성 (UUID 발급)
        ↓
메모리에 저장 + JSON 파일로 백업
```

**SessionController.java**의 핵심 코드:
```java
@PostMapping
public ResponseEntity<Session> createSession() {
    Session session = sessionService.createSession();
    return ResponseEntity.status(HttpStatus.CREATED).body(session);
}
```

### 4.2 대화 메시지 처리 흐름

```
POST /api/v1/sessions/{id}/messages
        ↓
ConversationController.sendMessage()
        ↓
ConversationService.processMessage()
        ↓
StepHandlerRegistry.getHandler(현재단계)
        ↓
해당 StepHandler.handle(세션, 메시지)
        ↓
AI 호출 (Claude API) → 응답 생성
        ↓
포트폴리오 데이터 업데이트
        ↓
다음 단계로 진행 or 현재 단계 유지
```

---

## 5. 12단계 대화 흐름

| Step | 이름 | 수집 정보 |
|------|------|-----------|
| 0 | GATE | 기술 직군 확인 (비기술 직군은 서비스 종료) |
| 1 | PERSONAL_INFO | 이름, 이메일, GitHub |
| 2 | WORK_EXPERIENCE | 경력 사항 (회사, 기간, 역할) |
| 3 | EDUCATION | 학력 정보 |
| 4 | REPRESENTATIVE_PROJECTS | 대표 프로젝트 |
| 5 | PROJECTS | 추가 프로젝트 |
| 6 | AWARDS | 수상 내역 |
| 7 | MAJOR_ACTIVITIES | 주요 활동 |
| 8 | OTHER_ACTIVITIES | 기타 활동 |
| 9 | CERTIFICATIONS | 자격증 |
| 10 | TECHNICAL_SKILLS | 기술 스택 |
| 11 | ABOUT | AI가 생성한 자기소개 확정 |

### 다중 입력 단계
2~9단계는 여러 항목을 입력받을 수 있습니다:
- "완료" 또는 "더 없습니다" → 다음 단계로
- "건너뛰기" → 해당 단계 스킵

---

## 6. 주요 파일 설명

### 6.1 Controller (컨트롤러)

**역할**: HTTP 요청을 받아 처리하고 응답을 반환

```java
// ConversationController.java
@RestController
@RequestMapping("/api/v1/sessions/{sessionId}")
public class ConversationController {

    @PostMapping("/messages")
    public ConversationResponse sendMessage(
        @PathVariable String sessionId,
        @RequestBody ConversationRequest request) {
        return conversationService.processMessage(sessionId, request);
    }
}
```

- `@RestController`: JSON으로 응답하는 컨트롤러
- `@PathVariable`: URL의 {sessionId} 부분을 변수로 받음
- `@RequestBody`: HTTP 본문을 객체로 변환

### 6.2 Service (서비스)

**역할**: 실제 비즈니스 로직 처리

```java
// ConversationService.java
@Service
public class ConversationService {

    public ConversationResponse processMessage(String sessionId, ConversationRequest request) {
        // 1. 세션 조회
        Session session = sessionService.getSession(sessionId);

        // 2. 현재 단계의 핸들러 가져오기
        StepHandler handler = stepHandlerRegistry.getHandler(session.getCurrentStep());

        // 3. 메시지 처리
        String response = handler.handle(session, request.getMessage());

        // 4. 다음 단계로 진행 여부 확인
        if (handler.shouldAdvance(session, request.getMessage())) {
            advanceToNextStep(session);
        }

        // 5. 응답 반환
        return new ConversationResponse(response, session.getCurrentStep(), session.isCompleted());
    }
}
```

### 6.3 StepHandler (단계별 핸들러)

**역할**: 각 대화 단계의 로직 처리

```java
// Step1PersonalInfoHandler.java
@Component
public class Step1PersonalInfoHandler implements StepHandler {

    @Override
    public ConversationStep getStep() {
        return ConversationStep.STEP_1_PERSONAL_INFO;
    }

    @Override
    public String handle(Session session, String userMessage) {
        // AI에게 메시지 보내고 응답 받기
        String aiResponse = aiService.chat(systemPrompt, userMessage);

        // 응답에서 정보 추출하여 포트폴리오에 저장
        extractAndSavePersonalInfo(session, userMessage);

        return aiResponse;
    }
}
```

### 6.4 Model (모델)

**역할**: 데이터 구조 정의

```java
// Session.java
public class Session {
    private String id;                    // UUID
    private ConversationStep currentStep; // 현재 단계
    private SessionStatus status;         // ACTIVE, COMPLETED, TERMINATED
    private Portfolio portfolio;          // 수집된 포트폴리오 데이터
    private List<ConversationMessage> history; // 대화 기록
}

// Portfolio.java
public class Portfolio {
    private PersonalInfo personalInfo;
    private List<WorkExperience> workExperiences;
    private EducationSection education;
    private List<Project> representativeProjects;
    private List<Project> projects;
    private List<Award> awards;
    private List<Activity> majorActivities;
    private List<Activity> otherActivities;
    private List<Certification> certifications;
    private TechnicalSkills technicalSkills;
    private About about;
    private Meta meta;
}
```

### 6.5 DTO (Data Transfer Object)

**역할**: 요청/응답 데이터 형식 정의

```java
// ConversationRequest.java
public record ConversationRequest(
    @NotBlank String message
) {}

// ConversationResponse.java
public record ConversationResponse(
    String message,
    ConversationStep currentStep,
    boolean completed
) {}
```

---

## 7. 테스트 구조

### 7.1 테스트 종류

| 종류 | 파일 | 설명 |
|------|------|------|
| 단위 테스트 | `ConversationControllerTest.java` | 컨트롤러만 테스트 (Mock 사용) |
| 단위 테스트 | `ConversationServiceTest.java` | 서비스만 테스트 (Mock 사용) |
| 단위 테스트 | `StepHandlerTest.java` | 핸들러 테스트 |
| 통합 테스트 | `ConversationFlowTest.java` | 전체 흐름 E2E 테스트 |

### 7.2 테스트 코드 읽는 법

```java
@Test
@DisplayName("유효한 요청 시 200 OK와 응답 반환")
void shouldReturn200WithResponse() throws Exception {
    // Given - 테스트 준비
    String sessionId = "test-session-id";
    when(conversationService.processMessage(...)).thenReturn(response);

    // When & Then - 실행 및 검증
    mockMvc.perform(post("/api/v1/sessions/{id}/messages", sessionId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("예상 응답"));
}
```

- `@Test`: 테스트 메서드임을 표시
- `@DisplayName`: 테스트 설명 (한글 가능)
- `Given-When-Then`: 준비-실행-검증 패턴
- `when().thenReturn()`: Mock 객체 동작 정의
- `andExpect()`: 결과 검증

### 7.3 테스트 실행

```bash
# 전체 테스트
./gradlew test

# 특정 테스트 클래스만
./gradlew test --tests "ConversationControllerTest"

# 테스트 결과 확인
open build/reports/tests/test/index.html

## wsl 환경이라면
explorer.exe $(wslpath -w build/reports/tests/test/index.html)

# 커버리지 확인
./gradlew jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

---

## 8. 설정 파일

### 8.1 application.yml

```yaml
server:
  port: ${SERVER_PORT:8080}  # 환경변수 또는 기본값 8080

folio:
  data:
    path: ${DATA_PATH:./data}  # 데이터 저장 경로
  ai:
    provider: ${AI_PROVIDER:anthropic}
    api-key: ${ANTHROPIC_API_KEY:}
    model: ${AI_MODEL:claude-3-5-sonnet-20241022}

logging:
  level:
    com.folio4me: DEBUG
```

### 8.2 .env 파일

```
SERVER_PORT=8080
DATA_PATH=./data
AI_PROVIDER=anthropic
ANTHROPIC_API_KEY=sk-ant-api03-xxxxx
AI_MODEL=claude-3-5-sonnet-20241022
LOG_LEVEL=INFO
```

---

## 9. 디자인 패턴

### 9.1 Strategy Pattern (전략 패턴)

각 대화 단계를 독립적인 핸들러로 분리:

```java
public interface StepHandler {
    ConversationStep getStep();
    String handle(Session session, String userMessage);
    boolean shouldAdvance(Session session, String userMessage);
}

// 12개의 구현체
Step0GateHandler, Step1PersonalInfoHandler, Step2WorkExperienceHandler...
```

### 9.2 Registry Pattern (레지스트리 패턴)

핸들러를 중앙에서 관리:

```java
@Component
public class StepHandlerRegistry {
    private final Map<ConversationStep, StepHandler> handlers = new EnumMap<>();

    public StepHandler getHandler(ConversationStep step) {
        return handlers.get(step);
    }
}
```

---

## 10. 자주 하는 작업

### 새 API 엔드포인트 추가하기

1. `controller/`에 메서드 추가
2. 필요시 `dto/`에 요청/응답 클래스 추가
3. `service/`에 비즈니스 로직 추가
4. 테스트 작성

### 새 대화 단계 추가하기

1. `ConversationStep` enum에 새 단계 추가
2. `handler/`에 새 `StepXxxHandler` 클래스 생성
3. `StepHandler` 인터페이스 구현
4. 테스트 작성

### 포트폴리오 필드 추가하기

1. `model/`에 새 모델 클래스 생성
2. `Portfolio.java`에 필드 추가
3. 해당 핸들러에서 데이터 추출 로직 추가

---

## 11. 디버깅 팁

### 로그 확인
```bash
# 애플리케이션 로그
./gradlew bootRun  # 콘솔에 로그 출력

# 로그 레벨 변경 (.env)
LOG_LEVEL=DEBUG
```

### API 테스트 (curl)
```bash
# 세션 생성
curl -X POST http://localhost:8080/api/v1/sessions | jq

# 메시지 전송
curl -X POST http://localhost:8080/api/v1/sessions/{id}/messages \
  -H "Content-Type: application/json" \
  -d '{"message": "백엔드 개발자입니다"}' | jq
```

### IntelliJ에서 디버깅
1. `FolioApplication.java` 우클릭 → Debug
2. 원하는 라인에 브레이크포인트 설정
3. 요청 보내면 해당 라인에서 멈춤

---

## 12. 용어 정리

| 용어 | 설명 |
|------|------|
| Spring Boot | 자바 웹 애플리케이션 프레임워크 |
| Gradle | 빌드 및 의존성 관리 도구 (Maven 대안) |
| Bean | 스프링이 관리하는 객체 |
| DI (Dependency Injection) | 의존성 주입, 스프링이 객체를 자동 연결 |
| DTO | 데이터 전송 객체, API 요청/응답용 |
| Mock | 테스트용 가짜 객체 |
| JUnit | 자바 테스트 프레임워크 |
| Mockito | Mock 객체 생성 라이브러리 |
| JaCoCo | 테스트 커버리지 측정 도구 |
