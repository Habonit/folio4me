# folio4me

> AI 기반 포트폴리오 자동 생성 서비스

## 소개

대화형 인터페이스를 통해 사용자의 경력, 프로젝트, 기술 역량을 수집하고 맞춤형 포트폴리오를 자동 생성합니다. 12단계 대화 흐름으로 체계적인 정보 수집을 지원합니다.

| 항목 | 내용 |
|:-----|:-----|
| 기술 스택 | Java 17, Spring Boot 3.x, Gradle |
| 현재 버전 | v0.1.0 |

---

## 시작하기

### 사전 요구사항

- Java >= 17
- Gradle >= 8.x

### 실행

```bash
# 환경 설정
cp .env.example .env

# 빌드 및 실행
./gradlew build
./gradlew bootRun

# 테스트
./gradlew test
```

### 환경변수

| 변수명 | 설명 | 필수 |
|:-------|:-----|:----:|
| `ANTHROPIC_API_KEY` | Claude API 키 | ✅ |
| `DATA_DIR` | 데이터 저장 경로 | |

---

## 변경 이력

### v0.1.0 (2025-12-24)
- 12단계 대화 흐름 구현
- 세션 관리 API
- 포트폴리오 데이터 모델

[전체 변경 이력](docs/version_change_log.md)

---

## 문서

- [API 명세](docs/api_spec.md)
- [에러 명세](docs/error_spec.md)
- [대화 흐름 명세](docs/project/conversation-flow-spec.md)
- [개발 가이드](docs/core/README.md)

---

# 프로젝트 템플릿 버전 관리

## initial_docs/{이니셜}

- 최초 프로젝트 템플릿을 작성하기 전까지의 기초 세팅 설정
- 초기 문서들에 대해선 모두 initial_docs/{이니셜} 브랜치로 따서 작성

## version/0.*

- initial_docs/*에서 작성된 것들을 ai 기반으로 합친 버전입니다.
