# 12/11

### Server API

- 적절한 요청에 대해 응답을 돌려주는 창구(Endpoint)  를 Web 을 통해 노출하는 것
- Server API 를 만드는 방법론
  - **REST** (REpresentational State Transfer)
  - **RESTful**
  - **GraphQL**



### REST

- 일반적으로 REST 라고 하면 좁은 의미로 HTTP 를 통해 CRUD 를 실행하는 API 를 뜻함
- 기본적으로 웹의 기존 기술과 HTTP 프로토콜을 그대로 활용하기 때문에 웹의 장점을 최대한 
  활용할 수 있는 아키텍쳐 스타일
- HTTP URL 를 통해 자원을 명시하고, HTTP Method(post,get,put,delete) 를 통해 해당자원에
  대한 CRUD Operation 을 적용하는 것을 의미 함
- 모든 REsource 들을 하나의 Endpoint 에 연결해놓고, 각 Endpoint 는 
  그 Reource  와 관련된 내용만관리하게 하자는 방법론

- 위에 REST 의 조건을 만족하는  API 를 **RESTful API** 라고 부름
- 장점
  - 범용성 -  HTTP 표준프로토콜을 따르는 모든 플랫폼에 호환됨
- 단점
  - 표준 x, 사용 가능한 메소드가 4개뿐 

### 

### GraphQL

- GraphQL 은 Server API 를 구성하기 위해 Facebook에서 만든 Query Language

- GraphQL이 탄생하게된 배경

  - RESTful API 로는 다양한 기종에서 필요한 정보들을 일일이 구현하기 힘듬
  - IOS 와 Android 에서 필요한 정보들이 조금씩 상이 -> 다른 부분마다 API 구현 부담

- 장점

  - 원하는 정보를 하나의 query 에 담아 요청하는 것이 가능 -> HTTP 응답의 **size**, **횟수**를 줄임

- 단점

  - File 전송 등  Text 만으로 하기 힘든 내용들을 처리하기 복잡함
  - 고정된 요청과 응답이 필요한 경우 RESTful API 보다 query의 요청 길이가 커짐
  - 재귀적인 query 불가능

  

###  GraphQL 과 RESTful 의 차이점

1. **GraphQL** 은 전체 API를 위해 단 하나의 Endpoint 를 사용/
   **RESTful API**는 Resource 마다 하나의 Endpoint 를 가짐->그 Endpoint 에서 Resource에 대한 모든 것을 담당
2. **GraphQL** 는 요청할 때 사용한 **Query 문에 따라 응답의 구조가 달라짐**
3. **GraphQL** == 뷔페, **RESTful API** == 세트메뉴
   뷔페는 원하는 것 맘껏 먹을수 있지만, 세트는 원치않는 부분도 포함될 수 있음

![img](RESTful vs GraphQL.assets/graphql-mobile-api.png)  



- **API Endpoint**

- 다음의 Github API v3 과 v4 이 좋은 예시가 될 것이다.

  - [Github API v3](https://developer.github.com/v3)
  - [Github API v4](https://developer.github.com/v4)

  각각 [v3 root endpoint](https://developer.github.com/v3/#root-endpoint) 와 [v4 root endpoint](https://developer.github.com/v4/guides/forming-calls/#the-graphql-endpoint) 로 Endpoint 를 제공하지만,
  v4 의 경우 Root endpoint 를 제외한 어떤 Endpoint 도 없는 반면,
  v3 의 경우는 각 Resource 마다 수많은 Endpoint 들을 제공한다.



- **API 응답의 구조**
  1. RESTful API 는 하나의 Endpoint 에서 돌려줄 수 있는 응답의 구조가 어느정도 정해져 있음
  2. API를 작성할 때 이미 정해놓은 구조로만 응답이 오는 구조
  3. GraphQL 은 사용자가 응답의 구조를 자신이 원하는 방식으로 바꿀 수 있음



==> 상황에 맞는 API 를 선택해야 한다!

- GraphQL - 서로 다른 모양의 다양한 요청에 대한 응답 필요
- RESTful -  file 전송 등 단순 text 만으로 처리되지 않는 요청일 때, 요청의 구조가 정해져 있을 때



===> 현재 프로젝트의 경우에는 **구조가 정해져**있고, 
          **file 전송**이 필요하므로 **RESTful API**를 활용하기로 함

