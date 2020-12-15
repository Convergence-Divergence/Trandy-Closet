# 12/15

### Android Studio 와 AWS RDS 간의 REST API 통신하기

- Android Studio 에서 API endpoint 를 원하는 정보(image, user_id)를 
  POST 형식에 담아서 연결해주면 lambda함수를 사용하여 DB(RDS)에 CRUD 작업을 수행함
- postman 은 정말 잘 만들어진 API test-tool 
  - RESTful API 테스트
  - API를 언어별 코드로 만들어 줌
  - Test-runner 을 만들어서 전체 API를 테스트
- 현재 까지 amplify 로 생성한 API 1개 생성되어 있음
  

### 람다함수 생성(python)

- API 명세서 - 필요한 람다함수 정의 필요한 메소드,파라미터 등
- 현재 총 4개의 lambda 함수 생성 되어 있음
  - api (1개)
  - auth (2개)
  - s3 (1개)

​	







### #참고

- [REST API 실습] 3. JPA(Hibernate) + HikariCP로 스프링부트 프로젝트와 RDS MariaDB 연동 후 CRUD 메소드 구현https://wickies.tistory.com/101?category=768093

- AWS를 활용한 안드로이드 앱 (6) 안드로이드에서 로그인, 회원가입 구현하기, Retrofit 라이브러리로 통신하기
  https://blog.naver.com/zion830/221661486117