# IoT

### 전체 아키텍처

![image-20201126095737081](README.assets/image-20201126095737081.png)  

<br>

<br>

### 11/26

**단순 DHT-11 & 라즈베리파이 테스트 예제**

http://blog.naver.com/PostView.nhn?blogId=chandong83&logNo=220902795488

**DHT-11 & 아두이노 & LED - IoT Core와 연동한 예제**

https://kwanulee.github.io/IoTPlatform/start-aws-iot.html

**AWS IoT + Raspberry Pi + Python 예제**

https://blog.iolate.kr/m/246

**amplify - 안드로이드 가이드**

https://aws.amazon.com/ko/getting-started/hands-on/build-android-app-amplify/

<br>

AWS_IoT_Core_Guide.md 파일 작성

<br>

**콘솔 주소**

https://us-west-2.console.aws.amazon.com/console/home?region=us-west-2

<br>

AWS IoT 대화형 자습서 확인

<br>

**AWS IoT에 연결** 

-   디바이스 온보딩 
-   플랫폼 : **Linux/OSX** 선택
-   디바이스 SDK : **Python** 선택

전제 조건 : 디바이스에 **Python 및 Git가 설치**되어 있고 **8883 포트를 통해 퍼블릭 인터넷에 연결(TCP)**되어 있어야 합니다.

<br>

**사물 이름**: TempHumi

사물 유형 및 속성 키·값 설정 X

<br>

**연결키트 다운로드**

![image-20201126105420574](README.assets/image-20201126105420574.png)  

<br>

디바이스를 구성하고 테스트하려면 다음 단계를 수행해야 합니다.

**1단계**: 디바이스에서 연결 키트의 압축 파일 해제

```
unzip connect_device_package.zip
```

**2단계**: 실행 권한 추가

```
chmod +x start.sh
```

**3단계**: 시작 스크립트 실행. 사물이 보내는 메시지는 아래와 같을 것입니다.

```
./start.sh
디바이스의 메시지 대기
```

<br>

**라즈베리파이 연결**

https://docs.aws.amazon.com/ko_kr/iot/latest/developerguide/connecting-to-existing-device.html

<br>

**AWS IoT MQTT 클라이언트를 사용하여 MQTT 메시지 보기**

https://docs.aws.amazon.com/ko_kr/iot/latest/developerguide/view-mqtt-messages.html

<br>

**강사님께 질문할 것**

DHT-11 과 DHT-22 중 선정 (정밀값)

코디 정보 제공 사진 겹치기에 대한 방법 (z축 이용 덮어쓰기가 가능한지 & How to 플래그먼트?...)

<br>

<br>

### 11/27

**AWS IoT Core MQTT 실행하기**

-   `cd ~/aws-iot-device-sdk-python-v2/samples`

-   `python pubsub.py --topic iot --root-ca ~/certs/AmazonRootCA1.pem --cert ~/certs/63e9aaa7f0-certificate.pem.crt --key ~/certs/63e9aaa7f0-private.pem.key --endpoint avul980x37035-ats.iot.us-west-2.amazonaws.com`

<br>

**나의 엔드포인트**

-   `avul980x37035-ats.iot.us-west-2.amazonaws.com`

<br>

<br>

**온습도 센서 아두이노에서 동작 확인**

**라즈베리파이에서 동작안됨 ㅠㅠ**

-   `cd Adafruit_Python_DHT/examples/`
-   `sudo python AdafruitDHT.py 11 4`

<br>

<br>

### 11/30