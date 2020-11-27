### 11/26

1.  명령 프롬프트에서 다음 명령을 실행하여 Raspberry Pi 소프트웨어를 업데이트합니다.

    `sudo apt-get update`

    `sudo apt-get upgrade`

2.  다음 명령을 실행하여 Phthon 3 설치를 업데이트합니다.

    `sudo pip3 install --upgrade setuptools`

3.  다음 명령을 실행하여 Raspberry Pi GPIO 라이브러리를 설치합니다.

    `pip3 install RPI.GPIO`

4.  다음 명령을 실행하여 Adafruit Blinka 라이브러리를 설치합니다.

    `pip3 install adafruit-blinka`

    자세한 내용은 을 참조하십시오. [설치 중 CircuitPython 라즈베리 파이의 라이브러리](https://learn.adafruit.com/circuitpython-on-raspberrypi-linux/installing-circuitpython-on-raspberry-pi).

5.  다음 명령을 실행하여 Adafruit Seesaw 라이브러리를 설치합니다.

    `sudo pip3 install adafruit-circuitpython-seesaw`

6.  다음 명령을 사용하여 AWS IoT Python용 디바이스 SDK를 설치합니다.

    `pip3 install AWSIoTPythonSDK`

<br>

**moistureSensor.py**

```python
from adafruit_seesaw.seesaw import Seesaw
from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTShadowClient
from board import SCL, SDA

import logging
import time
import json
import argparse
import busio

# Shadow JSON schema:
#
# {
#   "state": {
#       "desired":{
#           "moisture":<INT VALUE>,
#           "temp":<INT VALUE>            
#       }
#   }
# }

# Function called when a shadow is updated
def customShadowCallback_Update(payload, responseStatus, token):

    # Display status and data from update request
    if responseStatus == "timeout":
        print("Update request " + token + " time out!")

    if responseStatus == "accepted":
        payloadDict = json.loads(payload)
        print("~~~~~~~~~~~~~~~~~~~~~~~")
        print("Update request with token: " + token + " accepted!")
        print("moisture: " + str(payloadDict["state"]["reported"]["moisture"]))
        print("temperature: " + str(payloadDict["state"]["reported"]["temp"]))
        print("~~~~~~~~~~~~~~~~~~~~~~~\n\n")

    if responseStatus == "rejected":
        print("Update request " + token + " rejected!")

# Function called when a shadow is deleted
def customShadowCallback_Delete(payload, responseStatus, token):

     # Display status and data from delete request
    if responseStatus == "timeout":
        print("Delete request " + token + " time out!")

    if responseStatus == "accepted":
        print("~~~~~~~~~~~~~~~~~~~~~~~")
        print("Delete request with token: " + token + " accepted!")
        print("~~~~~~~~~~~~~~~~~~~~~~~\n\n")

    if responseStatus == "rejected":
        print("Delete request " + token + " rejected!")


# Read in command-line parameters
def parseArgs():

    parser = argparse.ArgumentParser()
    parser.add_argument("-e", "--endpoint", action="store", required=True, dest="host", help="Your AWS IoT custom endpoint")
    parser.add_argument("-r", "--rootCA", action="store", required=True, dest="rootCAPath", help="Root CA file path")
    parser.add_argument("-c", "--cert", action="store", dest="certificatePath", help="Certificate file path")
    parser.add_argument("-k", "--key", action="store", dest="privateKeyPath", help="Private key file path")
    parser.add_argument("-p", "--port", action="store", dest="port", type=int, help="Port number override")
    parser.add_argument("-n", "--thingName", action="store", dest="thingName", default="Bot", help="Targeted thing name")
    parser.add_argument("-id", "--clientId", action="store", dest="clientId", default="basicShadowUpdater", help="Targeted client id")

    args = parser.parse_args()
    return args


# Configure logging
# AWSIoTMQTTShadowClient writes data to the log
def configureLogging():

    logger = logging.getLogger("AWSIoTPythonSDK.core")
    logger.setLevel(logging.DEBUG)
    streamHandler = logging.StreamHandler()
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    streamHandler.setFormatter(formatter)
    logger.addHandler(streamHandler)


# Parse command line arguments
args = parseArgs()

if not args.certificatePath or not args.privateKeyPath:
    parser.error("Missing credentials for authentication.")
    exit(2)

# If no --port argument is passed, default to 8883
if not args.port: 
    args.port = 8883


# Init AWSIoTMQTTShadowClient
myAWSIoTMQTTShadowClient = None
myAWSIoTMQTTShadowClient = AWSIoTMQTTShadowClient(args.clientId)
myAWSIoTMQTTShadowClient.configureEndpoint(args.host, args.port)
myAWSIoTMQTTShadowClient.configureCredentials(args.rootCAPath, args.privateKeyPath, args.certificatePath)

# AWSIoTMQTTShadowClient connection configuration
myAWSIoTMQTTShadowClient.configureAutoReconnectBackoffTime(1, 32, 20)
myAWSIoTMQTTShadowClient.configureConnectDisconnectTimeout(10) # 10 sec
myAWSIoTMQTTShadowClient.configureMQTTOperationTimeout(5) # 5 sec

# Initialize Raspberry Pi's I2C interface
i2c_bus = busio.I2C(SCL, SDA)

# Intialize SeeSaw, Adafruit's Circuit Python library
ss = Seesaw(i2c_bus, addr=0x36)

# Connect to AWS IoT
myAWSIoTMQTTShadowClient.connect()

# Create a device shadow handler, use this to update and delete shadow document
deviceShadowHandler = myAWSIoTMQTTShadowClient.createShadowHandlerWithName(args.thingName, True)

# Delete current shadow JSON doc
deviceShadowHandler.shadowDelete(customShadowCallback_Delete, 5)

# Read data from moisture sensor and update shadow
while True:

    # read moisture level through capacitive touch pad
    moistureLevel = ss.moisture_read()

    # read temperature from the temperature sensor
    temp = ss.get_temp()

    # Display moisture and temp readings
    print("Moisture Level: {}".format(moistureLevel))
    print("Temperature: {}".format(temp))
    
    # Create message payload
    payload = {"state":{"reported":{"moisture":str(moistureLevel),"temp":str(temp)}}}

    # Update shadow
    deviceShadowHandler.shadowUpdate(json.dumps(payload), customShadowCallback_Update, 5)
    time.sleep(1)
```

파일을 찾을 수 있는 위치에 파일을 저장합니다. 다음 파라미터로 `moistureSensor.py` 명령줄을 실행할 수 있습니다.

-   endpoint

    사용자 지정 AWS IoT 엔드포인트입니다. 자세한 정보는 [디바이스 섀도우 REST API](https://docs.aws.amazon.com/ko_kr/iot/latest/developerguide/device-shadow-rest-api.html) 단원을 참조하십시오.

    ☆나의 endpoint `avul980x37035-ats.iot.us-west-2.amazonaws.com`

-   rootCA

    AWS IoT 루트 CA 인증서 전체 경로입니다.

-   cert

    AWS IoT 디바이스 인증서 전체 경로입니다.

-   키

    AWS IoT 디바이스 인증서 프라이빗 키 전체 경로

-   thingName

    사물 이름(이 경우는 `RaspberryPi`)입니다.

-   clientId

    MQTT 클라이언트 ID입니다. `RaspberryPi`를 사용합니다.

명령줄의 모양은 다음과 같아야 합니다.

```
python3 moistureSensor.py --endpoint your-endpoint --rootCA ~/certs/AmazonRootCA1.pem --cert ~/certs/raspberrypi-certificate.pem.crt --key ~/certs/raspberrypi-private.pem.key --thingName RaspberryPi --clientId RaspberryPi
```

센서를 만지거나, 화분에 넣거나, 물 컵에 넣어 센서가 다양한 습도에 반응하는지 확인해봅니다. 필요한 경우 `MoistureSensorRule`. 수분 센서 판독값이 규칙의 SQL 쿼리 문에 지정된 값 아래로 내려가면 AWS IoT 에 메시지를 게시합니다. Amazon SNS 주제. 습도와 온도 데이터가 있는 이메일 메시지가 수신되어야 합니다.

Amazon SNS에서 이메일 메시지 수신을 확인한 후에 **CTRL+C**를 눌러 Python 프로그램을 저장합니다. Python 프로그램으로 전송되는 메시지로 요금이 발생할 가능성은 적지만 사용 후에는 프로그램을 중지시키는 것이 좋습니다.