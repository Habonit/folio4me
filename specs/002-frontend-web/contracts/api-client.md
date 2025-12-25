# API Client Contract: Frontend Web

**Feature**: 002-frontend-web
**Date**: 2025-12-24

## Overview

프론트엔드에서 백엔드 API를 호출하기 위한 클라이언트 계약입니다.

---

## 1. Base Configuration

```typescript
// lib/api/config.ts
const API_BASE_URL = 'http://localhost:8080/api/v1';

interface ApiConfig {
  baseUrl: string;
  timeout: number;
}

const config: ApiConfig = {
  baseUrl: API_BASE_URL,
  timeout: 30000  // 30초 (AI 응답 대기)
};
```

---

## 2. Session API

### 2.1 Create Session

```typescript
// POST /sessions
async function createSession(): Promise<SessionResponse> {
  const response = await fetch(`${API_BASE_URL}/sessions`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' }
  });

  if (!response.ok) {
    throw await parseError(response);
  }

  return response.json();
}
```

### 2.2 Get Session

```typescript
// GET /sessions/{sessionId}
async function getSession(sessionId: string): Promise<SessionResponse> {
  const response = await fetch(`${API_BASE_URL}/sessions/${sessionId}`);

  if (!response.ok) {
    throw await parseError(response);
  }

  return response.json();
}
```

### 2.3 Delete Session

```typescript
// DELETE /sessions/{sessionId}
async function deleteSession(sessionId: string): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/sessions/${sessionId}`, {
    method: 'DELETE'
  });

  if (!response.ok) {
    throw await parseError(response);
  }
}
```

---

## 3. Conversation API

### 3.1 Send Message

```typescript
// POST /sessions/{sessionId}/messages
async function sendMessage(
  sessionId: string,
  message: string
): Promise<MessageResponse> {
  const response = await fetch(
    `${API_BASE_URL}/sessions/${sessionId}/messages`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ message })
    }
  );

  if (!response.ok) {
    throw await parseError(response);
  }

  return response.json();
}
```

### 3.2 Get Portfolio

```typescript
// GET /sessions/{sessionId}/portfolio
async function getPortfolio(sessionId: string): Promise<Portfolio> {
  const response = await fetch(
    `${API_BASE_URL}/sessions/${sessionId}/portfolio`
  );

  if (!response.ok) {
    throw await parseError(response);
  }

  return response.json();
}
```

### 3.3 Get Current Prompt

```typescript
// GET /sessions/{sessionId}/prompt
async function getCurrentPrompt(sessionId: string): Promise<MessageResponse> {
  const response = await fetch(
    `${API_BASE_URL}/sessions/${sessionId}/prompt`
  );

  if (!response.ok) {
    throw await parseError(response);
  }

  return response.json();
}
```

---

## 4. Error Handling

```typescript
// lib/api/error.ts
interface ApiError {
  status: number;
  error: string;
  message: string;
  path: string;
  timestamp: string;
}

async function parseError(response: Response): Promise<ApiError> {
  try {
    return await response.json();
  } catch {
    return {
      status: response.status,
      error: 'UNKNOWN_ERROR',
      message: response.statusText,
      path: '',
      timestamp: new Date().toISOString()
    };
  }
}

// Error code constants
const ErrorCodes = {
  SESSION_NOT_FOUND: 'SESSION_NOT_FOUND',
  INVALID_INPUT: 'INVALID_INPUT',
  INVALID_PERIOD: 'INVALID_PERIOD',
  STORAGE_ERROR: 'STORAGE_ERROR',
  INTERNAL_SERVER_ERROR: 'INTERNAL_SERVER_ERROR'
} as const;
```

---

## 5. API Client Export

```typescript
// lib/api/index.ts
export const api = {
  session: {
    create: createSession,
    get: getSession,
    delete: deleteSession
  },
  conversation: {
    sendMessage,
    getPortfolio,
    getCurrentPrompt
  }
};

export type { SessionResponse, MessageResponse, Portfolio, ApiError };
export { ErrorCodes };
```

---

## 6. Usage Examples

### 6.1 Create and Start Session

```typescript
// +page.svelte (Main)
import { api } from '$lib/api';
import { goto } from '$app/navigation';

async function handleCreateSession() {
  try {
    const session = await api.session.create();
    goto(`/chat/${session.id}`);
  } catch (error) {
    console.error('Failed to create session:', error);
  }
}
```

### 6.2 Send Message and Update UI

```typescript
// chat/[sessionId]/+page.svelte
import { api } from '$lib/api';
import { chatStore, sessionStore, portfolioStore } from '$lib/stores';

async function handleSendMessage(message: string) {
  chatStore.update(s => ({ ...s, isLoading: true }));

  try {
    const response = await api.conversation.sendMessage(sessionId, message);

    // Add user message
    chatStore.update(s => ({
      ...s,
      messages: [...s.messages, { role: 'user', content: message, ... }]
    }));

    // Add AI response
    chatStore.update(s => ({
      ...s,
      messages: [...s.messages, { role: 'assistant', content: response.message, ... }],
      isLoading: false
    }));

    // Update session state
    sessionStore.update(s => s ? { ...s, currentStep: response.currentStep } : null);

    // Refresh portfolio for preview
    const portfolio = await api.conversation.getPortfolio(sessionId);
    portfolioStore.set(portfolio);

  } catch (error) {
    chatStore.update(s => ({ ...s, isLoading: false, error: error.message }));
  }
}
```

---

## 7. Summary

| API | Method | Endpoint | Purpose |
|:----|:-------|:---------|:--------|
| createSession | POST | /sessions | 새 세션 생성 |
| getSession | GET | /sessions/{id} | 세션 상태 조회 |
| deleteSession | DELETE | /sessions/{id} | 세션 삭제 |
| sendMessage | POST | /sessions/{id}/messages | 메시지 전송 |
| getPortfolio | GET | /sessions/{id}/portfolio | 포트폴리오 조회 |
| getCurrentPrompt | GET | /sessions/{id}/prompt | 현재 프롬프트 조회 |
