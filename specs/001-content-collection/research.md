# Research: Backend Phase 1 - Content Collection

**Branch**: `001-content-collection` | **Date**: 2025-12-23
**Input**: plan.md, spec.md, portfolio-data-schema.md, conversation-flow-spec.md

## 1. Spring Boot 3.x + Java 17 프로젝트 설정

### 1.1 Gradle Kotlin DSL 기본 설정

```kotlin
// build.gradle.kts
plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.folio4me"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot 핵심
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // JSON 처리
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
```

### 1.2 의존성 목록

| 의존성 | 용도 | 버전 |
|--------|------|------|
| spring-boot-starter-web | REST API | 3.2.x |
| spring-boot-starter-validation | 입력 검증 | 3.2.x |
| jackson-databind | JSON 직렬화/역직렬화 | 2.15.x |
| jackson-datatype-jsr310 | LocalDateTime 지원 | 2.15.x |
| spring-boot-starter-test | 테스트 프레임워크 | 3.2.x |

### 1.3 AI SDK 선택

**옵션 비교:**

| SDK | 장점 | 단점 |
|-----|------|------|
| OpenAI Java SDK | 성숙한 API, GPT-4 지원 | 비용, 한국어 품질 |
| Anthropic Java SDK | Claude 3.5, 한국어 우수 | 상대적으로 신규 |
| Spring AI | Spring 통합, 추상화 | 아직 1.0 미만 |

**결론**: Anthropic Java SDK 권장 (Claude 3.5 Sonnet, 한국어 대화 품질 우수)

---

## 2. 파일 기반 JSON 저장소 패턴

### 2.1 디렉토리 구조

```
data/
├── 550e8400-e29b-41d4-a716-446655440000/
│   └── portfolio.json
├── 6ba7b810-9dad-11d1-80b4-00c04fd430c8/
│   └── portfolio.json
└── ...
```

### 2.2 동시성 처리 전략

**문제**: 동일 세션에 대한 동시 요청 시 파일 충돌

**해결책**: 세션별 `ReentrantLock` 사용

```java
/**
 * 세션별 파일 잠금을 관리하는 서비스.
 * 동일 세션에 대한 동시 쓰기 요청을 순차 처리합니다.
 */
public class StorageService {
    private final ConcurrentHashMap<UUID, ReentrantLock> sessionLocks = new ConcurrentHashMap<>();

    /**
     * 세션별 잠금을 획득하고 파일에 포트폴리오 데이터를 저장합니다.
     *
     * @param sessionId 세션 UUID
     * @param portfolio 저장할 포트폴리오 데이터
     */
    public void save(UUID sessionId, Portfolio portfolio) {
        ReentrantLock lock = sessionLocks.computeIfAbsent(sessionId, k -> new ReentrantLock());
        lock.lock();
        try {
            // 파일 쓰기 로직
        } finally {
            lock.unlock();
        }
    }
}
```

### 2.3 파일 I/O 패턴

```java
/**
 * JSON 파일 읽기/쓰기를 담당하는 유틸리티.
 */
public class JsonFileUtil {
    private final ObjectMapper objectMapper;

    /**
     * 포트폴리오를 JSON 파일로 저장합니다.
     *
     * @param path 저장 경로
     * @param portfolio 포트폴리오 객체
     * @throws IOException 파일 쓰기 실패 시
     */
    public void writePortfolio(Path path, Portfolio portfolio) throws IOException {
        Files.createDirectories(path.getParent());
        objectMapper.writerWithDefaultPrettyPrinter()
            .writeValue(path.toFile(), portfolio);
    }

    /**
     * JSON 파일에서 포트폴리오를 읽어옵니다.
     *
     * @param path 파일 경로
     * @return 포트폴리오 객체
     * @throws IOException 파일 읽기 실패 시
     */
    public Portfolio readPortfolio(Path path) throws IOException {
        return objectMapper.readValue(path.toFile(), Portfolio.class);
    }
}
```

---

## 3. Step 기반 상태 머신 구현

### 3.1 Step Enum 정의

```java
/**
 * 포트폴리오 콘텐츠 수집 대화의 12단계를 정의합니다.
 */
public enum ConversationStep {
    STEP_0_GATE("희망 직무 확인"),
    STEP_1_PERSONAL_INFO("기본 개인정보"),
    STEP_2_WORK_EXPERIENCE("이력"),
    STEP_3_EDUCATION("학력"),
    STEP_4_REPRESENTATIVE_PROJECTS("대표 프로젝트"),
    STEP_5_PROJECTS("일반 프로젝트"),
    STEP_6_AWARDS("수상 경력"),
    STEP_7_MAJOR_ACTIVITIES("주요 대외활동"),
    STEP_8_OTHER_ACTIVITIES("그 외 대외활동"),
    STEP_9_CERTIFICATIONS("자격증"),
    STEP_10_TECHNICAL_SKILLS("기술 역량"),
    STEP_11_ABOUT("자기소개");

    private final String displayName;

    ConversationStep(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ConversationStep next() {
        int nextOrdinal = this.ordinal() + 1;
        if (nextOrdinal >= values().length) {
            return null; // 완료
        }
        return values()[nextOrdinal];
    }
}
```

### 3.2 Step별 필수 필드 정의

| Step | 섹션 | 필수 필드 | 선택 필드 |
|------|------|----------|----------|
| 0 | meta | targetRole | - |
| 1 | personalInfo | name, github, email | profileImage |
| 2 | workExperience | company, period, responsibility, position | links |
| 3 | education | school, degree, major, period, display | - |
| 4 | representativeProjects | name, period, responsibility, techStack | links |
| 5 | projects | name, period, responsibility, techStack | links |
| 6 | awards | title, period, content, organization | links |
| 7 | activities.major | title, period, content | links |
| 8 | activities.minor | title, period, content | links |
| 9 | certifications | title, period, organization | links |
| 10 | technicalSkills | strong, knowledgeable | - |
| 11 | about | sentences (3개) | - |

### 3.3 상태 전이 로직

```java
/**
 * 대화 상태 전이를 관리하는 서비스.
 */
public class ConversationStateManager {

    /**
     * 현재 Step의 완성 여부를 확인하고 다음 Step으로 전이합니다.
     *
     * @param session 현재 세션
     * @param portfolio 포트폴리오 데이터
     * @return 전이 후 Step (null이면 완료)
     */
    public ConversationStep tryAdvance(Session session, Portfolio portfolio) {
        ConversationStep current = session.getCurrentStep();

        if (!isStepComplete(current, portfolio)) {
            return current; // 현재 Step 유지
        }

        ConversationStep next = current.next();
        if (next != null) {
            session.setCurrentStep(next);
        }
        return next;
    }

    /**
     * 특정 Step의 완성 여부를 확인합니다.
     *
     * @param step 확인할 Step
     * @param portfolio 포트폴리오 데이터
     * @return 완성 여부
     */
    private boolean isStepComplete(ConversationStep step, Portfolio portfolio) {
        return switch (step) {
            case STEP_0_GATE -> portfolio.getMeta().getTargetRole() != null;
            case STEP_1_PERSONAL_INFO -> portfolio.getPersonalInfo().isConfirmed();
            case STEP_2_WORK_EXPERIENCE -> true; // 0개 허용
            // ... 각 Step별 완성 조건
            case STEP_11_ABOUT -> portfolio.getAbout().isConfirmed();
        };
    }
}
```

---

## 4. 입력 검증 패턴

### 4.1 기간 형식 검증

```java
/**
 * 기간 형식(YYYYMMDD~YYYYMMDD 또는 YYYYMMDD~present)을 검증합니다.
 */
public class PeriodValidator implements ConstraintValidator<ValidPeriod, String> {

    private static final Pattern PERIOD_PATTERN =
        Pattern.compile("^\\d{8}~(\\d{8}|present)$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return PERIOD_PATTERN.matcher(value).matches();
    }
}
```

### 4.2 주제 이탈 감지

```java
/**
 * 사용자 입력이 현재 Step과 관련 있는지 판단합니다.
 */
public class TopicRelevanceChecker {

    private final AiService aiService;

    /**
     * 사용자 입력의 주제 관련성을 확인합니다.
     *
     * @param userInput 사용자 입력
     * @param currentStep 현재 대화 Step
     * @return 관련성 여부
     */
    public boolean isRelevant(String userInput, ConversationStep currentStep) {
        // AI를 통한 주제 관련성 판단
        String prompt = String.format(
            "현재 '%s' 정보를 수집 중입니다. 다음 사용자 입력이 관련 있습니까? " +
            "입력: '%s'. 관련 있으면 true, 없으면 false만 응답하세요.",
            currentStep.getDisplayName(), userInput
        );

        return aiService.checkRelevance(prompt);
    }
}
```

---

## 5. API 엔드포인트 설계 (예비)

### 5.1 세션 관리

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | /api/sessions | 새 세션 생성, UUID 반환 |
| GET | /api/sessions/{id} | 세션 상태 조회 |
| DELETE | /api/sessions/{id} | 세션 삭제 |

### 5.2 대화 처리

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | /api/sessions/{id}/messages | 사용자 메시지 전송, AI 응답 반환 |
| GET | /api/sessions/{id}/portfolio | 현재 포트폴리오 데이터 조회 |

### 5.3 이미지 업로드

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | /api/sessions/{id}/images | 증명사진 업로드 |

---

## 6. 미해결 사항 (Phase 1에서 결정)

1. **AI 프롬프트 템플릿 구조**: Step별 시스템 프롬프트 관리 방식
2. **이미지 저장 경로**: `data/{uuid}/images/` vs 별도 스토리지
3. **세션 만료 정책**: TTL 설정, 만료된 세션 정리 방식
4. **에러 코드 체계**: 도메인별 에러 코드 정의

---

## 7. 결론

### 7.1 기술 스택 확정

- **언어**: Java 17
- **프레임워크**: Spring Boot 3.2.x
- **빌드 도구**: Gradle (Kotlin DSL)
- **테스트**: JUnit 5 + Mockito
- **JSON**: Jackson
- **AI**: Anthropic Claude API (추후 구체화)

### 7.2 주요 설계 결정

1. **파일 기반 저장소**: DB 없이 JSON 파일로 단순화 (Constitution VI. Simplicity First)
2. **세션별 잠금**: `ReentrantLock`으로 동시성 처리
3. **Step Enum**: 12단계 대화 흐름을 타입 안전하게 관리
4. **confirmed 필드**: 각 섹션의 완성 상태 추적

### 7.3 Phase 1 진행 준비 완료

- Technical Context 확정
- 핵심 패턴 조사 완료
- API 설계 초안 작성
- 미해결 사항 명확화
