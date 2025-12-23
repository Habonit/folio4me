# 테스트 원칙 및 방법론 - React/TypeScript

## 개요

이 문서는 **TDD(Test-Driven Development)**를 React/TypeScript 환경에서 실천하기 위한 완전한 방법론을 제공한다.

### 이 문서를 숙지하면 할 수 있는 것

| 역량 | 설명 | 관련 섹션 |
|:-----|:-----|:-----|
| **Red-Green-Refactor 사이클 실천** | 실패하는 테스트 작성 → 통과하는 코드 구현 → 리팩토링의 반복 | Part 1, Section 1 |
| **효과적인 테스트 케이스 설계** | 경계값 분석, 동치 분할로 누락 없는 테스트 케이스 도출 | Part 1, Section 3 |
| **테스트 더블 활용** | jest.mock, MSW를 상황에 맞게 사용 | Part 1, Section 4 |
| **단위 테스트 작성** | AAA 패턴, React Testing Library 활용 | Part 2 |
| **통합 테스트 작성** | 컴포넌트 간 흐름 검증, MSW 활용 | Part 3 |
| **E2E 테스트 설계** | Playwright/Cypress로 사용자 시나리오 검증 | Part 4 |

### Red → Green → Refactor 달성 경로

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          TDD 사이클 달성 경로                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   🔴 RED (실패하는 테스트 작성)                                               │
│   ├─ Part 1, Section 3: 테스트 케이스 설계 기법으로 "무엇을 테스트할지" 결정     │
│   ├─ Part 2: 단위 테스트 작성 규칙으로 "어떻게 테스트할지" 결정                 │
│   └─ Part 3, 4: 통합/E2E 테스트로 "전체 흐름을 어떻게 검증할지" 결정            │
│                                                                             │
│                              ▼                                              │
│                                                                             │
│   🟢 GREEN (테스트를 통과하는 최소한의 코드 작성)                               │
│   ├─ Part 1, Section 1.2: "최소한의 코드"란 테스트만 통과하는 코드              │
│   ├─ Part 1, Section 4: jest.mock으로 외부 의존성 격리하며 구현                │
│   └─ Part 2, Section 5.3: AAA 패턴으로 구현 결과 검증                         │
│                                                                             │
│                              ▼                                              │
│                                                                             │
│   🔵 REFACTOR (코드 개선, 테스트는 계속 통과)                                  │
│   ├─ Part 1, Section 2: beforeEach로 중복 제거                               │
│   ├─ Part 2, Section 7: 테스트 우선순위로 중요한 것부터 개선                    │
│   └─ 모든 Part: 테스트가 통과하면 안전하게 리팩토링 가능                        │
│                                                                             │
│                              ▼                                              │
│                         (다음 기능으로 반복)                                  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### TDD 숙련도 체크리스트

- [ ] Red-Green-Refactor 사이클을 설명하고 실천할 수 있다
- [ ] 테스트를 먼저 작성하고, 그 테스트를 통과하는 코드를 구현할 수 있다
- [ ] 경계값 분석과 동치 분할로 테스트 케이스를 설계할 수 있다
- [ ] jest.mock, MSW의 차이를 알고 적절히 사용할 수 있다
- [ ] AAA 패턴(Arrange-Act-Assert)으로 테스트를 구조화할 수 있다
- [ ] beforeEach, afterEach를 활용하여 테스트 간 중복을 제거할 수 있다
- [ ] 단위/통합/E2E 테스트의 차이를 알고 상황에 맞게 선택할 수 있다
- [ ] 테스트 피라미드 비율(Unit 70% : Integration 25% : E2E 5%)을 이해한다

---

## 목차

- [Part 1: TDD 방법론](#part-1-tdd-방법론)
- [Part 2: 단위 테스트 규칙](#part-2-단위-테스트-규칙)
- [Part 3: 통합 테스트 규칙](#part-3-통합-테스트-규칙)
- [Part 4: E2E 테스트 규칙](#part-4-e2e-테스트-규칙)

---

# Part 1: TDD 방법론

## 1. TDD란?

**TDD(Test-Driven Development, 테스트 주도 개발)**는 테스트를 먼저 작성하고, 그 테스트를 통과하는 코드를 작성하는 개발 방법론이다.

### 1.1 핵심 철학

> "동작하는 깔끔한 코드(Clean code that works)"를 목표로 한다.

- **테스트가 개발을 이끈다**: 코드를 작성하기 전에 "이 코드가 무엇을 해야 하는가?"를 테스트로 먼저 정의
- **작은 단위로 반복**: 한 번에 하나의 기능만 테스트하고 구현
- **리팩토링 안전망**: 테스트가 있으면 코드를 안전하게 개선할 수 있음

### 1.2 Red-Green-Refactor 사이클

TDD의 핵심은 **3단계 사이클**을 반복하는 것이다:

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│    ┌─────────┐      ┌─────────┐      ┌───────────┐         │
│    │   Red   │ ───► │  Green  │ ───► │ Refactor  │ ──┐     │
│    │ (실패)  │      │ (통과)  │      │  (개선)   │   │     │
│    └─────────┘      └─────────┘      └───────────┘   │     │
│         ▲                                            │     │
│         └────────────────────────────────────────────┘     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

| 단계 | 설명 | 예시 |
|:-----|:-----|:-----|
| **Red** | 실패하는 테스트 작성 | `test('add')` 작성 → 실행 → 실패 (함수가 없으니까) |
| **Green** | 테스트를 통과하는 최소한의 코드 작성 | `const add = (a, b) => a + b` 구현 → 테스트 통과 |
| **Refactor** | 코드 개선 (테스트는 계속 통과해야 함) | 중복 제거, 네이밍 개선, 구조 정리 |

### 1.3 TDD 예시

```typescript
// 1단계: Red - 실패하는 테스트 작성
test('10% 할인이 적용되어야 한다', () => {
  expect(calculateDiscount(1000, 10)).toBe(900);
});
// → 실행하면 실패 (calculateDiscount 함수가 없음)

// 2단계: Green - 테스트를 통과하는 최소한의 코드
const calculateDiscount = (price: number, percent: number): number => {
  return price - (price * percent) / 100;
};
// → 테스트 통과

// 3단계: Refactor - 코드 개선
const calculateDiscount = (price: number, percent: number): number => {
  if (percent < 0 || percent > 100) {
    throw new Error('할인율은 0~100 사이여야 합니다');
  }
  const discountAmount = (price * percent) / 100;
  return price - discountAmount;
};
// → 테스트 여전히 통과, 코드는 더 견고해짐
```

---

## 2. TDD 적용 시나리오

### 2.1 현실적인 TDD

| 상황 | 적용 방법 |
|:-----|:---------|
| **새 기능 추가** | 테스트 먼저 작성 → 기능 구현 → 리팩토링 |
| **버그 수정** | 버그를 재현하는 테스트 작성 → 수정 → 테스트 통과 확인 |
| **기존 코드** | 현재 동작을 검증하는 테스트 작성 (Characterization Test) |

### 2.2 테스트 격리 (Test Isolation)

각 테스트는 **다른 테스트에 영향을 주거나 받지 않아야** 한다.

```typescript
// Bad: 테스트 간 상태 공유
let counter = 0;

test('increment', () => {
  counter++;
  expect(counter).toBe(1);  // 통과
});

test('increment again', () => {
  counter++;
  expect(counter).toBe(1);  // 실패! (counter가 이미 1이므로 2가 됨)
});

// Good: 각 테스트가 독립적
describe('Counter', () => {
  let counter: number;

  beforeEach(() => {
    counter = 0;  // 매 테스트마다 초기화
  });

  test('increment', () => {
    counter++;
    expect(counter).toBe(1);
  });

  test('increment again', () => {
    counter++;
    expect(counter).toBe(1);  // 통과
  });
});
```

---

## 3. 테스트 케이스 설계 기법

### 3.1 경계값 분석 (Boundary Value Analysis)

```typescript
// 예: 나이가 0~120 사이여야 유효한 경우
const isValidAge = (age: number): boolean => {
  return age >= 0 && age <= 120;
};

// 경계값 테스트 케이스
describe('isValidAge 경계값 테스트', () => {
  // 경계 안쪽 (유효)
  test('최소 경계값 0은 유효', () => {
    expect(isValidAge(0)).toBe(true);
  });

  test('최대 경계값 120은 유효', () => {
    expect(isValidAge(120)).toBe(true);
  });

  // 경계 바깥 (무효)
  test('최소 경계-1인 -1은 무효', () => {
    expect(isValidAge(-1)).toBe(false);
  });

  test('최대 경계+1인 121은 무효', () => {
    expect(isValidAge(121)).toBe(false);
  });
});
```

### 3.2 동등 분할 (Equivalence Partitioning)

```typescript
// 예: 점수에 따른 등급 계산
const getGrade = (score: number): string => {
  if (score >= 90) return 'A';
  if (score >= 80) return 'B';
  if (score >= 70) return 'C';
  return 'F';
};

// 동등 분할 테스트
describe('getGrade 동등 분할 테스트', () => {
  test('90-100 파티션은 A등급', () => {
    expect(getGrade(95)).toBe('A');
  });

  test('80-89 파티션은 B등급', () => {
    expect(getGrade(85)).toBe('B');
  });

  test('70-79 파티션은 C등급', () => {
    expect(getGrade(75)).toBe('C');
  });

  test('0-69 파티션은 F등급', () => {
    expect(getGrade(50)).toBe('F');
  });
});
```

### 3.3 테스트 케이스 우선순위

| 우선순위 | 케이스 유형 | 이유 |
|:---------|:-----------|:-----|
| P0 | 정상 케이스 (Happy Path) | 기본 기능이 동작해야 함 |
| P1 | 경계값 케이스 | 버그가 자주 발생하는 지점 |
| P2 | 에러 케이스 | 예외 상황 처리 검증 |
| P3 | 극단적 케이스 | null/undefined 입력, 빈 배열 등 |

---

## 4. 테스트 더블 (Test Double)

### 4.1 테스트 더블이란?

실제 객체 대신 **테스트용 가짜 객체**를 사용하여 외부 의존성을 격리한다.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        테스트 더블의 개념                                 │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│   실제 시스템                           테스트 환경                       │
│   ┌─────────────┐                      ┌─────────────┐                  │
│   │   내 코드   │                      │   내 코드   │                  │
│   └──────┬──────┘                      └──────┬──────┘                  │
│          │ 호출                               │ 호출                     │
│          ▼                                    ▼                         │
│   ┌─────────────┐                      ┌─────────────┐                  │
│   │  실제 API   │  ─────────────►      │  MSW Mock   │  ← 테스트 더블   │
│   │  실제 서버  │      대체            │  jest.mock  │                  │
│   └─────────────┘                      └─────────────┘                  │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### 4.2 Jest를 활용한 테스트 더블

| 종류 | Jest 구현 | 사용 시점 |
|:-----|:---------|:---------|
| **Stub** | `jest.fn().mockReturnValue(...)` | 미리 정해진 값 반환 |
| **Mock** | `jest.fn()`, `expect().toHaveBeenCalled()` | 호출 여부/횟수 검증 |
| **Spy** | `jest.spyOn(...)` | 실제 동작 + 호출 기록 |

### 4.3 jest.fn() 예시

```typescript
// Stub: 미리 정해진 값 반환
const mockFetch = jest.fn().mockResolvedValue({
  json: () => Promise.resolve({ id: 1, name: '홍길동' }),
});

// Mock: 호출 여부 검증
const mockCallback = jest.fn();
someFunction(mockCallback);
expect(mockCallback).toHaveBeenCalledTimes(1);
expect(mockCallback).toHaveBeenCalledWith('expected argument');
```

### 4.4 jest.mock() 모듈 모킹

```typescript
// api.ts 모듈 전체 모킹
jest.mock('@/api/userApi', () => ({
  fetchUser: jest.fn(),
}));

import { fetchUser } from '@/api/userApi';

describe('UserService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('사용자 조회 성공', async () => {
    // Given
    (fetchUser as jest.Mock).mockResolvedValue({ id: 1, name: '홍길동' });

    // When
    const result = await userService.getUser(1);

    // Then
    expect(result.name).toBe('홍길동');
    expect(fetchUser).toHaveBeenCalledWith(1);
  });
});
```

### 4.5 MSW (Mock Service Worker) 예시

```typescript
// mocks/handlers.ts
import { http, HttpResponse } from 'msw';

export const handlers = [
  http.get('/api/users/:id', ({ params }) => {
    return HttpResponse.json({
      id: params.id,
      name: '홍길동',
    });
  }),

  http.post('/api/users', async ({ request }) => {
    const body = await request.json();
    return HttpResponse.json({ id: 1, ...body }, { status: 201 });
  }),
];

// setupTests.ts
import { setupServer } from 'msw/node';
import { handlers } from './mocks/handlers';

const server = setupServer(...handlers);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
```

### 4.6 언제 어떤 도구를 사용하나?

| 상황 | 도구 | 이유 |
|:-----|:-----|:-----|
| 단순 함수 모킹 | `jest.fn()` | 가볍고 빠름 |
| 모듈 전체 모킹 | `jest.mock()` | 의존성 격리 |
| API 호출 모킹 | MSW | 실제 fetch 동작과 유사, 통합 테스트에 적합 |
| 실제 함수 + 호출 추적 | `jest.spyOn()` | 실제 동작 유지하면서 검증 |

---

## 5. 테스트 유형 선택 가이드

### 5.1 선택 플로우차트

```
시작: "무엇을 테스트하려고 하는가?"
            │
            ▼
┌─────────────────────────────────────┐
│ Q1. 테스트 대상이 단일 함수/컴포넌트인가? │
└─────────────────────────────────────┘
         │                    │
        Yes                  No
         │                    │
         ▼                    ▼
┌────────────────┐   ┌─────────────────────────────────┐
│ Q2. 외부 의존성 │   │ Q3. UI/사용자 시나리오를 검증하나? │
│ 없이 테스트     │   └─────────────────────────────────┘
│ 가능한가?      │            │                    │
└────────────────┘           Yes                  No
    │        │                │                    │
   Yes      No                ▼                    ▼
    │        │        ┌────────────┐      ┌────────────────┐
    ▼        │        │  E2E 테스트 │      │   통합 테스트   │
┌───────────┐│        └────────────┘      └────────────────┘
│ 단위 테스트 ││
└───────────┘│
             ▼
   ┌────────────────────────────────────┐
   │ 의존성을 Mock으로 대체할 수 있는가? │
   └────────────────────────────────────┘
         │                    │
        Yes                  No
         │                    │
         ▼                    ▼
┌───────────────┐    ┌────────────────┐
│ 단위 테스트    │    │   통합 테스트   │
│ (jest.mock)   │    │   (MSW 사용)   │
└───────────────┘    └────────────────┘
```

### 5.2 테스트 피라미드

```
                    ┌───────┐
                    │  E2E  │  ← 5% (핵심 시나리오만)
                   ─┴───────┴─
                  ┌───────────┐
                  │   통합    │  ← 25% (주요 워크플로우)
                 ─┴───────────┴─
                ┌───────────────┐
                │    단위       │  ← 70% (대부분의 테스트)
                └───────────────┘
```

---

# Part 2: 단위 테스트 규칙

## 6. 핵심 개념

### 6.1 beforeEach / afterEach

테스트 실행 전후에 필요한 설정과 정리를 수행한다.

```typescript
describe('Calculator', () => {
  let calculator: Calculator;

  beforeAll(() => {
    // 전체 테스트 스위트에서 한 번만 실행
    console.log('테스트 시작');
  });

  beforeEach(() => {
    // 각 테스트 전에 실행
    calculator = new Calculator();
  });

  afterEach(() => {
    // 각 테스트 후에 실행
    jest.clearAllMocks();
  });

  afterAll(() => {
    // 전체 테스트 스위트 종료 시 한 번만 실행
    console.log('테스트 종료');
  });

  test('add', () => {
    expect(calculator.add(1, 2)).toBe(3);
  });
});
```

| 함수 | 실행 시점 | 사용 사례 |
|:----|:---------|:---------|
| `beforeAll` | describe당 한 번 (시작 전) | DB 연결, 무거운 초기화 |
| `beforeEach` | 각 test 전 | 테스트 객체 초기화, Mock 리셋 |
| `afterEach` | 각 test 후 | Mock 정리, 상태 초기화 |
| `afterAll` | describe당 한 번 (종료 후) | 리소스 해제 |

### 6.2 테스트 스킵 및 포커스

```typescript
// 특정 테스트만 실행
test.only('이 테스트만 실행', () => { ... });

// 특정 테스트 스킵
test.skip('이 테스트는 스킵', () => { ... });

// describe에도 적용 가능
describe.only('이 그룹만 실행', () => { ... });
describe.skip('이 그룹은 스킵', () => { ... });
```

### 6.3 테스트 커버리지

```bash
# 커버리지 리포트 생성
npm test -- --coverage

# 특정 파일만 커버리지
npm test -- --coverage --collectCoverageFrom='src/utils/**/*.ts'
```

**jest.config.js 설정:**
```javascript
module.exports = {
  collectCoverageFrom: [
    'src/**/*.{ts,tsx}',
    '!src/**/*.d.ts',
    '!src/index.tsx',
  ],
  coverageThreshold: {
    global: {
      branches: 80,
      functions: 80,
      lines: 80,
      statements: 80,
    },
  },
};
```

---

## 7. 테스트 작성 규칙

### 7.1 양방향 추적성 (Bidirectional Traceability)

**테스트 → 소스 코드 (테스트 파일에 명시)**

```typescript
/**
 * 대상: src/utils/calculator.ts:45 - add()
 * 의도: 양수 두 개를 더했을 때 올바른 결과 반환 검증
 */
test('양수 두 개 덧셈', () => { ... });
```

**소스 코드 → 테스트 (소스 파일에 주석)**

```typescript
/**
 * 두 숫자를 더한 결과를 반환.
 *
 * @Test: src/__tests__/utils/calculator.test.ts
 */
export const add = (a: number, b: number): number => {
  return a + b;
};
```

### 7.2 AAA 패턴 (Arrange-Act-Assert)

모든 테스트는 **AAA 패턴**을 따라 구조화한다.

```typescript
test('유효한 입력 시 사용자 반환', async () => {
  // Arrange: 테스트 데이터 및 환경 준비
  const mockUser = { id: 1, name: '홍길동' };
  (fetchUser as jest.Mock).mockResolvedValue(mockUser);

  // Act: 테스트 대상 함수 실행
  const result = await userService.getUser(1);

  // Assert: 결과 검증
  expect(result).toEqual(mockUser);
  expect(fetchUser).toHaveBeenCalledWith(1);
});
```

### 7.3 React Testing Library 원칙

**사용자 관점에서 테스트**한다.

```typescript
// Bad: 구현 세부사항 테스트
const { container } = render(<Button />);
expect(container.querySelector('.btn-primary')).toBeInTheDocument();

// Good: 사용자 관점 테스트
render(<Button>클릭</Button>);
expect(screen.getByRole('button', { name: '클릭' })).toBeInTheDocument();
```

**쿼리 우선순위:**

| 우선순위 | 쿼리 | 용도 |
|:--------|:-----|:-----|
| 1 | `getByRole` | 접근성 기반 (버튼, 링크 등) |
| 2 | `getByLabelText` | 폼 요소 |
| 3 | `getByPlaceholderText` | placeholder 기반 |
| 4 | `getByText` | 텍스트 콘텐츠 |
| 5 | `getByTestId` | 최후의 수단 (data-testid) |

---

## 8. 컴포넌트 테스트 패턴

### 8.1 기본 렌더링 테스트

```typescript
import { render, screen } from '@testing-library/react';
import UserCard from './UserCard';

describe('UserCard 컴포넌트', () => {
  const defaultProps = {
    userId: '1',
    userName: '홍길동',
  };

  test('사용자 이름이 표시된다', () => {
    // Arrange & Act
    render(<UserCard {...defaultProps} />);

    // Assert
    expect(screen.getByText('홍길동')).toBeInTheDocument();
  });
});
```

### 8.2 사용자 인터랙션 테스트

```typescript
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import LoginForm from './LoginForm';

describe('LoginForm 컴포넌트', () => {
  test('유효한 입력 후 제출 시 onSubmit 호출', async () => {
    // Arrange
    const user = userEvent.setup();
    const mockOnSubmit = jest.fn();
    render(<LoginForm onSubmit={mockOnSubmit} />);

    // Act
    await user.type(screen.getByLabelText('이메일'), 'test@example.com');
    await user.type(screen.getByLabelText('비밀번호'), 'password123');
    await user.click(screen.getByRole('button', { name: '로그인' }));

    // Assert
    expect(mockOnSubmit).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password123',
    });
  });
});
```

### 8.3 비동기 테스트

```typescript
import { render, screen, waitFor } from '@testing-library/react';
import UserProfile from './UserProfile';

test('사용자 정보 로딩 후 표시', async () => {
  // Arrange
  render(<UserProfile userId="1" />);

  // Act & Assert
  expect(screen.getByText('로딩 중...')).toBeInTheDocument();

  await waitFor(() => {
    expect(screen.getByText('홍길동')).toBeInTheDocument();
  });
});
```

### 8.4 커스텀 훅 테스트

```typescript
import { renderHook, act } from '@testing-library/react';
import useCounter from './useCounter';

describe('useCounter 훅', () => {
  test('초기값 설정', () => {
    const { result } = renderHook(() => useCounter(10));
    expect(result.current.count).toBe(10);
  });

  test('increment 호출 시 count 증가', () => {
    const { result } = renderHook(() => useCounter(0));

    act(() => {
      result.current.increment();
    });

    expect(result.current.count).toBe(1);
  });
});
```

---

## 9. 테스트 구조 규칙

### 9.1 네이밍 규칙

**파일**: `{컴포넌트명}.test.tsx` 또는 `{모듈명}.test.ts`
- `UserCard.test.tsx`
- `calculator.test.ts`

**describe**: 테스트 대상 (컴포넌트/함수명)
- `describe('UserCard 컴포넌트', ...)`
- `describe('calculateDiscount 함수', ...)`

**test**: 테스트 의도를 한글로 명확히
- `test('유효한 입력 시 사용자 반환', ...)`
- `test('존재하지 않는 ID로 조회 시 에러 발생', ...)`

### 9.2 describe.each / test.each

```typescript
describe.each([
  { score: 95, expected: 'A' },
  { score: 85, expected: 'B' },
  { score: 75, expected: 'C' },
  { score: 50, expected: 'F' },
])('getGrade($score)', ({ score, expected }) => {
  test(`${expected} 등급 반환`, () => {
    expect(getGrade(score)).toBe(expected);
  });
});

// 또는 test.each
test.each([
  [0, true],
  [120, true],
  [-1, false],
  [121, false],
])('isValidAge(%i)는 %s를 반환', (age, expected) => {
  expect(isValidAge(age)).toBe(expected);
});
```

---

## 10. 단위 테스트 체크리스트

- [ ] 테스트 설명이 **한글로 의도**가 명시되어 있는가?
- [ ] JSDoc에 **대상**(파일:라인 - 함수명)이 명시되어 있는가?
- [ ] **AAA 패턴**(Arrange-Act-Assert)을 따르고 있는가?
- [ ] 재사용 설정은 **beforeEach**로 분리되어 있는가?
- [ ] React Testing Library **쿼리 우선순위**를 따르는가?
- [ ] 관련 테스트가 **describe**로 그룹화되어 있는가?

---

# Part 3: 통합 테스트 규칙

## 11. 통합 테스트란?

### 11.1 단위 테스트 vs 통합 테스트

| 구분 | 단위 테스트 | 통합 테스트 |
|:-----|:-----------|:-----------|
| **범위** | 함수/컴포넌트 하나 | 여러 컴포넌트/모듈 조합 |
| **의존성** | jest.mock으로 격리 | MSW로 API 모킹, 실제 컴포넌트 연결 |
| **속도** | 빠름 | 보통 |
| **검증 대상** | 개별 로직 | 컴포넌트 간 상호작용 |

### 11.2 MSW를 활용한 통합 테스트

```typescript
// src/__tests__/integration/UserFlow.test.tsx
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';
import App from '@/App';

const server = setupServer(
  http.get('/api/users/:id', () => {
    return HttpResponse.json({ id: '1', name: '홍길동' });
  }),
  http.put('/api/users/:id', async ({ request }) => {
    const body = await request.json();
    return HttpResponse.json({ id: '1', ...body });
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

describe('통합: 사용자 프로필 수정 플로우', () => {
  test('프로필 조회 → 수정 → 저장 성공', async () => {
    // Arrange
    const user = userEvent.setup();
    render(<App />);

    // Act: 프로필 페이지로 이동
    await user.click(screen.getByText('프로필'));

    // Assert: 사용자 정보 표시
    await waitFor(() => {
      expect(screen.getByText('홍길동')).toBeInTheDocument();
    });

    // Act: 이름 수정
    await user.click(screen.getByRole('button', { name: '수정' }));
    await user.clear(screen.getByLabelText('이름'));
    await user.type(screen.getByLabelText('이름'), '김철수');
    await user.click(screen.getByRole('button', { name: '저장' }));

    // Assert: 수정된 이름 표시
    await waitFor(() => {
      expect(screen.getByText('김철수')).toBeInTheDocument();
    });
  });
});
```

---

## 12. 통합 테스트 작성 규칙

### 12.1 파일 구조

```
src/
├── __tests__/
│   ├── unit/                    # 단위 테스트
│   │   ├── components/
│   │   │   └── UserCard.test.tsx
│   │   └── utils/
│   │       └── calculator.test.ts
│   │
│   └── integration/             # 통합 테스트
│       ├── UserFlow.test.tsx
│       └── OrderFlow.test.tsx
│
└── components/
    └── UserCard.tsx
```

### 12.2 네이밍 규칙

**파일명**: `{워크플로우명}Flow.test.tsx`
- `UserFlow.test.tsx`
- `OrderFlow.test.tsx`

**describe**: `통합: {시나리오}`
- `describe('통합: 사용자 프로필 수정 플로우', ...)`

### 12.3 테스트 설명 형식

```typescript
/**
 * 통합 테스트 ID: INT-001
 * 대상 흐름: UserList → UserDetail → UserEdit
 *
 * 검증 내용:
 * 1. 사용자 목록 조회
 * 2. 사용자 상세 페이지로 이동
 * 3. 정보 수정 및 저장
 *
 * 시나리오: 관리자가 사용자 정보를 수정한다
 */
describe('통합: 사용자 정보 수정 플로우', () => { ... });
```

---

## 13. 통합 테스트 체크리스트

- [ ] `integration/` 디렉토리에 위치해 있는가?
- [ ] describe에 **통합 테스트 ID** (INT-XXX)가 명시되어 있는가?
- [ ] describe에 **대상 흐름**이 명시되어 있는가?
- [ ] MSW로 **API 모킹**이 설정되어 있는가?
- [ ] 테스트 간 **독립성**이 보장되는가? (`server.resetHandlers()`)

---

# Part 4: E2E 테스트 규칙

## 14. E2E 테스트란?

**사용자 관점에서 실제 브라우저에서 전체 시스템**을 검증하는 테스트다.

### 14.1 E2E 테스트 도구

| 도구 | 특징 | 추천 상황 |
|:-----|:-----|:---------|
| **Playwright** | 빠름, 크로스 브라우저, 자동 대기 | 새 프로젝트, CI/CD |
| **Cypress** | 쉬운 디버깅, 풍부한 생태계 | 기존 Cypress 경험 있을 때 |

---

## 15. Playwright를 활용한 E2E 테스트

### 15.1 기본 구조

```typescript
// e2e/user-journey.spec.ts
import { test, expect } from '@playwright/test';

test.describe('E2E: 사용자 가입 및 첫 주문', () => {
  test('회원가입 → 로그인 → 상품 검색 → 주문 완료', async ({ page }) => {
    // 1. 회원가입
    await page.goto('/signup');
    await page.fill('[name="email"]', 'test@example.com');
    await page.fill('[name="password"]', 'password123');
    await page.click('button[type="submit"]');

    // 2. 로그인 확인
    await expect(page.locator('.welcome-message')).toBeVisible();

    // 3. 상품 검색
    await page.fill('[name="search"]', '테스트 상품');
    await page.click('button.search-btn');

    // 4. 장바구니 추가
    await page.click('button.add-to-cart');

    // 5. 주문 완료
    await page.click('button.checkout');
    await page.click('button.confirm');
    await expect(page.locator('.order-success')).toBeVisible();
  });
});
```

### 15.2 Page Object Model (POM) 패턴

```typescript
// e2e/pages/LoginPage.ts
import { Page, Locator } from '@playwright/test';

export class LoginPage {
  readonly page: Page;
  readonly emailInput: Locator;
  readonly passwordInput: Locator;
  readonly submitButton: Locator;

  constructor(page: Page) {
    this.page = page;
    this.emailInput = page.locator('[name="email"]');
    this.passwordInput = page.locator('[name="password"]');
    this.submitButton = page.locator('button[type="submit"]');
  }

  async goto() {
    await this.page.goto('/login');
  }

  async login(email: string, password: string) {
    await this.emailInput.fill(email);
    await this.passwordInput.fill(password);
    await this.submitButton.click();
  }
}

// e2e/login.spec.ts
import { test, expect } from '@playwright/test';
import { LoginPage } from './pages/LoginPage';

test('로그인 성공', async ({ page }) => {
  const loginPage = new LoginPage(page);
  await loginPage.goto();
  await loginPage.login('test@example.com', 'password123');
  await expect(page.locator('.dashboard')).toBeVisible();
});
```

---

## 16. E2E 테스트 작성 규칙

### 16.1 파일 구조

```
e2e/
├── pages/                       # Page Object Models
│   ├── LoginPage.ts
│   └── DashboardPage.ts
│
├── fixtures/                    # 테스트 데이터
│   └── users.json
│
├── user-journey.spec.ts         # 사용자 여정 테스트
├── admin-flow.spec.ts           # 관리자 플로우 테스트
│
└── playwright.config.ts         # Playwright 설정
```

### 16.2 네이밍 규칙

**파일명**: `{시나리오}.spec.ts`
- `user-journey.spec.ts`
- `checkout-flow.spec.ts`

**test.describe**: `E2E: {시나리오}`
**test**: 단계별로 명확하게

### 16.3 테스트 설명 형식

```typescript
/**
 * E2E 테스트 ID: E2E-001
 * 시나리오: 신규 사용자 가입 후 첫 주문 완료
 *
 * 검증 단계:
 * 1. 회원가입 폼 입력 및 제출
 * 2. 로그인 성공 확인
 * 3. 상품 검색 및 선택
 * 4. 장바구니 추가
 * 5. 주문 완료 메시지 확인
 *
 * 사전 조건:
 * - 테스트 환경 접속 가능
 * - 테스트용 상품 데이터 존재
 *
 * 예상 결과:
 * - 주문 완료 페이지에 성공 메시지 표시
 */
test.describe('E2E: 신규 사용자 첫 주문', () => { ... });
```

---

## 17. 테스트 실행

```bash
# 단위 테스트만 실행
npm test -- --testPathIgnorePatterns="integration"

# 통합 테스트만 실행
npm test -- --testPathPattern="integration"

# E2E 테스트 실행 (Playwright)
npx playwright test

# E2E 테스트 UI 모드
npx playwright test --ui

# 특정 브라우저만
npx playwright test --project=chromium
```

---

## 18. E2E 테스트 체크리스트

- [ ] `e2e/` 디렉토리에 위치해 있는가?
- [ ] JSDoc에 **E2E 테스트 ID** (E2E-XXX)가 명시되어 있는가?
- [ ] JSDoc에 **검증 단계**가 순서대로 명시되어 있는가?
- [ ] **Page Object Model** 패턴을 사용하고 있는가?
- [ ] 테스트가 **독립적으로 실행 가능**한가?
- [ ] 적절한 **대기(waitFor, expect)**가 사용되고 있는가?

---

## 19. 의존성 설정 (package.json)

```json
{
  "devDependencies": {
    "@testing-library/react": "^14.0.0",
    "@testing-library/jest-dom": "^6.1.0",
    "@testing-library/user-event": "^14.5.0",
    "@types/jest": "^29.5.0",
    "jest": "^29.7.0",
    "jest-environment-jsdom": "^29.7.0",
    "msw": "^2.0.0",
    "@playwright/test": "^1.40.0"
  },
  "scripts": {
    "test": "jest",
    "test:coverage": "jest --coverage",
    "test:e2e": "playwright test",
    "test:e2e:ui": "playwright test --ui"
  }
}
```

---

## 20. Jest 설정 (jest.config.js)

```javascript
module.exports = {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.ts'],
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1',
    '\\.(css|scss)$': 'identity-obj-proxy',
  },
  testPathIgnorePatterns: ['/node_modules/', '/e2e/'],
  collectCoverageFrom: [
    'src/**/*.{ts,tsx}',
    '!src/**/*.d.ts',
    '!src/index.tsx',
  ],
  coverageThreshold: {
    global: {
      branches: 80,
      functions: 80,
      lines: 80,
      statements: 80,
    },
  },
};
```

---

## 21. setupTests.ts

```typescript
import '@testing-library/jest-dom';

// MSW 설정 (필요한 경우)
import { server } from './mocks/server';

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
```

---