# 12/22 

- GetImages (@GET)

  ```python
      cursor = mysql.cursor(pymysql.cursors.DictCursor)
      sql = "SELECT * FROM Cloths; SELECT * FROM Users;"
      cursor.execute(sql)
      result = cursor.fetchall()
      print(result)
  ```

    

  Lambda 함수 하나에 MySQL 쿼리문을 입력해보았지만 
  -> lambda 함수에서는 동시에 여러 쿼리문을 허용하지 않음 (아래와 같은 error)

  ![image-20201222115345456](C:\Users\i\AppData\Roaming\Typora\typora-user-images\image-20201222115345456.png)  

  

  ```python
  cursor = mysql.cursor(pymysql.cursors.DictCursor)
      sql = "SELECT * FROM Cloths;"
      sql2 = " SELECT * FROM Users;"
      cursor.execute(sql)
      result = cursor.fetchall()	
      cursor.execute(sql2)
      result2 = cursor.fetchall()
      print(result, result2)
  ```

  - 위와 같이 구성시  sql 과 sql2 두 값 모두를 출력할 수 있나

    -> 고로, 위 문제를 해결 가능하다

  

  

  

  => 쿼리자체에서 카테고리를 분류 해서 전송 하지말고 전체이미지 정보를 전송한 다음 카테고리를 분류키로함 