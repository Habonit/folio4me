# Error Spec

---

## 이 레포에서 처리하는 에러

| 에러 ID | 에러 상황 | 상태 |
|:--------|:---------|:-----|
| ERR-API-001 | 토큰 없음 | ✅ |
| ERR-API-010 | 필수값 누락 | ✅ |
| ERR-API-020 | 데이터 없음 | 🚧 |

---

## 에러 상세

### ERR-API-001: 토큰 없음

**발생 조건**: Authorization 헤더 없이 인증 필요 API 호출

```python
if not request.headers.get("Authorization"):
    raise AuthException("ERR-API-001", "로그인이 필요합니다")
```

---

### ERR-API-010: 필수값 누락

**발생 조건**: 필수 필드가 null 또는 빈 문자열

```python
if not data.get("email"):
    raise ValidationException("ERR-API-010", "email은(는) 필수입니다")
```

---

### ERR-API-020: 데이터 없음

**발생 조건**: DB 조회 결과가 None

```python
user = db.query(User).filter_by(id=user_id).first()
if not user:
    raise NotFoundException("ERR-API-020", "데이터를 찾을 수 없습니다")
```

---

## 템플릿

```markdown
### ERR-XXX-NNN: << 에러 상황 >>

**발생 조건**: 에러가 발생하는 조건

```언어
// 처리 코드
```
```

---