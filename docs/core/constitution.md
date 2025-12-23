"반드시 해당 constitution을 사용할 때 <<>> 이라고 된 부분을 교체해야 합니다"

# Project Constitution
<< 예시 프로젝트 >>의 개발 규칙과 컨벤션을 정의합니다.

## 1. Git Branch Convention

### 브랜치 전략
```
main ← dev ← feature/~~
```

### 브랜치 네이밍

| 용도 | 형식 | 예시 |
|:-----|:-----|:-----|
| 기능 개발 / 버그 수정 | `feature/{YYYYMMDD}_{JIRA-ID}_{기능정의서ID}_{작업자}` | `feature/20250115_PROJ-123_FD-001_honggildong` |
| 배포 준비 | `prep/{YYYYMMDD}_{작업자}` | `prep/20250115_honggildong` |

### 브랜치 생성 규칙
- **분기 기준**: 항상 `dev` 브랜치에서 분기
- **YYYYMMDD**: 브랜치 생성 날짜 (예: 20250115)
- **JIRA-ID**: JIRA 스크럼 ID (예: PROJ-123). 연결된 JIRA가 없으면 `NONE`
- **기능정의서ID**: 기능 정의서 ID (예: FD-001). 연결된 정의서가 없으면 `NONE`
- **작업자**: `git config user.name`에 설정한 이름 (공백 없이)
- **구분자**: 영역 내 `-` (하이픈), 영역 간 `_` (언더스코어)
- **예외 예시**: `feature/20250115_NONE_NONE_honggildong` (JIRA/정의서 미연결 시)

---

## 2. Git Commit Convention

### 커밋 메시지 형식
```
<type>(<<브랜치 식별자>>): <subject>

<body>

<footer>
```

### Type
- `feat`: 새로운 기능 추가
- `fix`: 버그 수정
- `docs`: 문서 수정
- `style`: 코드 포맷팅, 세미콜론 누락 등 (코드 변경 없음)
- `refactor`: 코드 리팩토링
- `test`: 테스트 코드 추가/수정
- `chore`: 빌드, 설정 파일 수정

### 예시
```
feat(<<브랜치 식별자>>): 히트맵 시각화 기능 추가

- plotly heatmap 차트 생성 함수 구현
- 숫자형 컬럼 간 상관관계 시각화
```

---

## 3. Python Code Style

### 3.1 일반 규칙
- **Python 버전**: 3.10+
- **타입 힌트**: 모든 함수에 타입 힌트 사용 (예: `def func(arg: str) -> int:`)
- **Docstring**: Google 스타일 docstring 사용
- **라인 길이**: 최대 100자
- **들여쓰기**: 스페이스 4칸

### 3.2 임포트 순서
```python
# 1. 표준 라이브러리
import os
from math import sqrt

# 2. 서드파티 라이브러리
import pandas as pd
import streamlit as st

# 3. 로컬 모듈
from utils.loader import load_dataset
```

### 3.3 함수/변수 네이밍
- **함수명**: snake_case (예: `load_dataset`, `create_folium_map`)
- **변수명**: snake_case (예: `df_clean`, `lat_col`)
- **상수**: UPPER_SNAKE_CASE (예: `MAX_POINTS = 5000`)
- **클래스**: PascalCase (예: `DataLoader`)

### 3.4 Docstring 형식
```python
def function_name(param1: str, param2: int) -> dict:
    """
    함수에 대한 간략한 설명.

    Parameters:
        param1 (str): 첫 번째 파라미터 설명
        param2 (int): 두 번째 파라미터 설명

    Returns:
        dict: 반환값 설명

    Raises:
        ValueError: 에러 상황 설명
    """
```

## 4. Java Code Style

### 4.1 일반 규칙

- **Java 버전**: 17+
- **타입 명시**: 명확한 타입 선언 (var 사용 최소화)
- **Javadoc**: 모든 public 메서드에 Javadoc 작성
- **라인 길이**: 최대 120자
- **들여쓰기**: 스페이스 4칸

### 4.2 임포트 순서

```java
// 1. java.* 표준 라이브러리
import java.util.List;
import java.time.LocalDateTime;

// 2. javax.* 표준 확장
import javax.persistence.Entity;

// 3. 서드파티 라이브러리
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

// 4. 프로젝트 내부 모듈
import com.example.domain.User;
import com.example.repository.UserRepository;
```

### 4.3 클래스/변수 네이밍

- **클래스명**: PascalCase (예: `UserService`, `OrderController`)
- **메서드명**: camelCase (예: `findById`, `createOrder`)
- **변수명**: camelCase (예: `userName`, `orderList`)
- **상수**: UPPER_SNAKE_CASE (예: `MAX_RETRY_COUNT = 3`)
- **패키지명**: lowercase (예: `com.example.service`)

### 4.4 Javadoc 형식

```java
/**
 * 메서드에 대한 간략한 설명.
 *
 * @param param1 첫 번째 파라미터 설명
 * @param param2 두 번째 파라미터 설명
 * @return 반환값 설명
 * @throws IllegalArgumentException 에러 상황 설명
 */
public User findById(Long param1, String param2) {
    // ...
}
```

---

## 5. React/TypeScript Code Style

### 5.1 일반 규칙

- **TypeScript 버전**: 5.0+
- **타입 정의**: 모든 props, state에 타입 정의 필수
- **JSDoc**: 복잡한 컴포넌트 및 유틸 함수에 JSDoc 작성
- **라인 길이**: 최대 100자
- **들여쓰기**: 스페이스 2칸

### 5.2 임포트 순서

```typescript
// 1. React 관련
import React, { useState, useEffect } from 'react';

// 2. 서드파티 라이브러리
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';

// 3. 내부 컴포넌트
import { Button } from '@/components/ui/Button';
import { UserCard } from '@/components/UserCard';

// 4. 훅, 유틸, 타입
import { useAuth } from '@/hooks/useAuth';
import { formatDate } from '@/utils/date';
import type { User } from '@/types/user';

// 5. 스타일
import styles from './UserList.module.css';
```

### 5.3 컴포넌트/변수 네이밍

- **컴포넌트명**: PascalCase (예: `UserCard`, `LoginForm`)
- **함수/변수명**: camelCase (예: `handleClick`, `userName`)
- **상수**: UPPER_SNAKE_CASE (예: `MAX_ITEMS = 10`)
- **커스텀 훅**: use 접두사 (예: `useAuth`, `useFetch`)
- **타입/인터페이스**: PascalCase (예: `UserProps`, `ApiResponse`)

### 5.4 컴포넌트 및 타입 형식

```typescript
interface UserCardProps {
  /** 사용자 ID */
  userId: string;
  /** 사용자 이름 */
  userName: string;
  /** 클릭 이벤트 핸들러 */
  onClick?: (id: string) => void;
}

/**
 * 사용자 정보를 카드 형태로 표시하는 컴포넌트.
 *
 * @param props - 컴포넌트 props
 * @returns 사용자 카드 컴포넌트
 */
const UserCard: React.FC<UserCardProps> = ({ userId, userName, onClick }) => {
  // ...
};

export default UserCard;
```

---

## 6. Dependencies


### 6.1 Python
- **패키지 관리**: UV (pip 사용 지양)
- **의존성 파일**: `pyproject.toml`

### 6.2 Java
- **빌드 도구**: Gradle (또는 Maven)
- **의존성 파일**: `build.gradle` 또는 `pom.xml`

### 6.3 TypeScript
- **패키지 관리**: npm 또는 yarn
- **의존성 파일**: `package.json`

---

## 7. Development Philosophy

1. **단순성 우선**: 복잡한 로직보다 이해하기 쉬운 코드
2. **과한 최적화 지양**: 교육적 가치 > 성능 최적화
3. **명확한 구조**: 코드 구조는 일관되고 예측 가능해야 함

---

## 8. Documentation & Comments

### 8.1 언어 규칙
- **기본 언어**: 한글로 작성
- **영어 사용 허용**: 한글로 전달이 어려운 기술 용어 (예: DataFrame, API, cache 등)
- **불필요한 영어 지양**: 영어로 쓸 이유가 없으면 한글로 작성

### 8.2 주석 작성(python)
```python
# 좋은 예
# 좌표 결측값이 있는 행 제거
df_clean = df.dropna(subset=[lat_col, lng_col])

# 나쁜 예
# Drop rows with missing coordinates  ← 불필요한 영어
df_clean = df.dropna(subset=[lat_col, lng_col])
```

### 8.3 주석 작성(java)

```java
// 좋은 예
// 사용자 ID로 조회 후 없으면 예외 발생
User user = userRepository.findById(id)
    .orElseThrow(() -> new UserNotFoundException(id));

// 나쁜 예
// Find user by ID and throw exception if not found ← 불필요한 영어
User user = userRepository.findById(id)
    .orElseThrow(() -> new UserNotFoundException(id));
```

### 8.4 주석 작성(typescript/react)

```typescript
// 좋은 예
// 로그인 상태 확인 후 미인증 시 로그인 페이지로 리다이렉트
const { isAuthenticated } = useAuth();
if (!isAuthenticated) {
  router.push('/login');
}

// 나쁜 예
// Check authentication status and redirect to login page if not authenticated ← 불필요한 영어
const { isAuthenticated } = useAuth();
if (!isAuthenticated) {
  router.push('/login');
}
```


### 8.5 Docstring 작성 (python)
- 함수 설명: 한글로 간결하게
- Parameters/Returns: 타입은 영어, 설명은 한글
- 기술 용어(DataFrame, str, dict 등): 원문 유지

```python
def load_dataset(dataset_name: str) -> pd.DataFrame:
    """
    이름으로 사전 정의된 데이터셋 로드 (캐싱 적용).

    Parameters:
        dataset_name (str): 데이터셋 이름

    Returns:
        pd.DataFrame: 캐시된 데이터셋
    """
```

### 8.6 Docstring 작성 (java)

- 메서드 설명: 한글로 간결하게
- @param/@return/@throws: 타입은 영어, 설명은 한글
- 기술 용어(List, Optional, Entity 등): 원문 유지
```java
/**
 * 사용자 ID로 사용자 정보 조회.
 *
 * @param userId 조회할 사용자 ID
 * @return 조회된 사용자 Entity
 * @throws UserNotFoundException 사용자가 존재하지 않을 경우
 */
public User findById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
}
```
```java
// 좋은 예
/**
 * 주문 목록을 최신순으로 정렬하여 반환.
 *
 * @param customerId 고객 ID
 * @param limit 조회할 최대 개수
 * @return 정렬된 주문 List
 */
public List getRecentOrders(Long customerId, int limit) { ... }

// 나쁜 예
/**
 * Returns a list of orders sorted by date in descending order. ← 불필요한 영어
 *
 * @param customerId the customer ID ← 불필요한 영어
 * @param limit maximum number of orders to retrieve ← 불필요한 영어
 * @return sorted list of orders ← 불필요한 영어
 */
public List getRecentOrders(Long customerId, int limit) { ... }
```

### 8.7 Docstring 작성 (typescript)

- 컴포넌트/함수 설명: 한글로 간결하게
- @param/@returns: 타입은 영어, 설명은 한글
- 기술 용어(React.FC, Promise, void 등): 원문 유지
```typescript
/**
 * 날짜를 지정된 형식의 문자열로 변환.
 *
 * @param date 변환할 Date 객체
 * @param format 출력 형식 (기본값: 'YYYY-MM-DD')
 * @returns 형식화된 날짜 문자열
 */
const formatDate = (date: Date, format?: string): string => {
  // ...
};
```
```typescript
// 좋은 예
interface UserCardProps {
  /** 사용자 고유 ID */
  userId: string;
  /** 화면에 표시할 사용자 이름 */
  userName: string;
  /** 카드 클릭 시 호출되는 콜백 함수 */
  onClick?: (id: string) => void;
}

/**
 * 사용자 정보를 카드 형태로 표시하는 컴포넌트.
 *
 * @param props 컴포넌트 props
 * @returns 사용자 카드 JSX Element
 */
const UserCard: React.FC = ({ userId, userName, onClick }) => {
  // ...
};

// 나쁜 예
interface UserCardProps {
  /** Unique identifier for the user ← 불필요한 영어 */
  userId: string;
  /** Display name of the user ← 불필요한 영어 */
  userName: string;
  /** Callback function when card is clicked ← 불필요한 영어 */
  onClick?: (id: string) => void;
}

/**
 * A component that displays user information in a card format. ← 불필요한 영어
 *
 * @param props - Component props ← 불필요한 영어
 * @returns User card JSX Element ← 불필요한 영어
 */
const UserCard: React.FC = ({ userId, userName, onClick }) => {
  // ...
};
```

### 8.8 마크다운 문서
- 제목, 설명, 내용: 한글
- 코드 예시: 원본 유지
- 기술 용어 및 명령어: 원문 (예: `streamlit run`, `git commit`)

---

## 9. TDD (Test-Driven Development) 방법론 - python
자세한 TDD는 반드시 docs/core/tdd_constitution_python.md를 참조합니다.

### 9.1 개발 프로세스
1. **제안서 작성**: `docs/sdd/{브랜치 식별자}.md` 작성
	- `docs/core/improvement_proposal_template.md` 참고하여 작성
2. **테스트 코드 작성**: 구현 전에 테스트 코드를 먼저 작성
3. **구현**: 테스트를 통과하도록 기능 구현
4. **검증**: 모든 테스트 통과 확인

### 9.2 테스트 코드 규칙
- **테스트 위치**: `tests/` 디렉토리
- **테스트 파일명**: `test_<모듈명>.py`
- **테스트 함수명**: `test_<기능명>_<상황>` (예: `test_preprocess_datetime_valid_input`)
- **테스트 프레임워크**: pytest

### 9.3 테스트 작성 순서
```python
# 1. 정상 케이스 (Happy Path)
def test_preprocess_datetime_valid_input():
    ...

# 2. 경계 케이스 (Edge Case)
def test_preprocess_datetime_midnight():
    ...

# 3. 예외 케이스 (Error Case)
def test_preprocess_datetime_missing_column():
    ...
```

### 9.4 테스트 실행
```bash
# 전체 테스트 실행
pytest tests/

# 특정 테스트 파일 실행
pytest tests/test_preprocessing.py

# 커버리지 포함 실행
pytest tests/ --cov=utils
```

### 9.5 TDD 적용 대상
- `docs/sdd/{브랜치 식별자}.md`에 정의된 모든 신규 기능

---

## 10. TDD (Test-Driven Development) 방법론 - java
자세한 TDD는 반드시 docs/core/tdd_constitution_java.md를 참조합니다.

### 10.1 개발 프로세스

1. **제안서 작성**: `docs/sdd/{브랜치 식별자}.md` 작성
   - `docs/core/improvement_proposal_template.md` 참고하여 작성
2. **테스트 코드 작성**: 구현 전에 테스트 코드를 먼저 작성
3. **구현**: 테스트를 통과하도록 기능 구현
4. **검증**: 모든 테스트 통과 확인

### 10.2 테스트 코드 규칙
- **테스트 위치**: `src/test/java/` 디렉토리
- **테스트 파일명**: `<클래스명>Test.java` (예: `PreprocessingServiceTest.java`)
- **테스트 메서드명**: `<기능명>_<상황>` (예: `preprocessDatetime_ValidInput`)
- **테스트 프레임워크**: JUnit 5 + Mockito + AssertJ

### 10.3 테스트 작성 순서

```java
// 1. 정상 케이스 (Happy Path)
@Test
@DisplayName("유효한 입력 시 정상 처리")
void preprocessDatetime_ValidInput() { ... }

// 2. 경계 케이스 (Edge Case)
@Test
@DisplayName("자정 시간 처리")
void preprocessDatetime_Midnight() { ... }

// 3. 예외 케이스 (Error Case)
@Test
@DisplayName("컬럼 누락 시 예외 발생")
void preprocessDatetime_MissingColumn() { ... }
```

### 10.4 테스트 실행

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "PreprocessingServiceTest"

# 커버리지 포함 실행
./gradlew test jacocoTestReport
```

### 10.5 TDD 적용 대상

- `docs/sdd/{브랜치 식별자}.md`에 정의된 모든 신규 기능

---

## 11. TDD (Test-Driven Development) 방법론 - Typescript/React

자세한 TDD는 반드시 docs/core/tdd_constitution_typescript.md를 참조합니다.

### 11.1 개발 프로세스

1. **제안서 작성**: `docs/sdd/{브랜치 식별자}.md` 작성
   - `docs/core/improvement_proposal_template.md` 참고하여 작성
2. **테스트 코드 작성**: 구현 전에 테스트 코드를 먼저 작성
3. **구현**: 테스트를 통과하도록 기능 구현
4. **검증**: 모든 테스트 통과 확인

### 11.2 테스트 코드 규칙

- **테스트 위치**: `src/__tests__/` 또는 컴포넌트 동일 디렉토리
- **테스트 파일명**: `<컴포넌트명>.test.tsx` (예: `DatetimeInput.test.tsx`)
- **테스트 블록명**: `describe` + `test` 조합 (예: `test('유효한 입력 시 정상 처리')`)
- **테스트 프레임워크**: Jest + React Testing Library

### 11.3 테스트 작성 순서

```typescript
// 1. 정상 케이스 (Happy Path)
test('유효한 입력 시 onSubmit 호출', async () => { ... });

// 2. 경계 케이스 (Edge Case)
test('자정 시간 입력 시 정상 처리', async () => { ... });

// 3. 예외 케이스 (Error Case)
test('필수 필드 누락 시 에러 메시지 표시', async () => { ... });
```

### 11.4 테스트 실행

```bash
# 전체 테스트 실행
npm test

# 특정 테스트 파일 실행
npm test -- DatetimeInput.test.tsx

# 커버리지 포함 실행
npm test -- --coverage
```

### 11.5 TDD 적용 대상

- `docs/sdd/{브랜치 식별자}.md`에 정의된 모든 신규 기능

---

## 12. Error Handling

### 12.1 예외 처리 원칙
- 사용자에게 친절한 한글 에러 메시지 제공
- 내부 에러 로깅과 사용자 메시지 분리
- 복구 가능한 에러는 Graceful Degradation 적용

### 12.2 Graceful Degradation (우아한 성능 저하)
시스템의 일부 기능이 실패하더라도 전체 시스템이 중단되지 않고,
가능한 범위 내에서 서비스를 계속 제공하는 설계 원칙.

**예시:**
- ML 모델 로딩 실패 → ECLO 예측 기능만 비활성화, 나머지 앱은 정상 동작
- API Key 미입력 → 챗봇 탭에서 안내 메시지 표시, 다른 탭은 정상 동작
- 좌표 컬럼 미감지 → 지도 시각화만 비활성화, 통계/차트는 정상 표시

### 12.3 예외 처리 패턴
#### 12.3.1 python
- try-except 블록에서 구체적인 예외 타입 명시
- **bare except 사용 금지**

**bare except란?**
예외 타입을 명시하지 않고 모든 예외를 포괄적으로 잡는 패턴.
디버깅을 어렵게 하고 예상치 못한 에러를 숨길 수 있어 금지.

```python
# 나쁜 예 (bare except)
try:
    result = process_data(df)
except:  # ← 어떤 에러인지 알 수 없음
    pass

# 좋은 예 (구체적 예외 타입 명시)
try:
    result = process_data(df)
except ValueError as e:
    st.error(f"데이터 형식 오류: {e}")
except KeyError as e:
    st.error(f"필수 컬럼 누락: {e}")
except Exception as e:
    st.error(f"예상치 못한 오류: {e}")
    logging.exception("process_data 실패")
```

#### 12.3.2 java

- try-catch 블록에서 구체적인 예외 타입 명시
- **포괄적 Exception catch만 사용 금지**

**포괄적 Exception catch란?**
예외 타입을 `Exception`으로만 잡고 구체적인 처리 없이 무시하는 패턴. 디버깅을 어렵게 하고 예상치 못한 에러를 숨길 수 있어 금지.
```java
// 나쁜 예 (포괄적 Exception catch)
try {
    User user = userService.findById(id);
} catch (Exception e) {  // ← 어떤 에러인지 알 수 없음
    // 아무 처리 없음
}

// 나쁜 예 (예외 무시)
try {
    User user = userService.findById(id);
} catch (Exception e) {
    e.printStackTrace();  // ← 로그만 찍고 적절한 처리 없음
}

// 좋은 예 (구체적 예외 타입 명시)
try {
    User user = userService.findById(id);
} catch (UserNotFoundException e) {
    log.warn("사용자 조회 실패: {}", e.getMessage());
    throw new BusinessException("존재하지 않는 사용자입니다", e);
} catch (DatabaseConnectionException e) {
    log.error("DB 연결 오류: {}", e.getMessage());
    throw new ServiceUnavailableException("일시적인 오류가 발생했습니다", e);
} catch (Exception e) {
    log.error("예상치 못한 오류 발생", e);
    throw new InternalServerException("서버 오류가 발생했습니다", e);
}

// 좋은 예 (multi-catch 활용 - Java 7+)
try {
    Order order = orderService.createOrder(request);
} catch (InvalidRequestException | ValidationException e) {
    log.warn("요청 검증 실패: {}", e.getMessage());
    throw new BadRequestException(e.getMessage(), e);
}
```

#### 12.3.3 typescript/react

- try-catch 블록에서 에러 타입 검증 후 구체적 처리
- **unknown 타입 사용 및 타입 가드 필수**

**무분별한 any catch란?**
에러를 `any` 타입으로 받거나 타입 검증 없이 처리하는 패턴. 타입 안전성을 해치고 예상치 못한 에러를 숨길 수 있어 금지.
```typescript
// 나쁜 예 (타입 검증 없는 catch)
try {
  const user = await fetchUser(id);
} catch (e) {  // ← 암묵적 any, 타입 불명확
  console.log(e);
}

// 나쁜 예 (any 타입 사용)
try {
  const user = await fetchUser(id);
} catch (e: any) {  // ← any 타입 사용 지양
  alert(e.message);  // ← e.message 존재 보장 없음
}

// 좋은 예 (unknown + 타입 가드)
try {
  const user = await fetchUser(id);
} catch (e: unknown) {
  if (e instanceof NotFoundError) {
    toast.error(`사용자를 찾을 수 없습니다: ${e.message}`);
    router.push('/404');
  } else if (e instanceof NetworkError) {
    toast.error('네트워크 연결을 확인해주세요');
  } else if (e instanceof Error) {
    toast.error(`예상치 못한 오류: ${e.message}`);
    console.error('fetchUser 실패', e);
  } else {
    toast.error('알 수 없는 오류가 발생했습니다');
    console.error('fetchUser 실패', e);
  }
}

// 좋은 예 (커스텀 에러 클래스 활용)
class ApiError extends Error {
  constructor(
    message: string,
    public statusCode: number
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

try {
  const response = await api.get('/users');
} catch (e: unknown) {
  if (e instanceof ApiError && e.statusCode === 401) {
    toast.error('로그인이 필요합니다');
    router.push('/login');
  } else if (e instanceof ApiError && e.statusCode === 403) {
    toast.error('접근 권한이 없습니다');
  } else {
    toast.error('서버 오류가 발생했습니다');
  }
}
```


### 12.4 에러 명세서
상세 에러 케이스는 `docs/error_spec.md` 참조

---

## 13. Environment & Configuration

### 13.1 환경 변수
- API Key 등 민감 정보: `.env` 파일에 보관
- `.env` 파일은 `.gitignore`에 포함하여 버전 관리에서 제외
- 환경변수를 소스 파일에 하드코딩 금지

---

## 14. Code Review Checklist

- [ ] 타입 힌트 적용 여부
- [ ] Docstring 작성 여부
- [ ] 테스트 코드 존재 여부
- [ ] 한글 주석/문서 규칙 준수
- [ ] 구체적 예외 타입 명시 (bare except 금지)

---