# 클라우드 



- ## 12/1 클라우드

  - Amplify 와 android studio 연동하기

    - Amplify 생성
      https://dev.classmethod.jp/articles/amplify_android_tutorial/

    - Amplify+Android+Cognito 인증 추가 

      https://dev.classmethod.jp/articles/amplify-android-cognito-auth/

    - Amplify와 서버리스기반 소셜 로그인 안드로이드 앱 만들기 

      https://xmrrh.github.io/

      

  - **AWS AppSync**: GraphQL을 사용하여 애플리케이션에서 필요로 하는 데이터를 가져올 수 있는 관리형 서비스.

    - 오프라인 상태에서도 로컬로 데이터 엑세스 가능
    - 온라인 상태가 되면 다시 동기화 해주는 기능

  - **Amazon Cognito**: 가입, 로그인, 액세스 제어 기능을 갖춘 인증 관리 서비스

    - 소셜 로그인 연동 가능

  ---

  

  ### 작업

  - version check!

  - `amplify init` amplify 프로젝트 생성 (git 명령어와 유사함!)

  - Amplify  user 생성 시 정책 부여 ` administorFullaccess` 나머진 defalut!

  - `amplify add auth` 사용자 인증 기능 추가 -> 수정은 `amplify update auth` 

    - ```shell
      C:\MyApplication>amplify init
      Note: It is recommended to run this command from the root of your app directory
      ? Enter a name for the project MyApplication
      ? Enter a name for the environment dev
      ? Choose your default editor: None
      ? Choose the type of app that you're building android
      Please tell us about your project
      ? Where is your Res directory:  app/src/main/res
      Using default provider  awscloudformation
      
      For more information on AWS Profiles, see:
      https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html
      
      ? Do you want to use an AWS profile? Yes
      ? Please choose the profile you want to use profile-closet
      Adding backend environment dev to AWS Amplify Console app: d3nke7xdbbgtv2`
      ```

    - ```shell
      √ Successfully created initial AWS cloud resources for deployments.
      √ Initialized provider successfully.
      Initialized your environment successfully.
      
      Your project has been successfully initialized and connected to the cloud!
      
      Some next steps:
      "amplify status" will show you what you've added already and if it's locally configured or deployed
      "amplify add <category>" will allow you to add features like user login or a backend API
      "amplify push" will build all your local backend resources and provision it in the cloud
      "amplify console" to open the Amplify Console and view your project status
      "amplify publish" will build all your local backend and frontend resources (if you have hosting category added) and provision it in the cloud
      
      Pro tip:
      Try "amplify add api" to create a backend API and then "amplify publish" to deploy everything
      ```

    - ```shell
      C:\MyApplication>amplify add auth
      Using service: Cognito, provided by: awscloudformation
      
       The current configured provider is Amazon Cognito.
      
       Do you want to use the default authentication and security configuration? Default configuration
       Warning: you will not be able to edit these selections.
       How do you want users to be able to sign in? Email
       Do you want to configure advanced settings? No, I am done.
      Successfully added auth resource myapplicationb2126229 locally
      
      Some next steps:
      "amplify push" will build all your local backend resources and provision it in the cloud
      "amplify publish" will build all your local backend and frontend resources (if you have hosting category added) and provision it in the cloud
      ```

      

  - `amplify push` -> 수정 작업은 `amplify update auth`

  - AWS 관리 콘솔(AWS console->Services->Cognito->User Pools) 확인 가능

  - 안드로이드 스튜디오에서는 아래와 같이 생성값 확인

  

  ![image-20201201162846043](C:/Users/Trandy-Closet/Cloud/images/image-20201201162846043-1606819700184.png)

  

  

  

  - AWS AppSync 필요성 검토!