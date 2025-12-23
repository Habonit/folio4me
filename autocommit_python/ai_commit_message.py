import sys
from openai import OpenAI
import os
from pathlib import Path
from dotenv import load_dotenv

# 현재 파일 위치 기준 절대 경로 설정
script_dir = Path(__file__).parent.resolve()
env_path = script_dir / ".env"
template_path = script_dir / "commit_template.txt"

load_dotenv(dotenv_path=env_path)

api_key = os.getenv("COMMIT_OPENAI_API_KEY")
model = os.getenv("COMMIT_MODEL", "gpt-4o")

if not api_key:
    print("❌ OPENAI_API_KEY가 설정되어 있지 않습니다.")
    sys.exit(1)

client = OpenAI(api_key=api_key)

# surrogate 문자 제거하여 인코딩 에러 방지
diff = sys.argv[1].encode('utf-8', errors='surrogateescape').decode('utf-8', errors='replace')

with open(template_path, "r") as f:
    prompt_template = f.read()

prompt = prompt_template.format(diff=diff)

response = client.chat.completions.create(
    model=model,
    messages=[
        {"role": "user", "content": prompt}
    ]
)

print(response.choices[0].message.content)