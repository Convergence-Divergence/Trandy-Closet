# IoT

### 전체 아키텍처

![image-20201126095737081](README.assets/image-20201126095737081.png)  

<br>

<br>

# 11/26

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

# 11/27

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

# 11/30

UI 구체화

![image-20201201183750716](README.assets/image-20201201183750716.png)  

<br>

<br>

# 12/01

참고 : https://dev.classmethod.jp/author/jung-haeun/

<br>

#### Amplify + Android 프로젝트 생성 

Amplify CLI 설치

-   `npm install -g @aws-amplify`

IAM 사용자 추가

-   `amplify configure`
-   region : `ap-northeast-2`

username : `taelim`

new_user_credentials.csv 다운로드 받아둠

![image-20201201171237731](README.assets/image-20201201171237731.png)  

<br>

#### Amazon Cognito 인증

-   `amplify add auth`

![image-20201201171851159](README.assets/image-20201201171851159.png)  

**상태확인**

-   `amplify status`

프로젝트 갱신

-   `amplify push`

<br>

#### 알게 된 사실

AWS Amplify 연관된 서비스

-   AWS AppSync, Amazon API Gateway
-   Amazon Cognito
-   Amazon Pinpoint, Amazon Kinesis, Amazon Personalize
-   Amazon S3, Amazon CloudFront

<br>

-   **AWS AppSync** : GraphQL을 사용하여 애플리케이션에서 필요로 하는 데이터를 가져올 수 있도록 하는 관리형 서비스. 오프라인 상태이더라도 로컬로 데이터 액세스가 가능하도록 하고, 온라인 상태가 되면 데이터를 다시 동기화해줄 수 있는 기능을 갖추고 있습니다.
-   **Amazon Cognito** : 가입, 로그인, 액세스 제어 기능을 갖춘 인증 관리 서비스. Identity Pool 을 통해 Facebook, Google, Amazon과 같은 소셜 로그인과도 연동이 가능합니다.
-   **Amazon S3** : 데이터를 저장하고 검색할 수 있는 간단한 API를 제공하는 완전 관리형 스토리지 서비스. IAM 정책이나 S3 버킷 정책으로 지정된 대상에게 공유하도록 할 수 있어요.

<br>

<br>

# 12/02

**안드로이드앱**

test 폴더 : 달력에 사진 넣기 테스트 중

https://github.com/kizitonwose/CalendarView 라이브러리 5번 예제를 응용할 예정

**필수 코드**

build.gradle (APP)

```kotlin
implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
implementation 'androidx.core:core-ktx:1.2.0'
implementation 'androidx.appcompat:appcompat:1.1.0'
implementation 'com.google.android.material:material:1.1.0'
implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
implementation 'androidx.legacy:legacy-support-v4:1.0.0'
testImplementation 'junit:junit:4.+'
androidTestImplementation 'androidx.test.ext:junit:1.1.1'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.0.9'
implementation 'com.github.kizitonwose:CalendarView:1.0.0'
```

build.gradle (Project)

```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    ext.kotlin_version = "1.3.72"
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.1.1"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

<br>

<br>

# 12/03

**안드로이드앱**

목적 : 자신의 사진 데이터를 AWS에 저장 후 메인화면 달력 부분에서 저장하게 함

test 폴더 : 달력에 인터넷상의 경로를 통한 사진 올리기 접근 완료

#### **필수 코드**

**build.gradle (APP)**

```kotlin
// Glide 라이브러리
implementation 'com.github.bumptech.glide:glide:4.9.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.9.0'
implementation "org.jetbrains.anko:anko-commons:0.10.8"
```

**AndroidManifest.xml**

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

**Fragment.kt**

이미지 불러올 때 형식

```kotlin
Glide.with(view?.context).load(flights[0]?.imageurl?.toString()).into(flightImage)
```

data class 재정의 - 이름은 추후 변경 예정 (imageurl 부분 생성 null 허용)

```kotlin
data class Flight(val time: LocalDateTime, val departure: Airport, val destination: Airport, @ColorRes val color: Int, val imageurl: String?) {
    data class Airport(val city: String, val code: String)
}
```

**Utils.kt**

예제 데이터 add, 사진없으면 null로 접근

```kotlin
val currentMonth22 = currentMonth.atDay(22)
    list.add(Flight(currentMonth22.atTime(13, 20), Airport("Ibadan", "IBA"), Airport("Benin", "BNI"), R.color.blue_800,
        "https://github.com/Convergence-Divergence/Trandy-Closet/raw/master/IoT/README.assets/image-20201126095737081.png"
    ))
    list.add(Flight(currentMonth22.atTime(17, 40), Airport("Sokoto", "SKO"), Airport("Ilorin", "ILR"), R.color.red_800, "https://github.com/Convergence-Divergence/Trandy-Closet/raw/master/IoT/README.assets/image-20201126095737081.png"))
```

<br>

<br>

# 12/04

**안드로이드앱**

목적 : 사진 S3에, 정보 RDS에 저장하여 앱에서 접근하고자함.

RDS에 들어갈 필수요소 (날짜, 상의 이미지 경로, 하의 이미지 경로, 그 외 경로)

<br>

**현재시간 출력 필수코드**

```kotlin
<TextClock
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:format12Hour="yyyy-MM-dd HH:mm:ss"
                    android:textAppearance="@style/TextAppearance.AppCompat.Large" />
```

<br>

**날씨정보 받기**

- openweathermap.org

- API : 메일로 온 Key 참고

- 접근 방법 : `http://api.openweathermap.org/data/2.5/weather?lat=37.476200&lon=126.973154&units=metric&appid=API Key`

- 받은 데이터 형식  

  ```json
  {
      "coord": {
          "lon": 126.97,
          "lat": 37.48
      },
      "weather": [
          {
              "id": 800,
              "main": "Clear",
              "description": "clear sky",
              "icon": "01d"
          }
      ],
      "base": "stations",
      "main": {
          "temp": 4.59,
          "feels_like": -0.64,
          "temp_min": 4,
          "temp_max": 5,
          "pressure": 1028,
          "humidity": 29
      },
      "visibility": 10000,
      "wind": {
          "speed": 2.92,
          "deg": 303
      },
      "clouds": {
          "all": 1
      },
      "dt": 1607065175,
          "sys": {
          "type": 1,
          "id": 8117,
          "country": "KR",
          "sunrise": 1607034654,
          "sunset": 1607069648
      },
      "timezone": 32400,
      "id": 6800035,
      "name": "Banpobondong",
      "cod": 200
  }
  ```

참고

https://m.blog.naver.com/PostView.nhn?blogId=ivory82&logNo=220797022612&proxyReferer=https:%2F%2Fwww.google.com%2F

https://yongyi1587.tistory.com/32