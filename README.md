# <img src="https://user-images.githubusercontent.com/63537847/127521768-0f0cb3fb-7f23-4a4f-a1c1-4419986c45e2.png" width="30" height="30"> Hábito

> ### 팀원 : 강수아, 금나연, 박종회

여러 사람들과 공유하고 인증하면서 습관을 형성할 수 있게 도와주는 어플리케이션

 → 습관 인증의 기록들을 시각화하여 캘린더에 보여줌으로써 나의 습관 형성 과정을 볼 수 있음
 
 → 습관을 인증하면 1일 1회 받는 포인트로 본인만의 아쿠아리움을 채워나가며 동기부여의 역할을 함


## Development Environment

> *Android Studio 4.1.0*
>
>  * compileSDKversion : 30
>  * buildToolsversion : 30.0.3
>  * SDK 21 기준 호환
>  * Firebase Version : v4.6.0

## LOGIN & SIGN UP 

> 기본 로그인, 구글 로그인, 페이스북 로그인 가능 

<img src="https://user-images.githubusercontent.com/63537847/127522273-edce5623-4cd8-4fe5-9ec9-d9dc06ee76a3.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127522367-b278ee9a-2087-4467-8a36-7a0eb6791b48.jpg" width="200" height="400"> 

- 회원가입할 때 프로필 사진, 이메일, 이름 등을 입력함
- 비밀번호는 6자리 이상
- 페이스북으로 로그인하면 default 프로필 사진 사용 


## NAVIGATION & POINT DIALOG

> 5개의 네비게이션 메뉴 
> 
> 매일 뜨는 오늘의 포인트

<img src="https://user-images.githubusercontent.com/63537847/127522697-eebf1f28-9ebb-4f30-b86a-766a02e41b45.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127522783-a789bbd5-32eb-4aaf-83d5-83444822b03c.jpg" width="200" height="400"> 

- 하루에 한 번 처음으로 앱에 들어갈 때 어제의 습관 기록을 통해 얻은 포인트를 dialog로 보여줌 
- 5가지의 메뉴를 navigation drawer로 보여줌 

## MY PROFILE 

> 나의 정보 및 이름/ 비밀번호 변경 
> 
> Log out / Sign Out 가능  

<img src="https://user-images.githubusercontent.com/63537847/127523064-76df6c86-09f7-44cf-88a0-b501f0012bc8.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127523110-ae088e93-b718-4be7-8a44-ba7fce5df974.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127523162-a87f0027-0a35-43ac-901b-a99d3f8a087c.jpg" width="200" height="400"> 

- navigation drawer 상단에 있는 프로필을 클릭하면 개인 정보를 볼 수 있는 창으로 넘어감
- 이름 변경 버튼을 누르면 이름을 변경할 수 있고 바로 바뀐 모습을 볼 수 있음
- 비밀번호도 변경 가능함 
- Log out 하면 다시 로그인 화면으로 넘어감
- Sign out 하면 회원 탈퇴가 됨


## Tab 1 : MY GROUPS  

> 내가 가입한 그룹들만 보여주고 해당 게시판에 들어가면 나의 습관을 게시글로 올리면서 기록할 수 있음

<img src="https://user-images.githubusercontent.com/63537847/127523488-50fe8c2a-57b3-4bbf-965c-c7bec9b861f2.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127523540-9f7467ac-9847-458e-b4c3-42eeaa2e12f8.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127523597-dcf0f925-402c-49bf-b909-6ab7d21ca4f2.jpg" width="200" height="400"> 

- 내가 가입한 그룹들을 볼 수 있음
- 그룹 클릭하면 해당 그룹의 게시판으로 넘어감 
- 하단에 있는 버튼을 클릭하면 새로운 게시글을 쓸 수 있는 dialog가 뜸
- 게시글에 좋아요를 누를 수 있고 누적된 좋아요의 개수를 볼 수 있음 


## Tab 2 : SEARCH NEW GROUP  

> 새로운 그룹을 찾고 검색할 수 있음
> 
> 내가 직접 그룹을 생성할 수 있음

<img src="https://user-images.githubusercontent.com/63537847/127524611-1eaeb968-c799-465f-b724-259a0ab019fb.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127524658-289ae10b-8e71-4ff3-8fbb-c31eef83dff6.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127524704-7c21574f-c55d-407d-9106-5da4cad8d462.jpg" width="200" height="400"> 

- 앱 내에 저장되어 있는 모든 그룹을 볼 수 있음
- 검색 바에서 그룹의 이름을 검색할 수 있음
- Join 버튼을 클릭하면 해당 그룹에 참여할 수 있음 
- 하단에 있는 버튼을 클릭하여 직접 새로운 습관 형성 그룹을 만들 수 있음
- 새로운 그룹은 그룸의 프로필 사진과 그룹 이름, 그룹에 대한 설명이 필요함


## Tab 3 : MY AQUARIUM  

> 인증하면서 받은 포인트로 물고기를 받고 여러 물고기들을 모아서 볼 수 있는 곳 

<img src="https://user-images.githubusercontent.com/63537847/127525030-a4e7ad9d-93ce-4f9a-b8cd-27eb77a1831b.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127528386-a9499af4-82f9-49d4-824d-3cea87c0ed21.jpg" width="200" height="400"> 

- 내가 받은 물고기들을 볼 수 있음
- 한 곳을 오래 클릭하고 있으면 물고기 밥이 생기고 물고기들이 그곳으로 모임


## Tab 4 : GACHA  

> 인증하면서 받은 포인트로 물고기를 받을 수 있는 곳 
> 
> 물고기는 랜덤! 

<img src="https://user-images.githubusercontent.com/63537847/127525279-6c1a9a99-d012-47bd-bcb6-2a59f23273e7.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/63537847/127525311-44753a0f-d6b2-4c8c-8b2e-9a6eebdc0966.jpg" width="200" height="400"> 

- 내가 받은 포인트를 볼 수 있고 큰 버튼을 클릭하면 랜덤으로 물고기를 300 coin에 살 수 있음 
- 물고기는 나의 Collection에 저장됨

## Tab 5 : MY COLLECTION  

> 받은 물고기들을 볼 수 있음 

<img src="https://user-images.githubusercontent.com/63537847/127525619-13627fdc-f490-4fbc-aa4b-3c2f41da2565.jpg" width="200" height="400"> 

- 내가 받은 모든 물고기들을 볼 수 있음
- 아직 받지 못한 물고기의 모습은 볼 수 없음
- 받은 물고기들의 Add 버튼을 클릭하면 Aquarium에 추가할 수 있음
- Remove 버튼을 누르면 Aquarium에서 삭제할 수 있음 
