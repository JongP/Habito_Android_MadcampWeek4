# Hábito
madcamp week4

## 팀원 : 강수아, 금나연, 박종회

## Development Environment

### *Android Studio 4.1.2*

  * compileSDKversion : 30
  * buildToolsversion : 30.0.3
  * SDK 18(API Level 27) 기준 호환
  * NodeJS : v8.10.0
  * Ubuntu : 18.04.2
  * MongoDB : v3.6.3

## Introduction
- 대화형 챗봇 개발

## Pipeline
### Training
*1. Intent*
- 7개의 문맥(intent)이 라벨링된 105681개의 데이터에서 문장 내 키워드를 추출

    {0: "인사", 1: "욕설", 2: "주문", 3: "예약", 4: "기타", 5:"이별", 6:"사랑"}
- 단어 시퀀스 벡터를 만들어 CNN을 이용해 모델 학습, 평가 및 저장 ('models/intent/intent_model.h5')

*2. Ner*
- 형태소 별로 160458개의 문장을 분석하여 라벨링된 데이터 이용


    B_FOOD : 음식
    
    B_DT, B_TI : 시간 (학습 데이터의 영향으로 날짜와 시간을 혼용해서 사용)
    
    B_PS : 사람
    
    B_OG : 조직, 회사
    
    B_LC : 지역
    
- 단어 시퀀스 벡터를 만들어 Bi-LSTM을 이용해 모델 학습, 평가 및 저장 ('models/ner/ner_model.h5')

## 실행 
- Server : bot.py
- Client : chatbot_api/app.py
- Chatbot : https://talk.naver.com/ct/w4gxjf

## 화면캡처

<img width="400" alt="AI_hello" src="https://user-images.githubusercontent.com/82078588/126426791-3d05ed31-5669-4145-91f0-05832b730276.png">
<img width="400" alt="AI_etc" src="https://user-images.githubusercontent.com/82078588/126426779-fcce688f-1104-4ab6-a737-4a1a31847b84.png">
<img width="400" alt="AI_food" src="https://user-images.githubusercontent.com/82078588/126426788-7fbb0c71-1577-4709-8c23-927e803fe5da.png">
<img width="400" alt="AI_love" src="https://user-images.githubusercontent.com/82078588/126426794-4ef22d79-b746-4c77-8a85-0dd09781f5fe.png">
<img width="400" alt="AI_reserve" src="https://user-images.githubusercontent.com/82078588/126426797-54b54302-ba79-4c3b-a658-70a015a75765.png">
<img width="400" alt="AI_sad" src="https://user-images.githubusercontent.com/82078588/126426799-da564a5c-c5f8-48a2-abce-4684cbced8f8.png">
