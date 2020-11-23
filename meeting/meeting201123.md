# 회의록 11/23

**주요 이슈** : 기획안 작성

**주요 회의 내용** : 기획안, WBS 초안, AWS AI/IoT 문서 확인

**관련링크**

AWS 개발자 설명서

https://docs.aws.amazon.com/

<br>

# 기획안 초안

### 팀명

Get Ready With Me

<br>

### 프로젝트 주제

패션 피플로 만들어주는 색 기반 코디 트렌디 옷장

<br>

### 프로젝트 목적

매일 입는 코디에 필요한 시간을 단축시켜주고, 올해의 색상과 현재 트렌드를 반영하여 자신의 옷장 속 코디를 추천

<br>

### 프로젝트 수행 방향

**빅데이터**

-   문제제시 데이터 수집/분석
    -   매일 코디하기 위해서 들이는 시간과 노력
    -   가지고 있는 옷과 어울리는 조합 추천 (google forms)

-   AI 훈련에 사용될 데이터 수집/분석
    -   올해의 색이 포함된 자료
    -   (+) 색기반 앞으로의 패션 트렌드 예측

수행도구

-   Python or R

-   AWS

-   Spark

-   Python / Selenium

-   google forms

<br>

**AI**

-   상의, 하의, 외투, 원피스 데이터 분류

-   고른 옷과 어울릴 옷 색기반 분류 

-   지금 의상이 현재 트렌드와 얼마나 유사한지 예측

수행도구

- Jupyter notebook
- Google colab
- AWS
- Tenserflow2.x
- OpenCV
- cnn, multinomial classification
- grad-cam

<br>

**IoT**

-   카메라 이용 옷 이미지 정보 데이터화

-   옷장 내부 온습도 체크

-   LCD를 통한 온습도 및 코디 정보 제공

-   LCD touch UI (APP)

-   가능하다면 증강현실

수행도구

- vs code
라즈베리파이(Python)
아두이노(C++)
- Android Studio
  Kotlin
- USB카메라, LCD, 온습도 센서

- MQTT

<br>

**클라우드**

-   AWS를 활용한 클라우드 아키텍쳐 설계

-   CloudWatch를 사용하여 각 기능별 로그 및 모니터링

-   SNS를 활용한 온도, 습도 경고 알림

수행도구

- python or node.js
- Amazone Web Serivces
- EC2, S3, Lambda & Api Gateway, DynamoDB, SNS, Cloudwatch

<br>

<br>

<br>

<br>

<br>

<br>

