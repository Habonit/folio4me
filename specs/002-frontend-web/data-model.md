# Data Model: Frontend Web Implementation

**Feature**: 002-frontend-web
**Date**: 2025-12-24

## 1. Frontend State Models

### 1.1 Session Store

```typescript
interface Session {
  id: string;                    // UUID
  currentStep: ConversationStep;
  status: SessionStatus;
  createdAt: string;             // ISO 8601
  lastActivityAt: string;        // ISO 8601
  progressPercentage: number;    // 0-100
}

type SessionStatus = 'ACTIVE' | 'COMPLETED' | 'TERMINATED' | 'EXPIRED';

type ConversationStep =
  | 'STEP_0_GATE'
  | 'STEP_1_PERSONAL_INFO'
  | 'STEP_2_WORK_EXPERIENCE'
  | 'STEP_3_EDUCATION'
  | 'STEP_4_REPRESENTATIVE_PROJECTS'
  | 'STEP_5_PROJECTS'
  | 'STEP_6_AWARDS'
  | 'STEP_7_MAJOR_ACTIVITIES'
  | 'STEP_8_OTHER_ACTIVITIES'
  | 'STEP_9_CERTIFICATIONS'
  | 'STEP_10_TECHNICAL_SKILLS'
  | 'STEP_11_ABOUT';
```

### 1.2 Chat Store

```typescript
interface ChatMessage {
  id: string;                    // client-generated UUID
  role: 'user' | 'assistant';
  content: string;
  timestamp: Date;
}

interface ChatState {
  messages: ChatMessage[];
  isLoading: boolean;
  error: string | null;
}
```

### 1.3 Portfolio Store (Preview)

```typescript
interface Portfolio {
  meta: {
    targetRole: string;
    createdAt: string;
    status: 'IN_PROGRESS' | 'COMPLETED';
  };
  personalInfo: {
    name: string | null;
    github: string | null;
    email: string | null;
    profileImage: string | null;
    confirmed: boolean;
  };
  workExperience: WorkExperience[];
  education: {
    display: boolean;
    items: Education[];
    confirmed: boolean;
  };
  representativeProjects: Project[];
  projects: Project[];
  awards: Award[];
  activities: {
    major: Activity[];
    minor: Activity[];
  };
  certifications: Certification[];
  technicalSkills: {
    strong: string[];
    knowledgeable: string[];
    confirmed: boolean;
  };
  about: {
    sentences: string[];
    confirmed: boolean;
  };
}
```

---

## 2. API Response Types

### 2.1 Session Responses

```typescript
// POST /sessions, GET /sessions/{id}
interface SessionResponse {
  id: string;
  currentStep: ConversationStep;
  status: SessionStatus;
  createdAt: string;
  lastActivityAt: string;
  progressPercentage: number;
}
```

### 2.2 Message Responses

```typescript
// POST /sessions/{id}/messages
interface MessageRequest {
  message: string;
}

interface MessageResponse {
  message: string;
  currentStep: ConversationStep;
  completed: boolean;
  expectedInputType?: 'text' | 'image' | 'selection';
}
```

### 2.3 Error Response

```typescript
interface ApiError {
  status: number;
  error: string;
  message: string;
  path: string;
  timestamp: string;
}
```

---

## 3. Component Props

### 3.1 Chat Components

```typescript
// ChatMessage.svelte
interface ChatMessageProps {
  message: ChatMessage;
}

// ChatInput.svelte
interface ChatInputProps {
  disabled: boolean;
  placeholder: string;
  onSubmit: (message: string) => void;
}

// ChatArea.svelte
interface ChatAreaProps {
  messages: ChatMessage[];
  isLoading: boolean;
}
```

### 3.2 Layout Components

```typescript
// Sidebar.svelte
interface SidebarProps {
  currentStep: ConversationStep;
  progressPercentage: number;
}

// Header.svelte
interface HeaderProps {
  sessionId: string;
  onModeToggle: () => void;
  mode: 'single' | 'split';
}
```

### 3.3 Preview Components

```typescript
// MarkdownPreview.svelte
interface MarkdownPreviewProps {
  portfolio: Portfolio;
}
```

---

## 4. Store Definitions (Svelte)

```typescript
// stores/session.ts
import { writable } from 'svelte/store';

export const sessionStore = writable<Session | null>(null);

// stores/chat.ts
export const chatStore = writable<ChatState>({
  messages: [],
  isLoading: false,
  error: null
});

// stores/portfolio.ts
export const portfolioStore = writable<Portfolio | null>(null);
```

---

## 5. Step Display Mapping

```typescript
const STEP_LABELS: Record<ConversationStep, string> = {
  STEP_0_GATE: '희망 직무 확인',
  STEP_1_PERSONAL_INFO: '기본 개인정보',
  STEP_2_WORK_EXPERIENCE: '이력',
  STEP_3_EDUCATION: '학력',
  STEP_4_REPRESENTATIVE_PROJECTS: '대표 프로젝트',
  STEP_5_PROJECTS: '일반 프로젝트',
  STEP_6_AWARDS: '수상 경력',
  STEP_7_MAJOR_ACTIVITIES: '주요 대외활동',
  STEP_8_OTHER_ACTIVITIES: '그 외 대외활동',
  STEP_9_CERTIFICATIONS: '자격증',
  STEP_10_TECHNICAL_SKILLS: '기술 역량',
  STEP_11_ABOUT: '자기소개'
};

const STEP_ORDER: ConversationStep[] = [
  'STEP_0_GATE',
  'STEP_1_PERSONAL_INFO',
  'STEP_2_WORK_EXPERIENCE',
  'STEP_3_EDUCATION',
  'STEP_4_REPRESENTATIVE_PROJECTS',
  'STEP_5_PROJECTS',
  'STEP_6_AWARDS',
  'STEP_7_MAJOR_ACTIVITIES',
  'STEP_8_OTHER_ACTIVITIES',
  'STEP_9_CERTIFICATIONS',
  'STEP_10_TECHNICAL_SKILLS',
  'STEP_11_ABOUT'
];
```

---

## 6. Summary

| Model | Purpose | Source |
|:------|:--------|:-------|
| Session | 세션 상태 관리 | Backend API |
| ChatMessage | 채팅 메시지 | Local + API |
| Portfolio | 미리보기 데이터 | Backend API |
| ApiError | 에러 처리 | Backend API |
