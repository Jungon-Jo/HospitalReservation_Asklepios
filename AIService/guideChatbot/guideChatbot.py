from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import torch
from transformers import AutoModelForCausalLM, AutoTokenizer

app = FastAPI()

class ChatbotModel:
    _instance = None  # ✅ 싱글톤 객체 저장

    def __new__(cls):
        if cls._instance is None:
            print("🔹 [INFO] Fine-Tuned 챗봇 모델 로딩 중...")
            cls._instance = super(ChatbotModel, cls).__new__(cls)
            cls._instance.load_model()
        return cls._instance

    def load_model(self):
        """Fine-Tuned 모델 로드"""
        model_path = "./gemma-finetuned"
        self.tokenizer = AutoTokenizer.from_pretrained(model_path)
        self.model = AutoModelForCausalLM.from_pretrained(model_path).to("cuda" if torch.cuda.is_available() else "cpu")
        print("✅ Fine-Tuned 모델 로드 완료!")

    def get_response(self, user_input: str) -> str:
        """챗봇 응답 생성"""
        inputs = self.tokenizer(f"사용자: {user_input}\n챗봇:", return_tensors="pt").to("cuda" if torch.cuda.is_available() else "cpu")
        outputs = self.model.generate(**inputs, max_length=200)
        response_text = self.tokenizer.decode(outputs[0], skip_special_tokens=True)

        # ✅ "챗봇:" 이후의 응답만 추출
        if "챗봇:" in response_text:
            response_text = response_text.split("챗봇:")[1].strip()

        return response_text

# ✅ 싱글톤 인스턴스 생성
chatbot = ChatbotModel()

class ChatRequest(BaseModel):
    message: str

@app.post("/chatbot")
async def chatbot_api(request: ChatRequest):
    """FastAPI 엔드포인트"""
    if not request.message:
        raise HTTPException(status_code=400, detail="입력 메시지가 없습니다.")

    response = chatbot.get_response(request.message)
    return {"response": response}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=5100, reload=True)

@app.get("/")
def root():
    return {"message": "FastAPI 챗봇 서버가 실행 중입니다! 엔드포인트는 /chatbot 입니다."}
