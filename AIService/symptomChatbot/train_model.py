import pymysql
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB
import joblib

# DB 연결 및 데이터 로드
conn = pymysql.connect(host='localhost', user='root', password='1111', db='asklepios')
df = pd.read_sql("SELECT r.reservation_memo as 'memo', ud.user_doctor_medical as 'medical' FROM reservation r inner join user_doctor ud on r.reservation_user_doctor_code = ud.user_doctor_code", conn)
conn.close()

# 데이터 확인
print("\n[데이터 확인]")
print("==================================")
print(f"- 전체 데이터 개수: {len(df)}건")
print(f"- 컬럼 구조: {df.columns.tolist()}")
print("\n- 상위 5개 데이터 샘플:")
print(df.head())
print("\n- 결측치 확인:")
print(df.isnull().sum())
print("==================================\n")

# 텍스트 벡터화
tfidf = TfidfVectorizer(max_features=1000, stop_words=['은', '는', '이', '가', '을', '를'], ngram_range=(1, 2))
X = tfidf.fit_transform(df['memo'])
y = df['medical']

# 데이터 분할
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)

# 모델 학습
model = MultinomialNB()
model.fit(X, y)

# 저장
joblib.dump(model, 'model.joblib')
joblib.dump(tfidf, 'tfidf.joblib')
print("Model trained and saved!")