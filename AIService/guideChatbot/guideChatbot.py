#%% md
# # Guide Chatbot
#%%
# 라이브러리 빌드
import pandas as pd
from langchain_ollama import ChatOllama
from langchain.vectorstores import FAISS
from langchain.text_splitter import CharacterTextSplitter
from langchain.chains import ConversationalRetrievalChain
from langchain_huggingface import HuggingFaceEmbeddings
import gradio as gr
import os

os.environ["TOKENIZERS_PARALLELISM"] = "false"
#%%
# CSV 파일 로드 및 확인
df = pd.read_csv("guideChatbot.csv")
df = df.dropna(subset=["inputs", "response"])
#%%
def preprocess_text(text):
    text = text.strip()  # 양쪽 공백 제거
    # text = text.replace("\n", " ")  # 줄바꿈 제거
    return text
#%%
# 데이터 준비
texts = [
    f"질문: {preprocess_text(row['inputs'])}\n답변: {preprocess_text(row['response'])}"
    for _, row in df.iterrows()
]
if not texts:
    raise ValueError("텍스트 데이터가 비어 있습니다. CSV 파일을 확인하세요.")
#%%
# 임베딩 모델 초기화
embeddings = HuggingFaceEmbeddings(
    model_name="sentence-transformers/xlm-r-100langs-bert-base-nli-stsb-mean-tokens"
)
#%%
# 벡터 데이터베이스 생성
vectorstore = FAISS.from_texts(texts, embeddings)
# 저장된 문서 수 확인
print(f"벡터 스토어에 저장된 문서 수: {len(vectorstore.docstore._dict)}")
# 저장된 문서 확인
for key, value in vectorstore.docstore._dict.items():
    print(f"Key: {key}, Value: {value}")
#%%
# ChatOllama 모델 초기화
llm = ChatOllama(model="gemma2", temperature=0)
#%%
# Conversational Retrieval Chain 초기화
qa_chain = ConversationalRetrievalChain.from_llm(
    llm,
    vectorstore.as_retriever(search_kwargs={"k": 3}),
    return_source_documents=True,
    verbose=True
)
#%%
# 채팅 함수 정의
def chat(message, history):
    print(f"입력 메시지: {message}")
    print(f"대화 기록: {history}")

    # 대화 기록 준비
    chat_history = [(human, ai) for human, ai in history]

    # 모델 호출
    response = qa_chain({"question": message, "chat_history": chat_history})

    # 검색된 문서 확인
    print(f"검색된 문서: {[doc.page_content for doc in response['source_documents']]}")
    print(f"모델 응답: {response['answer']}")

    sources = set([doc.metadata.get('source', 'Unknown') for doc in response['source_documents']])
    source_info = f"\n\n참고 출처: {', '.join(sources)}" if sources else ""

    return response['answer'] + source_info
#%%
# Gradio 인터페이스 설정
demo = gr.ChatInterface(
    fn=chat,
    examples=[
        "로그인은 어디서 해야하나요?",
        "회원가입은 어떻게 해야하나요?",
        "병원 예약은 어떻게 하나요?"
    ],
    title="Asklepios Guide Chatbot",
    description="저희 Asklepios 이용에 궁금한 점이 있으면 언제든지 물어보세요."
)
#%%
# 서버 실행
demo.launch(server_port=7862, server_name="0.0.0.0")
#%%
demo.close()
#%%
