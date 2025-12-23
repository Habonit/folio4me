# AutoCommit 설정 가이드
- AutoCommit은 커밋 시 자동으로 메시지를 생성해주는 도구입니다.
- OpenAI API를 기반으로 커밋 메시지를 생성하며, git commit만 입력하면 자동으로 메시지를 완성합니다.

## 구조

```
autocommit_python/
├── .env.example          # 환경변수 예시 파일
├── .env                  # 실제 API 키 설정 (gitignore 대상)
├── ai_commit_message.py  # OpenAI API 호출 및 커밋 메시지 생성
├── commit_template.txt   # AI에게 전달할 프롬프트 템플릿
├── prepare-commit-msg    # Git hook 스크립트
├── setup_git_hook.sh     # hook 설치 스크립트
├── requirements.txt      # Python 의존성
└── README.md
```

### 동작 흐름

```
git commit 실행
    ↓
prepare-commit-msg hook 트리거
    ↓
git diff --cached로 staged 변경사항 추출
    ↓
ai_commit_message.py 실행
    ↓
commit_template.txt + diff → OpenAI API 호출
    ↓
생성된 커밋 메시지 반환
```

## 준비사항
- Python 3.x 환경
- OpenAI API 키

## 최초 세팅

1. `autocommit_python` 디렉토리를 레포지토리 최상단에 위치시킵니다.
2. 해당 디렉토리로 이동합니다.
   ```bash
   cd autocommit_python
   ```
3. `.env` 파일을 생성하고 API 키를 설정합니다.
   ```bash
   cp .env.example .env
   # .env 파일 내 COMMIT_OPENAI_API_KEY에 본인의 API 키 입력
   ```
4. 의존성을 설치합니다.
   ```bash
   pip install -r requirements.txt
   ```
5. Git hook을 설치합니다.
   ```bash
   bash setup_git_hook.sh
   ```

## 사용법
- `git commit` 명령어를 입력하면 자동으로 커밋 메시지가 생성됩니다.
- 자동 생성된 메시지가 적절하지 않을 경우, 에디터에서 직접 수정할 수 있습니다.