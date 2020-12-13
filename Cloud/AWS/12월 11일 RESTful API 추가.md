# 12/11

## #RESTfull API 생성하기

#전제조건

-  Android API 레벨 16 (Android4.1) 이상

### #API 구성

1. amplify 폴더가 포함된 프로젝트 경로에서 `amplify add api`
2. api settings

```shell
C:\Trandy-Closet\Cloud\aws-sdk-android-samples\S3TransferUtilitySample>amplify add api
? Please select from one of the below mentioned services: REST
? Provide a friendly name for your resource to be used as a label for this category in the project: RESTfulapi
? Provide a path (e.g., /book/{isbn}): /items
? Choose a Lambda source Create a new Lambda function
? Provide an AWS Lambda function name: s3transferimageslambdarestful
? Choose the runtime that you want to use: Python
You must have pipenv installed and available on your PATH as "pipenv". It can be installed by running "pip3 install --user pipenv".
Only one template found - using Hello World by default.

Available advanced settings:
- Resource access permissions
- Scheduled recurring invocation
- Lambda layers configuration

? Do you want to configure advanced settings? Yes
? Do you want to access other resources in this project from your Lambda function? (Y/n)
C:\Trandy-Closet\Cloud\aws-sdk-android-samples\S3TransferUtilitySample>
C:\Trandy-Closet\Cloud\aws-sdk-android-samples\S3TransferUtilitySample>
C:\Trandy-Closet\Cloud\aws-sdk-android-samples\S3TransferUtilitySample>
C:\Trandy-Closet\Cloud\aws-sdk-android-samples\S3TransferUtilitySample>amplify add api
? Please select from one of the below mentioned services: REST
? Provide a friendly name for your resource to be used as a label for this category in the project: RESTfulapi
? Provide a path (e.g., /book/{isbn}): /todo
? Choose a Lambda source Create a new Lambda function
? Provide an AWS Lambda function name: lambdarestfulapi
? Choose the runtime that you want to use: NodeJS
? Choose the function template that you want to use: Serverless ExpressJS function (Integration with API Gateway)

Available advanced settings:
- Resource access permissions
- Scheduled recurring invocation
- Lambda layers configuration

? Do you want to configure advanced settings? No
? Do you want to edit the local lambda function now? No
Successfully added resource lambdarestfulapi locally.
```

